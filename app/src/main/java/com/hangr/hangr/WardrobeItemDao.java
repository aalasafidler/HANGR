package com.hangr.hangr;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface WardrobeItemDao {
    // Define SQL queries you would like to run here
    // Let's you call java methods which will go on to execute SQL queries on our db

    // Insert an item into the db
    @Insert
    public void addItem(WardrobeItem item);

    // Get every row from the db
    @Query("select * from items")
    public List<WardrobeItem> getItems();

    @Query("SELECT * FROM outfits")
    public List<Outfit> getOutfits();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void addOutfit( Outfit outfit);

    @Query("SELECT MAX(position) FROM outfits")
    public int getMaxPosition();





}
