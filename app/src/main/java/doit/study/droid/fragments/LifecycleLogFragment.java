package doit.study.droid.fragments;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import timber.log.Timber;

/**
 * This abstract class extends the Fragment class and overrides lifecycle
 * callbacks for logging various lifecycle events.
 * See <a href="the complete Activity/Fragment lifecycle">
 *     https://raw.githubusercontent.com/mgolokhov/android-lifecycle/master/complete_android_fragment_lifecycle.png
 *     </a>
 */

public abstract class LifecycleLogFragment extends Fragment {
    protected boolean DEBUG = false;

    private void log(String message){
        if (DEBUG) Timber.d(message + " Hash: " + hashCode());
    }
    private void logWithState(String message, Bundle savedInstanceState){
        if (DEBUG){
            if (savedInstanceState == null)
                log(message + " brand new");
            else
                log(message + " re-creation");
        }
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        log("onAttach");
    }
    // Called to do _initial_ creation of a fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logWithState("onCreate", savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        logWithState("onCreateView", savedInstanceState);
        return super.onCreateView(inflater, parent, savedInstanceState);
    }
    @Override
    public  void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        logWithState("onCreateView", savedInstanceState);
    }
    @Override
    public void onViewStateRestored(Bundle savedInstanceState){
        super.onViewStateRestored(savedInstanceState);
        logWithState("onViewStateRestored", savedInstanceState);
    }
    // Called when the Fragment is visible to the user
    @Override
    public void onStart() {
        super.onStart();
        log("onStart");
    }
    @Override
    public void onResume(){
        super.onResume();
        log("onResume");
    }
    @Override
    public void onPause(){
        log("onPause");
        super.onPause();
    }
    @Override
    public void onStop(){
        log("onStop");
        super.onStop();
    }
    @Override
    public void onDestroyView(){
        log("onDestroyView");
        super.onDestroyView();
    }
    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        log("onSaveInstanceState");
    }
    @Override
    public void onDestroy(){
        log("onDestroy");
        super.onDestroy();
    }
    @Override
    public void onDetach(){
        log("onDetach");
        super.onDetach();
    }
}
