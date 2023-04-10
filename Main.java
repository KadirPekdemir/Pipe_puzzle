package com.example.pipedeneme;

import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.*;

public class Main extends Application {
    private Game game;
    private int numberOfLevel;
    private int numberOfMoves;
    private String[] levels;
    private Rectangle[][] tiles;
    private GridPane boardPane;
    private Label gameStatusLabel;
    private Circle ball;
    private Scene introScene;
    private Scene gameScene;
    private final ObjectProperty<Point2D> selectedRectangle = new SimpleObjectProperty<>();
    private  final String  BACKGROUNDCOLOR = "-fx-background-color: #C0C0C0; ";

    @Override
    public void start(Stage window) throws Exception{
        game = new Game();
        // **** SCENE - GAME *****
        tiles = new Rectangle[4][4];
        levels = new String[]{"level1.txt","level2.txt","level3.txt","level4.txt","level5.txt","level6.txt","level7.txt","level8.txt","level9.txt"};
        game.loadGame();
        // We create new GridPane
        boardPane = new GridPane();
        boardPane.setPadding(new Insets(15,54,20,54));
        boardPane.setVgap(0);
        boardPane.setHgap(0);
       // we create new Circle
        ball = new Circle(20,new ImagePattern(new Image(new File("images/ball.png").toURI().toString())));
        ball.setCenterX(102);
        ball.setCenterY(95);
        //In this for loop we create tiles of 4 by 4
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tiles[i][j] = new Rectangle(98,98,Color.WHITE);
                GridPane.setConstraints(tiles[i][j],i,j);
                boardPane.add(tiles[i][j],i,j);
            }
        }
        //we create a mainMenuButton and set size.
        Button mainMenuButton = new Button("Main Menu");
        mainMenuButton.setOnAction(e -> window.setScene(introScene));
        mainMenuButton.setMinSize(80,50);

        //we create a gameStatusLabel and set size.
        gameStatusLabel = new Label("Level-"+Integer.toString(game.getNumberOfUnlockedLevel()+1));
        Button btGameStatusLabel = new Button("",gameStatusLabel);
        btGameStatusLabel.setMinSize(190,50);

        //we create a moveCounter, set text and set size.
        game.moveCounter = new Label();
        game.getMoveCounter().setText("Moves: "+numberOfMoves);
        game.getMoveCounter().setMinSize(80,50);

        //we create a levelNumberButton and set center.
        HBox levelNumberButton = new HBox();
        levelNumberButton.getChildren().add(btGameStatusLabel);
        levelNumberButton.setAlignment(Pos.TOP_CENTER);
        levelNumberButton.setPadding(new Insets(0,0,0,0));

        // we create a gameStatusLayout and set color.
        HBox gameStatusLayout = new HBox(10);
        gameStatusLayout.setAlignment(Pos.CENTER);
        gameStatusLayout.getChildren().addAll(mainMenuButton, game.getMoveCounter());
        gameStatusLayout.setStyle(BACKGROUNDCOLOR);

        //we create a gameView.
        Pane gameView = new Pane();
        gameView.getChildren().addAll(boardPane,ball);
        gameView.setStyle(BACKGROUNDCOLOR);
        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(gameView);

        // we create a gameLayout
        VBox gameLayout = new VBox(10);
        gameLayout.setAlignment(Pos.CENTER);
        gameLayout.setStyle(BACKGROUNDCOLOR);
        gameLayout.getChildren().addAll(levelNumberButton,borderPane,gameStatusLayout);

        // we create a gameScene.
        gameScene = new Scene(gameLayout,500,600);
        Label title = new Label("Welcome to Unblock Ball  Game");
        title.setFont(new Font("Helvetica",40));

        // **** SCENE - WELCOME  *****

        //We create a buttons for levels
        Button[] levelButtons = new Button[9];
        String[] levelsButtonImagesString = new String[]{"images/level1.png","images/level2.png","images/level3.png","images/level4.png",
                "images/level5.png","images/level6.png","images/level7.png",
                "images/level8.png","images/level9.png","images/level_title.png"};
        FileInputStream[] levelsButtonImages = new FileInputStream[10];
        for (int i = 0; i < 10; i++) {
            levelsButtonImages[i] = new FileInputStream(levelsButtonImagesString[i]);
        }
        // we create for loop.This for loop adds photos to the level buttons.
        for (int i = 0; i < 9; i++) {
            Image image = new Image(levelsButtonImages[i],130,130,true,true);
            ImageView imageView = new ImageView(image);
            levelButtons[i]=new Button("",imageView);
            levelButtons[i].setStyle(BACKGROUNDCOLOR);
            Rectangle rectangle = new Rectangle(150, 150 );
            levelButtons[i].setShape(rectangle);
        }

        Image image1 = new Image(levelsButtonImages[9],190,190,true,true);
        ImageView imageView0 = new ImageView(image1);
        // we create an hBox.
        HBox hBox = new HBox();
        hBox.getChildren().addAll(imageView0);
        hBox.setPrefSize(20 ,70);
        hBox.setAlignment(Pos.CENTER);
        hBox.setStyle(BACKGROUNDCOLOR);

        // we create a levelNumber.
        String[] levelNumber = new String[9];
        String temp = "Level-";
        for (int i = 0; i <levelNumber.length ; i++) {
            levelNumber[i]=temp+(i+1);
        }
        
        for (int i = 0; i < 9; i++) {
            int finalI = i;
            levelButtons[i].setOnAction(e -> {
                if(game.getNumberOfUnlockedLevel()>(finalI -1)) {
                    numberOfLevel = finalI;
                    game.createMapData(levels[finalI]);
                    createGridPane();
                    gameStatusLabel.setText(levelNumber[finalI]);
                    window.setScene(gameScene);
                    backToStarter();
                }
            });
        }
        GridPane gridPane = new GridPane();
        int tempCounter = -1;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tempCounter++;
                gridPane.add(levelButtons[tempCounter],j,i);
            }
        }
        gridPane.setHgap(15);
        gridPane.setVgap(15);
        gridPane.setStyle(BACKGROUNDCOLOR);

        BorderPane welcomePane = new BorderPane();
        welcomePane.setTop(hBox);
        welcomePane.setCenter(gridPane);

        introScene = new Scene(welcomePane,500,600);
        window.setScene(introScene);
        window.setTitle("Unblock Ball");
        window.setOnCloseRequest(e -> game.saveGame());
        window.show();
    }
    public static void main(String[] args) {
        launch(args);

    }
    //This method causes the ball to roll down the pipe when the game is over.
    public  PathTransition displayAnimation() {

        int x0 = 0 ,y0 = 0;
        int x1,y1;
        int i=0,j=0;
        boolean w = true;
        //this loop finds the position of the starting rectangle.
        while(!game.getBoard()[x0][y0].type.equalsIgnoreCase("Starter")){
            x0++;
            if(x0>=4){
                y0++;
                x0 = 0;
            }
        }
        Path path = new Path();
        double a = 0 ,b = 0;
        if(game.getBoard()[x0][y0].property.equalsIgnoreCase("Vertical")){
            a = 102 + x0*98 ;
            b = 56 + y0*98;
            i+=y0;
            j+=x0;
        }
        else {
            a = 102 + x0*98;
            b = 64 + y0*98;
            i+=y0;
            j+=x0;
        }
        path.getElements().add(new MoveTo(a,b));
        if(game.getBoard()[x0][y0].property.equalsIgnoreCase("Vertical")){

            i++;
            path.getElements().add(new VLineTo(i*98+64));

            //We adjusted the position of the square where the ball reached.
            x1 = x0;
            y1 = y0+1;
        }
        else {
            j--;
            path.getElements().add(new HLineTo(j*98+102));

            //We adjusted the position of the square where the ball reached.
            x1 = x0-1;
            y1 = y0;
        }
        //this loop is broken when it reaches its end point.
        while(w) {


            //We use these variables to understand from which direction the ball enters the rectangle.
            int deltaX = x1 - x0;
            int deltaY = y1 - y0;

            Tiles currentCell = game.getBoard()[x1][y1];
            if (currentCell.type.equalsIgnoreCase("Pipe")||currentCell.type.equalsIgnoreCase("PipeStatic")) {
                if (currentCell.property.equalsIgnoreCase("Vertical")) {
                    if(deltaY==1) {//Did the ball go from the top?
                        i++;
                        path.getElements().add(new VLineTo(i*98+64));

                        //We adjusted the position of the square where the ball reached.
                        x0 = x1;
                        y0 = y1++;
                    }
                    else {
                        i--;
                        path.getElements().add(new VLineTo(i*98+64));

                        //We adjusted the position of the square where the ball reached.
                        x0 = x1;
                        y0 = y1--;
                    }
                }
                else if (currentCell.property.equalsIgnoreCase("Horizontal")) {
                    if (deltaX==1) {//Did the ball go from the left?
                        j++;
                        path.getElements().add(new HLineTo(j*98+102));

                        //We adjusted the position of the square where the ball reached.
                        x0 = x1++;
                        y0 = y1;
                    }
                    else {

                        j--;
                        path.getElements().add(new HLineTo(j*98+102));

                        //We adjusted the position of the square where the ball reached.
                        x0 = x1--;
                        y0 = y1;

                    }
                }
                else if (currentCell.property.equals("00")) {
                    if(deltaY==1) {//Did the ball go from the top?
                        j--;
                        path.getElements().add(new HLineTo(j*98+102));

                        //We adjusted the position of the square where the ball reached.
                        x0 = x1--;
                        y0 = y1;
                    }
                    else {
                        i--;
                        path.getElements().add(new VLineTo(i*98+64));

                        //We adjusted the position of the square where the ball reached.
                        x0 = x1;
                        y0 = y1--;
                    }
                }
                else if (currentCell.property.equals("01")) {
                    if(deltaY==1) {//Did the ball go from the top?
                        j++;
                        path.getElements().add(new HLineTo(j*98+102));

                        //We adjusted the position of the square where the ball reached.
                        x0 = x1++;
                        y0 = y1;
                    }
                    else {
                        i--;
                        path.getElements().add(new VLineTo(i*98+64));

                        //We adjusted the position of the square where the ball reached.
                        x0 = x1;
                        y0 = y1--;
                    }
                }
                else if (currentCell.property.equals("10")) {
                    if(deltaX==1) {//Did the ball go from the left?
                        i++;
                        path.getElements().add(new VLineTo(i*98+64));

                        //We adjusted the position of the square where the ball reached.
                        x0 = x1;
                        y0 = y1++;
                    }
                    else {
                        j--;
                        path.getElements().add(new HLineTo(j*98+102));

                        //We adjusted the position of the square where the ball reached.
                        x0 = x1--;
                        y0 = y1;
                    }
                }
                else if (currentCell.property.equals("11")) {
                    if(deltaX==-1) {//Did the ball go from the right?
                        i++;
                        path.getElements().add(new VLineTo(i*98+64));

                        //We adjusted the position of the square where the ball reached.
                        x0 = x1;
                        y0 = y1++;
                    }
                    else {
                        j++;
                        path.getElements().add(new HLineTo(j*98+102));

                        //We adjusted the position of the square where the ball reached.
                        x0 = x1++;
                        y0 = y1;
                    }
                }
            }
            else if(currentCell.type.equalsIgnoreCase("End")) {
                if(currentCell.property.equalsIgnoreCase("Horizontal")) {
                    path.getElements().add(new HLineTo(j*98+111));
                }
                else {
                    path.getElements().add(new VLineTo(i*98+56));
                }
                w=false;
            }
        }

        PathTransition animation = new PathTransition();
        animation.setPath(path);
        animation.setNode(ball);
        animation.setDuration(Duration.seconds(1.5));
        animation.setCycleCount(1);
        animation.play();

        return animation;
    }
    //This method creates the level according to the entered input.
     public void createGridPane(){
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                String type = game.getBoard()[i][j].type;
                String property = game.getBoard()[i][j].property;
                Image temp;
                if (type.equalsIgnoreCase("Starter")) {
                    if(property.equalsIgnoreCase("Horizontal")){
                        temp = new Image(new File("images/Starter_Horizontal.png").toURI().toString());
                        (tiles[i][j]).setFill(new ImagePattern(temp));
                    }
                    else {
                        temp = new Image(new File("images/Starter_Vertical.png").toURI().toString());
                        (tiles[i][j]).setFill(new ImagePattern(temp));
                    }
                }

                else if (type.equalsIgnoreCase("End")) {
                    if(property.equalsIgnoreCase("Horizontal")) {
                        temp=new Image(new File("images/End_Horizontal.png").toURI().toString());
                        (tiles[i][j]).setFill(new ImagePattern(temp));
                    }
                    else{
                        temp=new Image(new File("images/End_Vertical.png").toURI().toString());
                        (tiles[i][j]).setFill(new ImagePattern(temp));
                    }
                }

                else if (type.equalsIgnoreCase("Pipe")) {
                    if(property.equalsIgnoreCase("Vertical")) {
                        temp=new Image(new File("images/Pipe_Vertical.png").toURI().toString());
                        (tiles[i][j]).setFill(new ImagePattern(temp));
                        setDraggable(tiles[i][j]);
                    }
                    else if(property.equalsIgnoreCase("Horizontal")){
                        temp=new Image(new File("images/Pipe_Horizontal.png").toURI().toString());
                        (tiles[i][j]).setFill(new ImagePattern(temp));
                        setDraggable(tiles[i][j]);
                    }
                    else if(property.equalsIgnoreCase("00")){
                        temp=new Image(new File("images/CurvedPipe_00.png").toURI().toString());
                        (tiles[i][j]).setFill(new ImagePattern(temp));
                        setDraggable(tiles[i][j]);
                    }
                    else if(property.equalsIgnoreCase("01")){
                        temp=new Image(new File("images/CurvedPipe_01.png").toURI().toString());
                        (tiles[i][j]).setFill(new ImagePattern(temp));
                        setDraggable(tiles[i][j]);
                    }
                    else if(property.equalsIgnoreCase("10")){
                        temp=new Image(new File("images/CurvedPipe_10.png").toURI().toString());
                        (tiles[i][j]).setFill(new ImagePattern(temp));
                        setDraggable(tiles[i][j]);
                    }
                    else{
                        temp=new Image(new File("images/CurvedPipe_11.png").toURI().toString());
                        (tiles[i][j]).setFill(new ImagePattern(temp));
                        setDraggable(tiles[i][j]);
                    }
                }
                else if (type.equalsIgnoreCase("PipeStatic")) {
                    if(property.equalsIgnoreCase("Horizontal")){
                        temp=new Image(new File("images/PipeStatic_Horizontal.png").toURI().toString());
                        (tiles[i][j]).setFill(new ImagePattern(temp));
                    }
                    else if(property.equalsIgnoreCase("00")){
                        temp=new Image(new File("images/PipeStatic_00.png").toURI().toString());
                        (tiles[i][j]).setFill(new ImagePattern(temp));
                    }
                    else if(property.equalsIgnoreCase("01")){
                        temp=new Image(new File("images/PipeStatic_01.png").toURI().toString());
                        (tiles[i][j]).setFill(new ImagePattern(temp));
                    }
                    else if(property.equalsIgnoreCase("10")){
                        temp=new Image(new File("images/PipeStatic_10.png").toURI().toString());
                        (tiles[i][j]).setFill(new ImagePattern(temp));
                    }
                    else if(property.equalsIgnoreCase("11")){
                        temp=new Image(new File("images/PipeStatic_11.png").toURI().toString());
                        (tiles[i][j]).setFill(new ImagePattern(temp));
                    }
                    else{
                        temp=new Image(new File("images/PipeStatic_Vertical.png").toURI().toString());
                        (tiles[i][j]).setFill(new ImagePattern(temp));
                    }
                } else if (type.equalsIgnoreCase("Empty")) {
                    if(property.equalsIgnoreCase("Free")){
                        temp=new Image(new File("images/Empty_Free.png").toURI().toString());
                        (tiles[i][j]).setFill(new ImagePattern(temp));
                        setSlot(tiles[i][j]);
                    }
                    else{
                        temp=new Image(new File("images/Empty.png").toURI().toString());
                        (tiles[i][j]).setFill(new ImagePattern(temp));
                        setDraggable(tiles[i][j]);
                    }
                }
                else {
                    System.out.println("Board cannot be generated.");
                    System.exit(-1);

                }
            }
        }
    }
    //This method checks the end of the level with the checkOver method.
    //If the level is over, it makes the ball animation and moves to the new level.
    public void checkTiles() {
        if(game.checkOver()){
            if(game.getNumberOfUnlockedLevel()<8) {
                PathTransition p = displayAnimation();
                p.setOnFinished(t -> {

                    numberOfLevel++;
                    if(numberOfLevel>8){
                        numberOfLevel = 0;
                    }
                    nextLevel();
                   backToStarter();


                    gameStatusLabel.setText("Level-"+Integer.toString(numberOfLevel+1));
                });
            }
            else {
                PathTransition p = displayAnimation();
                p.setOnFinished(t -> {

                    numberOfLevel++;
                    if(numberOfLevel>8){
                        numberOfLevel = 0;
                    }
                    game.createMapData(levels[numberOfLevel]);
                    createGridPane();
                    backToStarter();
                    gameStatusLabel.setText("Level-"+Integer.toString(numberOfLevel+1));
                });
            }
        }
    }

    //This method takes the ball to the starter position of the new level after the level is over.
   public  PathTransition backToStarter() {
        int x0 = 0 ,y0 = 0;
        double a , b;
        //this loop finds the position of the starting rectangle.
        while(!game.getBoard()[x0][y0].type.equalsIgnoreCase("Starter")){
            x0++;
            if(x0>=4){
                y0++;
                x0 = 0;
            }
        }
        Path path = new Path();
        if(game.getBoard()[x0][y0].property.equalsIgnoreCase("Vertical")){
            a = 102 + x0*98 ;
            b = 56 + y0*98;
        }
        else {
            a = 102 + x0*98;
            b = 64 + y0*98;
        }
        path.getElements().add(new MoveTo(a+1,b+1));
        if(game.getBoard()[x0][y0].property.equalsIgnoreCase("Vertical")){
            path.getElements().add(new VLineTo(b));
            path.getElements().add(new HLineTo(a));

        }
        else {
            path.getElements().add(new VLineTo(b));
            path.getElements().add(new HLineTo(a+7));
        }

        PathTransition animation = new PathTransition();
        animation.setPath(path);
        animation.setNode(ball);
        animation.setDuration(Duration.seconds(0.001));
        animation.setCycleCount(1);
        animation.play();
        //We set numberOfMoves to zero because we moved to the new level.
        numberOfMoves=0;
        game.getMoveCounter().setText("Moves: "+numberOfMoves);
        return animation;
    }
    public void nextLevel(){
        game.numberOfUnlockedLevel++;
        game.createMapData(levels[numberOfLevel]);
        createGridPane();
        game.moveCounter.setText("Moves: 0");//moveCounter.setText("Moves: "+numberOfMoves); boyleydi

    }
    public void setDraggable(Rectangle r){
        r.setOnDragDetected(e -> {
            Rectangle temp=(Rectangle) e.getSource();
            int i;
            for(i=0;i<16;i++){
                if(tiles[i%4][i/4].equals(temp)){
                    break;
                }
            }
            selectedRectangle.set(new Point2D(i%4,i/4));
            r.startFullDrag();
            e.consume();
        });
    }
    //If the drag operation is legal, this method changes the position of the rectangles with the exchangeRectangles method.
    public void setSlot(Rectangle r){
        r.setOnMouseDragExited(e -> {
            Rectangle target = (Rectangle) e.getSource();
            int i;
            for(i=0;i<16;i++){
                if(tiles[i%4][i/4].equals(target)){
                    break;
                }
            }
            int x0=(int) selectedRectangle.get().getX();
            int y0=(int) selectedRectangle.get().getY();
            int x1=i%4;
            int y1=i/4;
            if(game.isLegal(x0,y0,x1,y1)){
                game.makeMove(x0,y0,x1,y1);
                exchangeRectangles(x0,y0,x1,y1);
                //numberOfMoves is increased by 1 after my drag is done
                numberOfMoves++;
                game.getMoveCounter().setText("Moves: "+numberOfMoves);
                //After the drag movement, we check whether the game is over.
                checkTiles();

            }
            //We end DragExited event.
            e.consume();
        });
    }
    //This method swaps the positions of two rectangles in the drag action.
    public void exchangeRectangles(int x0,int y0,int x1,int y1){
        boardPane.getChildren().remove(tiles[x0][y0]);
        boardPane.getChildren().remove(tiles[x1][y1]);
        boardPane.add(tiles[x1][y1],x0,y0);
        boardPane.add(tiles[x0][y0],x1,y1);
        Rectangle temp = tiles[x0][y0];
        tiles[x0][y0] = tiles[x1][y1];
        tiles[x1][y1] = temp;

    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public int getNumberOfLevel() {
        return numberOfLevel;
    }

    public void setNumberOfLevel(int numberOfLevel) {
        this.numberOfLevel = numberOfLevel;
    }

    public int getNumberOfMoves() {
        return numberOfMoves;
    }

    public void setNumberOfMoves(int numberOfMoves) {
        this.numberOfMoves = numberOfMoves;
    }

    public String[] getLevels() {
        return levels;
    }

    public void setLevels(String[] levels) {
        this.levels = levels;
    }

    public Rectangle[][] getTiles() {
        return tiles;
    }

    public void setTiles(Rectangle[][] tiles) {
        this.tiles = tiles;
    }

    public GridPane getBoardPane() {
        return boardPane;
    }

    public void setBoardPane(GridPane boardPane) {
        this.boardPane = boardPane;
    }

    public Label getGameStatusLabel() {
        return gameStatusLabel;
    }

    public void setGameStatusLabel(Label gameStatusLabel) {
        this.gameStatusLabel = gameStatusLabel;
    }

    public Circle getBall() {
        return ball;
    }

    public void setBall(Circle ball) {
        this.ball = ball;
    }

    public Scene getIntroScene() {
        return introScene;
    }

    public void setIntroScene(Scene introScene) {
        this.introScene = introScene;
    }

    public Scene getGameScene() {
        return gameScene;
    }

    public void setGameScene(Scene gameScene) {
        this.gameScene = gameScene;
    }

    public Point2D getSelectedRectangle() {
        return selectedRectangle.get();
    }

    public ObjectProperty<Point2D> selectedRectangleProperty() {
        return selectedRectangle;
    }

    public void setSelectedRectangle(Point2D selectedRectangle) {
        this.selectedRectangle.set(selectedRectangle);
    }

    public String getBACKGROUNDCOLOR() {
        return BACKGROUNDCOLOR;
    }
}