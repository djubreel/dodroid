package doit.study.droid.app

import android.app.Application

import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import com.google.android.gms.analytics.Tracker

import doit.study.droid.BuildConfig
import doit.study.droid.data.QuizDataClient
import doit.study.droid.di.components.ApplicationComponent
import doit.study.droid.di.modules.ApplicationModule
import doit.study.droid.di.components.DaggerApplicationComponent
import io.fabric.sdk.android.Fabric
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject


abstract class BaseApp : Application() {
    lateinit var component: ApplicationComponent
    @Inject
    lateinit var tracker: Tracker

    fun setupDagger() {
        component = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this)).build()
    }

    fun getApplicationComponent(): ApplicationComponent {
        return component
    }

    val quizService: QuizDataClient by lazy {
        val httpClient = OkHttpClient.Builder()
        //httpClient.addInterceptor(createLogInterceptor());
        val builder = Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
        val retrofit = builder
                .client(httpClient.build())
                .build()
        retrofit.create(QuizDataClient::class.java)
    }


    override fun onCreate() {
        super.onCreate()
        setupDagger()
        setupCrashlytics()
    }

    private fun setupCrashlytics() {
        val core = CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()
        Fabric.with(this, Crashlytics.Builder().core(core).build(), Crashlytics())
    }

    private fun createLogInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        val level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        return logging.setLevel(level)
    }

    companion object {
        private const val API_BASE_URL = "https://raw.githubusercontent.com/mgolokhov/dodroid_questions/master/"
    }
}
