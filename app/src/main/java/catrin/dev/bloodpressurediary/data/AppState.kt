package catrin.dev.bloodpressurediary.data

sealed class AppState{
    data class SuccessState(val data: List<Measure>) : AppState()
    object LoadingState : AppState()
    data class ErrorState(val error: Throwable) : AppState()
}