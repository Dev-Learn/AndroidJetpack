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
    private val comicUseCase: ComicUseCase,
    private val mDataMapper: DataMapper
) :
    BaseFragmentViewModel(application),
    IProgressViewModel {

    private val results = MutableLiveData<Resource<List<ComicModel>>>()
    private var repoResult = comicUseCase.getComic(){
        mDataMapper.comicModelMapper.transform(it)
    }

    val posts = Transformations.switchMap(repoResult, { it.pagedList })
    val networkState = Transformations.switchMap(repoResult, { it.networkState })

    override fun resource(): Resource<*>? {
        return results.value
    }

    override fun onInitialized() {
//        getComic(0, 10)

    }

    fun getComic(offset: Int, count: Int){
        val view: IHomeViewModel? = view()
        view?.let { it ->
            getComic(it, offset, count).observe(it, Observer {
                if (it?.data != null && it.data!!.isNotEmpty()) {
                    view.updateData(it.data!!)
                }
                view.updateView()
            })
        }
    }

    fun getComic(view: IHomeViewModel, offset: Int, count: Int): MutableLiveData<Resource<List<ComicModel>>> {
        comicUseCase.getComic(offset, count, if (offset == 0) Loading.LOADING_NORMAL else Loading.LOADING_NONE)
            .observe(view, Observer { it ->
                it?.let {
                    results.value = mDataMapper.comicModelMapper.transform(it)
                }
            })

        return results
    }
}
