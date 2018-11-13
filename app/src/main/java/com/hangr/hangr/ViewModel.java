package com.hangr.hangr;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class ViewModel extends AndroidViewModel {

    private WardrobeRepository mRepository;
    private LiveData<List<WardrobeItem>> mAllItems;

    public ViewModel(Application application) {
        super(application);
        mRepository = new WardrobeRepository(application);
        mAllItems = mRepository.getItems();
    }

    LiveData<List<WardrobeItem>> getItems() {
        return mAllItems;
    }

    public void insert(WardrobeItem item) {
        mRepository.insert(item);
    }

}