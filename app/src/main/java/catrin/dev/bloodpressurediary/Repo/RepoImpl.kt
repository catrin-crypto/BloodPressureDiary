package catrin.dev.bloodpressurediary.Repo

import android.util.Log
import catrin.dev.bloodpressurediary.data.Measure
import catrin.dev.bloodpressurediary.data.ResourceState
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.zone.ZoneRulesException
import java.util.Calendar
import java.util.Date

class RepoImpl : Repo {
    val tableName = "blood_pressure_table"
    val TAG = this::class.java.toString()
    val db = Firebase.firestore
    override suspend fun loadData(): ResourceState<List<Measure>> {
        val ret = mutableListOf<Measure>()
        var ex : Throwable? = null
        db.collection(tableName)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "${document.id} => ${document.data}")
                    ret.add((document.toObject(MesureRow::class.java).toMeasure()))
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
                ex = exception
            }
            .await()
        if (ex == null)
            return ResourceState.SuccessState(ret)
        else return ResourceState.ErrorState(ex!!)

    }

    private fun LocalDateTime.toDate() = Date.from(this.atZone(ZoneId.systemDefault()).toInstant())

    override suspend fun addData(measure: Measure): Boolean {
        val timestamp = with(measure) {
            Timestamp(
                    LocalDateTime.of(date, time).toDate()
            )
        }
        val row = MesureRow(timestamp,measure.higher,measure.lower,measure.hbpm)
        var ret = false
          db.collection(tableName)
            .add({row})
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                ret = true
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
                ret = false
            }.await()
        return ret
    }

}