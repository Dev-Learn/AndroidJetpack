package nam.tran.domain.mapper

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataEntityMapper @Inject
internal constructor(
    val mPreferenceEntityMapper: PreferenceEntityMapper,
    val comicEntityMapper: ComicEntityMapper,
    val linkComicEntityMapper: LinkComicEntityMapper,
    val userEntityMapper: UserEntityMapper
)
