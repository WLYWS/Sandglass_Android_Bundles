package com.sandglass.sandglasslibrary.dao;

import com.sandglass.sandglasslibrary.moudle.SLFChatBotMsgData;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

/**
 * Created by wangjian on 2023/1/6
 */
@Dao
public interface SLFMsgDao {
    //查找所有
    @Query("select * from chatbotmessage")
    List <SLFChatBotMsgData> selectAll();

    //查找10条数据
    @Query("Select * From chatbotmessage where id < (:id) order by id desc Limit 10")
    List <SLFChatBotMsgData> selectLimitTen(int id);

    // 根据id查找
    @Query("select * from chatbotmessage where msgTime = (:msgTime)")
     SLFChatBotMsgData selectByTime(long msgTime);

    //更新数据库中的内容
    @Update
    void updateMsgData(SLFChatBotMsgData sLFChatBotMsgData);

    //插入数据
    @Insert
    long insertMsgData(SLFChatBotMsgData sLFChatBotMsgData);

    //删除所有数据

    @Query("DELETE FROM chatbotmessage")
    void deleteAll();
}
