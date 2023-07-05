package catrin.dev.bloodpressurediary.repo

import catrin.dev.bloodpressurediary.data.ResourceState
import catrin.dev.bloodpressurediary.data.Measure

interface Repo {
    suspend fun loadData(): ResourceState<List<Measure>>
    suspend fun addData(measure: Measure) : Boolean
}