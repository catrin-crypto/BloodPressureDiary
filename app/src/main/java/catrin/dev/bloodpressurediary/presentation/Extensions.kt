package catrin.dev.bloodpressurediary.presentation

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper


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
