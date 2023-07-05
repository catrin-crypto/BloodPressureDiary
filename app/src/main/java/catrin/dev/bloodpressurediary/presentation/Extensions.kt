package catrin.dev.bloodpressurediary.presentation

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import java.time.LocalDate
import java.time.LocalTime


tailrec fun Context.findActivity(): Activity {
        if (this is Activity) {
            return this
        } else {
            if (this is ContextWrapper) {
                return this.baseContext.findActivity()
            }
            throw java.lang.IllegalStateException("Context chain has no activity")
        }
    }
fun LocalDate.toInt() =
    this.year * 10000 + this.month.value * 100 + this.dayOfMonth

fun LocalTime.toInt() =
    this.hour * 10000 + this.minute * 100 + this.second

fun Int.toLocalDate() =
    LocalDate.of(this / 10000, this / 100 % 100, this % 100)

fun Int.toLocalTime() =
    LocalTime.of(this / 10000, this / 100 % 100, this % 100)