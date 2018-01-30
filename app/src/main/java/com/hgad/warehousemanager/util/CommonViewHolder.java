package com.hgad.warehousemanager.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * // 通用的ViewHoder，用 集合<View> 来代替多个成员变量 ，然后用同一个方法传入不同的id，来拿到需要的小控件
 */
public class CommonViewHolder {
    public final View convertView;
    private Map<Integer, View> views = new HashMap<>();

    private CommonViewHolder(View convertView) {
        this.convertView = convertView;
    }

    public static CommonViewHolder createCVH(View convertView,
                                             ViewGroup parent, int layoutRes) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent
                    .getContext());
            convertView = layoutInflater.inflate(layoutRes, parent, false);
            CommonViewHolder cvh = new CommonViewHolder(convertView);

            convertView.setTag(cvh);
        }
        // SingleNews singleNews = mNewsInListView.get(position);
        CommonViewHolder cvh = (CommonViewHolder) convertView.getTag();
        return cvh;
    }

    // public void putView(int id,View view){
    // views.put(id,view);
    // }
    public View getView(int id) {
        if (views.get(id) == null) {
            views.put(id, convertView.findViewById(id));
        }
        return views.get(id);
    }


}
