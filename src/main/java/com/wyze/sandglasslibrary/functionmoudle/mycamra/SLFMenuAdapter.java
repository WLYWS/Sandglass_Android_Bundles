package com.wyze.sandglasslibrary.functionmoudle.mycamra;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.commonui.SLFAutoLocateHorizontalView;

import java.util.List;

/**
 * 选项适配器
 *
 * @author yangjie
 * @date 2023年1月11日 18:17
 */

public class SLFMenuAdapter extends RecyclerView.Adapter<SLFMenuAdapter.ItemViewHolder>
        implements SLFAutoLocateHorizontalView.IAutoLocateHorizontalView {
    private Context context;
    private View view;
    private List<String> ages;
    private SLFAutoLocateHorizontalView recyclerView;

    public SLFMenuAdapter(Context context, List<String> ages, SLFAutoLocateHorizontalView recyclerView){
        this.context = context;
        this.ages = ages;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.slf_item_age,parent,false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.tvAge.setText(ages.get(position));
    }

    @Override
    public int getItemCount() {
        return  ages.size();
    }

    @Override
    public View getItemView() {
        return view;
    }

    @Override
    public void onViewSelected(boolean isSelected, int pos, RecyclerView.ViewHolder holder, int itemWidth) {
        if(isSelected) {
            ((ItemViewHolder) holder).tvAge.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
            ((ItemViewHolder) holder).tvAge.setTextColor(Color.WHITE);
            ((ItemViewHolder) holder).tvAge.setAlpha(1.0f);
        }else{
            ((ItemViewHolder) holder).tvAge.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
            ((ItemViewHolder) holder).tvAge.setTextColor(Color.rgb(0xfe,0xfe,0xfe));
            ((ItemViewHolder) holder).tvAge.setAlpha(0.5f);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView tvAge;
        ItemViewHolder(View itemView) {
            super(itemView);
            tvAge = itemView.findViewById(R.id.slf_tv_age);
            tvAge.setTag(this);
            tvAge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ItemViewHolder itemViewHolder = (ItemViewHolder)v.getTag();
                    int position = recyclerView.getChildAdapterPosition(itemViewHolder.itemView);
                    position--;
                    recyclerView.moveToPosition(position);
                }
            });
        }
    }
}
