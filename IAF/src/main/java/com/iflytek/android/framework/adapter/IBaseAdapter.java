package com.iflytek.android.framework.adapter;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 
 * @author wzgao
 * 
 * @param <T>
 */
public class IBaseAdapter<T> extends BaseAdapter {

    private Context mContext;
    /**
     * item layout
     */
    private int resource;
    /**
	 * 
	 */
    private List<T> content;
    private LayoutInflater inflater;
    /**
	 * 
	 */
    private Class<T> clss;

    public IBaseAdapter(Context mContext, int resource, List<T> content,
            Class<T> clss) {
        super();
        this.mContext = mContext;
        this.resource = resource;
        this.content = content;
        inflater = LayoutInflater.from(mContext);
        this.clss = clss;
    }

    /**
	 * 
	 */
    public int getCount() {
        return content.size();
    }

    /**
	 * 
	 */
    public Object getItem(int location) {
        return content.get(location);
    }

    /**
	 * 
	 */
    public long getItemId(int location) {
        return location;
    }

    /**
	 * 
	 */
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(resource, null);
            findItemView(convertView, holder);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        bindValue(holder, content.get(position));
        bindListener(position, convertView, parent, content);
        return convertView;
    }

    /**
     * 
     * 描述：TODO
     * 
     * @param itemView
     * @param holder
     * @throws
     */
    protected void findItemView(View itemView, ViewHolder holder) {

        Field[] fields = clss.getDeclaredFields();
        try {
            for (Field field : fields) {

                Field temp = Class.forName(mContext.getPackageName() + ".R$id")
                        .getField(field.getName());
                holder.root.put(field.getName(),
                        itemView.findViewById(temp.getInt(temp)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void bindListener(int position, View convertView, ViewGroup parent,
            List<T> content) {

    }

    /**
     * 
     * 描述：TODO
     * 
     * @param holder
     * @param obj
     * @throws
     */
    public void bindValue(ViewHolder holder, T obj) {
        Set<String> keys = holder.root.keySet();
        try {
            for (String key : keys) {
                View item = holder.root.get(key);
//                Field temp = obj.getClass().getField(key);
                Field field = obj.getClass().getDeclaredField(key);
                field.setAccessible(true);
                Object o = field.get(obj);
                if (item instanceof ImageView) {
                    ImageView imagev = (ImageView) item;
                    if (o instanceof Drawable) {
                        imagev.setImageDrawable((Drawable) o);
                    } else if (o instanceof Bitmap) {
                        imagev.setImageBitmap((Bitmap) o);
                    } else if (o instanceof Integer) {
                        imagev.setImageResource((Integer) o);
                    } else if (o instanceof String) {
                        ImageLoader.getInstance().displayImage((String) o,
                                (ImageView) imagev);
                    }
                } else if (item instanceof TextView) {
                    if (o instanceof CharSequence) {
                        ((TextView) item).setText((CharSequence) o);
                    } else {
                        ((TextView) item).setText(o.toString());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static class ViewHolder {
        Map<String, View> root;

        public ViewHolder() {
            root = new HashMap<String, View>();
        }
    }
}
