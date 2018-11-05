package com.hangr.hangr;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {WardrobeItem.class},version = 1)
public abstract class WardrobeItemDatabase extends RoomDatabase {
    public abstract WardrobeItemDao wardrobeItemDao();
}
