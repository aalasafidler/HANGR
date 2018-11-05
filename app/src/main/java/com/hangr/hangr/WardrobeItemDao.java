package com.hangr.hangr;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface WardrobeItemDao {

    @Insert
    public void addItem(WardrobeItem item);

    @Query("select * from items")
    public List<WardrobeItem> getItems();
}
