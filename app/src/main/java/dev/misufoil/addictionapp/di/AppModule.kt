package dev.misufoil.addictionapp.di

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationManagerCompat
import androidx.work.WorkManager
import com.example.activity_api.MainActivityIntentRouter
import com.example.activity_impl.MainActivityIntentRouterImpl
import dagger.Binds
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
interface AppModule {

    @Binds
    fun bindMainActivityIntentRouter(intentRouter: MainActivityIntentRouterImpl): MainActivityIntentRouter


    companion object {
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

        @Provides
        @Singleton
        fun provideContext(@ApplicationContext context: Context): Context {
            return context
        }

        @Provides
        @Singleton
        fun provideNotificationManager(
            @ApplicationContext context: Context
        ): NotificationManagerCompat {
            val notificationManagerCompat = NotificationManagerCompat.from(context)
            val channel = NotificationChannel(
                "addiction_channel",
                "Main Channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManagerCompat.createNotificationChannel(channel)
            return notificationManagerCompat
        }

        @Provides
        @Singleton
        fun provideWorkManager(@ApplicationContext context: Context): WorkManager {
            return WorkManager.getInstance(context)
        }
    }
}