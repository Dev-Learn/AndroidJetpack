package nam.tran.flatform.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import nam.tran.flatform.model.response.Genre


class ConvertData {

    var gson = Gson()

    @TypeConverter
    fun stringToObject(data: String?): ArrayList<Genre> {
        if (data == null) {
            return ArrayList()
        }

        val listType = object : TypeToken<ArrayList<Genre>>() {

        }.getType()

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun objectToString(someObjects: ArrayList<Genre>): String {
        return gson.toJson(someObjects)
    }
}