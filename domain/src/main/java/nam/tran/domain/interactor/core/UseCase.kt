package nam.tran.domain.interactor.core

import nam.tran.domain.IRepository

abstract class UseCase protected constructor(protected val iRepository: IRepository)
