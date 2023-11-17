package com.example.myapplication.appRoomDB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.myapplication.VirtualObjectDetails;

@Database(entities = {VirtualObjectDetails.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract VirtualObjectDetailsDao virtualObjectDao();
}
