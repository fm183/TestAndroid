package com.example.testandroid.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.testandroid.bean.User;

import java.util.List;

@Dao
public interface UserDao {

    @Query("Select * from User")
    List<User> getAllUser();

    @Update
    void updateUser(User user);

    @Insert
    void insertUser(User user);

    @Query("SELECT * FROM User where id= :id")
    User getUserById(int id);

    @Delete
    void deleteUser(User user);
}
