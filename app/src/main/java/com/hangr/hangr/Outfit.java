package com.hangr.hangr;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.net.Uri;
import android.support.annotation.NonNull;

@Entity(tableName = "outfits")
public class Outfit {

    @PrimaryKey
    @ColumnInfo( name  = "position" )
    private int mPosition;

    @ColumnInfo( name = "uri")
    private String mUri;

    @ColumnInfo( name = "file_path")
    private String mFilePath;

    //Default constructor
    public Outfit()
    {
    }

    //Overloaded constructor
    public void Outfit(@NonNull int position, String uri, String filePath)
    {
        this.mPosition = position;
        this.mUri = uri;
        this.mFilePath = filePath;

    }

    public int getPosition() { return mPosition; }

    public void setPosition( int position ) { this.mPosition = position; }

    public String getUri() { return mUri; }

    public void setUri( String uri ) { this.mUri = uri; }

    public String getFilePath() { return mFilePath; }

    public void setFilePath( String filePath ) { this.mFilePath = filePath; }

}
