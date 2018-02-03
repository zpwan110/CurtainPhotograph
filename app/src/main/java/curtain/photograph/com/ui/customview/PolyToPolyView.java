package curtain.photograph.com.ui.customview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import curtain.photograph.com.R;


/**
 * <请描述这个类是干什么的>
 *
 * @author guobin
 * @data: 2016/9/20 09:30
 * @Email: 702250823@qq.com
 * @version: V1.0
 */
public class PolyToPolyView extends View {
    private static final String TAG = "SetPolyToPoly";

    private int testPoint = 0;
    private int framPadding = 35;    // 边框内距离180px

    private Bitmap mBitmap;             // 要绘制的图片
    private Matrix mPolyMatrix;         // 测试setPolyToPoly用的Matrix
    //图片的中心点坐标
    private int centerX, centerY,canvasX,canvasY;

    private float[] src = new float[8];
    private float[] dst = new float[8];
    private float[] center = new float[2];
    private float[] delete = new float[2];
    private float[] translate = new float[2];
    private float[] temp ;
    float scale = 2;

    private Paint pointPaint,pointLine;
    float[] pointXY = new float[8];
    private float triggerRadius = 280;
    private boolean isMove =false;
    private boolean isTranslate =false;
    private boolean isShow =true;
    private boolean isDelete = false;
    private CanvasWrapper mCanvasWrapper;

    public PolyToPolyView(Context context) {
        this(context, null);
    }

    public PolyToPolyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PolyToPolyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initBitmapAndMatrix();
    }

    private void initBitmapAndMatrix() {
        mBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.small_curtain);
        temp = new float[]{0-framPadding, 0-framPadding,                                    // 左上
                mBitmap.getWidth()+framPadding, 0-framPadding,                     // 右上
                mBitmap.getWidth()+framPadding, mBitmap.getHeight()+framPadding,  // 右下
                0-framPadding, mBitmap.getHeight()+framPadding    //左下
        };
        src = temp.clone();
        dst = temp.clone();
        pointPaint = new Paint();
        pointLine = new Paint();
        pointPaint.setAntiAlias(true);
        pointLine.setAntiAlias(true);
        pointPaint.setStrokeWidth(50);
        pointLine.setStrokeWidth(8);
        pointPaint.setColor(0xffd19165);
        pointLine.setColor(0xffd19165);
        pointPaint.setStrokeCap(Paint.Cap.ROUND);
        mPolyMatrix = new Matrix();
        mPolyMatrix.setPolyToPoly(src, 0, dst, 0, 4);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mCanvasWrapper = new CanvasWrapper();
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.centerX = w/2;
        this.centerY =h/2;
    }
    int currentPoint=-1 ;

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        canvasX = Math.abs(centerX-mBitmap.getWidth()/2);
        canvasY = Math.abs(centerY-mBitmap.getHeight()/2);
        src = temp.clone();
        dst = temp.clone();
        translate[0] = canvasX;
        translate[1] = canvasY;
        delete[0] = centerX;
        delete[1] = canvasY-framPadding*2;
        center[0] = centerX;
        center[1] = centerY;
        setTestPoint(4);

}
    public void resetPolyMatrix(int pointCount,float moveX,float moveY) {
        mPolyMatrix.reset();
        mPolyMatrix.setPolyToPoly(src, 0, dst, 0, pointCount);
        mPolyMatrix.postTranslate(translate[0],translate[1]);
        mPolyMatrix.mapPoints(pointXY, src);
        switch (currentPoint){
            case -1:
                break;
            default:

                break;
        }
        if(isTranslate){
            center[0] += moveX;
            center[1] += moveY;
            delete[0] += moveX;
            delete[1] += moveY;
        }else if(currentPoint!=-1){
            switch (currentPoint){
                case 0:
                    center[0] += moveX/2;
                    center[1] += moveY/2;
                    delete[0] += moveX/2;
                    delete[1] += moveY/2;
                    dst[currentPoint] +=moveX;
                    dst[currentPoint + 1] +=moveY;
                    break;
                case 2:
                    center[0] += moveX/2;
                    delete[0] += moveX/2;
                    delete[1] += moveY/2;
                    dst[currentPoint] +=moveX;
                    dst[currentPoint + 1] +=moveY;
                    break;
                 case 4:
                    dst[currentPoint] +=moveX;
                    dst[currentPoint + 1] +=moveY;
                    break;
                case 6:
                    center[1] += moveY/2;
                    dst[currentPoint] +=moveX;
                    dst[currentPoint + 1] +=moveY;
                    break;
            }

        }
        invalidate();
    }
    float locationX ,locationY ;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                locationX = event.getX();
                locationY = event.getY();
                currentPoint =-1;
                isMove = false;
                isTranslate =false;
                if(Math.abs(locationX - center[0]) <= 100 && Math.abs(locationY - center[1]) <= 100){
                    isTranslate =true;
                    break;
                }
                // 根据触控位置改变dst
                for (int i = 0; i < testPoint * 2; i += 2) {
                    float shortX = locationX - pointXY[i];
                    float shortY = locationY - pointXY[i+1];
                    if (Math.abs(shortX) <= triggerRadius && Math.abs(shortY) <= triggerRadius&&!isMove) {
                        currentPoint = i;
                        isMove =true;
                        isShow = true;
                        break;
                    }
                    /*switch (i){
                        case 0:
                            if (shortX >= 0 && shortX<=triggerRadius&&shortY>=0&&shortY<=triggerRadius&&!isMove) {
                                currentPoint = 0;
                                isMove =true;
                                isShow = true;
                                break;
                            }
                        case 2:
                            if (shortX <= 0 && Math.abs(shortX)<=triggerRadius&&shortY>=0&&shortY<=triggerRadius&&!isMove) {
                                currentPoint = 2;
                                isMove =true;
                                isShow = true;
                                break;
                            }
                        case 4:
                            if (shortX <= 0 && Math.abs(shortX)<=triggerRadius&&shortY<=0&&Math.abs(shortY)<=triggerRadius&&!isMove) {
                                currentPoint = 4;
                                isMove =true;
                                isShow = true;
                                break;
                            }
                            break;
                        case 6:
                            if (shortX >= 0 && shortX<=triggerRadius&&shortY<=0&&Math.abs(shortY)<=triggerRadius&&!isMove) {
                                currentPoint = 6;
                                isMove =true;
                                isShow = true;
                                break;
                            }
                            break;
                    }*/
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float tempX = event.getX();
                float tempY = event.getY();
                if(isTranslate){
                    translate[0] +=(tempX-locationX);
                    translate[1] +=(tempY-locationY);
                }
                resetPolyMatrix(4,(tempX-locationX),(tempY-locationY));
                locationX = event.getX();
                locationY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                float shortX = locationX - delete[0];
                float shortY = locationY - delete[1];
                if(Math.abs(shortX) <= 50 && Math.abs(shortY) <= 50&&isShow){
                    isDelete = true;
                    invalidate();
                }
//                locationX = event.getX();
//                locationY = event.getY();
                break;
        }
        return true;
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        // 根据Matrix绘制一个变换后的图片
        if(!isDelete){
            mCanvasWrapper.setCanvasSize(canvas.getWidth(), canvas.getHeight());
            mCanvasWrapper.drawBitmap(mBitmap, mPolyMatrix, pointPaint);
            mCanvasWrapper.draw(canvas);
//            canvas.drawBitmap(mBitmap, mPolyMatrix, null);
            if(isShow){
                canvas.drawPoint(delete[0], delete[1], pointPaint);
                canvas.drawPoint(center[0], center[1], pointPaint);
                // 绘制触控点
                for (int i = 0; i < dst.length; i += 2) {
                    canvas.drawPoint(pointXY[i], pointXY[i + 1], pointPaint);
                    if(i==6){
                        canvas.drawLine(pointXY[i],pointXY[i + 1],pointXY[0],pointXY[1],pointLine);
                    }else{
                        canvas.drawLine(pointXY[i],pointXY[i + 1],pointXY[i + 2],pointXY[i + 3],pointLine);
                    }
                }
            }
        }
        super.dispatchDraw(mCanvasWrapper.getCanvas());
    }

    @Override
    protected void onDraw(Canvas canvas) {



    }

    public void setTestPoint(int testPoint) {
        this.testPoint = testPoint > 4 || testPoint < 0 ? 4 : testPoint;
        dst = src.clone();
        resetPolyMatrix(testPoint,0,0);
    }
}