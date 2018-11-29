package nam.tran.android.helper.view.local.viewmodel;

import android.app.Application
import androidx.lifecycle.Transformations
import nam.tran.android.helper.mapper.DataMapper
import nam.tran.domain.entity.state.Resource
import nam.tran.domain.interactor.comic.IComicUseCase
import tran.nam.core.viewmodel.BaseFragmentViewModel
import tran.nam.core.viewmodel.IProgressViewModel
import javax.inject.Inject

class ComicLocalViewModel @Inject internal constructor(
    application: Application, iComicUseCase: IComicUseCase,
    private val mDataMapper: DataMapper
) : BaseFragmentViewModel(application),
    IProgressViewModel {

    val results = Transformations.map(iComicUseCase.loadComicLike()) {
        mDataMapper.comicModelMapper.transform(it)
    }

    override fun resource(): Resource<*>? {
        return results.value
    }
}
