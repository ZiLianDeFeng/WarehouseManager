package com.hgad.warehousemanager.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/28.
 */
public class ProSectionBean {
    public static final int ITEM = 0;
    public static final int SECTION = 1;

    public final int type;
    public final WareInfo WareInfo;
    public final int dataCount;

    public ProSectionBean(int type, WareInfo WareInfo, int dataCount) {
        this.type = type;
        this.WareInfo = WareInfo;
        this.dataCount = dataCount;
    }

    public int getType() {
        return type;
    }

    public com.hgad.warehousemanager.bean.WareInfo getWareInfo() {
        return WareInfo;
    }

    public int getDataCount() {
        return dataCount;
    }

    public static ArrayList<ProSectionBean> getData(
            List<WareInfo> details) {
        ArrayList<ProSectionBean> list = new ArrayList<>();
        Map<WareInfo, List<WareInfo>> map = new HashMap<>();
        WareInfo wareInfo = new WareInfo();
        List<WareInfo> dates = new ArrayList<>();
        dates.add(wareInfo);
        for (int i = 0; i < details.size(); i++) {
            String key = details.get(i).getOutPlateNumber();
            if (wareInfo.getCurOutNunber() != null && !"".equals(wareInfo.getCurOutNunber())) {
                boolean b = !key.equals(wareInfo.getCurOutNunber());
                if (b) {
                    boolean isHave = false;
                    for (WareInfo date :
                            dates) {
                        if (date.getCurOutNunber().equals(key)) {
                            wareInfo = date;
                            isHave = true;
                        }
                    }
                    if (!isHave) {
                        wareInfo = new WareInfo();
                        dates.add(wareInfo);
                    }
                }
            }
            wareInfo.setCurOutNunber(key);
            List<WareInfo> curList = map.get(wareInfo);
            if (curList == null) {
                curList = new ArrayList<WareInfo>();
            }
            String outPlateNumber = details.get(i).getOutPlateNumber();
            details.get(i).setCurOutNunber(outPlateNumber);
            curList.add(details.get(i));
            map.put(wareInfo, curList);
        }
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            WareInfo key = (WareInfo) entry.getKey();
            List<WareInfo> li = (List<WareInfo>) entry.getValue();
            int size = li.size();
            list.add(new ProSectionBean(SECTION, key, size));
            for (WareInfo warnDetail : li) {
                list.add(new ProSectionBean(ITEM, warnDetail, size));
            }
        }
        return list;
    }
}
