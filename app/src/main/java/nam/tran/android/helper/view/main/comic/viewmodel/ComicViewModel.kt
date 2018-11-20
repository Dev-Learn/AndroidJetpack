package nam.tran.android.helper.view.main.comic.viewmodel;

import android.app.Application
import androidx.lifecycle.Transformations
import nam.tran.android.helper.mapper.DataMapper
import nam.tran.android.helper.model.ComicModel
import nam.tran.domain.entity.state.Resource
import nam.tran.domain.interactor.ComicUseCase
import tran.nam.core.viewmodel.BaseFragmentViewModel
import tran.nam.core.viewmodel.IProgressViewModel
import javax.inject.Inject

class ComicViewModel @Inject internal constructor(
    application: Application,
    private val comicUseCase: ComicUseCase,
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

    private var repoResult = comicUseCase.getComicItem {
        mDataMapper.comicModelMapper.transform(it)
    }

    val posts = Transformations.switchMap(repoResult, { it.pagedList })
    val results = Transformations.switchMap(repoResult, { it.networkState })

    override fun resource(): Resource<*>? {
        return results.value
    }

    fun like(it: ComicModel?) {
        return comicUseCase.likeComic(mDataMapper.comicModelMapper.transformEntity(it))
    }
}
