package catrin.dev.bloodpressurediary.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import catrin.dev.bloodpressurediary.R
import catrin.dev.bloodpressurediary.data.Measure


class BloodPressureRecyclerViewAdapter(var measures: List<Measure>) :
    RecyclerView.Adapter<MeasureViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeasureViewHolder =
        MeasureViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.measure_item, parent, false)
        )

    override fun getItemCount(): Int = measures.size
    override fun onBindViewHolder(holder: MeasureViewHolder, position: Int) {
        try {
            holder.bind(
                measures[position],
                position == 0 || measures[position - 1].date != measures[position].date
            )
        } catch (t: Throwable) {
            Log.e(this::class.java.toString(), "", t)
        }
    }
}