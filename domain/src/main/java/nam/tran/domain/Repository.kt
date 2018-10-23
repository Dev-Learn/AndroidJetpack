package nam.tran.domain

import nam.tran.domain.executor.AppExecutors
import nam.tran.domain.mapper.DataEntityMapper
import nam.tran.flatform.IApi
//import nam.tran.flatform.database.DbProvider
import nam.tran.flatform.local.IPreference
import javax.inject.Inject

@Suppress("unused")
class Repository @Inject
internal constructor(private val appExecutors: AppExecutors, private val iPreference: IPreference, private val dataEntityMapper: DataEntityMapper, private val iApi: IApi/*, private val dbProvider: DbProvider*/) : IRepository {


}
