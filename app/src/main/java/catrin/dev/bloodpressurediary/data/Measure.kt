package catrin.dev.bloodpressurediary.data

import java.time.LocalDate
import java.time.LocalTime

data class Measure(
    val date: LocalDate,
    val time: LocalTime,
    val higher: Int,
    val lower: Int,
    val hbpm: Int,
)