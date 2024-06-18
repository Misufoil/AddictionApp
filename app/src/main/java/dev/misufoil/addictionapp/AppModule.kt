package dev.misufoil.addictionapp

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.misufoil.core_utils.AndroidLogcatLogger
import dev.misufoil.core_utils.AppDispatchers
import dev.misufoil.core_utils.Logger
import dev.misufoil.database.AddictionDatabase
import dev.misufoil.database.addictionsDataBase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AddictionDatabase {
        return addictionsDataBase(context)
    }

    @Provides
    @Singleton
    fun provideAppCoroutineDispatchers(): AppDispatchers = AppDispatchers()

    @Provides
    fun provideLogger(): Logger = AndroidLogcatLogger()
}