package com.hangr.hangr;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "items")
public class WardrobeItem {
    // These instance variables are the columns of the database

    // autoGenerate: Increments ID every time an item is added, 1, 2, ...
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String category;

    private String location;

    private String style;

    private String colour;

    private boolean clean;

    private String imageFilePath;

    private int mGridPosition;

    // Default constructor
//    public WardrobeItem() {
//    }

    // Getters and setters are used to get the value of a column or set the values of the columns when you add a new row
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public boolean getClean() {
        return clean;
    }

    public void setClean(boolean clean) {
        this.clean = clean;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public void setImageFilePath(String filePath) {
        this.imageFilePath = filePath;
    }

    public int getGridPosition() { return mGridPosition; }

    public void setGridPosition(int gridPosition) { this.mGridPosition = gridPosition; }

    public WardrobeItem(String category, String location, String style, String colour, boolean clean, int gridPosition)
    {
        this.category = category;
        this.location = location;
        this.style = style;
        this.colour = colour;
        this.clean = clean;
        this.mGridPosition = gridPosition;
    }
}
