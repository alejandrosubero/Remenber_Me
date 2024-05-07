package com.me.remenber.repositorys;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.me.remenber.dao.SortDataDao;
import com.me.remenber.dataModel.DataConverter;
import com.me.remenber.entitys.SortData;


@Database(entities = SortData.class,
        version = 1,
        exportSchema = false)
@TypeConverters(DataConverter.class)
public abstract class SortDataDataBase extends RoomDatabase {


    private static SortDataDataBase storageDB = null;

    public abstract SortDataDao sortDataDao();

    public static synchronized SortDataDataBase getDBIstance(Context context) {
        if (storageDB == null) {
            storageDB = Room.databaseBuilder(context.getApplicationContext(),
                            SortDataDataBase.class, "sortTORAGE19B2")
                    .allowMainThreadQueries()
                    .build();
        }
        return storageDB;
    }

}
