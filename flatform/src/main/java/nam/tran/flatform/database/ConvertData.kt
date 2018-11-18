package nam.tran.flatform.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import nam.tran.flatform.model.response.Genre
import java.util.*


class ConvertData {

    var gson = Gson()

    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<Genre> {
        if (data == null) {
            return Collections.emptyList()
        }

        val listType = object : TypeToken<List<Genre>>() {

        }.getType()

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: List<Genre>): String {
        return gson.toJson(someObjects)
    }
}