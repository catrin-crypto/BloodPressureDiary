package catrin.dev.bloodpressurediary.Repo

import catrin.dev.bloodpressurediary.data.Measure
import com.google.firebase.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

data class MesureRow(
    var dateTime: Timestamp? = null,
    var higher: Number? = null,
    var lower: Number? = null,
    var hbpm: Number? = null
) {
    fun toMeasure(): Measure {
        val offset = ZonedDateTime.now().getOffset()
        val localDT = LocalDateTime.ofEpochSecond(dateTime!!.seconds, 0, offset)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
        return Measure(
            localDT.toLocalDate(),
            localDT.toLocalTime(),
            this.higher!!.toInt(),
            this.lower!!.toInt(),
            this.hbpm!!.toInt()
        )
    }
}