package com.example.myapplication.appRoomDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.myapplication.VirtualObjectDetails;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

@Dao
public interface VirtualObjectDetailsDao {

    @Query("SELECT * FROM VirtualObjectDetails WHERE id = :id")
    ListenableFuture<VirtualObjectDetails> findById(int id);

    @Query("SELECT * FROM VirtualObjectDetails")
    ListenableFuture<List<VirtualObjectDetails>> getAll();

    @Query("SELECT * FROM VirtualObjectDetails WHERE id IN (:virtualObjectIds)")
    ListenableFuture<List<VirtualObjectDetails>> loadAllByIds(int[] virtualObjectIds);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<Void> insertAll(VirtualObjectDetails... virtualObjectDetails);

    @Delete
    ListenableFuture<Void> delete(VirtualObjectDetails virtualObjectDetails);
}
