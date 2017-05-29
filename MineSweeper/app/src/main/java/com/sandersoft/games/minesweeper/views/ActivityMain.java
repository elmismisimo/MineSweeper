package com.sandersoft.games.minesweeper.views;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sandersoft.games.minesweeper.GPManager;
import com.sandersoft.games.minesweeper.R;
import com.sandersoft.games.minesweeper.controllers.BoardController;
import com.sandersoft.games.minesweeper.utils.Globals;

public class ActivityMain extends AppCompatActivity {

    public GPManager gameManager;

    FragmentMain fragmentMain;
    FragmentGame fragmentGame;
    public static ActivityMain This;
    boolean inGame = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameManager = new GPManager(this);

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
            changeToMenu(0);
        } else {
            //recover the values after a destroy
            inGame = savedInstanceState.getBoolean("inGame");
            if (!inGame)
                fragmentMain = (FragmentMain) getFragmentManager().findFragmentByTag(Globals.MAIN_FRAGMENT);
            else
                fragmentGame = (FragmentGame) getFragmentManager().findFragmentByTag(Globals.GAME_FRAGMENT);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("inGame", inGame);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(Globals.GAMES_CONNECT, true))
            gameManager.connectGP();
    }

    @Override
    public void onDestroy() {
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(Globals.GAMES_CONNECT, true)
                && gameManager.isConnectedGooglePlay())
            gameManager.disconnectGP();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (!inGame && !fragmentMain.onBackPressed())
            super.onBackPressed();
        else if (inGame)
            fragmentGame.onBackPressed();
    }

    public void changeToGame(BoardController.Type type, int cols, int rows, int mines){
        inGame = true;
        fragmentGame = FragmentGame.getInstance(type, cols, rows, mines);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(R.id.main_fragment, fragmentGame, Globals.GAME_FRAGMENT);
        ft.commit();
    }
    public void changeToMenu(int pos){
        inGame = false;
        fragmentMain = FragmentMain.getInstance(pos);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        ft.replace(R.id.main_fragment, fragmentMain, Globals.MAIN_FRAGMENT);
        ft.commit();
    }

    public GPManager getGameManager(){
        return gameManager;
    }

    public void onGPConnected(){
        if (!inGame)
            fragmentMain.updateGoogleButtons();
        else
            fragmentGame.updateGoogleButtons();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        gameManager.onActivityResult(requestCode, resultCode, data);
    }
}
