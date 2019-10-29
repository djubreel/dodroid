package doit.study.droid.di.modules

import android.app.Application
import com.google.android.gms.analytics.GoogleAnalytics
import com.google.android.gms.analytics.Tracker
import dagger.Module
import dagger.Provides
import doit.study.droid.R
import javax.inject.Singleton


@Module
class ApplicationModule(var application: Application) {

    @Singleton
    @Provides
    fun provideApplicationContext(): Application {
        return application
    }

    @Provides
    @Singleton
    fun provideTracker(application: Application): Tracker{
        val ga = GoogleAnalytics.getInstance(application)
        ga.enableAutoActivityReports(application)
        return ga.newTracker(R.xml.track_app)
    }

}