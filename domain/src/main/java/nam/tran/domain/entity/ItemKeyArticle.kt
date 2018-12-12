package nam.tran.domain.entity.core

open class ItemKeyArticle<T>(id : Int, override val isHeader: Boolean = false, override val headerValue: T) : BaseItemKey(id),IHeader<T>