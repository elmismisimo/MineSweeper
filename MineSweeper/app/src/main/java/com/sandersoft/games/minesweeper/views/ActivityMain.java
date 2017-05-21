package com.sandersoft.games.minesweeper.views;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sandersoft.games.minesweeper.R;
import com.sandersoft.games.minesweeper.utils.Globals;

public class ActivityMain extends AppCompatActivity {

    FragmentMain fragmentMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (null == savedInstanceState) {
            //place the fragment in the container
            fragmentMain = FragmentMain.getInstance(20,20);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.main_fragment, fragmentMain, Globals.MAIN_FRAGMENT);
            ft.commit();
        } else {
            //recover the values after a destroy
            fragmentMain = (FragmentMain) getFragmentManager().findFragmentByTag(Globals.MAIN_FRAGMENT);
        }
    }
}
