package nam.tran.android.helper.view.main.home.viewmodel;

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import nam.tran.android.helper.mapper.DataMapper
import nam.tran.android.helper.model.ComicModel
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

    companion object {
        const val COUNT: Int = 10
    }

    var OFFSET = 0

    private val results = MutableLiveData<Resource<List<ComicModel>>>()

    override fun resource(): Resource<*>? {
        return results.value
    }

    override fun onInitialized() {
        getComic(OFFSET, COUNT)
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
