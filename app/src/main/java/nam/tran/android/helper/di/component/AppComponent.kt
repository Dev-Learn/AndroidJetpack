package nam.tran.android.helper.di.component

import javax.inject.Singleton

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import nam.tran.android.helper.di.module.AppModule
import nam.tran.android.helper.view.AppState

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent : AndroidInjector<AppState> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(appState: AppState): Builder

        fun build(): AppComponent
    }
}
