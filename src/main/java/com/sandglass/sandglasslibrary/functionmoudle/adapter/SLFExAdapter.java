package com.sandglass.sandglasslibrary.functionmoudle.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.putrack.putrack.commonapi.PUTClickAgent;
import com.sandglass.sandglasslibrary.R;
import com.sandglass.sandglasslibrary.bean.SLFAgentEvent;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFFirstPageFAQProblemBean;
import com.sandglass.sandglasslibrary.theme.SLFFontSet;

import java.util.List;

public class SLFExAdapter extends BaseExpandableListAdapter
{
    Context context;
    int selectParentItem = -1;
    int selectChildItem = -1;
    private List<SLFFirstPageFAQProblemBean> arrayList_memberData;

    public SLFExAdapter(Context context, List<SLFFirstPageFAQProblemBean> list) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.arrayList_memberData = list;
    }

    public void setChildSelection(int groupPosition, int childPosition)
    {
        selectParentItem = groupPosition;
        selectChildItem = childPosition;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return arrayList_memberData.get(groupPosition).getFaqList().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                             ViewGroup parent) {
        // TODO Auto-generated method stub
        View view = convertView;
        if (null == view)
        {
            //获取LayoutInflater
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //获取对应的布局
            view = layoutInflater.inflate(R.layout.slf_help_and_feedback_sub_problem_item, null);
        }
        TextView textView = (TextView)view.findViewById(R.id.slf_problem_sub_title);
        ImageView imgView = (ImageView) view.findViewById(R.id.slf_problem_sub);
        View devider =view.findViewById(R.id.slf_child_devider);
        textView.setText(arrayList_memberData.get(groupPosition).getFaqList().get(childPosition).getTitle());
        SLFFontSet.setSLF_RegularFont(context, textView);
        imgView.setImageResource(R.drawable.slf_btn_icon_right);
        if(isLastChild){
            devider.setVisibility(View.GONE);
        }else{
            devider.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        return arrayList_memberData.get(groupPosition).getFaqList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return arrayList_memberData.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return arrayList_memberData.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                             ViewGroup parent) {
        // TODO Auto-generated method stub
        View view = convertView;
        Log.i("++++++++++", "groupPosition="+groupPosition);
        if (null == view)
        {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.slf_help_and_feedback_problem_item, null);
        }
        TextView textView = (TextView)view.findViewById(R.id.slf_problem_title);
        textView.setText(arrayList_memberData.get(groupPosition).getName());
        SLFFontSet.setSLF_MediumFontt(context, textView);
        ImageView image=(ImageView) view.findViewById(R.id.slf_problem_isExtend);
        if(isExpanded)
        {
            image.setBackgroundResource(R.drawable.slf_btn_icon_up);
            //打点展开列表
            PUTClickAgent.clickTypeAgent(SLFAgentEvent.SLF_FAQ_ExpandSecondaryClassification,null);
        }
        else
        {
            image.setBackgroundResource(R.drawable.slf_btn_icon_down);
            //打点收起列表
            PUTClickAgent.clickTypeAgent(SLFAgentEvent.SLF_FAQ_CollapseSecondaryClassification,null);
        }
        return view;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }

}
