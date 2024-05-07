package com.me.remenber.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.me.remenber.entitys.Settings;


import java.util.List;

@Dao
public interface SettingDao {

    @Query("select * from Settings")
    List<Settings> getAll();

    @Insert
    void insert(Settings data);

    @Update
    void update(Settings data);

    @Delete
    void delete(Settings data);

    @Query("SELECT * FROM Settings WHERE settingsId IN (:ids)")
    List<Settings> loadAllByIds(int[] ids);

    @Query("SELECT * FROM Settings WHERE codeUser IN (:code)")
    List<Settings> findByCode(String code);


}
