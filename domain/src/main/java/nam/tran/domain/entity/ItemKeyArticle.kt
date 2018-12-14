package nam.tran.domain.entity

import nam.tran.domain.entity.core.BaseItemKey
import nam.tran.domain.entity.core.IHeader

open class ItemKeyArticle<T>(id : Int, override val isHeader: Boolean = false, override val headerValue: T) : BaseItemKey(id),
    IHeader<T>