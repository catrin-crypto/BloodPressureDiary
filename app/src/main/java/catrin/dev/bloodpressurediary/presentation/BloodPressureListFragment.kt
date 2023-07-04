package catrin.dev.bloodpressurediary.presentation

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import catrin.dev.bloodpressurediary.R
import by.kirich1409.viewbindingdelegate.viewBinding
import catrin.dev.bloodpressurediary.data.AppState
import catrin.dev.bloodpressurediary.data.Measure
import catrin.dev.bloodpressurediary.databinding.FragmentBloodPressureListBinding

class BloodPressureListFragment : Fragment() {

    companion object {
        fun newInstance() = BloodPressureListFragment()
    }

    private val binding: FragmentBloodPressureListBinding by viewBinding()
    private var bloodPressureAdapter: BloodPressureRecyclerViewAdapter? = null
    private lateinit var viewModel: BloodPressureListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
       ): View = inflater.inflate(R.layout.fragment_blood_pressure_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            bloodPressureAdapter = BloodPressureRecyclerViewAdapter(viewModel.storedMeasures)
            binding.listOfUserData.adapter = bloodPressureAdapter
            binding.listOfUserData.isSaveEnabled = true

            val addMeasureFab = binding.addUserDataFab
            addMeasureFab.setOnClickListener {
                onFabClicked()
            }
            viewModel.statesLiveData.observe(this.viewLifecycleOwner) { appState ->
                processAppState(appState)
            }
        } catch (t: Throwable) {
            viewModel.handleError(t)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            viewModel = ViewModelProvider(this).get(BloodPressureListViewModel::class.java)
            viewModel.loadMeasures()
        } catch (t: Throwable) {
            viewModel.handleError(t)
        }
    }

    private fun processAppState(appState: AppState) {
        try {
            when (appState) {
                is AppState.SuccessState -> {
                    val data = appState.data
                    showMeasures(data)
                }

                is AppState.LoadingState -> {
                    //Show Loading shimmer
                }

                is AppState.ErrorState -> {
                    logAndToast(appState.error)
                }
            }
        } catch (t: Throwable) {
            logAndToast(t)
        }
    }

    private fun showMeasures(measures: List<Measure>) {
        try {
            val diffResult = DiffUtil.calculateDiff(
                MeasuresDiffUtil(
                    viewModel.storedMeasures,
                    measures
                )
            )
            viewModel.storedMeasures.clear()
            viewModel.storedMeasures.addAll(measures)
            bloodPressureAdapter?.let { diffResult.dispatchUpdatesTo(it) }
        } catch (t: Throwable) {
            viewModel.handleError(t)
        }
    }

    private fun onFabClicked() {
        try {
            findNavController().navigate(R.id.create_measure_dialog)
        } catch (t: Throwable) {
            viewModel.handleError(t)
        }
    }

    private fun logAndToast(t: Throwable) = logAndToast(t, this::class.java.toString())

    private fun logAndToast(t: Throwable, tag: String?) {
        try {
            Log.e(tag, "", t)
            Toast.makeText(requireContext().applicationContext, t.toString(), Toast.LENGTH_LONG)
                .show()
        } catch (_: Throwable) {
        }
    }

    override fun onDestroy() {
        bloodPressureAdapter = null
        super.onDestroy()
    }
}