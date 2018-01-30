package com.hgad.warehousemanager.view;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.hgad.warehousemanager.R;

/**
 * Created by Administrator on 2017/10/27.
 */
public class NewMarkerView extends MarkerView {

    private TextView tvContent;
    private float screenWidth;
    private final RelativeLayout rl_bg;

    public NewMarkerView(Context context, int layoutResource, float screenWidth) {
        super(context, layoutResource);
        this.screenWidth = screenWidth;
        rl_bg = (RelativeLayout) findViewById(R.id.rl_bg);
        tvContent = (TextView) findViewById(R.id.tvContent);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        super.refreshContent(e, highlight);
    }

    @Override
    public void draw(Canvas canvas, float posX, float posY) {
//        super.draw(canvas, posX, posY);

        MPPointF offset = this.getOffsetForDrawingAtPoint(posX, posY);
        int saveId = canvas.save();
        if (posX < screenWidth / 2) {
            rl_bg.setBackgroundResource(R.drawable.bubble_left);
            offset = new MPPointF(0, -getHeight());
        } else {
            rl_bg.setBackgroundResource(R.drawable.bubble_right);
            offset = new MPPointF(-getWidth(), -getHeight());
        }
        canvas.translate(posX + offset.x, posY + offset.y);
        this.draw(canvas);
        canvas.restoreToCount(saveId);
    }

}