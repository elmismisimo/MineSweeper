package com.sandersoft.games.minesweeper.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.sandersoft.games.minesweeper.controllers.CellController;

/**
 * Created by meixi on 21/05/2017.
 */

public class Cell implements Parcelable {
    int number;

    public Cell(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }


    // Parcelling part
    public Cell(Parcel in){
        number = in.readInt();
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(number);
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Cell createFromParcel(Parcel in) {
            return new Cell(in);
        }

        public Cell[] newArray(int size) {
            return new Cell[size];
        }
    };
}
