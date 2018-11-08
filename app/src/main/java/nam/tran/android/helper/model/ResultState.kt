package nam.tran.android.helper.model

data class ResultState private constructor(
    val status: State,
    val message: String? = null
)

enum class State {
    LOADING,
    LOADED
}