package com.hgad.warehousemanager.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hgad.warehousemanager.R;
import com.hgad.warehousemanager.bean.UserInfo;
import com.hgad.warehousemanager.constants.Constants;
import com.hgad.warehousemanager.util.CommonUtils;
import com.hgad.warehousemanager.util.CommonViewHolder;
import com.hgad.warehousemanager.view.CommonDialog;

import java.util.List;

/**
 * Created by Administrator on 2017/12/20.
 */
public class MemberAdapter extends BaseAdapter {
    private Context context;
    private List<UserInfo> list;
    private OnDeleteMemberListener onDeleteMemberListener;
    private String post;

    public void setOnDeleteMemberListener(OnDeleteMemberListener onDeleteMemberListener) {
        this.onDeleteMemberListener = onDeleteMemberListener;
    }

    public MemberAdapter(Context context, List<UserInfo> list) {
        this.context = context;
        this.list = list;
    }

    public void setPost(String post) {
        this.post = post;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final UserInfo userInfo = list.get(position);
        CommonViewHolder viewHolder = CommonViewHolder.createCVH(convertView, parent, R.layout.item_member);
        TextView tv_name = (TextView) viewHolder.getView(R.id.tv_name);
        tv_name.setText(userInfo.getRealName());
        TextView tv_delete = (TextView) viewHolder.getView(R.id.tv_delete);
        TextView tv_create_man = (TextView) viewHolder.getView(R.id.tv_create_man);
        if (position == 0) {
            tv_delete.setVisibility(View.GONE);
            tv_create_man.setVisibility(View.VISIBLE);
        } else {
            tv_delete.setVisibility(View.VISIBLE);
            tv_create_man.setVisibility(View.GONE);
        }

        tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!post.contains("组长")) {
                    CommonUtils.showToast(context, "人员只能由组长移除");
                } else {
                    CommonDialog commonDialog = new CommonDialog(context, "提示", "确认移除" + userInfo.getRealName() + "?", "确认", "取消");
                    commonDialog.setCanceledOnTouchOutside(false);
                    commonDialog.setCancelable(true);
                    commonDialog.setClicklistener(new CommonDialog.ClickListenerInterface() {
                        @Override
                        public void doConfirm() {
                            if (Constants.DEBUG) {
                                list.remove(userInfo);
                                notifyDataSetChanged();
                            } else {
                                if (onDeleteMemberListener != null) {
                                    onDeleteMemberListener.onDeletMember(userInfo);
                                }
                            }
                        }

                        @Override
                        public void doCancel() {

                        }
                    });
                    commonDialog.show();
                }
            }
        });
        return viewHolder.convertView;
    }

    public interface OnDeleteMemberListener {
        void onDeletMember(UserInfo userInfo);
    }
}
