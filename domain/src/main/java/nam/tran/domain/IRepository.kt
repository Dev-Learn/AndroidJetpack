package nam.tran.domain

import android.arch.lifecycle.LiveData
import nam.tran.domain.entity.ComicEntity
import nam.tran.domain.entity.state.Resource

interface IRepository {

    fun getComic(offset : Int, count : Int,typeLoading : Int): LiveData<Resource<List<ComicEntity>>>

}
