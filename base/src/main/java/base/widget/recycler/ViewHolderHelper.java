package base.widget.recycler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.text.Html;
import android.util.SparseArray;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/12/22.
 */

public class ViewHolderHelper implements View.OnClickListener{
    protected final SparseArray<View> mViews;
    protected ViewOnItemChildClickListener mOnItemChildClickListener;
    protected View mConvertView;
    protected Context mContext;
    protected int mPosition;
    protected BaseRecyclerViewHolder mRecyclerViewHolder;
    /**
     * 留着以后作为扩充对象
     */
    protected Object mObj;

    public ViewHolderHelper(View convertView){
        mViews = new SparseArray<>();
        mConvertView = convertView;
        mContext = convertView.getContext();
    }

    public void setRecyclerViewHolder(BaseRecyclerViewHolder recyclerViewHolder) {
        mRecyclerViewHolder = recyclerViewHolder;
    }

    public BaseRecyclerViewHolder getRecyclerViewHolder() {
        return mRecyclerViewHolder;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public int getPosition() {
        if (mRecyclerViewHolder != null) {
            return mRecyclerViewHolder.getAdapterPosition();
        }
        return mPosition;
    }

    /**
     * 设置item子控件点击事件监听器
     * @param onItemChildClickListener
     */
    public void setOnItemChildClickListener(ViewOnItemChildClickListener onItemChildClickListener) {
        mOnItemChildClickListener = onItemChildClickListener;
    }

    /**
     * 为id为viewId的item子控件设置点击事件监听器
     * @param viewId
     */
    public void setItemChildClickListener(int viewId) {
        getView(viewId).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (mOnItemChildClickListener != null) {
            mOnItemChildClickListener.onItemChildClick(v, getPosition());
        }
    }


    /**
     * 通过控件的Id获取对应的控件，如果没有则加入mViews，则从item根控件中查找并保存到mViews中
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 获取item的根控件
     *
     * @return
     */
    public View getConvertView() {
        return mConvertView;
    }

    public void setObj(Object obj) {
        mObj = obj;
    }

    public Object getObj() {
        return mObj;
    }

    /**
     * 设置对应id的控件的文本内容
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolderHelper setText(int viewId, String text) {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    /**
     * 设置对应id的控件的文本内容
     *
     * @param viewId
     * @param stringResId 字符串资源id
     * @return
     */
    public ViewHolderHelper setText(int viewId, @StringRes int stringResId) {
        TextView view = getView(viewId);
        view.setText(stringResId);
        return this;
    }

    /**
     * 设置对应id的控件的html文本内容
     *
     * @param viewId
     * @param source html文本
     * @return
     */
    public ViewHolderHelper setHtml(int viewId, String source) {
        TextView view = getView(viewId);
        view.setText(Html.fromHtml(source));
        return this;
    }

    /**
     * 设置对应id的控件是否选中
     *
     * @param viewId
     * @param checked
     * @return
     */
    public ViewHolderHelper setChecked(int viewId, boolean checked) {
        Checkable view = getView(viewId);
        view.setChecked(checked);
        return this;
    }

    public ViewHolderHelper setTag(int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    public ViewHolderHelper setTag(int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    public ViewHolderHelper setVisibility(int viewId, int visibility) {
        View view = getView(viewId);
        view.setVisibility(visibility);
        return this;
    }

    public ViewHolderHelper setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    public ViewHolderHelper setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    /**
     * @param viewId
     * @param textColorResId 颜色资源id
     * @return
     */
    public ViewHolderHelper setTextColorRes(int viewId, @ColorRes int textColorResId) {
        TextView view = getView(viewId);
        view.setTextColor(mContext.getResources().getColor(textColorResId));
        return this;
    }

    /**
     * @param viewId
     * @param textColor 颜色值
     * @return
     */
    public ViewHolderHelper setTextColor(int viewId, int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    /**
     * @param viewId
     * @param backgroundResId 背景资源id
     * @return
     */
    public ViewHolderHelper setBackgroundRes(int viewId, int backgroundResId) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundResId);
        return this;
    }

    /**
     * @param viewId
     * @param color  颜色值
     * @return
     */
    public ViewHolderHelper setBackgroundColor(int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    /**
     * @param viewId
     * @param colorResId 颜色值资源id
     * @return
     */
    public ViewHolderHelper setBackgroundColorRes(int viewId, @ColorRes int colorResId) {
        View view = getView(viewId);
        view.setBackgroundColor(mContext.getResources().getColor(colorResId));
        return this;
    }

    /**
     * @param viewId
     * @param imageResId 图像资源id
     * @return
     */
    public ViewHolderHelper setImageResource(int viewId, int imageResId) {
        ImageView view = getView(viewId);
        view.setImageResource(imageResId);
        return this;
    }

}

