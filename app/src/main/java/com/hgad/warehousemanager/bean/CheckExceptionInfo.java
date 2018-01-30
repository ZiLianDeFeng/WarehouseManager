package com.hgad.warehousemanager.bean;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Administrator on 2017/11/15.
 */
public class CheckExceptionInfo {

    public static final int ITEM = 1;
    public static final int SECTION = 2;

    public int type;
    public CheckFloorInfo checkFloorInfo;

    public CheckExceptionInfo(int type, CheckFloorInfo checkFloorInfo) {
        this.type = type;
        this.checkFloorInfo = checkFloorInfo;
    }

    public int getType() {
        return type;
    }

    public CheckFloorInfo getCheckFloorInfo() {
        return checkFloorInfo;
    }

    public static List<CheckExceptionInfo> getData(List<CheckFloorInfo> floorInfos) {
        ArrayList<CheckExceptionInfo> list = new ArrayList<>();
        Map<CheckFloorInfo, List<CheckFloorInfo>> map = new TreeMap<>(new Comparator<CheckFloorInfo>() {
            public int compare(CheckFloorInfo key1, CheckFloorInfo key2) {
                return key1.getState().compareTo(key2.getState());
            }
        });
        CheckFloorInfo checkFloorInfo = new CheckFloorInfo();
        List<CheckFloorInfo> dates = new ArrayList<>();
        dates.add(checkFloorInfo);
        for (int i = 0; i < floorInfos.size(); i++) {
            if (!TextUtils.isEmpty(checkFloorInfo.getState())) {
                String key = floorInfos.get(i).getState();
                boolean isHave = false;
                for (CheckFloorInfo date :
                        dates) {
                    if (date.getState().equals(key)) {
                        checkFloorInfo = date;
                        isHave = true;
                    }
                }
                if (!isHave) {
                    checkFloorInfo = new CheckFloorInfo();
                    dates.add(checkFloorInfo);
                }
            }
            checkFloorInfo.setState(floorInfos.get(i).getState());
            List<CheckFloorInfo> curList = map.get(checkFloorInfo);
            if (curList == null) {
                curList = new ArrayList<CheckFloorInfo>();
            }
            curList.add(floorInfos.get(i));
            map.put(checkFloorInfo, curList);
        }
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            CheckFloorInfo key = (CheckFloorInfo) entry.getKey();
            List<CheckFloorInfo> li = (List<CheckFloorInfo>) entry.getValue();
            list.add(new CheckExceptionInfo(SECTION, key));
            for (CheckFloorInfo warnDetail : li) {
                list.add(new CheckExceptionInfo(ITEM, warnDetail));
            }
        }
        return list;
    }
}
