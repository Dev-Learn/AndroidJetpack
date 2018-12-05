package nam.tran.domain.di

import dagger.Binds
import dagger.Module
import nam.tran.domain.*
import nam.tran.domain.interactor.app.IAppUseCase
import nam.tran.domain.interactor.article.IArticleUseCase
import nam.tran.domain.interactor.comic.IComicUseCase
import nam.tran.domain.interactor.login.ILoginUseCase
import nam.tran.domain.interactor.user.IUserUseCase
import nam.tran.flatform.di.DbModule
import nam.tran.flatform.di.NetModule
import javax.inject.Singleton


@Module(includes = [NetModule::class, DbModule::class])
abstract class DataModule {

    @Binds
    @Singleton
    internal abstract fun provideAppUseCase(appUseCase: AppUseCase): IAppUseCase

    @Binds
    @Singleton
    internal abstract fun provideLoginUseCase(loginUseCase: LogicUseCase): ILoginUseCase

    @Binds
    @Singleton
    internal abstract fun provideComicUseCase(comicUseCase: ComicUseCase): IComicUseCase

    @Binds
    @Singleton
    internal abstract fun provideComicUseCase(userUseCase: UserUseCase): IUserUseCase

    @Binds
    @Singleton
    internal abstract fun provideArticleUseCase(articleUseCase: ArticleUseCase): IArticleUseCase


}
