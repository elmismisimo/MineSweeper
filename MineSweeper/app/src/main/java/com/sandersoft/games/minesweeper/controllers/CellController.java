package com.sandersoft.games.minesweeper.controllers;

import android.os.Parcel;
import android.os.Parcelable;

import com.sandersoft.games.minesweeper.models.Cell;
import com.sandersoft.games.minesweeper.views.FragmentMain;

import java.util.ArrayList;

/**
 * Created by meixi on 21/05/2017.
 */

public class CellController implements Parcelable {

    FragmentMain view;

    ArrayList<Cell> cells = new ArrayList<>();

    public CellController(FragmentMain view, int size){
        setView(view);
        createList(size);
    }
    public void setView(FragmentMain view){
        this.view = view;
    }

    void createList(int size){
        for (int i = 0; i < size; i++)
            cells.add(new Cell(i));
    }

    public ArrayList<Cell> getCells(){
        return cells;
    }

    // Parcelling part
    public CellController(Parcel in){
        cells = in.readArrayList(Cell.class.getClassLoader());
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(cells);
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public CellController createFromParcel(Parcel in) {
            return new CellController(in);
        }

        public CellController[] newArray(int size) {
            return new CellController[size];
        }
    };
}
