package edu.ucne.registroocupaciones.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.registroocupacion.data.db.OcupacionDb
import edu.ucne.registroocupacion.data.local.dao.EmpleadoDao
import edu.ucne.registroocupacion.data.local.dao.HoraExtraDao
import edu.ucne.registroocupacion.data.local.dao.OcupacionDao
import edu.ucne.registroocupacion.data.repository.EmpleadoRepositoryImpl
import edu.ucne.registroocupacion.data.repository.HoraExtraRepositoryImpl
import edu.ucne.registroocupacion.domain.Empleados.repository.EmpleadoRepository
import edu.ucne.registroocupacion.domain.HoraExtra.repository.HoraExtraRepository
import edu.ucne.registroocupacion.domain.Ocupaciones.repository.OcupacionRepository
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

    @Provides
    @Singleton
    fun provideEmpleadoDao (ocupacionDb: OcupacionDb): EmpleadoDao {
        return ocupacionDb.EmpleadoDao()
    }

    @Provides
    @Singleton
    fun provideEmpleadoRepositoryImpl(empleadoDao: EmpleadoDao): EmpleadoRepositoryImpl{
        return EmpleadoRepositoryImpl(empleadoDao)
    }
    @Provides
    @Singleton
    fun provideEmpleadoRepository(impl:EmpleadoRepositoryImpl): EmpleadoRepository{
        return impl
    }

    @Provides
    @Singleton
    fun provideHoraExtraRepository(horaExtraDao: HoraExtraDao): HoraExtraRepository {
        return HoraExtraRepositoryImpl(horaExtraDao)
    }

    @Provides
    @Singleton
    fun providesHoraExtraDao(database: OcupacionDb): HoraExtraDao{
        return database.HoraExtraDao()
    }
}