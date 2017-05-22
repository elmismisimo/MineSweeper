package com.sandersoft.games.minesweeper.views;

import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sandersoft.games.minesweeper.R;
import com.sandersoft.games.minesweeper.utils.Globals;

public class ActivityMain extends AppCompatActivity {

    FragmentMain fragmentMain;
    public static ActivityMain This;
    AsyncUpdate asyncUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        This = this;

        if (PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(Globals.THEME_LIGHT, false)) {
            setTheme(R.style.AppTheme_Light_NoActionBar);
        }

        if (null == savedInstanceState) {
            //place the fragment in the container
            fragmentMain = FragmentMain.getInstance(50, 20,20);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.main_fragment, fragmentMain, Globals.MAIN_FRAGMENT);
            ft.commit();
        } else {
            //recover the values after a destroy
            fragmentMain = (FragmentMain) getFragmentManager().findFragmentByTag(Globals.MAIN_FRAGMENT);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTimer();
    }

    public void startTimer(){
        stopTimer();
        asyncUpdate = new AsyncUpdate();
        asyncUpdate.execute();
    }
    public void stopTimer(){
        if (asyncUpdate != null) {
            asyncUpdate.cancel(true);
            asyncUpdate.doCancel();
            asyncUpdate = null;
        }
    }

    private class AsyncUpdate extends AsyncTask<Void, Void, Void>{
        boolean goAhead = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            while(goAhead && !fragmentMain.controller.isGameOver()){
                try{
                    Thread.sleep(1000);
                    publishProgress();
                }catch (Exception ex){}
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            //update the top bar
            fragmentMain.updateTopBarTime();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        public void doCancel(){
            goAhead = false;
        }
    }
}
