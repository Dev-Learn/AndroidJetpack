package nam.tran.android.helper.model

import android.net.Uri

data class UserModel(val id : Int, val name : String, val email : String, var avarta : String?){
    var uri : Uri? = null
}