package com.sandersoft.games.minesweeper.views;

import android.app.FragmentTransaction;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sandersoft.games.minesweeper.R;
import com.sandersoft.games.minesweeper.controllers.BoardController;
import com.sandersoft.games.minesweeper.utils.Globals;

public class ActivityGame extends AppCompatActivity {

    FragmentGame fragmentGame;
    public static ActivityGame This;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

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
            Bundle extras = getIntent().getExtras();
            BoardController.Type type = BoardController.Type.Custom;
            int cols = 6;
            int rows = 6;
            int mines = 6;
            try {
                type = BoardController.Type.fromInteger(extras.getInt(Globals.MAIN_TYPE));
                cols = extras.getInt(Globals.MAIN_COLS);
                rows = extras.getInt(Globals.MAIN_ROWS);
                mines = extras.getInt(Globals.MAIN_MINES);
            } catch (Exception ex){}
            //place the fragment in the container
            fragmentGame = FragmentGame.getInstance(type, mines, cols, rows);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.game_fragment, fragmentGame, Globals.GAME_FRAGMENT);
            ft.commit();
        } else {
            //recover the values after a destroy
            fragmentGame = (FragmentGame) getFragmentManager().findFragmentByTag(Globals.GAME_FRAGMENT);
        }

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //openDialogMenu();
        //SoundManager.playMenuIn(this);
        fragmentGame.onBackPressed();
    }

    protected void quit(){
        finish();
    }
}
