package catrin.dev.bloodpressurediary.presentation

import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import catrin.dev.bloodpressurediary.R
import catrin.dev.bloodpressurediary.data.Measure
import catrin.dev.bloodpressurediary.databinding.MeasureItemBinding

class MeasureViewHolder(view: View) : RecyclerView.ViewHolder(view){
    private val binding: MeasureItemBinding by viewBinding()
    private val activity = view.context.findActivity() as MainActivity

    /**
     * Привязать к вьюхолдеру конкретный Measure
     * @param item Measure для привязки
     * @param isDataHeader Показывать ли в карточке Дату вверху карточки
     * */
    fun bind(item: Measure, isDataHeader: Boolean) {
        try {
            with(binding) {
              //  timeTextview.text = item.time.toString()
                dateHeaderTextview.visibility =
                    if (isDataHeader) {
                        VISIBLE.also {
                            dateHeaderTextview.text =
                                item.date.toString()
                        }
                    } else GONE
                timeTextview.text = item.time.toString()
                bloodPressureTextview.text = buildString {
        append(item.higher)
        append("  /  ")
        append(item.lower)
    }
                pulseTextview.text = buildString {
        append(Character.toChars(0x2665))
        append("  ")
        append(item.hbpm.toString())
    }
                if(((item.higher > 125) && (item.higher < 140)) || (item.lower >= 88) && (item.lower < 95) ){
                    userDataLinearLayout.setBackgroundResource(R.drawable.gradient_little_higher)
                } else if (item.higher in 110..125 && item.lower in 70..87){
                    userDataLinearLayout.setBackgroundResource(R.drawable.gradient_normal)
                } else if (item.higher < 110 || item.lower < 70){
                    userDataLinearLayout.setBackgroundResource(R.drawable.gradient_low)
                } else userDataLinearLayout.setBackgroundResource(R.drawable.gradient_high)
            }
        } catch (t: Throwable) {
            logAndToast(t)
        }
    }

    private fun logAndToast(t: Throwable) = logAndToast(t, this::class.java.toString())

    private fun logAndToast(t: Throwable, TAG: String) {
        try {
            Log.e(TAG, "", t)
            Toast.makeText(activity.applicationContext, t.toString(), Toast.LENGTH_LONG).show()
        } catch (_: Throwable) {
        }
    }
}