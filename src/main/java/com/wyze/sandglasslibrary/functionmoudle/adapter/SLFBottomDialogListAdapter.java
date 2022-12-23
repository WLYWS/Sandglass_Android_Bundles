package com.wyze.sandglasslibrary.functionmoudle.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.bean.SLFConstants;
import com.wyze.sandglasslibrary.functionmoudle.adapter.recycler.SLFRecyclerAdatper;
import com.wyze.sandglasslibrary.functionmoudle.adapter.recycler.SLFRecyclerHolder;
import com.wyze.sandglasslibrary.moudle.SLFProblemOverviewType;
import com.wyze.sandglasslibrary.moudle.SLFProblemType;
import com.wyze.sandglasslibrary.moudle.SLFServiceType;
import com.wyze.sandglasslibrary.utils.SLFResourceUtils;
import com.wyze.sandglasslibrary.utils.logutil.SLFLogUtil;

import java.util.List;

/**
 * Greated by yangjie
 * describe:底部弹窗的list的adapter
 * time:2022/12/15
 */
public class SLFBottomDialogListAdapter<T> extends SLFRecyclerAdatper<Object> {

    private Context mContext;
    private List<T> mDatas;
    private boolean isEdit;

    /**
     * 定义选中的下标, 默认-1
     */
    private int selPos = -1;

    public void setSelPos(int selPos) {
        this.selPos = selPos;
    }

    public int getSelPos() {
        return selPos;
    }


    private final int TYPE_ALL_ROUND = 0; // 四角都是圆角
    private final int TYPE_LEFT_TOP_ROUND = 1;//左上角圆角
    private final int TYPE_LEFT_BOTTOM_ROUND = 2;//左下角圆角
    private final int TYPE_EMPTY_ROUND = 3;//四角都是直角
    private final int TYPE_TITLE_TITLE = 4;//是年份

    private final int TYPE_CLEAN = 5;
    private final int TYPE_MOP = 6;
    private final int TYPE_HURRICANE = 7;

    public static final int TOP_TYPE = R.layout.slf_photo_selector_bottom_dialog_item_title;
    public static final int CONTAINER_TYPE = R.layout.slf_photo_selector_bottom_dialog_item;


    public SLFBottomDialogListAdapter(Context context, List<T> list) {
        super(context, (List<Object>) list);
        this.mContext = context;
        this.mDatas = list;
    }


    @Override
    public int getContentView(int i) {
        if (i == TOP_TYPE) {
            return R.layout.slf_photo_selector_bottom_dialog_item_title;
        } else {
            return R.layout.slf_photo_selector_bottom_dialog_item;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public void onInitView(SLFRecyclerHolder recyclerHolder, Object object, int position) {

        int type = getItemViewType(position);
        if (type == CONTAINER_TYPE) {

            TextView serviceTypeTitle = recyclerHolder.getView(R.id.slf_bottom_dialog_type_title);
            View  slf_item_divider = recyclerHolder.getView(R.id.slf_item_divider);
            CheckBox checkBox = recyclerHolder.getView(R.id.slf_bottom_dialog_type_check);


            switch (getType(position)) {
                case TYPE_ALL_ROUND:
                    recyclerHolder.itemView.setBackground(mContext.getResources().getDrawable(R.drawable.slf_feedback_bottom_dialog_single));
                    slf_item_divider.setVisibility(View.GONE);
                    break;
                case TYPE_LEFT_TOP_ROUND:
                    recyclerHolder.itemView.setBackground(mContext.getResources().getDrawable(R.drawable.slf_feedback_bottom_dialog_first));
                    slf_item_divider.setVisibility(View.VISIBLE);
                    break;
                case TYPE_EMPTY_ROUND:
                    recyclerHolder.itemView.setBackground(mContext.getResources().getDrawable(R.drawable.slf_feedback_bottom_dialog_middle));
                    slf_item_divider.setVisibility(View.VISIBLE);
                    break;
                case TYPE_LEFT_BOTTOM_ROUND:
                    recyclerHolder.itemView.setBackground(mContext.getResources().getDrawable(R.drawable.slf_feedback_bottom_dialog_last));
                    slf_item_divider.setVisibility(View.GONE);
                    break;
                default:
                    recyclerHolder.itemView.setBackground(mContext.getResources().getDrawable(R.drawable.slf_feedback_bottom_dialog_middle));
                    break;
            }
                if(object instanceof SLFServiceType) {
                    SLFLogUtil.d("yj","checked:::"+((SLFServiceType) object).isChecked());
                    serviceTypeTitle.setText(((SLFServiceType) object).getName());
                    checkBox.setChecked(((SLFServiceType) object).isChecked());
                }else if(object instanceof SLFProblemType) {
                    serviceTypeTitle.setText(((SLFProblemType) object).getName());
                    checkBox.setChecked(((SLFProblemType) object).isChecked());
                }else{
                    serviceTypeTitle.setText(((SLFProblemOverviewType) object).getName());
                    checkBox.setChecked(((SLFProblemOverviewType) object).isChecked());
                }


                if(checkBox.isChecked()){
                    checkBox.setBackground(SLFResourceUtils.getDrawable(R.drawable.slf_feed_back_bottom_dialog_selected));
                    serviceTypeTitle.setTextColor(SLFResourceUtils.getColor(R.color.slf_feedback_page_submit_btn_bg));
                }else{
                    checkBox.setBackground(null);
                    serviceTypeTitle.setTextColor(SLFResourceUtils.getColor(R.color.white));
                }


        } else {
            TextView serviceType_title = recyclerHolder.getView(R.id.slf_tv_content);
            serviceType_title.setText((String) object);
        }

    }

    public void setEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    @Override
    public int getItemViewType(int position) {
        if (getType(position) == TYPE_TITLE_TITLE) {
            return R.layout.slf_photo_selector_bottom_dialog_item_title;
        } else {
            return R.layout.slf_photo_selector_bottom_dialog_item;
        }
    }

    public int getType(int position) {
        int type = 0;
        Object object = mDatas.get(position);
        if (mDatas.size() > 0) {
            if (mDatas.size() == 1) {
                if (object instanceof String) {
                    type = TYPE_TITLE_TITLE;
                } else {
                    type = TYPE_ALL_ROUND;
                }
            } else {
                if (object instanceof String) {
                    type = TYPE_TITLE_TITLE;
                } else {
                    if(object instanceof SLFServiceType) {
                        if (((SLFServiceType) object).round_type.equals(SLFConstants.ALL_ROUND)) {
                            type = TYPE_ALL_ROUND;
                        } else if (((SLFServiceType) object).round_type.equals(SLFConstants.ROUND_FIRST)) {
                            type = TYPE_LEFT_TOP_ROUND;
                        } else if (((SLFServiceType) object).round_type.equals(SLFConstants.ROUND_END)) {
                            type = TYPE_LEFT_BOTTOM_ROUND;
                        } else {
                            type = TYPE_EMPTY_ROUND;
                        }
                    }else if(object instanceof SLFProblemType){
                        if (((SLFProblemType) object).round_type.equals(SLFConstants.ALL_ROUND)) {
                            type = TYPE_ALL_ROUND;
                        } else if (((SLFProblemType) object).round_type.equals(SLFConstants.ROUND_FIRST)) {
                            type = TYPE_LEFT_TOP_ROUND;
                        } else if (((SLFProblemType) object).round_type.equals(SLFConstants.ROUND_END)) {
                            type = TYPE_LEFT_BOTTOM_ROUND;
                        } else {
                            type = TYPE_EMPTY_ROUND;
                        }
                    }else if(object instanceof SLFProblemOverviewType){
                        if (((SLFProblemOverviewType) object).round_type.equals(SLFConstants.ALL_ROUND)) {
                            type = TYPE_ALL_ROUND;
                        } else if (((SLFProblemOverviewType) object).round_type.equals(SLFConstants.ROUND_FIRST)) {
                            type = TYPE_LEFT_TOP_ROUND;
                        } else if (((SLFProblemOverviewType) object).round_type.equals(SLFConstants.ROUND_END)) {
                            type = TYPE_LEFT_BOTTOM_ROUND;
                        } else {
                            type = TYPE_EMPTY_ROUND;
                        }
                    }
                }
            }
        }else{
           type = TYPE_ALL_ROUND;
        }
        return type;
    }

}