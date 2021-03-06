package com.sandersoft.games.minesweeper.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.sandersoft.games.minesweeper.R;
import com.sandersoft.games.minesweeper.SoundManager;
import com.sandersoft.games.minesweeper.controllers.BoardController;
import com.sandersoft.games.minesweeper.utils.Globals;

/**
 * Created by Sander on 22/05/2017.
 */

public class FragmentMain extends Fragment {

    View rootview;
    LinearLayout lay_main_layout;
    ViewFlipper sld_main;
    TextView lbl_title;
    TextView lbl_subtitle;
    Button btn_play;
    Button btn_settings;
    ImageView img_sandersoft;

    LinearLayout lay_settings;
    TextView lbl_title_settings;
    Button btn_sound;
    Button btn_google;
    Button btn_theme;

    LinearLayout lay_game_play;
    TextView lbl_play;
    Button btn_easy;
    Button btn_medium;
    Button btn_hard;
    Button btn_custom;

    LinearLayout lay_main_custom;
    TextView lbl_custom_title;
    TextView lbl_custom_subtitle;
    TextView lbl_cols;
    EditText fld_cols;
    TextView lbl_rows;
    EditText fld_rows;
    TextView lbl_mines;
    EditText fld_mines;
    Button btn_play_custom;

    LinearLayout lay_about;
    ImageView img_logo_about;
    TextView lbl_sandersoft;
    TextView lbl_created;
    TextView lbl_alexander;
    TextView lbl_version_title;
    TextView lbl_version;
    Button btn_support;
    Button btn_rate;

    LinearLayout lay_google;
    TextView lbl_title_google;
    TextView lbl_subtitle_google;
    Button btn_google_conn;
    Button btn_leaderboards;
    Button btn_achievements;

    int pos = 0;

    public FragmentMain(){}
    public static FragmentMain getInstance(int pos){
        FragmentMain frag = new FragmentMain();
        frag.pos = pos;
        //if (!ApplicationMineSweeper.getGameManager().isConnectedGooglePlay())
        //    ApplicationMineSweeper.getGameManager().connectGP();
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_main, container, false);

        if (null != savedInstanceState){
            pos = savedInstanceState.getInt(Globals.MAIN_POS);
        }

        //instantiates the views of the layout
        defineElements();

        return rootview;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //get objects backedup so we can reload the activity with the same infromation
        outState.putInt(Globals.MAIN_POS, pos);
    }

    public void defineElements(){
        lay_main_layout = (LinearLayout) rootview.findViewById(R.id.lay_main_layout);
        sld_main = (ViewFlipper) rootview.findViewById(R.id.sld_main);
        lbl_title = (TextView) rootview.findViewById(R.id.lbl_title);
        lbl_subtitle = (TextView) rootview.findViewById(R.id.lbl_subtitle);
        btn_play = (Button) rootview.findViewById(R.id.btn_play);
        btn_settings = (Button) rootview.findViewById(R.id.btn_settings);
        img_sandersoft = (ImageView) rootview.findViewById(R.id.img_sandersoft);

        lay_settings = (LinearLayout) rootview.findViewById(R.id.lay_settings);
        lbl_title_settings = (TextView) rootview.findViewById(R.id.lbl_title_settings);
        btn_sound = (Button) rootview.findViewById(R.id.btn_sound);
        btn_google = (Button) rootview.findViewById(R.id.btn_google);
        btn_theme = (Button) rootview.findViewById(R.id.btn_theme);

        lay_game_play = (LinearLayout) rootview.findViewById(R.id.lay_game_play);
        lbl_play = (TextView) rootview.findViewById(R.id.lbl_play);
        btn_easy = (Button) rootview.findViewById(R.id.btn_easy);
        btn_medium = (Button) rootview.findViewById(R.id.btn_medium);
        btn_hard = (Button) rootview.findViewById(R.id.btn_hard);
        btn_custom = (Button) rootview.findViewById(R.id.btn_custom);

        lay_main_custom = (LinearLayout) rootview.findViewById(R.id.lay_main_custom);
        lbl_custom_title = (TextView) rootview.findViewById(R.id.lbl_custom_title);
        lbl_custom_subtitle = (TextView) rootview.findViewById(R.id.lbl_custom_subtitle);
        lbl_cols = (TextView) rootview.findViewById(R.id.lbl_cols);
        fld_cols = (EditText) rootview.findViewById(R.id.fld_cols);
        lbl_rows = (TextView) rootview.findViewById(R.id.lbl_rows);
        fld_rows = (EditText) rootview.findViewById(R.id.fld_rows);
        lbl_mines = (TextView) rootview.findViewById(R.id.lbl_mines);
        fld_mines = (EditText) rootview.findViewById(R.id.fld_mines);
        btn_play_custom = (Button) rootview.findViewById(R.id.btn_play_custom);

        lay_about = (LinearLayout) rootview.findViewById(R.id.lay_about);
        img_logo_about = (ImageView) rootview.findViewById(R.id.img_logo_about);
        lbl_sandersoft = (TextView) rootview.findViewById(R.id.lbl_sandersoft);
        lbl_created = (TextView) rootview.findViewById(R.id.lbl_created);
        lbl_alexander = (TextView) rootview.findViewById(R.id.lbl_alexander);
        lbl_version_title = (TextView) rootview.findViewById(R.id.lbl_version_title);
        lbl_version = (TextView) rootview.findViewById(R.id.lbl_version);
        btn_support = (Button) rootview.findViewById(R.id.btn_support);
        btn_rate = (Button) rootview.findViewById(R.id.btn_rate);

        lay_google = (LinearLayout) rootview.findViewById(R.id.lay_google);
        lbl_title_google = (TextView) rootview.findViewById(R.id.lbl_title_google);
        lbl_subtitle_google = (TextView) rootview.findViewById(R.id.lbl_subtitle_google);
        btn_google_conn = (Button) rootview.findViewById(R.id.btn_google_conn);
        btn_leaderboards = (Button) rootview.findViewById(R.id.btn_leaderboards);
        btn_achievements = (Button) rootview.findViewById(R.id.btn_achievements);

        updateSoundButton();
        updateThemeButton();
        updateGoogleButtons();
        lbl_version.setText(getVersion());
        sld_main.setDisplayedChild(pos);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        //enable buttons
        btn_medium.setEnabled(preferences.getBoolean(Globals.MAIN_MEDIUM, false));
        btn_hard.setEnabled(preferences.getBoolean(Globals.MAIN_HARD, false));

        //verify if there is light theme
        setTheme();

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
                play(BoardController.Type.Easy);
            }
        });
        btn_medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play(BoardController.Type.Medium);
            }
        });
        btn_hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play(BoardController.Type.Hard);
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
                changeSettingSound();
                updateSoundButton();
                SoundManager.playMenuIn(getActivity());
            }
        });
        btn_theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeSettingTheme();
                updateThemeButton();
                SoundManager.playMenuIn(getActivity());
                ((ActivityMain)getActivity()).getGameManager().unlockAchievement(R.string.achievement_oh_my_eyes);
            }
        });
        btn_play_custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cols = fld_cols.getText().toString().trim();
                String rows = fld_rows.getText().toString().trim();
                String mines = fld_mines.getText().toString().trim();
                play(BoardController.Type.Custom,
                        Integer.valueOf(cols.length() > 0 ? cols : "0"),
                        Integer.valueOf(rows.length() > 0 ? rows : "0"),
                        Integer.valueOf(mines.length() > 0 ? mines : "0"));
            }
        });
        img_sandersoft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToAbout();
                SoundManager.playMenuIn(getActivity());
                ((ActivityMain)getActivity()).getGameManager().unlockAchievement(R.string.achievement_whos_that_guy);
            }
        });
        btn_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogSupport();
            }
        });
        btn_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRate();
            }
        });
        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToGoogle();
                SoundManager.playMenuIn(getActivity());
            }
        });
        btn_google_conn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferences.edit().putBoolean(Globals.GAMES_CONNECT, !preferences.getBoolean(Globals.GAMES_CONNECT, true)).apply();
                updateGoogleButtons();
                if (preferences.getBoolean(Globals.GAMES_CONNECT, true))
                    ((ActivityMain)getActivity()).getGameManager().connectGP();
                else
                    ((ActivityMain)getActivity()).getGameManager().disconnectGP();
                SoundManager.playMenuIn(getActivity());
            }
        });
        btn_leaderboards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ActivityMain)getActivity()).getGameManager().seeScoreBoard(getActivity());
            }
        });
        btn_achievements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ActivityMain)getActivity()).getGameManager().seeAchievements(getActivity());
            }
        });
    }
    public void setTheme(){
        boolean light = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(Globals.THEME_LIGHT, false);
        //layout_main_menu
        lay_main_layout.setBackgroundResource(light ? R.color.color_LBackground : R.color.color_Background);
        lbl_title.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        lbl_subtitle.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        btn_play.setBackgroundResource(light ? R.drawable.cell_l : R.drawable.cell);
        btn_play.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        btn_settings.setBackgroundResource(light ? R.drawable.cell_l : R.drawable.cell);
        btn_settings.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        img_sandersoft.setBackgroundResource(light ? R.drawable.cell_l : R.drawable.cell);
        img_sandersoft.setImageResource(light ? R.mipmap.logo_sander_soft_blk : R.mipmap.logo_sander_soft);
        //layout_game_settings
        lay_settings.setBackgroundResource(light ? R.color.color_LBackground : R.color.color_Background);
        lbl_title_settings.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        btn_sound.setBackgroundResource(light ? R.drawable.cell_l : R.drawable.cell);
        btn_sound.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        btn_google.setBackgroundResource(light ? R.drawable.cell_l : R.drawable.cell);
        btn_google.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        btn_theme.setBackgroundResource(light ? R.drawable.cell_l : R.drawable.cell);
        btn_theme.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        //layout_play
        lay_game_play.setBackgroundResource(light ? R.color.color_LBackground : R.color.color_Background);
        lbl_play.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        btn_easy.setBackgroundResource(light ? R.drawable.cell_l : R.drawable.cell);
        btn_easy.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        btn_medium.setBackgroundResource(light ? R.drawable.cell_l : R.drawable.cell);
        btn_medium.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        btn_hard.setBackgroundResource(light ? R.drawable.cell_l : R.drawable.cell);
        btn_hard.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        btn_custom.setBackgroundResource(light ? R.drawable.cell_l : R.drawable.cell);
        btn_custom.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        //layout_custom
        lay_main_custom.setBackgroundResource(light ? R.color.color_LBackground : R.color.color_Background);
        lbl_custom_title.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        lbl_custom_subtitle.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        lbl_cols.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        fld_cols.setBackgroundResource(light ? R.color.color_LText : R.color.color_Text);
        fld_cols.setTextColor(getResources().getColor(light ? R.color.color_LBackground : R.color.color_Background));
        lbl_rows.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        fld_rows.setBackgroundResource(light ? R.color.color_LText : R.color.color_Text);
        fld_rows.setTextColor(getResources().getColor(light ? R.color.color_LBackground : R.color.color_Background));
        lbl_mines.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        fld_mines.setBackgroundResource(light ? R.color.color_LText : R.color.color_Text);
        fld_mines.setTextColor(getResources().getColor(light ? R.color.color_LBackground : R.color.color_Background));
        btn_play_custom.setBackgroundResource(light ? R.drawable.cell_l : R.drawable.cell);
        btn_play_custom.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        //layout_about
        lay_about.setBackgroundResource(light ? R.color.color_LBackground : R.color.color_Background);
        img_logo_about.setImageResource(light ? R.mipmap.logo_sander_soft_blk : R.mipmap.logo_sander_soft);
        lbl_sandersoft.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        lbl_created.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        lbl_alexander.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        lbl_version_title.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        lbl_version.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        btn_support.setBackgroundResource(light ? R.drawable.cell_l : R.drawable.cell);
        btn_support.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        btn_rate.setBackgroundResource(light ? R.drawable.cell_l : R.drawable.cell);
        btn_rate.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        //layout_google
        lay_google.setBackgroundResource(light ? R.color.color_LBackground : R.color.color_Background);
        lbl_title_google.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        lbl_subtitle_google.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        btn_google_conn.setBackgroundResource(light ? R.drawable.cell_l : R.drawable.cell);
        btn_google_conn.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        btn_leaderboards.setBackgroundResource(light ? R.drawable.cell_l : R.drawable.cell);
        btn_leaderboards.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        btn_achievements.setBackgroundResource(light ? R.drawable.cell_l : R.drawable.cell);
        btn_achievements.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
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
    public void updateGoogleButtons(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String google_state = getString(R.string.google_play) + " " +
                getString(preferences.getBoolean(Globals.GAMES_CONNECT, true)
                        ? (((ActivityMain)getActivity()).getGameManager().isConnectedGooglePlay() ? R.string.connected : R.string.on)
                        : R.string.disconnected);
        btn_google_conn.setText(google_state);
        btn_leaderboards.setEnabled(preferences.getBoolean(Globals.GAMES_CONNECT, true) && ((ActivityMain)getActivity()).getGameManager().isConnectedGooglePlay());
        btn_achievements.setEnabled(preferences.getBoolean(Globals.GAMES_CONNECT, true) && ((ActivityMain)getActivity()).getGameManager().isConnectedGooglePlay());
    }

    public void changeSettingSound(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        preferences.edit().putBoolean(Globals.SOUND, !preferences.getBoolean(Globals.SOUND, true)).apply();
    }
    public void changeSettingTheme(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        preferences.edit().putBoolean(Globals.THEME_LIGHT, !preferences.getBoolean(Globals.THEME_LIGHT, false)).apply();
        setTheme();
        //getActivity().recreate();
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
    public void moveToGoogle(){
        if (pos == 5) return;
        if (pos > 5) {
            sld_main.setInAnimation(getActivity(), R.anim.animation_in_right);
            sld_main.setOutAnimation(getActivity(), R.anim.animation_out_right);
        } else {
            sld_main.setInAnimation(getActivity(), R.anim.animation_in_left);
            sld_main.setOutAnimation(getActivity(), R.anim.animation_out_left);
        }
        pos = 5;
        sld_main.setDisplayedChild(pos);
    }

    public boolean onBackPressed(){
        SoundManager.playMenuOut(getActivity());
        if (pos == 0)
            return false;
        else if (pos == 1 || pos == 2 || pos == 4)
            moveToMain();
        else if (pos == 5)
            moveToSettings();
        else
            moveToPlay();
        return true;
    }

    public void play(BoardController.Type type){
        if (type == BoardController.Type.Easy) play(type, 6,6,6);
        if (type == BoardController.Type.Medium) play(type, 10,10,20);
        if (type == BoardController.Type.Hard) play(type, 15,27,75);
    }
    public void play(BoardController.Type type, int cols, int rows, int mines){
        if (cols < 5) cols = 5;
        if (cols > 15) cols = 15;
        if (rows < 5) rows = 5;
        if (mines < 5) mines = 5;
        if (cols * rows > 1000){
            cols = 15;
            rows = 67;
        }
        if (mines >= cols*rows) mines = cols*rows - 1;

        //open activity
        /*Intent playIntent = new Intent(getActivity(), ActivityGame.class);
        Bundle extras = new Bundle();
        extras.putInt(Globals.MAIN_TYPE, type.getValue());
        extras.putInt(Globals.MAIN_COLS, cols);
        extras.putInt(Globals.MAIN_ROWS, rows);
        extras.putInt(Globals.MAIN_MINES, mines);
        playIntent.putExtras(extras);
        startActivity(playIntent);*/
        ((ActivityMain)getActivity()).changeToGame(type, cols, rows, mines);

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

    public void openRate(){
        final String appPackageName = getActivity().getPackageName(); // getPackageName() from Context or Activity object
        try {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName));
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } catch (android.content.ActivityNotFoundException anfe) {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName));
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }
    }

    //publicidad
    public InterstitialAd interstitialAd;

    /**
     * Abrimos el dialogo de apoyo
     */
    public void openDialogSupport(){
        //publicidad
        setNewInterstitialRequest();

        AlertDialog.Builder supportDialog = new AlertDialog.Builder(getActivity());
        supportDialog.setTitle(R.string.menu_support);
        supportDialog.setMessage(R.string.support_desc);
        supportDialog.setPositiveButton("...", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                //publicidad
                displayInterstitial();
            }
        });
        supportDialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                dialog.cancel();
            }
        });
        final AlertDialog dialog = supportDialog.create();
        dialog.show();

        ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setText(R.string.support_btn);
            }
        });

    }
    /**
     * Carga un nuevo anuncio para el itersticial
     */
    public void setNewInterstitialRequest(){
        getActivity().runOnUiThread(new Runnable() {
            public void run()
            {
                try {
                    interstitialAd = new InterstitialAd(getActivity());
                    //This id is not included, here you cn put the admob id of the ad
                    interstitialAd.setAdUnitId("...");

                    // Create ad request.
                    AdRequest adRequest = new AdRequest.Builder()
                            //.addTestDevice("Place the Test Device ID here")
                            .build();

                    // Begin loading your interstitial.
                    interstitialAd.loadAd(adRequest);
                }
                catch (Exception ex){
                    String ext = ex.toString();
                }
            }
        });
    }
    /**
     *  Invoke displayInterstitial() when you are ready to display an interstitial.
     */
    public void displayInterstitial() {
        getActivity().runOnUiThread(new Runnable() {
            public void run()
            {
                try {
                    if (interstitialAd.isLoaded()) {
                        interstitialAd.show();
                    }
                    //else
                    //	showToast("Not loaded");
                }
                catch (Exception ex){}

            }
        });
    }




}
