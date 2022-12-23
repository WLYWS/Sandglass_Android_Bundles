package com.wyze.sandglasslibrary.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.view.LayoutInflater;
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

import com.wyze.sandglasslibrary.R;
import com.wyze.sandglasslibrary.base.SLFBaseApplication;

/**
 * created by yangjie
 * describe:Adapter utils
 * time: 2022/12/5
 */
public class SLFAdapterUtils {

    private int position;

    private final View convertView;

    /**
     * Package private field to retain the associated user object and detect a
     * change
     */

    public SLFAdapterUtils(View parent) {
        convertView = parent;
        convertView.setTag(R.id.slf_adatper_utils, this);
    }

    /**
     * This method is package private and should only be used by QuickAdapter.
     */
    public static SLFAdapterUtils get(View convertView, int position) {

        SLFAdapterUtils existingHelper = (SLFAdapterUtils) convertView.getTag(R.id.slf_adatper_utils);
        if (null == existingHelper) {
            existingHelper = new SLFAdapterUtils(convertView);
        }
        existingHelper.position = position;

        return existingHelper;
    }

    /**
     * This method allows you to retrieve a view and perform custom operations
     * on it, not covered by the BaseAdapterHelper.<br/>
     * If you think it's a common use case, please consider creating a new issue
     * at https://github.com/JoanZapata/base-adapter-helper/issues.
     *
     * @param viewId The id of the view you want to retrieve.
     */
    public <T extends View> T getView(int viewId) {
        return retrieveView(viewId);
    }

    protected <T extends View> T retrieveView(int viewId) {
        return SLFViewUtil.get(convertView, viewId);
    }

    /**
     * Will set the text of a TextView.
     *
     * @param viewId The view id.
     * @param value  The text to put in the text view.
     * @return The BaseAdapterHelper for chaining.
     */
    public SLFAdapterUtils setText(int viewId, CharSequence value) {
        TextView view = retrieveView(viewId);
        if (TextUtils.isEmpty(value)) {
            view.setText("");
        } else {
            view.setText(value);
        }
        return this;
    }

    public SLFAdapterUtils setContentDescriptionTv(int viewId, CharSequence value) {
        TextView textView = retrieveView(viewId);
        if (TextUtils.isEmpty(value)) {
            textView.setContentDescription("");
        } else {
            textView.setContentDescription(value);
        }
        return this;
    }

    public SLFAdapterUtils setContentDescriptionIv(int viewId, CharSequence value) {
        ImageView imageView = retrieveView(viewId);
        if (TextUtils.isEmpty(value)) {
            imageView.setContentDescription("");
        } else {
            imageView.setContentDescription(value);
        }
        return this;
    }

    /**
     * Will set the image of an ImageView from a resource id.
     *
     * @param viewId     The view id.
     * @param imageResId The image resource id.
     * @return The BaseAdapterHelper for chaining.
     */
    public SLFAdapterUtils setImageResource(int viewId, int imageResId) {
        ImageView view = retrieveView(viewId);
        view.setImageResource(imageResId);
        return this;
    }

    /**
     * 通过ImageLoader设置图片
     *
     * @param viewId     view的id
     * @param imageResId 图片资源id
     * @param shape      ImageShapes
     * @return AdapterHelper
     */
    public SLFAdapterUtils setImageResource(int viewId, int imageResId, SLFImageShapes shape) {
        ImageView view = retrieveView(viewId);
        SLFImageUtil.loadImage(view.getContext(), imageResId, view, shape);
        return this;
    }

    /**
     * Will set background color of a view.
     *
     * @param viewId The view id.
     * @param color  A color, not a resource id.
     * @return The BaseAdapterHelper for chaining.
     */
    public SLFAdapterUtils setBackgroundColor(int viewId, int color) {
        View view = retrieveView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    /**
     * Will set background of a view.
     *
     * @param viewId        The view id.
     * @param backgroundRes A resource to use as a background.
     * @return The BaseAdapterHelper for chaining.
     */
    public SLFAdapterUtils setBackgroundRes(int viewId, int backgroundRes) {
        View view = retrieveView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    /**
     * Will set text color of a TextView.
     *
     * @param viewId    The view id.
     * @param textColor The text color (not a resource id).
     * @return The BaseAdapterHelper for chaining.
     */
    public SLFAdapterUtils setTextColor(int viewId, int textColor) {
        TextView view = retrieveView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    /**
     * Will set text color of a TextView.
     *
     * @param viewId       The view id.
     * @param textColorRes The text color resource id.
     * @return The BaseAdapterHelper for chaining.
     */
    @SuppressWarnings("java:S1874")
    public SLFAdapterUtils setTextColorRes(int viewId, int textColorRes) {
        TextView view = retrieveView(viewId);
        view.setTextColor(view.getContext().getResources().getColor(textColorRes));
        return this;
    }

    /**
     * Will set the image of an ImageView from a drawable.
     *
     * @param viewId   The view id.
     * @param drawable The image drawable.
     * @return The BaseAdapterHelper for chaining.
     */
    public SLFAdapterUtils setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = retrieveView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    /**
     * Will download an image from a URL and put it in an ImageView.<br/>
     * It uses Square's Picasso library to download the image asynchronously and
     * put the result into the ImageView.<br/>
     * Picasso manages recycling of views in a ListView.<br/>
     * If you need more control over the Picasso settings, use
     * {BaseAdapterHelper#setImageBuilder}.
     *
     * @param viewId   The view id.
     * @param imageUrl The image URL.
     * @return The BaseAdapterHelper for chaining.
     */
    public SLFAdapterUtils setImageUrl(int viewId, String imageUrl) {
        ImageView view = retrieveView(viewId);
        SLFImageUtil.loadImage(view.getContext(), imageUrl, view);

        return this;
    }

    /**
     * 通过ImageLoader设置图片
     *
     * @param viewId   The view id.
     * @param imageUrl 图片的url
     * @param shape    ImageShapes
     * @return AdapterHelper
     */
    public SLFAdapterUtils setImageUrl(int viewId, String imageUrl, SLFImageShapes shape) {
        ImageView view = retrieveView(viewId);
        SLFImageUtil.loadImage(view.getContext(), imageUrl, view, shape);
        return this;
    }

    /**
     * Add an action to set the image of an image view. Can be called multiple
     * times.
     */
    public SLFAdapterUtils setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = retrieveView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    /**
     * Add an action to set the alpha of a view. Can be called multiple times.
     * Alpha between 0-1.
     */
    @SuppressLint({"NewApi", "ObsoleteSdkInt"})
    public SLFAdapterUtils setAlpha(int viewId, float value) {
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

    /**
     * Set a view visibility to VISIBLE (true) or GONE (false).
     *
     * @param viewId  The view id.
     * @param visible True for VISIBLE, false for GONE.
     * @return The BaseAdapterHelper for chaining.
     */
    public SLFAdapterUtils setVisible(int viewId, boolean visible) {
        View view = retrieveView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public SLFAdapterUtils setVisible(int viewId, int visibility) {
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
    public SLFAdapterUtils linkify(int viewId) {
        TextView view = retrieveView(viewId);
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    /**
     * Apply the typeface to the given viewId, and enable subpixel rendering.
     */
    public SLFAdapterUtils setTypeface(int viewId, Typeface typeface) {
        TextView view = retrieveView(viewId);
        view.setTypeface(typeface);
        view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        return this;
    }

    /**
     * Apply the typeface to all the given viewIds, and enable subpixel
     * rendering.
     */
    public SLFAdapterUtils setTypeface(Typeface typeface, int... viewIds) {
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
    public SLFAdapterUtils setProgress(int viewId, int progress) {
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
    public SLFAdapterUtils setProgress(int viewId, int progress, int max) {
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
    public SLFAdapterUtils setMax(int viewId, int max) {
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
    public SLFAdapterUtils setRating(int viewId, float rating) {
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
    public SLFAdapterUtils setRating(int viewId, float rating, int max) {
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
    public SLFAdapterUtils setTag(int viewId, Object tag) {
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
    public SLFAdapterUtils setTag(int viewId, int key, Object tag) {
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
    public SLFAdapterUtils setChecked(int viewId, boolean checked) {
        Checkable view = retrieveView(viewId);
        view.setChecked(checked);
        return this;
    }

    /**
     * Sets the adapter of a adapter view.
     *
     * @param viewId  The view id.
     * @param adapter The adapter;
     * @return The BaseAdapterHelper for chaining.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public SLFAdapterUtils setAdapter(int viewId, Adapter adapter) {
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
    public SLFAdapterUtils setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = retrieveView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    public SLFAdapterUtils setOnCheckedChangeListener(int viewId, CompoundButton.OnCheckedChangeListener listener) {
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
    public SLFAdapterUtils setOnTouchListener(int viewId, View.OnTouchListener listener) {
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
    public SLFAdapterUtils setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
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

    /**
     * Retrieve the overall position of the data in the list.
     *
     * @throws IllegalArgumentException If the position hasn't been set at the construction of the
     *                                  this helper.
     */
    public int getPosition() {
        if (position == -1) {
            throw new IllegalStateException(
                    "Use BaseAdapterHelper constructor " + "with position if you need to retrieve the position.");
        }
        return position;
    }
}
