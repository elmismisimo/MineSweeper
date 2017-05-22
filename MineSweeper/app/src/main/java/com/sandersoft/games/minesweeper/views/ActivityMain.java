package com.sandersoft.games.minesweeper.views;

import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

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
            /*if (Build.VERSION.SDK_INT >= 21) {
                int background = getResources().getColor(R.color.color_LBackground);
                getWindow().setNavigationBarColor(background);
                getWindow().setStatusBarColor(background);
            }*/
        }

        if (null == savedInstanceState) {
            //place the fragment in the container
            fragmentMain = FragmentMain.getInstance(6, 6, 6);
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        openDialogMenu();
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

    public void openDialogMenu(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_game_menu, null);
        dialogBuilder.setView(dialogView);

        final AlertDialog dialog = dialogBuilder.create();

        Button btn_restart = (Button) dialogView.findViewById(R.id.btn_restart);
        Button btn_quit = (Button) dialogView.findViewById(R.id.btn_quit);
        Button btn_settings = (Button) dialogView.findViewById(R.id.btn_settings);
        btn_restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentMain.controller.startBoard();
                fragmentMain.cellsAdapter.notifyData();
                fragmentMain.updateTopBar();
                startTimer();
                dialog.dismiss();
            }
        });
        btn_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                dialog.dismiss();
            }
        });
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialogMenuSettings();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    public void openDialogMenuSettings(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_game_settings, null);
        dialogBuilder.setView(dialogView);

        final AlertDialog dialog = dialogBuilder.create();

        final Button btn_sound = (Button) dialogView.findViewById(R.id.btn_sound);
        Button btn_theme = (Button) dialogView.findViewById(R.id.btn_theme);
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String sound = getString(R.string.sound) + " " + getString(preferences.getBoolean(Globals.SOUND, false) ? R.string.on : R.string.off);
        btn_sound.setText(sound);
        btn_theme.setText(preferences.getBoolean(Globals.THEME_LIGHT, false) ? R.string.light : R.string.dark);
        btn_sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.edit().putBoolean(Globals.SOUND, !preferences.getBoolean(Globals.SOUND, false)).apply();
                String sound = getString(R.string.sound) + " " + getString(preferences.getBoolean(Globals.SOUND, false) ? R.string.on : R.string.off);
                btn_sound.setText(sound);
            }
        });
        btn_theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.edit().putBoolean(Globals.THEME_LIGHT, !preferences.getBoolean(Globals.THEME_LIGHT, false)).apply();
                recreate();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    public void openDialogCompleted(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_game_completed, null);
        dialogBuilder.setView(dialogView);

        final AlertDialog dialog = dialogBuilder.create();

        TextView lbl_completed = (TextView) dialogView.findViewById(R.id.lbl_completed);
        String completed = getString(R.string.completed);
        completed = completed.replace("XXX", fragmentMain.controller.getCols() + "x" + fragmentMain.controller.getRows());
        completed = completed.replace("YYY", fragmentMain.lbl_time.getText().toString());
        lbl_completed.setText(completed);
        Button btn_new_game = (Button) dialogView.findViewById(R.id.btn_new_game);
        btn_new_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentMain.controller.startBoard();
                fragmentMain.cellsAdapter.notifyData();
                fragmentMain.updateTopBar();
                startTimer();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
