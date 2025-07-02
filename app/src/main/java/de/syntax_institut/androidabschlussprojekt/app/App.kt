package de.syntax_institut.androidabschlussprojekt.app

import android.app.Application
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import de.syntax_institut.androidabschlussprojekt.features.user.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)
        startKoin {
            androidContext(this@App)
            modules(appModule)
        }
    }
}