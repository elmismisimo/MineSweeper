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
import com.sandersoft.games.minesweeper.controllers.BoardController;
import com.sandersoft.games.minesweeper.models.Cell;
import com.sandersoft.games.minesweeper.utils.Globals;

/**
 * Created by Sander on 21/05/2017.
 */

public class FragmentMain extends Fragment {

    BoardController controller;

    HorizontalScrollView scr_board;
    RecyclerView board;
    public CellsAdapter cellsAdapter;

    public FragmentMain(){}

    public static FragmentMain getInstance(int mines, int cols, int rows){
        FragmentMain frag = new FragmentMain();
        frag.controller = new BoardController(frag);
        frag.controller.setCols(cols);
        frag.controller.setRows(rows);
        frag.controller.setMines(mines);
        frag.controller.createBoard();
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_main, container, false);

        if (null != savedInstanceState){
            controller = savedInstanceState.getParcelable(Globals.MAIN_CONTROLLER);
            controller.setView(this);
        }

        //instantiates the views of the layout
        defineElements(rootview);

        if (null != savedInstanceState) {
            int[] arr = savedInstanceState.getIntArray(Globals.MAIN_SCROLL);
            scr_board.scrollTo(arr[0], arr[1]);
        }

        return rootview;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //get objects backedup so we can reload the activity with the same infromation
        outState.putParcelable(Globals.MAIN_CONTROLLER, controller);
        outState.putIntArray(Globals.MAIN_SCROLL,
                new int[] {scr_board.getScrollX(), scr_board.getScrollY()});
    }

    public void defineElements(View rootview){
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
                        controller.openCell(position);
                    }
                });
                ih.lay_item.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        controller.markCell(position);
                        return true;
                    }
                });
            }
            else if (holder instanceof ItemCellMarkedHolder){
                //reference the cell
                final Cell c = controller.getCells().get(position);
                //cast the item holder
                final ItemCellMarkedHolder ih = (ItemCellMarkedHolder) holder;
                ih.lay_item.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        controller.markCell(position);
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
                ih.lbl_number.setText(String.valueOf(c.getNumber()));
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

}
