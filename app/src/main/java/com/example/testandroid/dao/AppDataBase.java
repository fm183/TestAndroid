package com.example.testandroid.dao;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.testandroid.bean.User;

@Database(entities = {User.class},version = 3,exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    public abstract UserDao userDao();

    public static final Migration MIGRATION = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE User ADD COLUMN extra TEXT");
        }
    };
    public static final Migration MIGRATION2 = new Migration(2,3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // 创建临时表
            database.execSQL(
                    "CREATE TABLE users_new (id INTEGER NOT NULL,name TEXT, password TEXT,extras TEXT, PRIMARY KEY(id))");
            // 拷贝数据
            database.execSQL(
                    "INSERT INTO users_new (id, name, password,extras) SELECT id, name, password,extra FROM User");
            // 删除老的表
            database.execSQL("DROP TABLE User");
            // 改名
            database.execSQL("ALTER TABLE users_new RENAME TO User");
        }
    };
}
