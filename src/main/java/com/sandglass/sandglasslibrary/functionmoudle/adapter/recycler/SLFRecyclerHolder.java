package com.sandglass.sandglasslibrary.functionmoudle.adapter.recycler;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.sandglass.sandglasslibrary.utils.SLFImageShapes;
import com.sandglass.sandglasslibrary.utils.SLFImageUtil;
import com.sandglass.sandglasslibrary.utils.SLFViewUtil;

/**
 * created by yangjie
 * describe:RecyclerView holder
 * time: 2022/12/15
 */
public class SLFRecyclerHolder extends RecyclerView.ViewHolder implements SLFExpandableViewHoldersUtil.Expandable {

    private final View convertView;
    private View expandView;
    private View iconArrow;
    private int angle;

    public SLFRecyclerHolder(View itemView) {
        super(itemView);
        convertView = itemView;
    }

    public SLFRecyclerHolder(View itemView, View itemExpandView, View itemIconArrow, int rotationAngle) {
        super(itemView);
        convertView = itemView;
        expandView = itemExpandView;
        iconArrow = itemIconArrow;
        angle = rotationAngle;
        if(expandView!=null){
            expandView.setVisibility(View.GONE);
            expandView.setAlpha(0);
        }
    }

    public <T extends View> T getView(int viewId) {
        return retrieveView(viewId);
    }

    protected <T extends View> T retrieveView(int viewId) {
        return SLFViewUtil.get(convertView, viewId);
    }

    public SLFRecyclerHolder setText(int viewId, CharSequence value) {
        TextView view = retrieveView(viewId);
        if (TextUtils.isEmpty(value)) {
            view.setText("");
        } else {
            view.setText(value);
        }
        return this;
    }

    public SLFRecyclerHolder setImageResource(int viewId, int imageResId) {
        ImageView view = retrieveView(viewId);
        view.setImageResource(imageResId);
        return this;
    }

    public SLFRecyclerHolder setBackgroundColor(int viewId, int color) {
        View view = retrieveView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public SLFRecyclerHolder setBackgroundRes(int viewId, int backgroundRes) {
        View view = retrieveView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    public SLFRecyclerHolder setTextColor(int viewId, int textColor) {
        TextView view = retrieveView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    public SLFRecyclerHolder setTextColorRes(int viewId, int textColorRes) {
        TextView view = retrieveView(viewId);
        view.setTextColor(ContextCompat.getColor(view.getContext(), textColorRes));
        return this;
    }

    public SLFRecyclerHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = retrieveView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    /**
     * 通过ImageLoader设置图片
     *
     * @param viewId viewId
     * @param imageResId 图片资源id
     * @param shape ImageShapes
     * @return SLFRecyclerHolder
     */
    public SLFRecyclerHolder setImageResource(int viewId, int imageResId, SLFImageShapes shape) {
        ImageView view = retrieveView(viewId);
        SLFImageUtil.loadImage(view.getContext(), imageResId, view,shape);
        return this;
    }
    public SLFRecyclerHolder setImageUrl(int viewId, String imageUrl) {
        ImageView view = retrieveView(viewId);
        SLFImageUtil.loadImage(getView().getContext(),imageUrl, view);
        return this;
    }
    public SLFRecyclerHolder setImageUrl(int viewId, String imageUrl, SLFImageShapes shape) {
        ImageView view = retrieveView(viewId);
        SLFImageUtil.loadImage(getView().getContext(),imageUrl, view,shape);
        return this;
    }
    public SLFRecyclerHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = retrieveView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    @SuppressLint({"NewApi", "ObsoleteSdkInt"})
    public SLFRecyclerHolder setAlpha(int viewId, float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            retrieveView(viewId).setAlpha(value);
        } else {
            // Pre-honeycomb hack to set Alpha value
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            retrieveView(viewId).startAnimation(alpha);
        }
        return this;
    }

    public SLFRecyclerHolder setVisible(int viewId, boolean visible) {
        View view = retrieveView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public SLFRecyclerHolder setVisible(int viewId, int visibility) {
        View view = retrieveView(viewId);
        view.setVisibility(visibility);
        return this;
    }

    /**
     * Add links into a TextView.
     *
     * @param viewId The id of the TextView to linkify.
     * @return The BaseAdapterHelper for chaining.
     */
    public SLFRecyclerHolder linkify(int viewId) {
        TextView view = retrieveView(viewId);
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    /**
     * Apply the typeface to the given viewId, and enable subpixel rendering.
     */
    public SLFRecyclerHolder setTypeface(int viewId, Typeface typeface) {
        TextView view = retrieveView(viewId);
        view.setTypeface(typeface);
        view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        return this;
    }

    /**
     * Apply the typeface to all the given viewIds, and enable subpixel
     * rendering.
     */
    public SLFRecyclerHolder setTypeface(Typeface typeface, int... viewIds) {
        for (int viewId : viewIds) {
            TextView view = retrieveView(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }

    /**
     * Sets the progress of a ProgressBar.
     *
     * @param viewId   The view id.
     * @param progress The progress.
     * @return The BaseAdapterHelper for chaining.
     */
    public SLFRecyclerHolder setProgress(int viewId, int progress) {
        ProgressBar view = retrieveView(viewId);
        view.setProgress(progress);
        return this;
    }

    /**
     * Sets the progress and max of a ProgressBar.
     *
     * @param viewId   The view id.
     * @param progress The progress.
     * @param max      The max value of a ProgressBar.
     * @return The BaseAdapterHelper for chaining.
     */
    public SLFRecyclerHolder setProgress(int viewId, int progress, int max) {
        ProgressBar view = retrieveView(viewId);
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    /**
     * Sets the range of a ProgressBar to 0...max.
     *
     * @param viewId The view id.
     * @param max    The max value of a ProgressBar.
     * @return The BaseAdapterHelper for chaining.
     */
    public SLFRecyclerHolder setMax(int viewId, int max) {
        ProgressBar view = retrieveView(viewId);
        view.setMax(max);
        return this;
    }

    /**
     * Sets the rating (the number of stars filled) of a RatingBar.
     *
     * @param viewId The view id.
     * @param rating The rating.
     * @return The BaseAdapterHelper for chaining.
     */
    public SLFRecyclerHolder setRating(int viewId, float rating) {
        RatingBar view = retrieveView(viewId);
        view.setRating(rating);
        return this;
    }

    /**
     * Sets the rating (the number of stars filled) and max of a RatingBar.
     *
     * @param viewId The view id.
     * @param rating The rating.
     * @param max    The range of the RatingBar to 0...max.
     * @return The BaseAdapterHelper for chaining.
     */
    public SLFRecyclerHolder setRating(int viewId, float rating, int max) {
        RatingBar view = retrieveView(viewId);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    /**
     * Sets the tag of the view.
     *
     * @param viewId The view id.
     * @param tag    The tag;
     * @return The BaseAdapterHelper for chaining.
     */
    public SLFRecyclerHolder setTag(int viewId, Object tag) {
        View view = retrieveView(viewId);
        view.setTag(tag);
        return this;
    }

    /**
     * Sets the tag of the view.
     *
     * @param viewId The view id.
     * @param key    The key of tag;
     * @param tag    The tag;
     * @return The BaseAdapterHelper for chaining.
     */
    public SLFRecyclerHolder setTag(int viewId, int key, Object tag) {
        View view = retrieveView(viewId);
        view.setTag(key, tag);
        return this;
    }

    /**
     * Sets the checked status of a checkable.
     *
     * @param viewId  The view id.
     * @param checked The checked status;
     * @return The BaseAdapterHelper for chaining.
     */
    public SLFRecyclerHolder setChecked(int viewId, boolean checked) {
        Checkable view = retrieveView(viewId);
        view.setChecked(checked);
        return this;
    }

    /**
     * Sets the checked status of a checkable.
     *
     * @param viewId The view id.
     * @return The BaseAdapterHelper for chaining.
     */
    public SLFRecyclerHolder setChecked(int viewId) {
        Checkable view = retrieveView(viewId);
        view.setChecked(!view.isChecked());
        return this;
    }

    /**
     * Sets the adapter of a adapter view.
     *
     * @param viewId  The view id.
     * @param adapter The adapter;
     * @return The BaseAdapterHelper for chaining.
     */
    @SuppressWarnings({"rawtypes","unchecked"})
    public SLFRecyclerHolder setAdapter(int viewId, Adapter adapter) {
        AdapterView view = retrieveView(viewId);
        view.setAdapter(adapter);
        return this;
    }

    /**
     * Sets the on click listener of the view.
     *
     * @param viewId   The view id.
     * @param listener The on click listener;
     * @return The BaseAdapterHelper for chaining.
     */
    public SLFRecyclerHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = retrieveView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    public SLFRecyclerHolder setOnCheckedChangeListener(int viewId, CompoundButton.OnCheckedChangeListener listener) {
        CompoundButton view = retrieveView(viewId);
        view.setOnCheckedChangeListener(listener);
        return this;

    }

    /**
     * Sets the on touch listener of the view.
     *
     * @param viewId   The view id.
     * @param listener The on touch listener;
     * @return The BaseAdapterHelper for chaining.
     */
    public SLFRecyclerHolder setOnTouchListener(int viewId, View.OnTouchListener listener) {
        View view = retrieveView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    /**
     * Sets the on long click listener of the view.
     *
     * @param viewId   The view id.
     * @param listener The on long click listener;
     * @return The BaseAdapterHelper for chaining.
     */
    public SLFRecyclerHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        View view = retrieveView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }

    /**
     * Retrieve the convertView
     */
    public View getView() {
        return convertView;
    }

    @Override
    public View getExpandView() {
        return expandView;
    }

    @Override
    public void doCustomAnim(boolean isOpen) {
        if(iconArrow == null){
            return;
        }
        if (isOpen) {
            SLFExpandableViewHoldersUtil.getInstance().rotateExpandIcon(iconArrow, angle, 0);
        } else {
            SLFExpandableViewHoldersUtil.getInstance().rotateExpandIcon(iconArrow, 0, angle);
        }
    }
}

