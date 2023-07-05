package catrin.dev.bloodpressurediary.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import catrin.dev.bloodpressurediary.R
import catrin.dev.bloodpressurediary.data.Measure
import catrin.dev.bloodpressurediary.databinding.CreateMeasureDialogBinding
import java.time.LocalDate
import java.time.LocalTime
import java.util.Calendar

class CreateMeasureDialog : DialogFragment() {
    private lateinit var viewModel: BloodPressureListViewModel
    private val binding: CreateMeasureDialogBinding by viewBinding()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
            inflater.inflate(R.layout.create_measure_dialog, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[BloodPressureListViewModel::class.java]
        with(binding) {
            inputTimePicker.setIs24HourView(true)
            val cal = Calendar.getInstance()
            inputDatePicker.updateDate(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            inputTimePicker.hour = cal.get(Calendar.HOUR)
            inputTimePicker.minute = cal.get(Calendar.MINUTE)

            positiveBtnCreateMeasure.setOnClickListener {
                okClicked()
            }
        }
    }

    private fun CreateMeasureDialogBinding.okClicked() {
        try {
            val date = with(inputDatePicker) {
                LocalDate.of(year, month + 1, dayOfMonth)
            }
            val time = with(inputTimePicker) { LocalTime.of(hour, minute) }
            viewModel.addMeasure(
                Measure(
                    date.toInt(), time.toInt(),
                    inputHigherText.text.toString().toInt(),
                    inputLowerText.text.toString().toInt(),
                    inputHbpmText.text.toString().toInt()
                )
            )
            findNavController().navigate(R.id.action_create_measure_dialog_to_fragment_blood_pressure_list)
        } catch (t: Throwable) {
            viewModel.handleError(t)
        }
    }
}