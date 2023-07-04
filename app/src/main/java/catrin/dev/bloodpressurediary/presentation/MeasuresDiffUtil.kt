package catrin.dev.bloodpressurediary.presentation

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import catrin.dev.bloodpressurediary.data.Measure

class MeasuresDiffUtil(private val oldList: List<Measure>, private val newList: List<Measure>) :
    DiffUtil.Callback() {
    private val payload = Any()
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        try {
            val oldItem: Measure = oldList[oldItemPosition]
            val newItem: Measure = newList[newItemPosition]
            oldItem.date == newItem.date
                    && oldItem.time == newItem.time
        } catch (t: Throwable) {
            logErr(t)
            false
        }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        try {
            val oldItem: Measure = oldList[oldItemPosition]
            val newItem: Measure = newList[newItemPosition]
            oldItem.lower == newItem.lower &&
                    oldItem.date == newItem.date &&
                    oldItem.time == newItem.time &&
                    oldItem.higher == newItem.higher
                    oldItem.hbpm == newItem.hbpm
        } catch (t: Throwable) {
            logErr(t)
            false
        }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int) = payload

    private fun logErr(t: Throwable) = logErr(t, this::class.java.toString())

    private fun logErr(t: Throwable, tag: String?) {
        try {
            Log.e(tag, "", t)
        } catch (_: Throwable) {
        }
    }
}