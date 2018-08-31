package com.example.ecare_client.mainpageview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class SexangleViewGroup extends ViewGroup {
    private static final int SPACE = 15;// view与view之间的间隔

    public SexangleViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int lenght = (int) (getWidth() / 2.5) - SPACE;// 每个子VIEW的长度
        double radian30 = 30 * Math.PI / 180;
        float h = (float) (lenght * Math.cos(radian30));
        int bottomSpace = (int) ((lenght - 2 * h) / 2);// 六边形离底部的间隔
        int offsetX = lenght * 3 / 4 + SPACE;// X轴每次偏移的长度
        int offsetY = lenght / 2;// Y轴每次偏移的长度


        for (int i = 0; i < getChildCount(); i++) {

            if (i == 1) {
                View child1 = getChildAt(1);
                child1.layout(offsetX, offsetY, offsetX + lenght, offsetY + lenght - bottomSpace);
            }
            if(i==2) {
                View child2 = getChildAt(2);
                child2.layout(0, 0, lenght, lenght - bottomSpace);
            }
            if(i==3) {
                View child3 = getChildAt(3);
                child3.layout(2*offsetX, 0, 2*offsetX + lenght, lenght - bottomSpace);
            }
            if(i==4) {
                View child4 = getChildAt(4);
                child4.layout(0, 2*offsetY , lenght, 2*offsetY + lenght - bottomSpace);
            }
            if (i==5) {
                View child5 = getChildAt(5);
                child5.layout(2*offsetX, 2*offsetY , 2*offsetX+lenght, 2*offsetY + lenght - bottomSpace);
            }

        }

    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure((int) (getWidth() / 2.5), (int) (getWidth() / 2.5));
            child.setTag(i);
        }
    }
}