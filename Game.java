package com.example.pipedeneme;
import javafx.scene.control.Label;


import java.io.*;
import java.util.Formatter;



class Game {

    public Label moveCounter;
    private Tiles[][] board;

    public int numberOfUnlockedLevel;

    public Game(){
        numberOfUnlockedLevel=0;

        board=new Tiles[4][4];

    }

    public void createMapData(String inputFilePath) {
        BufferedReader br = null;
        String line = "";
        try {
            br = new BufferedReader(new FileReader(inputFilePath));
            int i = 0;
            while ((line = br.readLine()) != null) {
                String[] info = line.split(",");
                try {
                    Tiles cellData = new Tiles(Integer.parseInt(info[0]), info[1], info[2]);

                    board[i % 4][i / 4] =cellData;
                    i++;
                } catch (NumberFormatException e) {
                    System.out.printf("Error at line -> %s\n", line);
                    System.out.println(e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //checks the legality of the drag gesture.
    public boolean isLegal(int x0,int y0,int x1,int y1){
        if(board[x0][y0].type.equalsIgnoreCase("Starter")||board[x0][y0].type.equalsIgnoreCase("End")||board[x0][y0].type.equalsIgnoreCase("PipeStatic")||board[x0][y0].property.equalsIgnoreCase("Free")){
            return false;
        }else{
            if(board[x1][y1].property.equalsIgnoreCase("Free")){
                return (Math.abs(x0-x1)==1 && Math.abs(y0-y1)==0)||(Math.abs(y0-y1)==1 && Math.abs(x0-x1)==0);
            }else{
                return false;
            }
        }
    }
    //This method creates the new level.


    public void makeMove(int x0,int y0,int x1,int y1){
        Tiles temp = board[x0][y0];
        board[x0][y0] = board[x1][y1];
        board[x1][y1] = temp;
    }
    public void loadGame(){
        File f = new File("save.dat");
        if(f.exists()){
            try{
                BufferedReader b = new BufferedReader(new FileReader("save.dat"));
                numberOfUnlockedLevel=b.read() - ((int) '0');
            }catch(FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }else{
            numberOfUnlockedLevel=0;
        }
    }
    public void saveGame() {
        try {
            File f = new File("save.dat");
            Formatter fo = new Formatter(f);
            fo.format("%d", numberOfUnlockedLevel);
            fo.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    //this method checks that the level has been completed correctly
    public boolean checkOver(){
        int x0 = 0 ,y0 = 0;
        int x1,y1;
        boolean result=true;
        //this loop finds the position of the starting rectangle.
        while(!board[x0][y0].type.equalsIgnoreCase("Starter")){
            x0++;
            if(x0>=4){
                y0++;
                x0 = 0;
            }
        }
        if(board[x0][y0].property.equalsIgnoreCase("Vertical")) {
            x1 = x0;
            y1 = y0 + 1;
        }else{
            x1 = x0 - 1;
            y1 = y0;
        }
        //this loop is broken when it reaches its end point.
        while(true) {
            //We use these variables to understand from which direction the ball enters the rectangle.
            int deltaX = x1 - x0;
            int deltaY = y1 - y0;
            Tiles currentCell = board[x1][y1];
            if (currentCell.type.equalsIgnoreCase("Pipe")||currentCell.type.equalsIgnoreCase("PipeStatic")) {
                if (currentCell.property.equalsIgnoreCase("00")) {
                    if (deltaY==1) {
                        y0 = y1;
                        x0 = x1--;
                    }
                    else if(deltaX==1){
                        y0 = y1--;
                        x0 = x1;
                    }
                    else {
                        result=false;
                        break;
                    }
                }
                else if (currentCell.property.equalsIgnoreCase("01")) {
                    if (deltaY==1) {
                        y0 = y1;
                        x0 = x1++;
                    }
                    else if(deltaX==-1){
                        y0=y1--;
                        x0=x1;
                    }
                    else {
                        result= false;
                        break;
                    }
                }
                else if (currentCell.property.equalsIgnoreCase("10")) {
                    if(deltaX==1) {
                        y0=y1++;
                        x0=x1;
                    }
                    else if(deltaY==-1){
                        y0 = y1;
                        x0 = x1--;
                    }
                    else {
                        result= false;
                        break;
                    }
                }
                else if (currentCell.property.equalsIgnoreCase("11")) {
                    if(deltaY==-1) {
                        y0 = y1;
                        x0 = x1++;
                    }
                    else if(deltaX==-1){
                        y0 = y1++;
                        x0 = x1;
                    }
                    else {
                        result= false;
                        break;
                    }
                }
                else if (currentCell.property.equalsIgnoreCase("Horizontal")) {
                    if(deltaX==1) {
                        x0=x1++;
                        y0=y1;
                    }
                    else if(deltaX==-1){
                        x0=x1--;
                        y0=y1;
                    }
                    else {
                        result= false;
                        break;
                    }
                }
                else {
                    if(deltaY==1) {
                        y0=y1++;
                        x0=x1;
                    }
                    else if (deltaY==-1){
                        y0=y1--;
                        x0=x1;
                    }
                    else {
                        result=false;
                        break;
                    }
                }
            }
            else if(currentCell.type.equalsIgnoreCase("End")){
                if(deltaX==1 && currentCell.property.equalsIgnoreCase("Horizontal")){
                    break;
                }
                else if(deltaY==-1 && currentCell.property.equalsIgnoreCase("Vertical")){
                    break;
                }
                else{
                    result=false;
                    break;
                }
            }
            else{
                result=false;
                break;
            }

        }
        return result;


    }

    public Label getMoveCounter() {
        return moveCounter;
    }

    public void setMoveCounter(Label moveCounter) {
        this.moveCounter = moveCounter;
    }

    public Tiles[][] getBoard() {
        return board;
    }

    public void setBoard(Tiles[][] board) {
        this.board = board;
    }

    public int getNumberOfUnlockedLevel() {
        return numberOfUnlockedLevel;
    }

    public void setNumberOfUnlockedLevel(int numberOfUnlockedLevel) {
        this.numberOfUnlockedLevel = numberOfUnlockedLevel;
    }


}