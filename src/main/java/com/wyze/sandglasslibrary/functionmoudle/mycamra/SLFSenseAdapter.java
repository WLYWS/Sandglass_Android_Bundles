package com.wyze.sandglasslibrary.functionmoudle.mycamra;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wyze.sandglasslibrary.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * effect 适配器
 *
 * @fileName: SLFEffectAdapter
 * @date: 2023/1/11
 * @author: yangjie
 */

public class SLFSenseAdapter extends RecyclerView.Adapter<SLFSenseAdapter.EffectViewHolder> {
    private LayoutInflater mLayoutInflater;
    private String[] senseArr;

    public SLFSenseAdapter(Context mContext, String[] arr) {
        this.senseArr = arr;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public EffectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EffectViewHolder(mLayoutInflater.inflate(R.layout.slf_item_rv_text, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull EffectViewHolder holder, int position) {
        holder.mTextView.setText(senseArr[position]);
    }

    @Override
    public int getItemCount() {
        return senseArr.length;
    }

    public class EffectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTextView;
        public View mView;

        public EffectViewHolder(View view){
            super(view);
            this.mView = view;
            this.mTextView = view.findViewById(R.id.slf_text_view);
        }



        @Override
        public void onClick(View v) {
            if (senseOnItemClickListener != null) {
                senseOnItemClickListener.itemOnClick(getPosition());
            }
        }
    }

    private SenseOnItemClickListener senseOnItemClickListener;

    public interface SenseOnItemClickListener {

        void itemOnClick(int position);

    }

    public void setSenseOnItemClickListener(SenseOnItemClickListener senseOnItemClickListener) {
        this.senseOnItemClickListener = senseOnItemClickListener;
    }
}
