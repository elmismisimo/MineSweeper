package com.sandersoft.games.minesweeper.controllers;

import android.os.Parcel;
import android.os.Parcelable;

import com.sandersoft.games.minesweeper.models.Cell;
import com.sandersoft.games.minesweeper.views.FragmentMain;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by meixi on 21/05/2017.
 */

public class BoardController implements Parcelable {

    FragmentMain view;

    ArrayList<Cell> cells = new ArrayList<>();
    int cols = 0;
    int rows = 0;
    int mines = 0;

    boolean boardDefined = false;
    boolean gameOver = false;

    public BoardController(FragmentMain view){
        setView(view);
    }
    public void setView(FragmentMain view){
        this.view = view;
    }

    public void createBoard(){
        for (int i = 0; i < cols * rows; i++)
            cells.add(new Cell(0, false));
    }
    void defineBoard(int positionToIgnore){
        //set the mines randomly, avoiding placing them on the positionToignore location
        int i = 0;
        Random rand = new Random();
        int p;
        while (i < mines){
            p = rand.nextInt(cells.size());
            if (p == positionToIgnore || cells.get(p).isMine())
                continue;
            cells.get(p).setMine(true);
            i++;
        }
        //adjust the numbers
        for (i = 0; i < cells.size(); i++){
            int count = 0;
            p = i - cols - 1; //upleft
            count += p > 0 && cells.get(p).isMine() ? 1 : 0;
            p++; //up
            count += p > 0 && cells.get(p).isMine() ? 1 : 0;
            p++; //upright
            count += p > 0 && cells.get(p).isMine() ? 1 : 0;
            p = i - 1; //left
            count += p > 0 && cells.get(p).isMine() ? 1 : 0;
            p = i + 1; //right
            count += p < cells.size() && cells.get(p).isMine() ? 1 : 0;
            p = i + cols - 1; //down left
            count += p < cells.size() && cells.get(p).isMine() ? 1 : 0;
            p++; //down
            count += p < cells.size() && cells.get(p).isMine() ? 1 : 0;
            p++; //down right
            count += p < cells.size() && cells.get(p).isMine() ? 1 : 0;
            cells.get(i).setNumber(count);
        }
        //mark the baord as constructed
        boardDefined = true;
    }

    public void openCell(int position){
        //if gameover, do nothing
        if (gameOver) return;
        //if the board is not defined yet, construct it
        if (!boardDefined)
            defineBoard(position);
        //open location and checl if gameover
        gameOver = cells.get(position).openCell();
        //notify cells changed
        view.cellsAdapter.notifyData(); //.notifyItemChanged(position);
    }
    public void markCell(int position){
        //if gameover, do nothing
        if (gameOver) return;
        //toogle the mark on a cell
        cells.get(position).toogleMark();
        //notify cell changed
        view.cellsAdapter.notifyItemChanged(position);
    }

    public ArrayList<Cell> getCells(){
        return cells;
    }

    public int getCols() {
        return cols;
    }
    public void setCols(int cols) {
        this.cols = cols;
    }
    public int getRows() {
        return rows;
    }
    public void setRows(int rows) {
        this.rows = rows;
    }
    public int getMines() {
        return mines;
    }
    public void setMines(int mines) {
        this.mines = mines;
    }

    // Parcelling part
    public BoardController(Parcel in){
        cells = in.readArrayList(Cell.class.getClassLoader());
        cols = in.readInt();
        rows = in.readInt();
        mines = in.readInt();
        boardDefined = in.readInt() == 1;
        gameOver = in.readInt() == 1;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(cells);
        dest.writeInt(cols);
        dest.writeInt(rows);
        dest.writeInt(mines);
        dest.writeInt(boardDefined ? 1 : 0);
        dest.writeInt(gameOver ? 1 : 0);
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public BoardController createFromParcel(Parcel in) {
            return new BoardController(in);
        }

        public BoardController[] newArray(int size) {
            return new BoardController[size];
        }
    };
}
