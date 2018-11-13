package com.hangr.hangr;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.lifecycle.LiveData;


import java.util.List;

@Dao
public interface WardrobeItemDao {
    // Define SQL queries you would like to run here
    // Let's you call java methods which will go on to execute SQL queries on our db

    // Insert an item into the db
    @Insert
    public void addItem(WardrobeItem item);

    @Query("DELETE FROM items")
    void deleteAll();

    // Get every row from the db
    @Query("select * from items")
    public LiveData<List<WardrobeItem>> getItems();
}
