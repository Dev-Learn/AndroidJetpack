package nam.tran.domain.entity

import nam.tran.domain.entity.core.BaseItemKey

data class LinkComicEntity(var id: Int, var idcomic: Int, var image: String) : BaseItemKey(id)