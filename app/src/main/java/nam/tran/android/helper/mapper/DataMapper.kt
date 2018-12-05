package nam.tran.android.helper.mapper

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataMapper @Inject
internal constructor(
    val preferenceMapper: PreferenceMapper,
    val comicModelMapper: ComicModelMapper,
    val linkComicModelMapper: LinkComicModelMapper,
    val userModelMapper: UserModelMapper,
    val articleModelMapper: ArticleModelMapper
)
