package com.hangr.hangr;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

// Have to add 1 to version every time you change the schema of the db, e.g. if you add columns
@Database(entities = {WardrobeItem.class}, version = 3)
public abstract class WardrobeItemDatabase extends RoomDatabase {

    public abstract WardrobeItemDao wardrobeItemDao();

    public static volatile WardrobeItemDatabase INSTANCE;

    static WardrobeItemDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WardrobeItemDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WardrobeItemDatabase.class, "wardrobe_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            // If you want to keep the data through app restarts,
            // comment out the following line.
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    /**
     * Populate the database in the background.
     * If you want to start with more words, just add them.
     */
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final WardrobeItemDao mDao;

        PopulateDbAsync(WardrobeItemDatabase db) {
            mDao = db.wardrobeItemDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            mDao.deleteAll();

            WardrobeItem item = new WardrobeItem("Top", "home", "formal", "red", true, 1);
            mDao.addItem(item);
            return null;
        }
    }



}