package com.example.myapplication.appRoomDB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class VirtualObject {
    @PrimaryKey
    public int id;

    @ColumnInfo(name = "type")
    public String type;

    @ColumnInfo(name = "level")
    public int level;

    @ColumnInfo(name = "latitude")
    public double lat;

    @ColumnInfo(name = "longitude")
    public double lon;

    @ColumnInfo(name = "image")
    public String image;

    @ColumnInfo(name = "name")
    public String name;

    public VirtualObject(int id, String type, int level, double lat, double lon, String image, String name) {
        this.id = id;
        this.type = type;
        this.level = level;
        this.lat = lat;
        this.lon = lon;
        this.image = image;
        this.name = name;
    }
}
