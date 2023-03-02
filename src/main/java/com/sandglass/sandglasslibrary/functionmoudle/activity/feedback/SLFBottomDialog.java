package com.sandglass.sandglasslibrary.functionmoudle.activity.feedback;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sandglass.sandglasslibrary.R;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFCategoryBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFCategoryCommonBean;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFCategoryDetailBean;
import com.sandglass.sandglasslibrary.functionmoudle.adapter.SLFBottomDialogListAdapter;
import com.sandglass.sandglasslibrary.base.SLFBaseBottomDialog;
import com.sandglass.sandglasslibrary.theme.SLFFontSet;
import com.sandglass.sandglasslibrary.utils.SLFCommonUtils;
import com.sandglass.sandglasslibrary.utils.SLFResourceUtils;

import java.util.List;

/**
 *
 **created by yangjie
 **describe:选择问题类型的底部弹窗
 **time:2022/12/15
 *
 */
public class SLFBottomDialog extends SLFBaseBottomDialog {
    /**dialog title*/
    private TextView slf_tvTitle;
    /**取消按钮*/
    private ImageView slf_cancel_img;
    /**列表*/
    private RecyclerView slf_recycler;
    /**列表adapter*/
    private SLFBottomDialogListAdapter mAdapter;
    private String selectedStr;
    private String title;
    private List<Object> slf_typeList;
    public <T extends Object> SLFBottomDialog(Context context,List<T> list) {
        super(context);
        View view= LayoutInflater.from(context).inflate(R.layout.slf_feed_back_bottom_dialog,null);
        setContentView(view);
        this.slf_typeList = (List<Object>) list;
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());

        slf_tvTitle = findViewById(R.id.slf_bottom_dialog_title);
        slf_cancel_img = findViewById(R.id.slf_bottom_dialog_title_img);
        slf_recycler = findViewById(R.id.slf_feed_back_bottom_dialog_list);
        SLFFontSet.setSLF_RegularFont(getContext(),slf_tvTitle);
        mAdapter = new SLFBottomDialogListAdapter(context, list);
        slf_cancel_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        slf_recycler.setNestedScrollingEnabled(true);
        slf_recycler.setLayoutManager(mLinearLayoutManager);
        slf_recycler.setAdapter(mAdapter);
        //getAllList();
        mAdapter.setOnItemClickListener((holder, position) -> {
            if(list.get(holder.getAdapterPosition()) instanceof SLFCategoryBean) {
                    ((SLFCategoryBean) list.get(holder.getAdapterPosition())).setChecked(true);
                    changedChecked(list,holder.getAdapterPosition());
                    selectedStr = ((SLFCategoryBean) list.get(holder.getAdapterPosition())).name;
                    if(onSeletedTypeListener!=null){
                        onSeletedTypeListener.getSeletedType(selectedStr,holder.getLayoutPosition(),getTtileText());
                    }
                    mAdapter.notifyDataSetChanged();
                    dialogDismiss();

            }else if(list.get(holder.getAdapterPosition()) instanceof SLFCategoryDetailBean){
                    ((SLFCategoryDetailBean) list.get(holder.getAdapterPosition())).setChecked(true);
                    changedChecked(list,holder.getAdapterPosition());
                    selectedStr = ((SLFCategoryDetailBean) list.get(holder.getAdapterPosition())).name;
                    if(onSeletedTypeListener!=null){
                        onSeletedTypeListener.getSeletedType(selectedStr,holder.getLayoutPosition(),getTtileText());
                    }
                    mAdapter.notifyDataSetChanged();
                    dialogDismiss();
            }else if(list.get(holder.getAdapterPosition()) instanceof SLFCategoryCommonBean){
                    ((SLFCategoryCommonBean) list.get(holder.getAdapterPosition())).setChecked(true);
                    changedChecked(list,holder.getAdapterPosition());
                    selectedStr = ((SLFCategoryCommonBean) list.get(holder.getAdapterPosition())).name;
                    if(onSeletedTypeListener!=null){
                        onSeletedTypeListener.getSeletedType(selectedStr,holder.getLayoutPosition(),getTtileText());
                    }
                    mAdapter.notifyDataSetChanged();
                    dialogDismiss();
            }

        });
        setCanceledOnTouchOutside(true);
    }

    private <T extends Object> void changedChecked(List<T> list,int position){
        for(int i=0;i<list.size();i++){
            if(list.get(i) instanceof SLFCategoryBean){
                if(i != position){
                    ((SLFCategoryBean) list.get(i)).setChecked(false);
                }
            }else if(list.get(i) instanceof SLFCategoryDetailBean){
                if(i != position){
                    ((SLFCategoryDetailBean) list.get(i)).setChecked(false);
                }
            }else if(list.get(i) instanceof SLFCategoryCommonBean){
                if(i != position){
                    ((SLFCategoryCommonBean) list.get(i)).setChecked(false);
                }
            }
        }
    }


//    private void getAllList(){
//        for(int i = 0;i<15;i++){
//
//            if(i==0||i==3||i==5||i==7||i==11){
//                String title = "caiyicai";
//                slf_typeList.add(title);
//            }else{
//                SLFServiceType serviceType = new SLFServiceType();
//                serviceType.setName("wode");
//                serviceType.setChecked(false);
//                if(i==1){
//                    serviceType.setRound_type("first");
//                }
//                if(i==2){
//                    serviceType.setRound_type("end");
//                }
//                if(i==4)
//                    serviceType.setRound_type("all_round");
//                if(i==6) serviceType.setRound_type("all_round");
//                if(i==8) serviceType.setRound_type("first");
//                if(i==9) {
//                    serviceType.setRound_type("");
//                }
//                if(i==10) serviceType.setRound_type("end");
//                if(i==12) serviceType.setRound_type("first");
//                if(i==14) serviceType.setRound_type("end");
//                slf_typeList.add(serviceType);
//            }
//        }
//    }

    public void setTitleText(String titleText) {
        slf_tvTitle.setText(titleText);
        slf_tvTitle.setVisibility(View.VISIBLE);
        title = titleText;
    }

    public void setPositionChecked(int position){
        if(position!=-1&&slf_typeList!=null&&slf_typeList.size()>0){
            if(slf_typeList.get(position) instanceof SLFCategoryBean){
                ((SLFCategoryBean) slf_typeList.get(position)).setChecked(true);
            }else if(slf_typeList.get(position) instanceof SLFCategoryDetailBean){
                ((SLFCategoryDetailBean) slf_typeList.get(position)).setChecked(true);
            }else if(slf_typeList.get(position) instanceof SLFCategoryCommonBean){
                ((SLFCategoryCommonBean) slf_typeList.get(position)).setChecked(true);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    private String getTtileText(){
        return title;
    }

    private void dialogDismiss(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               dismiss();
            }
        },200);  //延迟200ms// 秒执行
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        Window window = this.getWindow();

        window.setGravity(Gravity.BOTTOM);

        WindowManager.LayoutParams params = window.getAttributes();

        params.width = WindowManager.LayoutParams.MATCH_PARENT;

        // 设置宽高
        if(window.getDecorView().getHeight() >= (SLFCommonUtils.getScreenHeight() - SLFCommonUtils.getStatusBarHeights(context) - SLFResourceUtils.dp2px(context,SLFResourceUtils.getDimension(R.dimen.std_titlebar_height)))){
            params.height = (int)(SLFCommonUtils.getScreenHeight() - SLFCommonUtils.getStatusBarHeights(context)-SLFResourceUtils.dp2px(context,20));
        }else{
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        }
        window.setWindowAnimations(R.style.slf_dialogBottomAnimation); // 添加动画
        window.setAttributes(params);

    }

    private OnSeletedTypeListener onSeletedTypeListener;

    public void setonSeletedTypeListener(OnSeletedTypeListener onSeletedTypeListener){

        this.onSeletedTypeListener = onSeletedTypeListener;
    }

    public  interface OnSeletedTypeListener{

        void getSeletedType(String type,int position,String title);

    }
}
