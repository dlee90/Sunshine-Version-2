package com.example.android.sunshine.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;

public class Compass extends View {

    private double direction = Double.NaN;

    public Compass(Context context) {
        super(context);
    }
    public Compass(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Compass(Context context, AttributeSet attrs,
                   int defaultStyle) {
        super(context, attrs, defaultStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(
                MeasureSpec.getSize(widthMeasureSpec),
                MeasureSpec.getSize(heightMeasureSpec)
        );
    }

    @Override
    protected void onDraw(Canvas canvas) {

        // don't draw an image if no direction has been provided
        if (direction == Double.NaN) {
            return;
        }
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();
        int r;
        if(w > h){
            r = h/2;
        }else{
            r = w/2;
        }

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setColor(Color.GRAY);

        canvas.drawCircle(w / 2, h / 2, r, paint);

        paint.setColor(getResources().getColor(R.color.sunshine_dark_blue));
        double dirRad = Utility.convertDegreesToRadians(direction);
        canvas.drawLine(
                w / 2,
                h / 2,
                (float) (w / 2 + r * Math.sin(-dirRad)),
                (float) (h / 2 - r * Math.cos(-dirRad)),
                paint);

    }

    public void update(double dir){

        AccessibilityManager accessibilityManager =
                (AccessibilityManager) getContext().getSystemService(
                        Context.ACCESSIBILITY_SERVICE);
        if (accessibilityManager.isEnabled()) {
            sendAccessibilityEvent(
                    AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED);
        }

        direction = dir;

        // Call invalidate to force drawing on page.

        invalidate();
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        event.getText().add(String.valueOf(direction));
        return true;
    }
}
