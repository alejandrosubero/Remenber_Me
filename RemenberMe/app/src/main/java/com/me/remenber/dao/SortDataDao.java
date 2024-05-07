package com.me.remenber.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.me.remenber.entitys.SortData;

import java.util.List;

@Dao
public interface SortDataDao {

    @Query("select * from SortData")
    List<SortData> getAll();

    @Insert
    void insert(SortData data);

    @Update
    void update(SortData data);

    @Delete
    void delete(SortData data);

    @Query("SELECT * FROM SortData WHERE uid IN (:ids)")
    List<SortData> loadAllByIds(int[] ids);

    @Query("SELECT * FROM SortData WHERE name IN (:name)")
    List<SortData> findByName(String name);


    @Query("SELECT * FROM SortData WHERE codeUser IN (:code)")
    List<SortData> findByCode(String code);

    @Query("SELECT * FROM SortData WHERE codeUser LIKE :search AND typeData = :dataType")
    public List<SortData> findBySearch(String search, String dataType);

    @Query("SELECT * FROM SortData WHERE codeUser LIKE :search AND typeData = :dataType AND isblock = :isblock ")
    public List<SortData> findBySearch2(String search, String dataType, boolean isblock);

    @Query("SELECT * FROM SortData WHERE keyr LIKE :search")
    List<SortData> findByCode3(String search);

}
