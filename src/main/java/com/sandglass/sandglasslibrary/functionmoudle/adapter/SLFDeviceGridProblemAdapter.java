package com.sandglass.sandglasslibrary.functionmoudle.adapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sandglass.sandglasslibrary.R;
import com.sandglass.sandglasslibrary.moudle.net.responsebean.SLFFirstPageFAQProblemBean;
import com.tencent.mars.xlog.Log;

import java.util.List;

/**
 * created by yangjie
 * describe:选中设备问题列表adapter
 * time: 2023/2/21
 */
public class SLFDeviceGridProblemAdapter extends RecyclerView.Adapter<SLFDeviceGridProblemAdapter.MainRecyclerViewHolder> {

    private Context mContext;
    private List<SLFFirstPageFAQProblemBean> datas;


    public SLFDeviceGridProblemAdapter (Context context, List<SLFFirstPageFAQProblemBean> list) {
        this.datas = list;
        this.mContext = context;
    }
    @Override
    public MainRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.slf_help_and_feedback_problem_item,parent,false);
        MainRecyclerViewHolder viewHolder = new MainRecyclerViewHolder(item);
        viewHolder.setIsRecyclable(false);//取消viewHolder的重用机制。没有这句话子布局subView会被重复添加。

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SLFDeviceGridProblemAdapter.MainRecyclerViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.problem_title.setText(datas.get(position).getName());
        if(datas.get(position).isExtend()){
            holder.problem_extend.setImageResource(R.drawable.slf_btn_icon_up);
        }else{
            holder.problem_extend.setImageResource(R.drawable.slf_btn_icon_down);
        }

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    protected class MainRecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView problem_title;
        private ImageView problem_extend;

        public MainRecyclerViewHolder(View itemView) {
            super(itemView);
            problem_title = itemView.findViewById(R.id.slf_problem_title);
            problem_extend = itemView.findViewById(R.id.slf_problem_isExtend);
            //item点击事件监听
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("yj","holder.click::::)))))::::");
                    int flag = 1;//用于判断当前是展开还是收缩状态
                    //获取外层linearlayout布局
                    RelativeLayout relativeLayout = view.findViewById(R.id.slf_problem_item_relate);
                    //new一个RecyclerView来当展开的子布局。
                    RecyclerView subView = new RecyclerView(view.getContext());
                    SubViewAdapter adapter = new SubViewAdapter();
                    subView.setLayoutManager(new LinearLayoutManager(view.getContext()));
                    subView.setAdapter(adapter);
                    //当flag不为空的时候,获取flag的值。
                    if (relativeLayout.getTag() != null) {
                        flag = (int) relativeLayout.getTag();
                    }
                    //当flag为1时，添加子布局。否则，移除子布局。
                    if (flag == 1) {
                        relativeLayout.addView(subView);
                        subView.setTag(101);
                        relativeLayout.setTag(2);
                        problem_extend.setImageResource(R.drawable.slf_btn_icon_up);
                    } else {
                        relativeLayout.removeView(view.findViewWithTag(101));
                        relativeLayout.setTag(1);
                        problem_extend.setImageResource(R.drawable.slf_btn_icon_down);
                    }
                }
            });
        }
    }

    //subView的adapter
    private class SubViewAdapter extends RecyclerView.Adapter{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new SubViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.slf_help_and_feedback_sub_problem_item,parent,false));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 5;
        }

        private class SubViewHolder extends RecyclerView.ViewHolder{
            private SubViewHolder(View itemView){
                super(itemView);
            }
        }
    }
}
