package com.me.remenber.repositorys;

import android.content.Context;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.me.remenber.dao.UserDao;
import com.me.remenber.dataModel.DataConverter;
import com.me.remenber.entitys.User;


@Database(entities = User.class,
        version = 1,
        exportSchema = false)
@TypeConverters(DataConverter.class)
public abstract class UserDataBase extends RoomDatabase {
//    public abstract class UserDataBase{
    private static UserDataBase storageDB = null;

    public abstract UserDao userDao();

    public static synchronized UserDataBase getDBIstance(Context context) {
        if (storageDB == null) {
            storageDB = Room.databaseBuilder(context.getApplicationContext(),
                            UserDataBase.class, "USERSTORAGE19B2")
                    .allowMainThreadQueries()
                    .build();
        }
        return storageDB;
    }

}
