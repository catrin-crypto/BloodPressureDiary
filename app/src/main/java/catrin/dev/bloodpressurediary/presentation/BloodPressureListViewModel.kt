package catrin.dev.bloodpressurediary.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import catrin.dev.bloodpressurediary.Repo.Repo
import catrin.dev.bloodpressurediary.Repo.RepoImpl
import catrin.dev.bloodpressurediary.data.AppState
import catrin.dev.bloodpressurediary.data.Measure
import catrin.dev.bloodpressurediary.data.ResourceState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class BloodPressureListViewModel() : ViewModel() {
    private val repo: Repo = RepoImpl() // Заготовка под di
    val statesLiveData: MutableLiveData<AppState> = MutableLiveData()
    private val cachedMRList = mutableListOf<Measure>()
    private val viewModelCoroutineScope = CoroutineScope(
        Dispatchers.IO
                + SupervisorJob()
                + CoroutineExceptionHandler { _, throwable -> handleError(throwable) }
    )
    private var repoJob: Job? = null

    /**
     * Используется для хранения списка из RecyclerViewAdapter
     * */
    val storedMeasures = mutableListOf<Measure>()
    fun handleError(error: Throwable) {
        try {
            statesLiveData.postValue(AppState.ErrorState(error))
        } catch (_: Throwable) {
        }
    }

    override fun onCleared() {
        try {
            super.onCleared()
            viewModelCoroutineScope.cancel()
        } catch (t: Throwable) {
            handleError(t)
        }
    }

    fun loadMeasures(){
        try {
            repoJob?.let { return }
            if (cachedMRList.any())
                statesLiveData.value = AppState.SuccessState(cachedMRList)
            else statesLiveData.value = AppState.LoadingState
            repoJob = viewModelCoroutineScope.launch {
                when (val res = repo.loadData()) {
                    is ResourceState.SuccessState -> {
                        cachedMRList.clear()
                        cachedMRList.addAll(res.data)
                        statesLiveData.postValue(AppState.SuccessState(res.data))
                    }
                    is ResourceState.ErrorState ->
                        statesLiveData.postValue(AppState.ErrorState(res.error))
                }
            }
            repoJob?.invokeOnCompletion { repoJob = null }
        } catch (t: Throwable) {
            handleError(t)
        }
    }

    fun addMeasure(measure: Measure) {
        try {
            do {
                repoJob?.let { Thread.sleep(1) }
            } while (repoJob != null)
            repoJob = viewModelCoroutineScope.launch {
                if(repo.addData(measure))
                    cachedMRList.add(measure)
            }
            repoJob?.invokeOnCompletion {
                repoJob = null
                loadMeasures()
            }
        } catch (t: Throwable) {
            handleError(t)
        }
    }

}