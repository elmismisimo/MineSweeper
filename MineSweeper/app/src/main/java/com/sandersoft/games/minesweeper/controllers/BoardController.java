package com.sandersoft.games.minesweeper.controllers;

import android.os.Parcel;
import android.os.Parcelable;

import com.sandersoft.games.minesweeper.R;
import com.sandersoft.games.minesweeper.models.Cell;
import com.sandersoft.games.minesweeper.views.ActivityMain;
import com.sandersoft.games.minesweeper.views.FragmentGame;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by meixi on 21/05/2017.
 */

public class BoardController implements Parcelable {

    public enum Type {Easy(0), Medium(1), Hard(2), Custom(-1);
        int value;
        Type(int value){this.value = value;}
        public int getValue() { return this.value;}
        public static Type fromInteger(int t){
            switch (t){
                case -1: return Custom;
                case 0: return Easy;
                case 1: return Medium;
                case 2: return Hard;
            }
            return null;
        }
    }
    FragmentGame view;

    ArrayList<Cell> cells = new ArrayList<>();
    Type type = Type.Custom;
    int cols = 0;
    int rows = 0;
    int mines = 0;
    Calendar ini_date = Calendar.getInstance();
    long timeAcumulated = 0;

    boolean boardDefined = false;
    boolean gameOver = false;

    public BoardController(FragmentGame view){
        setView(view);
    }
    public void setView(FragmentGame view){
        this.view = view;
    }
    public void initiateBoard (Type type, int cols, int rows, int mines){
        this.type = type;
        this.mines = mines;
        this.cols = cols;
        this.rows = rows;
    }

    public void startBoard(){
        ini_date = Calendar.getInstance();
        timeAcumulated = 0;
        boardDefined = false;
        gameOver = false;
        cells.clear();
        createBoard();
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
            count += p >= 0 && p / cols == y && cells.get(p).isMine() ? 1 : 0;
            p++; //up
            count += p >= 0 && p / cols == y && cells.get(p).isMine() ? 1 : 0;
            p++; //upright
            count += p >= 0 && p / cols == y && cells.get(p).isMine() ? 1 : 0;
            y++;
            p = i - 1; //left
            count += p >= 0 && p / cols == y && cells.get(p).isMine() ? 1 : 0;
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
        gameOver |= cells.get(position).openCell();
        //check if cell is 0
        if (!gameOver && cells.get(position).getNumber() == 0){
            //open all cells around this one
            openCellsAround(position);
            //notify cells changed
            view.cellsAdapter.notifyData(); //.notifyItemChanged(position);
        } else {
            //notify cells changed
            if (!gameOver)
                view.cellsAdapter.notifyItemChanged(position);
            else {
                //if is game over, open the unmarked mines
                openMineCells();
                view.cellsAdapter.notifyData();
            }
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
        //if is game over, open the unmarked mines
        if (gameOver)
            openMineCells();
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
        if (p >= 0 && p < cells.size() && p / cols == y
                && !cells.get(p).isOpened() && !cells.get(p).isMarked()) {
            gameOver |= cells.get(p).openCell();
            if (!cells.get(p).isMine() && cells.get(p).getNumber() == 0)
                openCellsAround(p);
        }
    }

    public int getMarkedCells(){
        int n = 0;
        for (Cell c : cells)
            n += c.isMarked() ? 1 : 0;
        return n;
    }

    public boolean verifyGameCompleted(){
        for (Cell c : cells)
            if (!c.isOpened() && !c.isMine()) return false;
        int marked = 0;
        for (Cell c : cells)
            if (c.isMine()) {
                if (!c.isMarked())
                    c.toogleMark();
                else
                    marked++;
            }
        view.cellsAdapter.notifyData();
        view.updateTopBar();
        //check for achievement
        ((ActivityMain)view.getActivity()).getGameManager().unlockIncrementalAchievement(R.string.achievement_remarkable, marked);
        gameOver = true;
        return true;
    }
    public void openMineCells(){
        for (Cell c : cells)
            if (c.isMine() && !c.isMarked()) c.openCell();
    }

    public long getCurrentTime(){
        Calendar now = Calendar.getInstance();
        long dif = now.getTimeInMillis() - ini_date.getTimeInMillis();
        return dif + timeAcumulated;
        //long seconds = dif / 1000;
        //return seconds;
    }
    public void acumulateTime(){
        Calendar now = Calendar.getInstance();
        long dif = now.getTimeInMillis() - ini_date.getTimeInMillis();
        timeAcumulated += dif;
    }
    public void setNewIniDate(){
        ini_date = Calendar.getInstance();
    }

    public ArrayList<Cell> getCells(){
        return cells;
    }

    public Type getType() {
        return type;
    }
    public int getCols() {
        return cols;
    }
    public int getRows() {
        return rows;
    }
    public int getMines() {
        return mines;
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
        type = Type.fromInteger(in.readInt());
        cols = in.readInt();
        rows = in.readInt();
        mines = in.readInt();
        boardDefined = in.readInt() == 1;
        gameOver = in.readInt() == 1;
        ini_date.setTimeInMillis(in.readLong());
        timeAcumulated = in.readLong();
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(cells);
        dest.writeInt(type.getValue());
        dest.writeInt(cols);
        dest.writeInt(rows);
        dest.writeInt(mines);
        dest.writeInt(boardDefined ? 1 : 0);
        dest.writeInt(gameOver ? 1 : 0);
        dest.writeLong(ini_date.getTimeInMillis());
        dest.writeLong(timeAcumulated);
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
