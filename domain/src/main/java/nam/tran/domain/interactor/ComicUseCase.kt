package nam.tran.domain.interactor

import androidx.lifecycle.LiveData
import nam.tran.domain.IRepository
import nam.tran.domain.entity.ComicEntity
import nam.tran.domain.entity.state.Listing
import nam.tran.domain.entity.state.Resource
import javax.inject.Inject

class ComicUseCase @Inject internal constructor(val iRepository: IRepository) {

    fun getComic(offset : Int = 0,count : Int = 10,typeLoading : Int): LiveData<Resource<List<ComicEntity>>>{
        return iRepository.getComic(offset,count,typeLoading = typeLoading)
    }

    fun getComic(convert: (List<ComicEntity>) -> List<Any>): LiveData<Listing<Any>>{
        return iRepository.getComic(convert)
    }

}