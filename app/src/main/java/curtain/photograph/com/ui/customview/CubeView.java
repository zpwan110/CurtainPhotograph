package curtain.photograph.com.ui.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import curtain.photograph.com.R;

/**
 * Created by zhangpeng on 2018/1/29.
 */

public class CubeView extends View {
    //摄像机
    private Camera mCamera;

    //翻转用的图片
    private Bitmap face;
    private Matrix mMatrix = new Matrix();
    private Paint mPaint = new Paint();

    private int mLastMotionX, mLastMotionY;

    //图片的中心点坐标
    private int centerX, centerY;
    //转动的总距离，跟度数比例1:1
    private int deltaX, deltaY;
    //图片宽度高度
    private int bWidth, bHeight;

    public CubeView(Context context) {
        super(context);
        initCube(context);
    }

    public CubeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initCube(context);
    }

    public CubeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCube(context);
    }
   void initCube(Context context){
        setWillNotDraw(false);
        mCamera = new Camera();
        mPaint.setAntiAlias(true);
        face = BitmapFactory.decodeResource(context.getResources(), R.drawable.curtain);
        bWidth = face.getWidth();
        bHeight = face.getHeight();
        centerX = bWidth>>1;
        centerY = bHeight>>1;
    }

    /**
     * 转动
     * @param degreeX
     * @param degreeY
     */
    void rotate(int degreeX, int degreeY) {
        deltaX += degreeX;
        deltaY += degreeY;

        mCamera.save();
        mCamera.rotateY(deltaX);
        mCamera.rotateX(-deltaY);
        mCamera.translate(0, 0, -centerX);
        mCamera.getMatrix(mMatrix);
        mCamera.restore();
        //以图片的中心点为旋转中心,如果不加这两句，就是以（0,0）点为旋转中心
        mMatrix.preTranslate(-centerX, -centerY);
        mMatrix.postTranslate(centerX, centerY);
        mCamera.save();

        postInvalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionX = x;
                mLastMotionY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = x - mLastMotionX;
                int dy = y - mLastMotionY;
                rotate(dx, dy);
                mLastMotionX = x;
                mLastMotionY = y;
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    @Override
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvas.drawBitmap(face, mMatrix, mPaint);
    }
}
