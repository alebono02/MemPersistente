package com.example.myapplication.appRoomDB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {VirtualObject.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract VirtualObjectDao virtualObjectDao();
}
