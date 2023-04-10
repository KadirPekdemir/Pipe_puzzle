package com.example.pipedeneme;

class Tiles{
    //Each id number dedicated to cells(Pipes and etc.).
    public int id;
    //Each type for cells. Ex: Pipe,PipeStatic,Empty,etc.
    public String type;
    //Each property for cells. Ex: 00,01,10,11,Vertical,Horizontal,Free,none,etc.
    public String property;

    //Constructor for cells.
    public Tiles(int id, String type, String property){
        this.id = id;
        this.type = type;
        this.property = property;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }
}