package doit.study.droid.di;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Component;
import doit.study.droid.activities.SplashActivity;
import doit.study.droid.fragments.TopicsChooserFragment;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent {
    void inject(Application application);
    void inject(SplashActivity activity);
    void inject(TopicsChooserFragment fragment);
}
