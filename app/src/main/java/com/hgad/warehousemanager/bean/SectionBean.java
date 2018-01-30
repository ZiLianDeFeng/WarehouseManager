package com.hgad.warehousemanager.bean;

import com.max.pinnedsectionrefreshlistviewdemo.TimeManagement;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Administrator on 2017/4/27.
 */
public class SectionBean {
    public static final int ITEM = 0;
    public static final int SECTION = 1;

    public int type;
    public CheckTaskInfo CheckTaskInfo;
    public int dataCount;

    public SectionBean(int type, CheckTaskInfo CheckTaskInfo, int dataCount) {
        this.type = type;
        this.CheckTaskInfo = CheckTaskInfo;
        this.dataCount = dataCount;
    }

    public int getType() {
        return type;
    }

    public CheckTaskInfo getCheckTaskInfo() {
        return CheckTaskInfo;
    }

    public int getDataCount() {
        return dataCount;
    }

    public static ArrayList<SectionBean> getData(
            List<CheckTaskInfo> details) {
//        String currentTime = CommonUtils.getCurrentTime();
//        final String currentDate = currentTime.substring(0, 10);
        ArrayList<SectionBean> list = new ArrayList<>();
        Map<CheckTaskInfo, List<CheckTaskInfo>> map = new TreeMap<>(new Comparator<CheckTaskInfo>() {
            public int compare(CheckTaskInfo key1, CheckTaskInfo key2) {
                return key2.getStartDate().compareTo(key1.getStartDate());
            }
        });
        CheckTaskInfo checkTaskInfo = new CheckTaskInfo();
        List<CheckTaskInfo> dates = new ArrayList<>();
        dates.add(checkTaskInfo);
        for (int i = 0; i < details.size(); i++) {
            try {
                String key = TimeManagement.exchangeStringDate(details.get(i).getPlanTime());
                if (checkTaskInfo.getStartDate() != null && !"".equals(checkTaskInfo.getStartDate())) {
                    boolean b = !key.equals(checkTaskInfo.getStartDate());
                    if (b) {
                        boolean isHave = false;
                        for (CheckTaskInfo date :
                                dates) {
                            if (date.getStartDate().equals(key)) {
                                checkTaskInfo = date;
                                isHave = true;
                            }
                        }
                        if (!isHave) {
                            checkTaskInfo = new CheckTaskInfo();
                            dates.add(checkTaskInfo);
                        }
                    }
                }
                checkTaskInfo.setStartDate(key);
                List<CheckTaskInfo> curList = map.get(checkTaskInfo);
                if (curList == null) {
                    curList = new ArrayList<CheckTaskInfo>();
                }
                String time = details.get(i).getPlanTime();
                time = TimeManagement.exchangeStringTime(time);
                details.get(i).setStartDate(time);
                curList.add(details.get(i));
                map.put(checkTaskInfo, curList);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            CheckTaskInfo key = (CheckTaskInfo) entry.getKey();
            List<CheckTaskInfo> li = (List<CheckTaskInfo>) entry.getValue();
            int size = li.size();
            list.add(new SectionBean(SECTION, key, size));
            for (CheckTaskInfo warnDetail : li) {
                list.add(new SectionBean(ITEM, warnDetail, size));
            }
        }
        return list;
    }
}
