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
import com.sandersoft.games.minesweeper.controllers.CellController;
import com.sandersoft.games.minesweeper.models.Cell;
import com.sandersoft.games.minesweeper.utils.Globals;

/**
 * Created by Sander on 21/05/2017.
 */

public class FragmentMain extends Fragment {

    CellController controller;

    HorizontalScrollView scr_board;
    RecyclerView board;
    CellsAdapter cellsAdapter;
    int cols = 0;
    int rows = 0;

    public FragmentMain(){}

    public static FragmentMain getInstance(int cols, int rows){
        FragmentMain frag = new FragmentMain();
        frag.cols = cols;
        frag.rows = rows;
        frag.controller = new CellController(frag, cols * rows);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_main, container, false);

        if (null != savedInstanceState){
            controller = savedInstanceState.getParcelable(Globals.MAIN_CONTROLLER);
            controller.setView(this);
            cols = savedInstanceState.getInt(Globals.MAIN_COLS);
            rows = savedInstanceState.getInt(Globals.MAIN_ROWS);
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
        outState.putInt(Globals.MAIN_COLS, cols);
        outState.putInt(Globals.MAIN_ROWS, rows);
        outState.putIntArray(Globals.MAIN_SCROLL,
                new int[] {scr_board.getScrollX(), scr_board.getScrollY()});
    }

    public void defineElements(View rootview){
        scr_board = (HorizontalScrollView) rootview.findViewById(R.id.scr_board);
        board = (RecyclerView) rootview.findViewById(R.id.board);
        cellsAdapter = new CellsAdapter();
        GridLayoutManager gridLayout = new GridLayoutManager(getActivity(), cols);
        board.setLayoutManager(gridLayout);
        board.setAdapter(cellsAdapter);
    }

    public class CellsAdapter extends RecyclerView.Adapter {
        private final int VIEW_TYPE_ITEM_ELM = 1;

        public CellsAdapter(){
            setHasStableIds(true);
        }


        public class ItemCellHolder extends RecyclerView.ViewHolder {
            public LinearLayout lay_item;
            public TextView lbl_number;

            public ItemCellHolder(View view) {
                super(view);
                lay_item = (LinearLayout) view.findViewById(R.id.lay_item);
                lbl_number = (TextView) view.findViewById(R.id.lbl_number);
            }
        }

        @Override
        public int getItemViewType(int position) {
            return VIEW_TYPE_ITEM_ELM; //cell element
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == VIEW_TYPE_ITEM_ELM){
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_cell, parent, false);
                return new ItemCellHolder(itemView);
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
                //place values
                ih.lbl_number.setText(String.valueOf(c.getNumber()));
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
