package com.wyze.sandglasslibrary.dao;

import android.content.Context;
import android.os.AsyncTask;

import com.wyze.sandglasslibrary.moudle.SLFChatBotMsgData;
import com.wyze.sandglasslibrary.moudle.event.SLFTenMsgData;
import com.wyze.sandglasslibrary.utils.logutil.SLFLogUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by wangjian on 2023/1/6
 */
public class SLFDBEngine {

    private static final String TAG = "SLFDBEngine";

    private SLFMsgDao slfMsgDao;

    public SLFDBEngine (Context context) {
        SLFChatBotDatabase slfChatBotDatabase = SLFChatBotDatabase.getInstance(context);
        slfMsgDao = slfChatBotDatabase.msgDao();
    }

    //插入
    public void insert_msg(SLFChatBotMsgData slfChatBotMsgData) {
        new InsertAsynTask(slfMsgDao).execute(slfChatBotMsgData);
    }
    //更新
    public void update_msg(SLFChatBotMsgData  slfChatBotMsgData) {
        new UpdateAsynTask(slfMsgDao).execute(slfChatBotMsgData);
    }
    //全部删除
    public void delete_all_msg() {
        new DeleteAllAsynTask(slfMsgDao).execute();
    }
    //全部查询
    public void quary_all_msg() {
        new QuaryAllAsynTask(slfMsgDao).execute();
    }

    //查询10条数据
    public void quary_ten_msg(int id) {
        new QuaryTenAsynTask(slfMsgDao).execute(id);
    }

    static class QuaryTenAsynTask extends AsyncTask <Integer,Void,List<SLFChatBotMsgData>> {

        private SLFMsgDao slfMsgDao;
        public QuaryTenAsynTask(SLFMsgDao slfMsgDao) {
            this.slfMsgDao = slfMsgDao;
        }

        @Override
        protected List <SLFChatBotMsgData> doInBackground(Integer... integers) {
            List <SLFChatBotMsgData> all_msg = this.slfMsgDao.selectLimitTen(integers[0]);
            //遍历全部查询的结果
            for (SLFChatBotMsgData SLFChatBotMsgData:all_msg)
            {
                SLFLogUtil.i(TAG, "doInBackground: "+SLFChatBotMsgData.toString());
            }
            return all_msg;
        }

        @Override
        protected void onPostExecute (List <SLFChatBotMsgData> slfChatBotMsgData) {
            super.onPostExecute(slfChatBotMsgData);
            EventBus.getDefault().post(new SLFTenMsgData(slfChatBotMsgData));
        }
    }

    //开启插入异步操作
    static class InsertAsynTask extends AsyncTask<SLFChatBotMsgData,Void,Void> {
        private SLFMsgDao slfMsgDao;
        public InsertAsynTask(SLFMsgDao slfMsgDao) {
            this.slfMsgDao = slfMsgDao;
        }

        @Override
        protected Void doInBackground (SLFChatBotMsgData... slfChatBotMsgData) {
            long id = slfMsgDao.insertMsgData(slfChatBotMsgData[0]);
            SLFChatBotMsgData data = slfChatBotMsgData[0];
            data.setId((int) id);
            return null;
        }
    }

    static class UpdateAsynTask extends AsyncTask<SLFChatBotMsgData,Void,Void> {
        private SLFMsgDao slfMsgDao;
        public UpdateAsynTask(SLFMsgDao slfMsgDao) {
            this.slfMsgDao = slfMsgDao;
        }


        @Override
        protected Void doInBackground (SLFChatBotMsgData... slfChatBotMsgData) {
            SLFChatBotMsgData msgData = this.slfMsgDao.selectByTime(slfChatBotMsgData[0].getMsgTime());
            SLFChatBotMsgData data = slfChatBotMsgData[0];
            data.setId(msgData.getId());
            this.slfMsgDao.updateMsgData(data);
            return null;
        }

    }

    //全部删除
    static class DeleteAllAsynTask extends AsyncTask<Void,Void,Void> {
        private SLFMsgDao slfMsgDao;
        public DeleteAllAsynTask(SLFMsgDao slfMsgDao) {
            this.slfMsgDao = slfMsgDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            this.slfMsgDao.deleteAll();
            return null;
        }
    }
    static class QuaryAllAsynTask extends AsyncTask <Void,Void,List<SLFChatBotMsgData>> {

        private SLFMsgDao slfMsgDao;
        public QuaryAllAsynTask(SLFMsgDao slfMsgDao) {
            this.slfMsgDao = slfMsgDao;
        }

        @Override
        protected List <SLFChatBotMsgData> doInBackground(Void... voids) {
            List <SLFChatBotMsgData> all_msg = this.slfMsgDao.selectAll();
            //遍历全部查询的结果
            for (SLFChatBotMsgData SLFChatBotMsgData:all_msg)
            {
                SLFLogUtil.i(TAG, "doInBackground: "+SLFChatBotMsgData.toString());
            }
            return all_msg;
        }

        @Override
        protected void onPostExecute (List <SLFChatBotMsgData> slfChatBotMsgData) {
            super.onPostExecute(slfChatBotMsgData);
            EventBus.getDefault().post(slfChatBotMsgData);
        }
    }
}
