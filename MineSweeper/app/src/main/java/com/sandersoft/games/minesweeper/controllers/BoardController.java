package com.sandersoft.games.minesweeper.controllers;

import android.os.Parcel;
import android.os.Parcelable;

import com.sandersoft.games.minesweeper.models.Cell;
import com.sandersoft.games.minesweeper.views.FragmentMain;

import java.util.ArrayList;
import java.util.Calendar;
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
    Calendar ini_date = Calendar.getInstance();

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
            int y = i / cols;
            y--;
            p = i - cols - 1; //upleft
            count += p > 0 && p / cols == y && cells.get(p).isMine() ? 1 : 0;
            p++; //up
            count += p > 0 && p / cols == y && cells.get(p).isMine() ? 1 : 0;
            p++; //upright
            count += p > 0 && p / cols == y && cells.get(p).isMine() ? 1 : 0;
            y++;
            p = i - 1; //left
            count += p > 0 && p / cols == y && cells.get(p).isMine() ? 1 : 0;
            p = i + 1; //right
            count += p < cells.size() && p / cols == y && cells.get(p).isMine() ? 1 : 0;
            y++;
            p = i + cols - 1; //down left
            count += p < cells.size() && p / cols == y && cells.get(p).isMine() ? 1 : 0;
            p++; //down
            count += p < cells.size() && p / cols == y && cells.get(p).isMine() ? 1 : 0;
            p++; //down right
            count += p < cells.size() && p / cols == y && cells.get(p).isMine() ? 1 : 0;
            cells.get(i).setNumber(count);
        }
        //mark the baord as constructed
        boardDefined = true;
    }

    public boolean openCell(int position){
        //if gameover, do nothing
        if (gameOver) return gameOver;
        //if the board is not defined yet, construct it
        if (!boardDefined)
            defineBoard(position);
        //open location and check if gameover
        gameOver = cells.get(position).openCell();
        //checl if cell is 0
        if (!gameOver && cells.get(position).getNumber() == 0){
            //open all cells around this one
            openCellsAround(position);
            //notify cells changed
            view.cellsAdapter.notifyData(); //.notifyItemChanged(position);
        } else {
            //notify cells changed
            view.cellsAdapter.notifyItemChanged(position);
        }
        return gameOver;
    }
    public void markCell(int position){
        //if gameover, do nothing
        if (gameOver) return;
        //toogle the mark on a cell
        cells.get(position).toogleMark();
        //notify cell changed
        view.cellsAdapter.notifyItemChanged(position);
    }
    public boolean openRelatedOpenCells(int position){
        //if gameover, do nothing
        if (gameOver) return gameOver;
        openCellsAround(position);
        //notify cells changed
        view.cellsAdapter.notifyData(); //.notifyItemChanged(position);
        return gameOver;
    }

    void openCellsAround(int position){
        int y = position / cols;
        y--;
        int p = position - cols - 1; //upleft
        openCellAround(p, y);
        p++; //up
        openCellAround(p, y);
        p++; //upright
        openCellAround(p, y);
        y++;
        p = position - 1; //left
        openCellAround(p, y);
        p = position + 1; //right
        openCellAround(p, y);
        y++;
        p = position + cols - 1; //down left
        openCellAround(p, y);
        p++; //down
        openCellAround(p, y);
        p++; //down right
        openCellAround(p, y);
    }
    void openCellAround(int p, int y){
        if (p > 0 && p < cells.size() && p / cols == y
                && !cells.get(p).isOpened() && !cells.get(p).isMarked()) {
            gameOver = cells.get(p).openCell();
            if (cells.get(p).getNumber() == 0)
                openCellsAround(p);
        }
    }

    public int getMarkedCells(){
        int n = 0;
        for (Cell c : cells)
            n += c.isMarked() ? 1 : 0;
        return n;
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
    public Calendar getIni_date() {
        return ini_date;
    }
    public boolean isGameOver() {
        return gameOver;
    }

    // Parcelling part
    public BoardController(Parcel in){
        cells = in.readArrayList(Cell.class.getClassLoader());
        cols = in.readInt();
        rows = in.readInt();
        mines = in.readInt();
        boardDefined = in.readInt() == 1;
        gameOver = in.readInt() == 1;
        ini_date.setTimeInMillis(in.readLong());
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
        dest.writeLong(ini_date.getTimeInMillis());
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
