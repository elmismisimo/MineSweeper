package com.sandersoft.games.minesweeper.views;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.sandersoft.games.minesweeper.R;
import com.sandersoft.games.minesweeper.SoundManager;
import com.sandersoft.games.minesweeper.controllers.BoardController;
import com.sandersoft.games.minesweeper.models.Cell;
import com.sandersoft.games.minesweeper.utils.Globals;

/**
 * Created by Sander on 21/05/2017.
 */

public class FragmentGame extends Fragment {

    BoardController controller;

    View rootview;
    RelativeLayout lay_game_layout;
    TextView lbl_mines_title;
    TextView lbl_mines;
    TextView lbl_grid;
    TextView lbl_time_title;
    TextView lbl_time;
    HorizontalScrollView scr_board;
    RecyclerView board;
    public CellsAdapter cellsAdapter;
    LinearLayout lay_menu;
    ViewFlipper sld_menu;

    LinearLayout lay_game_menu;
    Button btn_restart;
    Button btn_quit;
    Button btn_settings;
    Button btn_tutorial;

    LinearLayout lay_settings;
    TextView lbl_title_settings;
    Button btn_sound;
    Button btn_google;
    Button btn_theme;

    LinearLayout lay_google;
    TextView lbl_title_google;
    TextView lbl_subtitle_google;
    Button btn_google_conn;
    Button btn_leaderboards;
    Button btn_achievements;

    LinearLayout lay_game_completed;
    TextView lbl_completed_title;
    TextView lbl_completed;
    Button btn_new_game;

    LinearLayout lay_game_over;
    TextView lbl_over_title;
    Button btn_restart_gameover;

    boolean light;
    int pos = 0;
    AsyncUpdate asyncUpdate;

    public FragmentGame(){}

    public static FragmentGame getInstance(BoardController.Type type, int cols, int rows, int mines){
        FragmentGame frag = new FragmentGame();
        frag.controller = new BoardController(frag);
        frag.controller.initiateBoard(type, cols, rows, mines);
        frag.controller.startBoard();
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_game, container, false);

        if (null != savedInstanceState){
            controller = savedInstanceState.getParcelable(Globals.GAME_CONTROLLER);
            controller.setView(this);
        }

        //instantiates the views of the layout
        defineElements();

        if (null != savedInstanceState) {
            int[] arr = savedInstanceState.getIntArray(Globals.GAME_SCROLL);
            scr_board.scrollTo(arr[0], arr[1]);
        }

        //verify if its the first time opening the app, so show tutorial
        if (PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(Globals.GAME_TUTORIAL, true)){
            changeSettingTutorial();
            openTutorialDialog1();
        }

        return rootview;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //get objects backedup so we can reload the activity with the same infromation
        outState.putParcelable(Globals.GAME_CONTROLLER, controller);
        outState.putIntArray(Globals.GAME_SCROLL,
                new int[] {scr_board.getScrollX(), scr_board.getScrollY()});
    }

    @Override
    public void onPause() {
        super.onPause();
        stopTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        startTimer();
    }

    public void defineElements(){
        lay_game_layout = (RelativeLayout) rootview.findViewById(R.id.lay_game_layout);
        lbl_mines_title = (TextView) rootview.findViewById(R.id.lbl_mines_title);
        lbl_mines = (TextView) rootview.findViewById(R.id.lbl_mines);
        lbl_grid = (TextView) rootview.findViewById(R.id.lbl_grid);
        lbl_grid.setText(controller.getType() == BoardController.Type.Custom ? controller.getCols() + "x" + controller.getRows() :
                getString(controller.getType() == BoardController.Type.Easy ? R.string.easy :
                        controller.getType() == BoardController.Type.Medium ? R.string.medium :
                                R.string.hard));
        lbl_time_title = (TextView) rootview.findViewById(R.id.lbl_time_title);
        lbl_time = (TextView) rootview.findViewById(R.id.lbl_time);
        updateTopBar();
        scr_board = (HorizontalScrollView) rootview.findViewById(R.id.scr_board);
        board = (RecyclerView) rootview.findViewById(R.id.board);
        cellsAdapter = new CellsAdapter();
        GridLayoutManager gridLayout = new GridLayoutManager(getActivity(), controller.getCols());
        board.setLayoutManager(gridLayout);
        board.setAdapter(cellsAdapter);

        lay_menu = (LinearLayout) rootview.findViewById(R.id.lay_menu);
        sld_menu = (ViewFlipper) rootview.findViewById(R.id.sld_menu);

        lay_game_menu = (LinearLayout) rootview.findViewById(R.id.lay_game_menu);
        btn_restart = (Button) rootview.findViewById(R.id.btn_restart);
        btn_quit = (Button) rootview.findViewById(R.id.btn_quit);
        btn_settings = (Button) rootview.findViewById(R.id.btn_settings);
        btn_tutorial = (Button) rootview.findViewById(R.id.btn_tutorial);

        lay_settings = (LinearLayout) rootview.findViewById(R.id.lay_settings);
        lbl_title_settings = (TextView) rootview.findViewById(R.id.lbl_title_settings);
        btn_sound = (Button) rootview.findViewById(R.id.btn_sound);
        btn_google = (Button) rootview.findViewById(R.id.btn_google);
        btn_theme = (Button) rootview.findViewById(R.id.btn_theme);

        lay_google = (LinearLayout) rootview.findViewById(R.id.lay_google);
        lbl_title_google = (TextView) rootview.findViewById(R.id.lbl_title_google);
        lbl_subtitle_google = (TextView) rootview.findViewById(R.id.lbl_subtitle_google);
        btn_google_conn = (Button) rootview.findViewById(R.id.btn_google_conn);
        btn_leaderboards = (Button) rootview.findViewById(R.id.btn_leaderboards);
        btn_achievements = (Button) rootview.findViewById(R.id.btn_achievements);

        lay_game_completed = (LinearLayout) rootview.findViewById(R.id.lay_game_completed);
        lbl_completed_title = (TextView) rootview.findViewById(R.id.lbl_completed_title);
        lbl_completed = (TextView) rootview.findViewById(R.id.lbl_completed);
        btn_new_game = (Button) rootview.findViewById(R.id.btn_new_game);

        lay_game_over = (LinearLayout) rootview.findViewById(R.id.lay_game_over);
        lbl_over_title = (TextView) rootview.findViewById(R.id.lbl_over_title);
        btn_restart_gameover = (Button) rootview.findViewById(R.id.btn_restart_gameover);

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        //verify if there is light theme
        setTheme();

        lay_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeMenu(true);
            }
        });
        btn_restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartGame();
                closeMenu(true);
            }
        });
        btn_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitGame();
            }
        });
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToSettings();
            }
        });
        btn_tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTutorialDialog1();
                closeMenu(true);
            }
        });
        btn_sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSettingSound();
                updateSoundButton();
                SoundManager.playMenuIn(getActivity());
            }
        });
        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToGoogle();
            }
        });
        btn_theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeSettingTheme();
                updateThemeButton();
                SoundManager.playMenuIn(getActivity());
                ((ActivityMain)getActivity()).getGameManager().unlockAchievement(R.string.achievement_oh_my_eyes);
            }
        });
        btn_google_conn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                SoundManager.playMenuIn(getActivity());
            }
        });
        btn_achievements.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ActivityMain)getActivity()).getGameManager().seeAchievements(getActivity());
                SoundManager.playMenuIn(getActivity());
            }
        });
        btn_new_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartGame();
                SoundManager.playOpen(getActivity());
                closeMenu(false);
            }
        });
        btn_restart_gameover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartGame();
                SoundManager.playOpen(getActivity());
                closeMenu(false);
            }
        });
    }
    public void setTheme(){
        light = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(Globals.THEME_LIGHT, false);
        //layout_main_menu
        lay_game_layout.setBackgroundResource(light ? R.color.color_LBackground : R.color.color_Background);
        lbl_mines_title.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        lbl_grid.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        lbl_time_title.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        lbl_time.setTextColor(getResources().getColor(light ? R.color.color_LAccent : R.color.color_Accent));
        //layout_game_menu
        lay_game_menu.setBackgroundResource(light ? R.color.color_LBackground : R.color.color_Background);
        btn_restart.setBackgroundResource(light ? R.drawable.cell_l : R.drawable.cell);
        btn_restart.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        btn_quit.setBackgroundResource(light ? R.drawable.cell_l : R.drawable.cell);
        btn_quit.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        btn_settings.setBackgroundResource(light ? R.drawable.cell_l : R.drawable.cell);
        btn_settings.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        btn_tutorial.setBackgroundResource(light ? R.drawable.cell_l : R.drawable.cell);
        btn_tutorial.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        //layout_game_settings
        lay_settings.setBackgroundResource(light ? R.color.color_LBackground : R.color.color_Background);
        lbl_title_settings.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        btn_sound.setBackgroundResource(light ? R.drawable.cell_l : R.drawable.cell);
        btn_sound.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        btn_google.setBackgroundResource(light ? R.drawable.cell_l : R.drawable.cell);
        btn_google.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        btn_theme.setBackgroundResource(light ? R.drawable.cell_l : R.drawable.cell);
        btn_theme.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
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
        //layout_game_completed
        lay_game_completed.setBackgroundResource(light ? R.color.color_LBackground : R.color.color_Background);
        lbl_completed_title.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        lbl_completed.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        btn_new_game.setBackgroundResource(light ? R.drawable.cell_l : R.drawable.cell);
        btn_new_game.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        //layout_game_completed
        lay_game_over.setBackgroundResource(light ? R.color.color_LBackground : R.color.color_Background);
        lbl_over_title.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        btn_restart_gameover.setBackgroundResource(light ? R.drawable.cell_l : R.drawable.cell);
        btn_restart_gameover.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
        //adapter
        cellsAdapter.notifyData();
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
    public void updateGoogleButtons() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String google_state = getString(R.string.google_play) + " " +
                getString(preferences.getBoolean(Globals.GAMES_CONNECT, true)
                        ? (((ActivityMain)getActivity()).getGameManager().isConnectedGooglePlay() ? R.string.connected : R.string.on)
                        : R.string.disconnected);
        btn_google_conn.setText(google_state);
        btn_leaderboards.setEnabled(preferences.getBoolean(Globals.GAMES_CONNECT, true) && ((ActivityMain)getActivity()).getGameManager().isConnectedGooglePlay());
        btn_achievements.setEnabled(preferences.getBoolean(Globals.GAMES_CONNECT, true) && ((ActivityMain)getActivity()).getGameManager().isConnectedGooglePlay());
    }

    public void scrollsToZero(){
        scr_board.scrollTo(0,0);
        board.getLayoutManager().scrollToPosition(0);
    }

    public class CellsAdapter extends RecyclerView.Adapter {
        private final int VIEW_TYPE_CELL = 0;
        private final int VIEW_TYPE_CELL_MARKED = 1;
        private final int VIEW_TYPE_CELL_OPENED = 2;
        private final int VIEW_TYPE_CELL_GAMEOVER = 3;

        public CellsAdapter(){
            setHasStableIds(true);
        }


        public class ItemCellHolder extends RecyclerView.ViewHolder {
            public LinearLayout lay_item;

            public ItemCellHolder(View view) {
                super(view);
                lay_item = (LinearLayout) view.findViewById(R.id.lay_item);
            }
        }
        public class ItemCellMarkedHolder extends RecyclerView.ViewHolder {
            public LinearLayout lay_item;

            public ItemCellMarkedHolder(View view) {
                super(view);
                lay_item = (LinearLayout) view.findViewById(R.id.lay_item);
            }
        }
        public class ItemCellOpenedHolder extends RecyclerView.ViewHolder {
            public LinearLayout lay_item;
            public TextView lbl_number;

            public ItemCellOpenedHolder(View view) {
                super(view);
                lay_item = (LinearLayout) view.findViewById(R.id.lay_item);
                lbl_number = (TextView) view.findViewById(R.id.lbl_number);
            }
        }
        public class ItemCellGameOverHolder extends RecyclerView.ViewHolder {
            public LinearLayout lay_item;

            public ItemCellGameOverHolder(View view) {
                super(view);
                lay_item = (LinearLayout) view.findViewById(R.id.lay_item);
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (controller.getCells().get(position).isOpened()) {
                if (controller.getCells().get(position).isMine())
                    return VIEW_TYPE_CELL_GAMEOVER; //gameover cell element
                else
                    return VIEW_TYPE_CELL_OPENED; //opened cell element
            } else if (controller.getCells().get(position).isMarked())
                return VIEW_TYPE_CELL_MARKED; //marked cell element
            else
                return VIEW_TYPE_CELL; //cell element
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == VIEW_TYPE_CELL_OPENED){
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_cell_open, parent, false);
                return new ItemCellOpenedHolder(itemView);
            }
            else if (viewType == VIEW_TYPE_CELL_MARKED){
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_cell_marked, parent, false);
                return new ItemCellMarkedHolder(itemView);
            }
            else if (viewType == VIEW_TYPE_CELL){
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_cell, parent, false);
                return new ItemCellHolder(itemView);
            }
            else if (viewType == VIEW_TYPE_CELL_GAMEOVER){
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_cell_gameover, parent, false);
                return new ItemCellGameOverHolder(itemView);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            //verify the element type
            if (holder instanceof ItemCellHolder){
                cellHolder(holder, position);
            }
            else if (holder instanceof ItemCellMarkedHolder){
                cellMarkedHolder(holder, position);
            }
            else if (holder instanceof ItemCellOpenedHolder){
                cellOpenedHolder(holder, position);
            }
            else if (holder instanceof ItemCellGameOverHolder){
                cellGameOverHolder(holder, position);
            }
        }

        @Override
        public int getItemCount() {
            return controller.getCells().size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void notifyData() {
            notifyDataSetChanged();
        }
    }
    public void cellHolder(RecyclerView.ViewHolder holder, final int position){
        //reference the cell
        final Cell c = controller.getCells().get(position);
        //cast the item holder
        final CellsAdapter.ItemCellHolder ih = (CellsAdapter.ItemCellHolder) holder;
        ih.lay_item.setBackgroundResource(light ? R.drawable.cell_l : R.drawable.cell);
        //set listeners
        ih.lay_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!controller.isGameOver() && controller.openCell(position))
                    gameOver();
                else if (controller.verifyGameCompleted())
                    gameCompleted();
                else if (!controller.isGameOver())
                    SoundManager.playClick(getActivity());
            }
        });
        ih.lay_item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                controller.markCell(position);
                updateTopBar();
                if (!controller.isGameOver()) {
                    SoundManager.playClick2(getActivity());
                }
                return true;
            }
        });
    }
    public void cellMarkedHolder(RecyclerView.ViewHolder holder, final int position) {
        //reference the cell
        final Cell c = controller.getCells().get(position);
        //cast the item holder
        final CellsAdapter.ItemCellMarkedHolder ih = (CellsAdapter.ItemCellMarkedHolder) holder;
        ih.lay_item.setBackgroundResource(light ? R.drawable.cell_l_marked : R.drawable.cell_marked);
        ih.lay_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!controller.isGameOver())
                    SoundManager.playClick(getActivity());
            }
        });
        ih.lay_item.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                controller.markCell(position);
                updateTopBar();
                if (!controller.isGameOver())
                    SoundManager.playClick2(getActivity());
                return true;
            }
        });
    }
    public void cellOpenedHolder(RecyclerView.ViewHolder holder, final int position) {
        //reference the cell
        final Cell c = controller.getCells().get(position);
        //cast the item holder
        final CellsAdapter.ItemCellOpenedHolder ih = (CellsAdapter.ItemCellOpenedHolder) holder;
        //place values
        ih.lbl_number.setText(c.getNumber() > 0 ? String.valueOf(c.getNumber()) : "");
        if (c.getNumber() <= 0) {
            ih.lay_item.setBackgroundResource(0);
        } else {
            ih.lay_item.setBackgroundResource(light ? R.drawable.cell_l_opened : R.drawable.cell_opened);
            ih.lbl_number.setTextColor(getResources().getColor(light ? R.color.color_LText : R.color.color_Text));
            ih.lay_item.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (!controller.isGameOver() && controller.openRelatedOpenCells(position))
                        gameOver();
                    else if (!controller.isGameOver() && controller.verifyGameCompleted())
                        gameCompleted();
                    else if (!controller.isGameOver())
                        SoundManager.playClick(getActivity());
                    return true;
                }
            });
        }    }
    public void cellGameOverHolder(RecyclerView.ViewHolder holder, final int position) {
        //reference the cell
        final Cell c = controller.getCells().get(position);
        //cast the item holder
        final CellsAdapter.ItemCellGameOverHolder ih = (CellsAdapter.ItemCellGameOverHolder) holder;
    }

    public void gameCompleted(){
        long currentTime = controller.getCurrentTime();
        //upload score
        if (controller.getType() != BoardController.Type.Custom)
            ((ActivityMain)getActivity()).getGameManager().defineScore(controller.getType(), currentTime);
        //check achievements
        if (controller.getType() != BoardController.Type.Custom){
            ((ActivityMain)getActivity()).getGameManager().unlockIncrementalAchievement(R.string.achievement_winner, 1);
            ((ActivityMain)getActivity()).getGameManager().unlockIncrementalAchievement(R.string.achievement_master, 1);
            ((ActivityMain)getActivity()).getGameManager().unlockIncrementalAchievement(R.string.achievement_lord, 1);
        }
        if (controller.getType() == BoardController.Type.Easy){
            //enable medium mode
            changeSettingMedium();
            ((ActivityMain)getActivity()).getGameManager().unlockAchievement(R.string.achievement_easy_mode);
            if (currentTime <= 15 * 1000) ((ActivityMain)getActivity()).getGameManager().unlockAchievement(R.string.achievement_easy_fast);
            if (currentTime <= 10 * 1000) ((ActivityMain)getActivity()).getGameManager().unlockAchievement(R.string.achievement_easy_faster);
            if (currentTime <= 6 * 1000) ((ActivityMain)getActivity()).getGameManager().unlockAchievement(R.string.achievement_easy_impossible);
            ((ActivityMain)getActivity()).getGameManager().unlockIncrementalAchievement(R.string.achievement_easy_winner, 1);
            ((ActivityMain)getActivity()).getGameManager().unlockIncrementalAchievement(R.string.achievement_easy_master, 1);
            ((ActivityMain)getActivity()).getGameManager().unlockIncrementalAchievement(R.string.achievement_easy_lord, 1);
        }
        if (controller.getType() == BoardController.Type.Medium){
            //enable hard mode
            changeSettingHard();
            ((ActivityMain)getActivity()).getGameManager().unlockAchievement(R.string.achievement_medium_mode);
            if (currentTime <= 75 * 1000) ((ActivityMain)getActivity()).getGameManager().unlockAchievement(R.string.achievement_medium_fast);
            if (currentTime <= 60 * 1000) ((ActivityMain)getActivity()).getGameManager().unlockAchievement(R.string.achievement_medium_faster);
            if (currentTime <= 45 * 1000) ((ActivityMain)getActivity()).getGameManager().unlockAchievement(R.string.achievement_medium_impossible);
            ((ActivityMain)getActivity()).getGameManager().unlockIncrementalAchievement(R.string.achievement_medium_winner, 1);
            ((ActivityMain)getActivity()).getGameManager().unlockIncrementalAchievement(R.string.achievement_medium_master, 1);
            ((ActivityMain)getActivity()).getGameManager().unlockIncrementalAchievement(R.string.achievement_medium_lord, 1);
        }
        if (controller.getType() == BoardController.Type.Hard){
            ((ActivityMain)getActivity()).getGameManager().unlockAchievement(R.string.achievement_hard_mode);
            if (currentTime <= 240 * 1000) ((ActivityMain)getActivity()).getGameManager().unlockAchievement(R.string.achievement_hard_fast);
            if (currentTime <= 210 * 1000) ((ActivityMain)getActivity()).getGameManager().unlockAchievement(R.string.achievement_hard_faster);
            if (currentTime <= 180 * 1000) ((ActivityMain)getActivity()).getGameManager().unlockAchievement(R.string.achievement_hard_impossible);
            ((ActivityMain)getActivity()).getGameManager().unlockIncrementalAchievement(R.string.achievement_hard_winner, 1);
            ((ActivityMain)getActivity()).getGameManager().unlockIncrementalAchievement(R.string.achievement_hard_master, 1);
            ((ActivityMain)getActivity()).getGameManager().unlockIncrementalAchievement(R.string.achievement_hard_lord, 1);
        }
        if (controller.getType() == BoardController.Type.Custom)
            ((ActivityMain)getActivity()).getGameManager().unlockIncrementalAchievement(R.string.achievement_challenger, 1);
        //open the completed dialog
        openGameCompleted(formatTimeComplete(currentTime));
    }
    public void gameOver(){
        //check for achievement
        ((ActivityMain)getActivity()).getGameManager().unlockIncrementalAchievement(R.string.achievement_suicide_bomber, 1);
        openGameOver();
    }

    public void updateTopBar(){
        try {
            lbl_mines.setText(String.valueOf(controller.getMines() - controller.getMarkedCells()));
            updateTopBarTime();
        } catch (Exception ex){}
    }
    public void updateTopBarTime(){
        lbl_time.setText(formatTime(controller.getCurrentTime()));
    }
    public String formatTime(long milis){
        long seconds = milis / 1000;
        long hours = 0;
        long minutes = 0;
        if (seconds >= 60){
            minutes = seconds / 60;
            seconds %= 60;
        }
        if (minutes >= 60){
            hours = minutes / 60;
            minutes %= 60;
        }
        return (hours > 0 ? hours + ":" : "") + (hours > 0 ? String.format("%02d", minutes) : minutes) +
                ":" + String.format("%02d", seconds);
    }
    public String formatTimeComplete(long milis){
        long seconds = 0;
        long hours = 0;
        long minutes = 0;
        if (milis >= 1000){
            seconds = milis / 1000;
            milis %= 1000;
            milis /= 10;
        }
        if (seconds >= 60){
            minutes = seconds / 60;
            seconds %= 60;
        }
        if (minutes >= 60){
            hours = minutes / 60;
            minutes %= 60;
        }
        return (hours > 0 ? hours + ":" : "") + (hours > 0 ? String.format("%02d", minutes) : minutes) +
                ":" + String.format("%02d", seconds) + "." + String.format("%02d", milis);
    }

    public void onBackPressed(){
        if (lay_menu.getVisibility() == View.GONE)
            openMenu();
        else {
            if (pos == 0 || pos > 2)
                closeMenu(true);
            else if (pos == 1)
                moveToMenu();
            else if (pos == 2)
                moveToSettings();
        }
    }

    public void closeMenu(boolean playOut){
        if (playOut)
            SoundManager.playMenuOut(getActivity());
        //display the menu
        lay_menu.setVisibility(View.GONE);
    }
    public void openMenu(){
        //play menu in sound
        SoundManager.playMenuIn(getActivity());
        //display the menu
        lay_menu.setVisibility(View.VISIBLE);
        //position to main menu
        pos = 0;
        sld_menu.setInAnimation(null);
        sld_menu.setOutAnimation(null);
        sld_menu.setDisplayedChild(pos);
    }
    public void moveToMenu(){
        if (pos == 0) return;
        if (pos > 0) {
            //play menu in sound
            SoundManager.playMenuOut(getActivity());
            sld_menu.setInAnimation(getActivity(), R.anim.animation_in_right);
            sld_menu.setOutAnimation(getActivity(), R.anim.animation_out_right);
        }
        pos = 0;
        sld_menu.setDisplayedChild(pos);
    }
    public void moveToSettings(){
        if (pos == 1) return;
        if (pos > 1) {
            //play menu in sound
            SoundManager.playMenuOut(getActivity());
            sld_menu.setInAnimation(getActivity(), R.anim.animation_in_right);
            sld_menu.setOutAnimation(getActivity(), R.anim.animation_out_right);
        } else {
            //play menu in sound
            SoundManager.playMenuIn(getActivity());
            sld_menu.setInAnimation(getActivity(), R.anim.animation_in_left);
            sld_menu.setOutAnimation(getActivity(), R.anim.animation_out_left);
        }
        pos = 1;
        sld_menu.setDisplayedChild(pos);
        updateSoundButton();
        updateThemeButton();
    }
    public void moveToGoogle(){
        if (pos == 2) return;
        if (pos > 2) {
            //play menu in sound
            SoundManager.playMenuOut(getActivity());
            sld_menu.setInAnimation(getActivity(), R.anim.animation_in_right);
            sld_menu.setOutAnimation(getActivity(), R.anim.animation_out_right);
        } else {
            //play menu in sound
            SoundManager.playMenuIn(getActivity());
            sld_menu.setInAnimation(getActivity(), R.anim.animation_in_left);
            sld_menu.setOutAnimation(getActivity(), R.anim.animation_out_left);
        }
        pos = 2;
        sld_menu.setDisplayedChild(pos);
        updateGoogleButtons();
    }
    public void openGameCompleted(String currentTime) {
        //play menu in sound
        SoundManager.playSuccess(getActivity());
        //display the menu
        lay_menu.setVisibility(View.VISIBLE);
        //position to main menu
        pos = 3;
        sld_menu.setInAnimation(null);
        sld_menu.setOutAnimation(null);
        sld_menu.setDisplayedChild(pos);
        //set functionality for views
        String completed = getString(R.string.completed);
        if (controller.getType() == BoardController.Type.Custom)
            completed = completed.replace("XXX", controller.getCols() + "x" + controller.getRows());
        else {
            String type = getString(controller.getType() == BoardController.Type.Easy ? R.string.easy :
                    controller.getType() == BoardController.Type.Medium ? R.string.medium : R.string.hard);
            completed = completed.replace("XXX", type);
        }
        completed = completed.replace("YYY", currentTime);
        lbl_completed.setText(completed);
    }
    public void openGameOver() {
        //play menu in sound
        SoundManager.playError(getActivity());
        //display the menu
        lay_menu.setVisibility(View.VISIBLE);
        //position to main menu
        pos = 4;
        sld_menu.setInAnimation(null);
        sld_menu.setOutAnimation(null);
        sld_menu.setDisplayedChild(pos);
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
    public void changeSettingTutorial(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        preferences.edit().putBoolean(Globals.GAME_TUTORIAL, false).apply();
    }
    public void changeSettingMedium(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        preferences.edit().putBoolean(Globals.MAIN_MEDIUM, true).apply();
    }
    public void changeSettingHard(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        preferences.edit().putBoolean(Globals.MAIN_HARD, true).apply();
    }

    public void startTimer(){
        stopTimer();
        asyncUpdate = new AsyncUpdate();
        asyncUpdate.execute();
        controller.setNewIniDate();
    }
    public void stopTimer(){
        if (asyncUpdate != null) {
            asyncUpdate.cancel(true);
            asyncUpdate.doCancel();
            asyncUpdate = null;
            controller.acumulateTime();
        }
    }

    private class AsyncUpdate extends AsyncTask<Void, Void, Void> {
        boolean goAhead = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            while(goAhead && !controller.isGameOver()){
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
            if (!controller.isGameOver())
                updateTopBarTime();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        public void doCancel(){
            goAhead = false;
        }
    }

    public void restartGame(){
        controller.startBoard();
        cellsAdapter.notifyData();
        updateTopBar();
        scrollsToZero();
        //check achievements
        ((ActivityMain)getActivity()).getGameManager().unlockIncrementalAchievement(R.string.achievement_persistent, 1);
        startTimer();
    }
    public void quitGame(){
        stopTimer();
        //check achievements
        if (!controller.isGameOver())
        ((ActivityMain)getActivity()).getGameManager().unlockIncrementalAchievement(R.string.achievement_quitter, 1);
        //call quitGame on activity
        //((ActivityGame)getActivity()).quit();
        ((ActivityMain)getActivity()).changeToMenu(2);
    }

    public void openTutorialDialog1() {
        AlertDialog.Builder supportDialog = new AlertDialog.Builder(getActivity());
        supportDialog.setTitle(R.string.tutorial_t);
        supportDialog.setMessage(R.string.tutorial_1);
        supportDialog.setPositiveButton(R.string.tutorial_next, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                openTutorialDialog2();
                dialog.dismiss();
            }
        });
        supportDialog.setNegativeButton(R.string.tutorial_skip, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = supportDialog.create();
        dialog.show();
    }
    public void openTutorialDialog2() {
        AlertDialog.Builder supportDialog = new AlertDialog.Builder(getActivity());
        supportDialog.setTitle(R.string.tutorial_2_t);
        supportDialog.setMessage(R.string.tutorial_2);
        supportDialog.setPositiveButton(R.string.tutorial_next, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                openTutorialDialog3();
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = supportDialog.create();
        dialog.show();
    }
    public void openTutorialDialog3() {
        AlertDialog.Builder supportDialog = new AlertDialog.Builder(getActivity());
        supportDialog.setTitle(R.string.tutorial_3_t);
        supportDialog.setMessage(R.string.tutorial_3);
        supportDialog.setPositiveButton(R.string.tutorial_next, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                openTutorialDialog4();
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = supportDialog.create();
        dialog.show();
    }
    public void openTutorialDialog4() {
        AlertDialog.Builder supportDialog = new AlertDialog.Builder(getActivity());
        supportDialog.setTitle(R.string.tutorial_4_t);
        supportDialog.setMessage(R.string.tutorial_4);
        supportDialog.setPositiveButton(R.string.tutorial_play, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        final AlertDialog dialog = supportDialog.create();
        dialog.show();
    }
}
