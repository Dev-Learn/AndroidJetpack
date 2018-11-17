package nam.tran.android.helper.view.main.home.viewmodel;

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import nam.tran.android.helper.mapper.DataMapper
import nam.tran.android.helper.model.ComicModel
import nam.tran.domain.entity.state.Listing
import nam.tran.domain.entity.state.Loading
import nam.tran.domain.entity.state.Resource
import nam.tran.domain.interactor.ComicUseCase
import tran.nam.core.viewmodel.BaseFragmentViewModel
import tran.nam.core.viewmodel.IProgressViewModel
import javax.inject.Inject

class HomeViewModel @Inject internal constructor(
    application: Application,
    comicUseCase: ComicUseCase,
    private val mDataMapper: DataMapper
) :
    BaseFragmentViewModel(application),
    IProgressViewModel {

//    val results = Transformations.map(comicUseCase.getComic(0,20,Loading.LOADING_NORMAL)){
//        mDataMapper.comicModelMapper.transform(it)
//    }

//    private var repoResult = comicUseCase.getComicPage{
//        mDataMapper.comicModelMapper.transform(it)
//    }

    private var repoResult = comicUseCase.getComicItem{
        mDataMapper.comicModelMapper.transform(it)
    }

    val posts = Transformations.switchMap(repoResult, { it.pagedList })
    val results = Transformations.switchMap(repoResult, { it.networkState })

    override fun resource(): Resource<*>? {
        return results.value
    }
}
