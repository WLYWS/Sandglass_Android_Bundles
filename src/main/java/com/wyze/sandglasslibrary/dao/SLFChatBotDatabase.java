package com.wyze.sandglasslibrary.dao;

import android.content.Context;

import com.wyze.sandglasslibrary.moudle.SLFChatBotMsgData;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Created by wangjian on 2023/1/6
 */
@Database(version = 1,entities = {SLFChatBotMsgData.class},exportSchema=false)
public abstract class SLFChatBotDatabase extends RoomDatabase {

    private final static String db_name = "chatbot.db";
    // 提供Dao的实例
    public abstract SLFMsgDao msgDao();
    // 单例
    private static SLFChatBotDatabase database;

    public static SLFChatBotDatabase getInstance (Context context){
        if (database == null){
            synchronized (SLFChatBotDatabase.class){
                if (database == null){
                    database = Room.databaseBuilder(context.getApplicationContext(),SLFChatBotDatabase.class,db_name).build();
                }
            }
        }
        return database;
    }
}
