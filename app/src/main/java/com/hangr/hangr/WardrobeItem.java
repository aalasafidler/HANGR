package com.hangr.hangr;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "items")
public class WardrobeItem {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String category;

    private String location;

    private String style;

    private String colour;

    private boolean clean;

    private String imageFilePath;

    public WardrobeItem() {
    }

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
}
