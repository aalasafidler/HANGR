package com.hangr.hangr;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.net.Uri;
import android.support.annotation.NonNull;

@Entity(tableName = "outfits")
public class Outfit {

    @PrimaryKey
    private int mPosition;

    private Uri mUri;

    private String mFilePath;

    //Default constructor
    public Outfit()
    {
    }

    //Overloaded constructor
    public void Outfit(@NonNull int position, Uri uri, String filePath)
    {
        this.mPosition = position;
        this.mUri = uri;
        this.mFilePath = filePath;

    }

    public int getPosition() { return mPosition; }

    public void setPosition( int position ) { this.mPosition = position; }

    public Uri getUri() { return mUri; }

    public void setUri( Uri uri ) { this.mUri = uri; }

    public String getFilePath() { return mFilePath; }

    public void setFilePath( String filePath ) { this.mFilePath = filePath; }

}
