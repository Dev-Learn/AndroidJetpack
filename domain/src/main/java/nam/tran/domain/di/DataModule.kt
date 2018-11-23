package nam.tran.domain.di

import dagger.Binds
import dagger.Module
import nam.tran.domain.IRepository
import nam.tran.domain.Repository
import nam.tran.flatform.di.DbModule
import nam.tran.flatform.di.NetModule
import nam.tran.flatform.di.PreferenceModule
import javax.inject.Singleton


@Module(includes = arrayOf(NetModule::class, DbModule::class))
abstract class DataModule {

    @Binds
    @Singleton
    internal abstract fun provideRepository(repository: Repository): IRepository
}
