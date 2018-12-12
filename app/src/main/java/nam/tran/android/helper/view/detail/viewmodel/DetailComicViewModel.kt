package nam.tran.android.helper.view.detail.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.PagedList
import nam.tran.android.helper.mapper.DataMapper
import nam.tran.android.helper.model.ComicModel
import nam.tran.domain.ComicUseCase
import nam.tran.domain.entity.core.BaseItemKey
import nam.tran.domain.entity.state.Listing
import nam.tran.domain.entity.state.Resource
import tran.nam.core.viewmodel.BaseFragmentViewModel
import tran.nam.core.viewmodel.IProgressViewModel
import javax.inject.Inject

class DetailComicViewModel @Inject internal constructor(
    application: Application,
    val comicUseCase: ComicUseCase,
    val dataMapper: DataMapper
) : BaseFragmentViewModel(application),
    IProgressViewModel {

    private var repoResult = MutableLiveData<Listing<PagedList<BaseItemKey>>>()

    val posts = Transformations.switchMap(repoResult, { it.pagedList })

    val results = Transformations.switchMap(repoResult, { it.networkState })

    override fun resource(): Resource<*>? {
        return results.value
    }

    fun getData(comicModel: ComicModel?, isLocal: Boolean?) {
        comicModel?.let { it ->
            repoResult.postValue(comicUseCase.getLinkComicItem(isLocal ?: false, it.id) {
                dataMapper.linkComicModelMapper.transform(it)
            })
        }
    }
}
