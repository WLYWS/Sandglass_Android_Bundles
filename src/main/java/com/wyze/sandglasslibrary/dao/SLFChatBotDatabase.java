package com.wyze.sandglasslibrary.dao;

import android.content.Context;

import com.wyze.sandglasslibrary.moudle.SLFChatBotMsgData;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

/**
 * Created by wangjian on 2023/1/6
 */
@Database(version = 1,entities = {SLFChatBotMsgData.class},exportSchema=true)
public abstract class SLFChatBotDatabase extends RoomDatabase {

    // 提供Dao的实例
    public abstract SLFMsgDao bookDao();
    // 单例
    private static SLFChatBotDatabase database;

    public static SLFChatBotDatabase getInstance (Context context){
        if (database == null){
            synchronized (SLFChatBotDatabase.class){
                if (database == null){
                    database = Room.databaseBuilder(context.getApplicationContext(),SLFChatBotDatabase.class,"chatbotmessage.db").build();
                }
            }
        }
        return database;
    }
}
