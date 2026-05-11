package edu.ucne.registroocupaciones.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.registroocupacion.data.db.OcupacionDb
import edu.ucne.registroocupacion.data.local.dao.OcupacionDao
import edu.ucne.registroocupacion.domain.repository.OcupacionRepository
import edu.ucne.registroocupaciones.data.repository.OcupacionRepositoryImpl

import javax.inject.Singleton
import kotlin.jvm.java

@InstallIn(
    SingletonComponent::class
)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideOcupacionDb(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(
            appContext,
            OcupacionDb::class.java,
            "OcupacionDb"
        ).fallbackToDestructiveMigration(dropAllTables = false)
            .build()

    @Provides
    @Singleton
    fun provideOcupacionDao(ocupacionDb: OcupacionDb): OcupacionDao {
        return ocupacionDb.ocupacionDao()
    }

    @Provides
    @Singleton
    fun provideOcupacionRepositoryImpl(ocupacionDao: OcupacionDao): OcupacionRepositoryImpl {
        return OcupacionRepositoryImpl(ocupacionDao)
    }

    @Provides
    @Singleton
    fun provideOcupacionRepository(impl: OcupacionRepositoryImpl): OcupacionRepository {
        return impl
    }
}