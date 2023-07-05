package catrin.dev.bloodpressurediary.repo

import android.util.Log
import catrin.dev.bloodpressurediary.data.Measure
import catrin.dev.bloodpressurediary.data.ResourceState
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.concurrent.CountDownLatch


class RepoImpl : Repo {
    private val tableName = "blood_pressure_table"
    private val TAG = this::class.java.toString()
    private val db = Firebase.firestore
    override suspend fun loadData(): ResourceState<List<Measure>> {
        val ret = mutableListOf<Measure>()
        var ex: Throwable? = null
        val done = CountDownLatch(1)
        db.collection(tableName)
            .get()
            .addOnSuccessListener { result ->
                val map = mutableMapOf<Int, MutableList<Measure>>()
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    val measure = document.toObject(Measure::class.java)
                    map.putIfAbsent(measure.date, mutableListOf())
                    map[measure.date]?.add(measure)
                }
                map.toSortedMap()
                    .forEach { (key, list) -> ret.addAll(list.sortedBy(Measure::time)) }
                done.countDown()
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
                ex = exception
                done.countDown()
            }
            .asDeferred()
            .await()

            done.await()
        return if (ex == null)
            ResourceState.SuccessState(ret)
        else ResourceState.ErrorState(ex!!)

    }

    override suspend fun addData(measure: Measure): Boolean {
        var ret = false
        val done = CountDownLatch(1)
        db.collection(tableName)
            .add(measure.toHashMap())
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                ret = true
                done.countDown()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
                ret = false
                done.countDown()
            }.await()

        done.await()
        return ret
    }
}