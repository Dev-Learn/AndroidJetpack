package nam.tran.flatform.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import nam.tran.flatform.database.DbProvider
import javax.inject.Singleton

@Module
class DbModule {

    @Singleton
    @Provides
    fun provideDb(app: Application): DbProvider {
        return Room
                .databaseBuilder(app, DbProvider::class.java, "comic.db")
                .fallbackToDestructiveMigration()
                .build()
    }
}
