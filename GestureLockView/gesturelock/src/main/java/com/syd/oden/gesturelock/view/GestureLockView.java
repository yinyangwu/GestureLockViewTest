package com.syd.oden.gesturelock.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.view.View;

public class GestureLockView extends View {
    /**
     * GestureLockView的三种状态
     */
    enum Mode {
        STATUS_NO_FINGER, STATUS_FINGER_ON, STATUS_FINGER_UP
    }

    private Mode mCurrentStatus = Mode.STATUS_NO_FINGER;
    private int mRadius;

    private int mCenterX;
    private int mCenterY;
    private Paint mPaint;

    private int mArrowDegree = -1;
    private Path mArrowPath;

    /**
     * 四个颜色，可由用户自定义，初始化时由GestureLockViewGroup传入
     */
    private int mColorNoFinger;
    private int mColorFingerOn;
    private int mColorFingerUpCorrect;
    private int mColorFingerUpError;

    public GestureLockView(Context context) {
        super(context);
        this.mColorNoFinger = 0xFF378FC9;
        this.mColorFingerOn = 0XFFEC159F;
        this.mColorFingerUpCorrect = 0xFF91DC5A;
        this.mColorFingerUpError = 0xFFFF0000;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArrowPath = new Path();
    }

    public GestureLockView(Context context, int colorNoFinger, int colorFingerOn, int colorCorrect, int colorError) {
        super(context);
        this.mColorNoFinger = colorNoFinger;
        this.mColorFingerOn = colorFingerOn;
        this.mColorFingerUpCorrect = colorCorrect;
        this.mColorFingerUpError = colorError;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArrowPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int mWidth = MeasureSpec.getSize(widthMeasureSpec);
        int mHeight = MeasureSpec.getSize(heightMeasureSpec);

        // 取长和宽中的小值
        mWidth = mWidth < mHeight ? mWidth : mHeight;
        mRadius = mCenterX = mCenterY = mWidth / 2;

        //圆圈宽度
        int mStrokeWidth = 2;
        mRadius -= mStrokeWidth / 2;

        //箭头（小三角最长边的一半长度 = mArrowRate * mWidth / 2 ）
        float mArrowRate = 0.333f;

        // 绘制三角形，初始时是个默认箭头朝上的一个等腰三角形，用户绘制结束后，根据由两个GestureLockView决定需要旋转多少度
        float mArrowLength = mWidth / 2 * mArrowRate;
        mArrowPath.moveTo(mWidth / 2, mStrokeWidth + 2);
        mArrowPath.lineTo(mWidth / 2 - mArrowLength, mStrokeWidth + 2 + mArrowLength);
        mArrowPath.lineTo(mWidth / 2 + mArrowLength, mStrokeWidth + 2 + mArrowLength);
        mArrowPath.close();
        mArrowPath.setFillType(Path.FillType.WINDING);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //内圆的半径 = mInnerCircleRadiusRate * mRadius
        float mInnerCircleRadiusRate = 0.3F;
        switch (mCurrentStatus) {
            case STATUS_FINGER_ON:
                // 绘制外圆
                mPaint.setStyle(Style.STROKE);
                mPaint.setColor(mColorFingerOn);
                mPaint.setStrokeWidth(2);
                canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
                // 绘制内圆
                mPaint.setStyle(Style.FILL);
                canvas.drawCircle(mCenterX, mCenterY, mRadius * mInnerCircleRadiusRate, mPaint);
                break;
            case STATUS_FINGER_UP:
                // 绘制外圆
                if (GestureLockViewGroup.isCorrect)
                    mPaint.setColor(mColorFingerUpCorrect);
                else
                    mPaint.setColor(mColorFingerUpError);

                mPaint.setStyle(Style.STROKE);
                mPaint.setStrokeWidth(2);
                canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
                // 绘制内圆
                mPaint.setStyle(Style.FILL);
                canvas.drawCircle(mCenterX, mCenterY, mRadius * mInnerCircleRadiusRate, mPaint);
                drawArrow(canvas);
                break;
            case STATUS_NO_FINGER:
                // 绘制外圆
                mPaint.setStyle(Style.STROKE);
                mPaint.setColor(mColorNoFinger);
                canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
                // 绘制内圆
                mPaint.setStyle(Style.FILL);
                mPaint.setColor(mColorNoFinger);
                canvas.drawCircle(mCenterX, mCenterY, mRadius * mInnerCircleRadiusRate, mPaint);
                break;
        }
    }

    /**
     * 绘制箭头
     */
    private void drawArrow(Canvas canvas) {
        if (mArrowDegree != -1) {
            mPaint.setStyle(Style.FILL);

            canvas.save();
            canvas.rotate(mArrowDegree, mCenterX, mCenterY);
            canvas.drawPath(mArrowPath, mPaint);
            canvas.restore();
        }
    }

    /**
     * 设置当前模式并重绘界面
     */
    public void setMode(Mode mode) {
        this.mCurrentStatus = mode;
        invalidate();
    }

    public void setArrowDegree(int degree) {
        this.mArrowDegree = degree;
    }

}
