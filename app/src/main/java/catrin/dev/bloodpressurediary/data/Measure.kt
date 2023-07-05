package catrin.dev.bloodpressurediary.data

import catrin.dev.bloodpressurediary.presentation.toInt
import java.time.LocalDate
import java.time.LocalTime

data class Measure(
    var date: Int = LocalDate.now().toInt(),
    var time: Int = LocalTime.now().toInt(),
    var higher: Int = -1,
    var lower: Int = -1,
    var hbpm: Int = -1,
){
    fun toHashMap() = hashMapOf("date" to date, "time" to time, "higher" to higher,
        "lower" to lower, "hbpm" to hbpm)
}