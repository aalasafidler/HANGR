package com.hangr.hangr;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class WardrobeRepository {

    private WardrobeItemDao mWardrobeItemDao;
    private LiveData<List<WardrobeItem>> mAllItems;

    WardrobeRepository(Application application) {
        WardrobeItemDatabase db = WardrobeItemDatabase.getDatabase(application);
        mWardrobeItemDao = db.wardrobeItemDao();
        mAllItems = mWardrobeItemDao.getItems();
    }

    LiveData<List<WardrobeItem>> getItems() {
        return mAllItems;
    }
    void insert(WardrobeItem item) {
        new insertAsyncTask(mWardrobeItemDao).execute(item);

    }

    private static class insertAsyncTask extends AsyncTask<WardrobeItem, Void, Void> {
        private WardrobeItemDao mAsyncTaskDao;

        insertAsyncTask(WardrobeItemDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override protected Void doInBackground(final WardrobeItem... params) {
            mAsyncTaskDao.addItem(params[0]);
            return null;
        }
    }

}
