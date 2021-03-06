package com.hgad.warehousemanager.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.constants.SPConstants;
import com.hgad.warehousemanager.util.FastBlurUtils;
import com.hgad.warehousemanager.util.SPUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2017/7/7.
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.DrawerViewHolder> {

    private static final int TYPE_DIVIDER = 0;
    private static final int TYPE_NORMAL = 1;
    private static final int TYPE_HEADER = 2;

    private Context context;
    private String todayInWeight;
    private String todayOutWeight;
    private String monthInWeight;
    private String monthOutWeight;

    public DrawerAdapter(Context context) {
        this.context = context;
    }

    public String getTodayInWeight() {
        return todayInWeight;
    }

    public void setTodayInWeight(String todayInWeight) {
        this.todayInWeight = todayInWeight;
    }

    public String getTodayOutWeight() {
        return todayOutWeight;
    }

    public void setTodayOutWeight(String todayOutWeight) {
        this.todayOutWeight = todayOutWeight;
    }

    public String getMonthInWeight() {
        return monthInWeight;
    }

    public void setMonthInWeight(String monthInWeight) {
        this.monthInWeight = monthInWeight;
    }

    public String getMonthOutWeight() {
        return monthOutWeight;
    }

    public void setMonthOutWeight(String monthOutWeight) {
        this.monthOutWeight = monthOutWeight;
    }

    private List<DrawerItem> dataList = Arrays.asList(
            new DrawerItemHeader(),
            new DrawerItemNormal(R.drawable.ic_people_black, R.string.drawer_menu_group),
            new DrawerItemNormal(R.drawable.ic_menu_camera, R.string.drawer_menu_camera),
//            new DrawerItemNormal(R.drawable.ic_menu_tools, R.string.drawer_menu_tools),
//            new DrawerItemNormal(R.drawable.ic_grade_black, R.string.drawer_menu_favorites),
            new DrawerItemDivider(),
            new DrawerItemNormal(R.drawable.ic_settings_black, R.string.drawer_menu_setting),
            new DrawerItemNormal(R.drawable.ic_cloud_black, R.string.drawer_menu_air)
    );


    @Override
    public int getItemViewType(int position) {
        DrawerItem drawerItem = dataList.get(position);
        if (drawerItem instanceof DrawerItemDivider) {
            return TYPE_DIVIDER;
        } else if (drawerItem instanceof DrawerItemNormal) {
            return TYPE_NORMAL;
        } else if (drawerItem instanceof DrawerItemHeader) {
            return TYPE_HEADER;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return (dataList == null || dataList.size() == 0) ? 0 : dataList.size();
    }

    @Override
    public DrawerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DrawerViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TYPE_DIVIDER:
                viewHolder = new DividerViewHolder(inflater.inflate(R.layout.item_drawer_divider, parent, false));
                break;
            case TYPE_HEADER:
                viewHolder = new HeaderViewHolder(inflater.inflate(R.layout.item_drawer_header, parent, false));
                break;
            case TYPE_NORMAL:
                viewHolder = new NormalViewHolder(inflater.inflate(R.layout.item_drawer_normal, parent, false));
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DrawerViewHolder holder, int position) {
        final DrawerItem item = dataList.get(position);
        if (holder instanceof NormalViewHolder) {
            NormalViewHolder normalViewHolder = (NormalViewHolder) holder;
            final DrawerItemNormal itemNormal = (DrawerItemNormal) item;
            normalViewHolder.iv.setBackgroundResource(itemNormal.iconRes);
            normalViewHolder.tv.setText(itemNormal.titleRes);
            normalViewHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.itemClick(itemNormal);
                    }
                }
            });
        } else if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (headClickListener != null) {
                        headClickListener.headClick();
                    }
                }
            });
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.touxiang);
            Bitmap roundBitmap = FastBlurUtils.toRoundBitmap(bitmap);
            headerViewHolder.imageView.setImageBitmap(roundBitmap);
            String realName = SPUtils.getString(context, SPConstants.REAL_NAME);
            headerViewHolder.tv_name.setText(realName);
            headerViewHolder.tv_in_today.setText(todayInWeight == null ? "0.00" : todayInWeight);
            headerViewHolder.tv_out_today.setText(todayOutWeight == null ? "0.00" : todayOutWeight);
            headerViewHolder.tv_in_month.setText(monthInWeight == null ? "0.00" : monthInWeight);
            headerViewHolder.tv_out_month.setText(monthOutWeight == null ? "0.00" : monthOutWeight);
        }
    }

    public OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void itemClick(DrawerItemNormal drawerItemNormal);
    }

    public OnHeadClickListener headClickListener;

    public void setHeadClickListener(OnHeadClickListener headClickListener) {
        this.headClickListener = headClickListener;
    }

    public interface OnHeadClickListener {
        void headClick();
    }


    //-------------------------item数据模型------------------------------
    // drawerlayout item统一的数据模型
    public interface DrawerItem {
    }


    //有图片和文字的item
    public class DrawerItemNormal implements DrawerItem {
        public int iconRes;
        public int titleRes;

        public DrawerItemNormal(int iconRes, int titleRes) {
            this.iconRes = iconRes;
            this.titleRes = titleRes;
        }

    }

    //分割线item
    public class DrawerItemDivider implements DrawerItem {
        public DrawerItemDivider() {
        }
    }

    //头部item
    public class DrawerItemHeader implements DrawerItem {
        public DrawerItemHeader() {
        }
    }


    //----------------------------------ViewHolder数据模型---------------------------
    //抽屉ViewHolder模型
    public class DrawerViewHolder extends RecyclerView.ViewHolder {

        public DrawerViewHolder(View itemView) {
            super(itemView);
        }
    }

    //有图标有文字ViewHolder
    public class NormalViewHolder extends DrawerViewHolder {
        public View view;
        public TextView tv;
        public ImageView iv;

        public NormalViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            tv = (TextView) itemView.findViewById(R.id.tv);
            iv = (ImageView) itemView.findViewById(R.id.iv);
        }
    }

    //分割线ViewHolder
    public class DividerViewHolder extends DrawerViewHolder {

        public DividerViewHolder(View itemView) {
            super(itemView);
        }
    }

    //头部ViewHolder
    public class HeaderViewHolder extends DrawerViewHolder {
        private View view;
        private ImageView imageView;
        private TextView tv_name;
        private TextView tv_email;
        private TextView tv_in_today;
        private TextView tv_out_today;
        private TextView tv_in_month;
        private TextView tv_out_month;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            imageView = (ImageView) itemView.findViewById(R.id.iv_user_icon);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_email = (TextView) itemView.findViewById(R.id.tv_email);
            tv_in_today = (TextView) itemView.findViewById(R.id.tv_in_today);
            tv_out_today = (TextView) itemView.findViewById(R.id.tv_out_today);
            tv_in_month = (TextView) itemView.findViewById(R.id.tv_in_month);
            tv_out_month = (TextView) itemView.findViewById(R.id.tv_out_month);
        }
    }

}