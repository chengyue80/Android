package com.yue.demo.other.appInfo;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yue.demo.R;

public class MyListAdapter extends BaseAdapter {
    private Context mContext = null;
    private List<ApplicationInfo> mData = null;

    public MyListAdapter(Context pContext, List<ApplicationInfo> pData) {
        mContext = pContext;
        mData = pData;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LinearLayout.inflate(mContext, R.layout.appinfo_item,
                null);
        ImageView imageView = (ImageView) convertView
                .findViewById(R.id.iv_appinfo_item);
        TextView textView = (TextView) convertView
                .findViewById(R.id.tv_appinfo_item);
        LinearLayout ll = (LinearLayout) convertView
                .findViewById(R.id.ll_appinfo_item);

        // ll.setFocusable(true);

        imageView.setBackground(mContext.getPackageManager()
                .getApplicationIcon(mData.get(position)));
        textView.setText(mContext.getPackageManager().getApplicationLabel(
                mData.get(position)));

        // int num = (Integer) convertView.getTag();

        // Log.d("TAG", position + " : " + ll.isFocused()
        // + "===convertView.isSelected() :" +
        // convertView.isSelected()+"---- num = ");

        // if (getItemViewType(position) == 1) {
        // convertView.findViewById(R.id.iv_appinfo_item_right).setVisibility(
        // View.VISIBLE);
        // } else
        // convertView.findViewById(R.id.iv_appinfo_item_right).setVisibility(
        // View.GONE);

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mData.get(position);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        if (position == AppInfo.select_item)
            return 1;
        else

            return 0;
    }
}
