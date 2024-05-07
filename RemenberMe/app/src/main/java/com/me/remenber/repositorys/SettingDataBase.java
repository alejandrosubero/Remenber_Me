package com.me.remenber.repositorys;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.me.remenber.dao.SettingDao;
import com.me.remenber.dataModel.DataConverter;
import com.me.remenber.entitys.Settings;


@Database(entities = Settings.class,
        version = 1,
        exportSchema = false)
@TypeConverters(DataConverter.class)
public abstract class SettingDataBase extends RoomDatabase {

    private static SettingDataBase storageDB = null;
    public abstract SettingDao settingDao();

       public static synchronized SettingDataBase getDBIstance(Context context) {
        if (storageDB == null) {
            storageDB = Room.databaseBuilder(context.getApplicationContext(),
                            SettingDataBase.class, "SETTINGSTORAGEB612")
                    .allowMainThreadQueries()
                    .build();
        }
        return storageDB;
    }


}
