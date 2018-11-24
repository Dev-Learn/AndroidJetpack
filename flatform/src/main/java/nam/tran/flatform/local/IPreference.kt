package nam.tran.flatform.local

interface IPreference {
    fun saveToken(token: String?)
    fun getToken(): String
}
