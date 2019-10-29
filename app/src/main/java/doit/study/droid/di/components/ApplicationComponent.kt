package doit.study.droid.di.components

import android.app.Activity
import android.app.Application
import dagger.Component
import doit.study.droid.di.modules.ApplicationModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {

    fun inject (target: Activity)

    fun getApplication(): Application

}