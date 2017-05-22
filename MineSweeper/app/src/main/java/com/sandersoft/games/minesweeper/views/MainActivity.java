package com.sandersoft.games.minesweeper.views;

import android.app.FragmentTransaction;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sandersoft.games.minesweeper.R;
import com.sandersoft.games.minesweeper.utils.Globals;

public class MainActivity extends AppCompatActivity {

    FragmentMain fragmentMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            fragmentMain = FragmentMain.getInstance();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.main_fragment, fragmentMain, Globals.MAIN_FRAGMENT);
            ft.commit();
        } else {
            //recover the values after a destroy
            fragmentMain = (FragmentMain) getFragmentManager().findFragmentByTag(Globals.MAIN_FRAGMENT);
        }
    }

    @Override
    public void onBackPressed() {
        if (!fragmentMain.onBackPressed())
            super.onBackPressed();
    }
}
