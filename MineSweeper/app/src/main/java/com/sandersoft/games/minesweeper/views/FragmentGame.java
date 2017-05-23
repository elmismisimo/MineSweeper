package com.sandersoft.games.minesweeper.views;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sandersoft.games.minesweeper.R;
import com.sandersoft.games.minesweeper.SoundManager;
import com.sandersoft.games.minesweeper.controllers.BoardController;
import com.sandersoft.games.minesweeper.models.Cell;
import com.sandersoft.games.minesweeper.utils.Globals;

import java.util.Calendar;

/**
 * Created by Sander on 21/05/2017.
 */

public class FragmentGame extends Fragment {

    BoardController controller;

    TextView lbl_mines;
    TextView lbl_grid;
    public TextView lbl_time;
    HorizontalScrollView scr_board;
    RecyclerView board;
    public CellsAdapter cellsAdapter;

    public FragmentGame(){}

    public static FragmentGame getInstance(int type, int mines, int cols, int rows){
        FragmentGame frag = new FragmentGame();
        frag.controller = new BoardController(frag);
        frag.controller.initiateBoard(type, mines, cols, rows);
        frag.controller.startBoard();
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_game, container, false);

        if (null != savedInstanceState){
            controller = savedInstanceState.getParcelable(Globals.GAME_CONTROLLER);
            controller.setView(this);
        }

        //instantiates the views of the layout
        defineElements(rootview);

        if (null != savedInstanceState) {
            int[] arr = savedInstanceState.getIntArray(Globals.GAME_SCROLL);
            scr_board.scrollTo(arr[0], arr[1]);
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

    public void defineElements(View rootview){
        lbl_mines = (TextView) rootview.findViewById(R.id.lbl_mines);
        lbl_grid = (TextView) rootview.findViewById(R.id.lbl_grid);
        lbl_grid.setText(controller.getType() < 0 ? controller.getCols() + "x" + controller.getRows() :
                getString(controller.getType() == 0 ? R.string.easy : controller.getType() == 1 ? R.string.medium : R.string.hard));
        lbl_time = (TextView) rootview.findViewById(R.id.lbl_time);
        updateTopBar();
        scr_board = (HorizontalScrollView) rootview.findViewById(R.id.scr_board);
        board = (RecyclerView) rootview.findViewById(R.id.board);
        cellsAdapter = new CellsAdapter();
        GridLayoutManager gridLayout = new GridLayoutManager(getActivity(), controller.getCols());
        board.setLayoutManager(gridLayout);
        board.setAdapter(cellsAdapter);
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
                //reference the cell
                final Cell c = controller.getCells().get(position);
                //cast the item holder
                final ItemCellHolder ih = (ItemCellHolder) holder;
                //set listeners
                ih.lay_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!controller.isGameOver() && controller.openCell(position))
                            openGameOver();
                        else if (controller.verifyGameCompleted())
                            ((ActivityGame)getActivity()).openDialogCompleted();
                        else if (!controller.isGameOver())
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
            else if (holder instanceof ItemCellMarkedHolder){
                //reference the cell
                final Cell c = controller.getCells().get(position);
                //cast the item holder
                final ItemCellMarkedHolder ih = (ItemCellMarkedHolder) holder;
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
            else if (holder instanceof ItemCellOpenedHolder){
                //reference the cell
                final Cell c = controller.getCells().get(position);
                //cast the item holder
                final ItemCellOpenedHolder ih = (ItemCellOpenedHolder) holder;
                //place values
                ih.lbl_number.setText(c.getNumber() > 0 ? String.valueOf(c.getNumber()) : "");
                if (c.getNumber() <= 0) {
                    ih.lay_item.setBackgroundResource(0);
                } else {
                    ih.lay_item.setBackgroundResource(R.drawable.cell_opened);
                    ih.lay_item.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            if (!controller.isGameOver() && controller.openRelatedOpenCells(position))
                                openGameOver();
                            else if (controller.verifyGameCompleted())
                                ((ActivityGame)getActivity()).openDialogCompleted();
                            else if (!controller.isGameOver())
                                SoundManager.playClick(getActivity());
                            return true;
                        }
                    });
                }
            }
            else if (holder instanceof ItemCellGameOverHolder){
                //reference the cell
                final Cell c = controller.getCells().get(position);
                //cast the item holder
                final ItemCellGameOverHolder ih = (ItemCellGameOverHolder) holder;
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

    public void updateTopBar(){
        try {
            lbl_mines.setText(String.valueOf(controller.getMines() - controller.getMarkedCells()));
            updateTopBarTime();
        } catch (Exception ex){}
    }
    public void updateTopBarTime(){
        Calendar now = Calendar.getInstance();
        long dif = now.getTimeInMillis() - controller.getIni_date().getTimeInMillis();
        long seconds = dif / 1000;
        lbl_time.setText(formatTime(seconds));
    }
    public String formatTime(long seconds){
        long hours = 0;
        long minutes = 0;
        if (seconds > 60){
            minutes = seconds / 60;
            seconds %= 60;
        }
        if (minutes > 60){
            hours = minutes / 60;
            minutes %= 60;
        }
        return (hours > 0 ? hours + ":" : "") + (hours > 0 ? String.format("%02d", minutes) : minutes) +
                ":" + String.format("%02d", seconds);
    }

    public void openGameOver(){
        /*LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.toast_gameover,
                (ViewGroup) getActivity().findViewById(R.id.toast_container));

        Toast toast = new Toast(getActivity());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();*/
        ((ActivityGame)getActivity()).openDialogGameOver();
    }

}
