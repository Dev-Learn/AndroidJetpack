package nam.tran.android.helper.view.main.detail.viewmodel;

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import nam.tran.android.helper.mapper.DataMapper
import nam.tran.android.helper.model.ComicModel
import nam.tran.domain.entity.BaseItemKey
import nam.tran.domain.entity.state.Listing
import nam.tran.domain.entity.state.Resource
import nam.tran.domain.interactor.ComicUseCase
import tran.nam.core.viewmodel.BaseFragmentViewModel
import tran.nam.core.viewmodel.IProgressViewModel
import javax.inject.Inject

class DetailComicViewModel @Inject internal constructor(application: Application,val comicUseCase: ComicUseCase,val dataMapper: DataMapper) : BaseFragmentViewModel(application),
    IProgressViewModel {

    private val results = MutableLiveData<Resource<String>>()

    private var repoResult = MutableLiveData<Listing<BaseItemKey>>()

    val posts = Transformations.switchMap(repoResult, { it.pagedList })
    val networkState = Transformations.switchMap(repoResult, { it.networkState })

    override fun resource(): Resource<*>? {
        return results.value
    }

    fun getData(comicModel: ComicModel?) {
        comicModel?.let {
            repoResult.postValue(comicUseCase.getLinkComicItem(it.id){
                dataMapper.linkComicModelMapper.transform(it)
            })
        }
    }

    fun retry() {
        val retry = repoResult.value
        retry?.retry?.invoke()
    }
}
