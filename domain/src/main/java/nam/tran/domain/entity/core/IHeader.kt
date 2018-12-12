package nam.tran.domain.entity.core

interface IHeader<T>{
    val isHeader: Boolean
    val headerValue : T?
}