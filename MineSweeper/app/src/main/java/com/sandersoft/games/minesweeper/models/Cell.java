package com.sandersoft.games.minesweeper.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by meixi on 21/05/2017.
 */

public class Cell implements Parcelable {
    int number;
    boolean mine = false;
    boolean opened = false;
    boolean marked = false;

    public Cell(int number, boolean mine) {
        this.number = number;
        this.mine = mine;
    }

    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isMine() {
        return mine;
    }
    public boolean isOpened() {
        return opened;
    }
    public boolean isMarked() {
        return marked;
    }
    public void setMine(boolean mine) {
        this.mine = mine;
    }

    public void toogleMark(){
        marked = !marked;
    }
    public boolean openCell(){
        opened = true;
        marked = false;
        return isMine();
    }

    // Parcelling part
    public Cell(Parcel in){
        number = in.readInt();
        mine = in.readInt() == 1;
        opened = in.readInt() == 1;
        marked = in.readInt() == 1;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(number);
        dest.writeInt(mine ? 1 : 0);
        dest.writeInt(opened ? 1 : 0);
        dest.writeInt(marked ? 1 : 0);
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
