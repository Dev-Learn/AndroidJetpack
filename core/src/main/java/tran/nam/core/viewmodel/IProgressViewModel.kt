package tran.nam.core.viewmodel

import nam.tran.domain.entity.state.Resource

interface IProgressViewModel {
    fun resource(): Resource<*>?
}
