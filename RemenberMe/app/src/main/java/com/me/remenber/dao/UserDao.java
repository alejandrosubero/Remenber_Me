package com.me.remenber.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.me.remenber.entitys.User;

import java.util.List;

@Dao
public interface UserDao {

    @Query("select * from User")
    List<User> getAll();

    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM User WHERE userId IN (:ids)")
    List<User> loadAllByIds(int[] ids);

    @Query("SELECT * FROM User WHERE userName IN (:name)")
    List<User> findByName(String name);

    @Query("SELECT * FROM User WHERE userMail IN (:email)")
    List<User> findByMail(String email);

    @Query("SELECT * FROM User WHERE codeUser IN (:code)")
    List<User> findByCode(String code);

    @Query("SELECT * FROM User WHERE userName LIKE :search " + "AND userPass = :pass")
    public List<User> findBySearch(String search, String pass);

    @Query("SELECT * FROM User WHERE deletePassword LIKE :search")
    public List<User> findByDeletePassword(String search);

}
