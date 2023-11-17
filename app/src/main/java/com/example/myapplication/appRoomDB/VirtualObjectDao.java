package com.example.myapplication.appRoomDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

@Dao
public interface VirtualObjectDao {

    @Query("SELECT * FROM virtualobject WHERE id = :id")
    ListenableFuture<VirtualObject> findById(int id);

    @Query("SELECT * FROM virtualobject")
    ListenableFuture<List<VirtualObject>> getAll();

    @Query("SELECT * FROM virtualobject WHERE id IN (:virtualObjectIds)")
    ListenableFuture<List<VirtualObject>> loadAllByIds(int[] virtualObjectIds);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<Void> insertAll(VirtualObject... virtualObjects);

    @Delete
    ListenableFuture<Void> delete(VirtualObject virtualObject);
}
