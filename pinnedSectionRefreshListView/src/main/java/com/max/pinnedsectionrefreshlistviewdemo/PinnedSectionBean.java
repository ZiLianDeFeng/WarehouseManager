package com.max.pinnedsectionrefreshlistviewdemo;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class PinnedSectionBean {
	public static final int ITEM = 0;
	public static final int SECTION = 1;

	public final int type;
	public final Messages messages;

	public int sectionPosition;
	public int listPosition;

	public int getSectionPosition() {
		return sectionPosition;
	}

	public void setSectionPosition(int sectionPosition) {
		this.sectionPosition = sectionPosition;
	}

	public Messages getMessages() {
		return messages;
	}

	public int getListPosition() {
		return listPosition;
	}

	public void setListPosition(int listPosition) {
		this.listPosition = listPosition;
	}

	public int getType() {
		return type;
	}

	public PinnedSectionBean(int type, Messages messages) {
		super();
		this.type = type;
		this.messages = messages;
	}

	public PinnedSectionBean(int type, Messages messages,
			int sectionPosition, int listPosition) {
		super();
		this.type = type;
		this.messages = messages;
		this.sectionPosition = sectionPosition;
		this.listPosition = listPosition;
	}

	@Override
	public String toString() {
		return messages.getTime();
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ArrayList<PinnedSectionBean> getData(
			List<Messages> details) {
		ArrayList<PinnedSectionBean> list = new ArrayList<PinnedSectionBean>();
		TimeManagement management = new TimeManagement();
		Map<Messages, List<Messages>> map = new HashMap<Messages, List<Messages>>();
		Messages detail = new Messages();
		for (int i = 0; i < details.size(); i++) {
			try {
				String key = management.exchangeStringDate(details.get(i)
						.getTime());
				if (detail.getTime() != null && !"".equals(detail.getTime())) {
					boolean b = !key.equals(detail.getTime().toString());
					if (b) {
						detail = new Messages();
					}
				}
				detail.setTime(key);
				List<Messages> warnDetails = map.get(detail);
				if (warnDetails == null) {
					warnDetails = new ArrayList<Messages>();
				}
				String time = details.get(i).getTime();
				time = management.exchangeStringTime(time);
				details.get(i).setTime(time);
				warnDetails.add(details.get(i));
				map.put(detail, warnDetails);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		Iterator iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry entry = (Entry) iterator.next();
			Messages key = (Messages) entry.getKey();
			list.add(new PinnedSectionBean(SECTION, key));
			List<Messages> li = (List<Messages>) entry.getValue();
			for (Messages warnDetail : li) {
				list.add(new PinnedSectionBean(ITEM, warnDetail));
			}
		}
		return list;
	}

}
