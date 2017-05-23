package com.sandersoft.games.minesweeper.views;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.sandersoft.games.minesweeper.R;
import com.sandersoft.games.minesweeper.SoundManager;
import com.sandersoft.games.minesweeper.utils.Globals;

/**
 * Created by Sander on 22/05/2017.
 */

public class FragmentMain extends Fragment {

    public FragmentMain(){}

    ViewFlipper sld_main;
    Button btn_play;
    Button btn_settings;
    ImageView img_sandersoft;
    Button btn_sound;
    Button btn_theme;
    Button btn_easy;
    Button btn_medium;
    Button btn_hard;
    Button btn_custom;
    EditText fld_cols;
    EditText fld_rows;
    EditText fld_mines;
    Button btn_play_custom;
    TextView lbl_version;

    int pos = 0;

    public static FragmentMain getInstance(){
        FragmentMain frag = new FragmentMain();
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_main, container, false);

        if (null != savedInstanceState){
            pos = savedInstanceState.getInt(Globals.MAIN_POS);
        }

        //instantiates the views of the layout
        defineElements(rootview);

        return rootview;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //get objects backedup so we can reload the activity with the same infromation
        outState.putInt(Globals.MAIN_POS, pos);
    }

    public void defineElements(View rootview){
        sld_main = (ViewFlipper) rootview.findViewById(R.id.sld_main);
        btn_play = (Button) rootview.findViewById(R.id.btn_play);
        btn_settings = (Button) rootview.findViewById(R.id.btn_settings);
        img_sandersoft = (ImageView) rootview.findViewById(R.id.img_sandersoft);
        btn_sound = (Button) rootview.findViewById(R.id.btn_sound);
        btn_theme = (Button) rootview.findViewById(R.id.btn_theme);
        btn_easy = (Button) rootview.findViewById(R.id.btn_easy);
        btn_medium = (Button) rootview.findViewById(R.id.btn_medium);
        btn_hard = (Button) rootview.findViewById(R.id.btn_hard);
        btn_custom = (Button) rootview.findViewById(R.id.btn_custom);
        fld_cols = (EditText) rootview.findViewById(R.id.fld_cols);
        fld_rows = (EditText) rootview.findViewById(R.id.fld_rows);
        fld_mines = (EditText) rootview.findViewById(R.id.fld_mines);
        btn_play_custom = (Button) rootview.findViewById(R.id.btn_play_custom);
        lbl_version = (TextView) rootview.findViewById(R.id.lbl_version);

        updateSoundButton();
        updateThemeButton();
        lbl_version.setText(getVersion());
        sld_main.setDisplayedChild(pos);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        //set listeners
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToPlay();
                SoundManager.playMenuIn(getActivity());
            }
        });
        btn_easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play(0);
            }
        });
        btn_medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play(1);
            }
        });
        btn_hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play(2);
            }
        });
        btn_custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToCustom();
                SoundManager.playMenuIn(getActivity());
            }
        });
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToSettings();
                SoundManager.playMenuIn(getActivity());
            }
        });
        btn_sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferences.edit().putBoolean(Globals.SOUND, !preferences.getBoolean(Globals.SOUND, true)).apply();
                updateSoundButton();
                SoundManager.playMenuIn(getActivity());

            }
        });
        btn_theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferences.edit().putBoolean(Globals.THEME_LIGHT, !preferences.getBoolean(Globals.THEME_LIGHT, false)).apply();
                updateThemeButton();
                SoundManager.playMenuIn(getActivity());
                getActivity().recreate();
            }
        });
        btn_play_custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cols = fld_cols.getText().toString().trim();
                String rows = fld_rows.getText().toString().trim();
                String mines = fld_mines.getText().toString().trim();
                play(-1, Integer.valueOf(cols.length() > 0 ? cols : "0"),
                        Integer.valueOf(rows.length() > 0 ? rows : "0"),
                        Integer.valueOf(mines.length() > 0 ? mines : "0"));
            }
        });
        img_sandersoft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToAbout();
                SoundManager.playMenuIn(getActivity());
            }
        });
    }
    public void updateSoundButton(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sound = getString(R.string.sound) + " " + getString(preferences.getBoolean(Globals.SOUND, true) ? R.string.on : R.string.off);
        btn_sound.setText(sound);
    }
    public void updateThemeButton(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        btn_theme.setText(preferences.getBoolean(Globals.THEME_LIGHT, false) ? R.string.light : R.string.dark);
    }

    public void moveToMain(){
        if (pos == 0) return;
        if (pos > 0) {
            sld_main.setInAnimation(getActivity(), R.anim.animation_in_right);
            sld_main.setOutAnimation(getActivity(), R.anim.animation_out_right);
        } else {
            sld_main.setInAnimation(getActivity(), R.anim.animation_in_left);
            sld_main.setOutAnimation(getActivity(), R.anim.animation_out_left);
        }
        pos = 0;
        sld_main.setDisplayedChild(pos);
    }
    public void moveToSettings(){
        if (pos == 1) return;
        if (pos > 1) {
            sld_main.setInAnimation(getActivity(), R.anim.animation_in_right);
            sld_main.setOutAnimation(getActivity(), R.anim.animation_out_right);
        } else {
            sld_main.setInAnimation(getActivity(), R.anim.animation_in_left);
            sld_main.setOutAnimation(getActivity(), R.anim.animation_out_left);
        }
        pos = 1;
        sld_main.setDisplayedChild(pos);
    }
    public void moveToAbout(){
        if (pos == 4) return;
        if (pos > 4) {
            sld_main.setInAnimation(getActivity(), R.anim.animation_in_right);
            sld_main.setOutAnimation(getActivity(), R.anim.animation_out_right);
        } else {
            sld_main.setInAnimation(getActivity(), R.anim.animation_in_left);
            sld_main.setOutAnimation(getActivity(), R.anim.animation_out_left);
        }
        pos = 4;
        sld_main.setDisplayedChild(pos);
    }
    public void moveToPlay(){
        if (pos == 2) return;
        if (pos > 2) {
            sld_main.setInAnimation(getActivity(), R.anim.animation_in_right);
            sld_main.setOutAnimation(getActivity(), R.anim.animation_out_right);
        } else {
            sld_main.setInAnimation(getActivity(), R.anim.animation_in_left);
            sld_main.setOutAnimation(getActivity(), R.anim.animation_out_left);
        }
        pos = 2;
        sld_main.setDisplayedChild(pos);
    }
    public void moveToCustom(){
        if (pos == 3) return;
        if (pos > 3) {
            sld_main.setInAnimation(getActivity(), R.anim.animation_in_right);
            sld_main.setOutAnimation(getActivity(), R.anim.animation_out_right);
        } else {
            sld_main.setInAnimation(getActivity(), R.anim.animation_in_left);
            sld_main.setOutAnimation(getActivity(), R.anim.animation_out_left);
        }
        pos = 3;
        sld_main.setDisplayedChild(pos);
    }

    public boolean onBackPressed(){
        SoundManager.playMenuOut(getActivity());
        if (pos == 0)
            return false;
        if (pos == 1 || pos == 2 || pos == 4)
            moveToMain();
        else
            moveToPlay();
        return true;
    }

    public void play(int type){
        if (type == 0) play(type, 6,6,6);
        if (type == 1) play(type, 10,10,20);
        if (type == 2) play(type, 20,20,60);
    }
    public void play(int type, int cols, int rows, int mines){
        if (cols < 5) cols = 5;
        if (rows < 5) rows = 5;
        if (cols * rows > 1000){
            cols = 20;
            rows = 50;
        }
        if (mines >= cols*rows) mines = cols*rows - 1;

        Intent playIntent = new Intent(getActivity(), ActivityGame.class);
        Bundle extras = new Bundle();
        extras.putInt(Globals.MAIN_TYPE, type);
        extras.putInt(Globals.MAIN_COLS, cols);
        extras.putInt(Globals.MAIN_ROWS, rows);
        extras.putInt(Globals.MAIN_MINES, mines);
        playIntent.putExtras(extras);
        startActivity(playIntent);
        SoundManager.playOpen(getActivity());
    }

    public String getVersion() {
        try
        {
            return getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
        }
        catch(Exception ex)
        {
            return "1.0.0";
        }
    }
}
