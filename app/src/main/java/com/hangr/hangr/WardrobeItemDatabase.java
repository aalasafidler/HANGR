package com.hangr.hangr;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

// Have to add 1 to version every time you change the schema of the db, e.g. if you add columns
@Database(entities = {WardrobeItem.class, Outfit.class},version = 3)
public abstract class WardrobeItemDatabase extends RoomDatabase {
    public abstract WardrobeItemDao wardrobeItemDao();
}
