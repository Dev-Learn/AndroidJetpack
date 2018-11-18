package nam.tran.flatform.model.response

import androidx.room.Entity
import androidx.room.TypeConverters
import nam.tran.flatform.database.ConvertData

@Entity(tableName = "Comic", primaryKeys = arrayOf("id"))
class Comic(var id : Int,var title : String,var description : String,var image : String,@TypeConverters(ConvertData::class) var genre : ArrayList<Genre>)