package ece.course.pedometer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceView;

import static android.support.v7.appcompat.R.color.background_material_light;

/**
 * Created by mingzheliu on 2017/9/23.
 */

public class DisplayView extends SurfaceView {

    private float mCenterX = 0.0f;
    private float mCenterY = 0.0f;
    private float mRadius = 0.0f;

    private float mPtrCenterX = 0.0f;
    private float mPtrCenterY = 0.0f;
    private float mPtrRadius = 0.0f;

    public DisplayView(Context context, AttributeSet attrs)
    {
        super(context,attrs);

        setWillNotDraw(false);
    }

    public void setPtr(float posX, float posY)
    {
        mPtrCenterX = posX * mRadius * 0.9f + mCenterX;
        mPtrCenterY = posY * mRadius * 0.9f + mCenterY;
        invalidate();
    }

    public void onDraw(Canvas canvas) {
        if (canvas == null) return;

        canvas.drawColor(getResources().getColor(background_material_light));

        Paint paint = new Paint();

        paint.setColor(Color.RED);
        canvas.drawRect(mCenterX - mRadius/6,mCenterY - mRadius,mCenterX + mRadius/6,mCenterY + mRadius,paint);

        paint.setColor(Color.YELLOW);
        canvas.drawRect(mCenterX - mRadius/6,mCenterY - mRadius/3*2,mCenterX + mRadius/6,mCenterY + mRadius/3*2,paint);

        paint.setColor(Color.GREEN);
        canvas.drawRect(mCenterX - mRadius/6,mCenterY - mRadius/4,mCenterX + mRadius/6,mCenterY + mRadius/4,paint);

        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(5.0f);
        canvas.drawLine(mCenterX - mRadius/3,mCenterY,mCenterX + mRadius/3,mCenterY,paint);

        paint.setColor(Color.LTGRAY);
        canvas.drawCircle(mPtrCenterX, mPtrCenterY, mPtrRadius, paint);
    }
    public void onSizeChanged(int width, int height, int oldWidth, int oldHeight)
    {
        mCenterX = width / 2;
        mCenterY = height / 2;
        mRadius = ((width < height)? width:height) * 3.0f / 8.0f;

        mPtrCenterX = mCenterX;
        mPtrCenterY = mCenterY;
        mPtrRadius = mRadius / 10.0f;

        invalidate();
    }
}
