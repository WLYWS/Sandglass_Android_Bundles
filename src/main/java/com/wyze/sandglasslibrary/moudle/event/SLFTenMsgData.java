package com.wyze.sandglasslibrary.moudle.event;

import com.wyze.sandglasslibrary.moudle.SLFChatBotMsgData;

import java.util.List;

/**
 * Created by wangjian on 2023/1/12
 */
public class SLFTenMsgData {
    public List <SLFChatBotMsgData> slfChatBotMsgData;

    public SLFTenMsgData (List <SLFChatBotMsgData> slfChatBotMsgData) {
        this.slfChatBotMsgData = slfChatBotMsgData;
    }
}
