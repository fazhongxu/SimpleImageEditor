package com.yjing.imageeditlibrary.editimage.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.yjing.imageeditlibrary.editimage.inter.EditFunctionOperationInterface;
import com.yjing.imageeditlibrary.editimage.model.Shapes;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class CustomPaintView extends View implements EditFunctionOperationInterface {
    private Paint mPaint;
    private Bitmap mDrawBit;

    private Canvas mPaintCanvas = null;

    private float last_x;
    private float last_y;

    private boolean isOperation = false;
    private Path mPath;

    private ArrayList<Shapes> shapes = new ArrayList<Shapes>();



    //    private ImageView iv;//展示图片
    private Bitmap copyPic;//编辑图片
    private Canvas canvas;//画板
    private Paint paint;//画笔
    private Matrix matrix;//矩阵
    private Bitmap srcPic;//原图
    private int color = Color.BLACK;//画笔颜色
    private int width = 5;//画笔大小
    private int circle = 1;//形状

    private String camera_path;
    // 0 自由绘制 1 圆形
    private Shape mPaintType = Shape.Line;

    public enum Shape {
        Line, // 自由线条
        Circle, // 圆
        Rectangle //矩形
        ,Arrow // 箭头
    }

    private void setShapeType() {
        switch (mPaintType) {
            case Line:
                break;
            case Circle:
                circle = 0;
                break;
            case Rectangle:
                circle = 1;
                break;
            case Arrow:
                circle = 2;
                break;
            default:break;
        }
    }

    /**
     * 设置 形状 默认是自由绘制轨迹
     * @param paintType
     */
    public void setShape(Shape paintType) {
        this.mPaintType = paintType;
    }


    /**
     * 模拟栈，保存涂鸦操作，便于撤销
     */
    private CopyOnWriteArrayList<PaintPath> mUndoStack = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Object> mUndoStackList = new CopyOnWriteArrayList<>();

    public CustomPaintView(Context context) {
        super(context);
        initPaint();
    }

    public CustomPaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public CustomPaintView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomPaintView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initPaint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //System.out.println("width = "+getMeasuredWidth()+"     height = "+getMeasuredHeight());
        if (mDrawBit == null) {
            generatorBit();
//            drawPic(null);
        }
    }

    /**
     * 画画
     */
    public void drawPic(String camera_path) {
        /*srcPic = BitmapFactory.decodeFile(camera_path);
        copyPic = Bitmap.createBitmap(srcPic.getWidth(), srcPic.getHeight(),
                srcPic.getConfig());*/
        canvas = new Canvas(copyPic);
        paint = new Paint();
        paint.setAntiAlias(true);
        //绘制原图
        drawOld();



        // canvas.drawBitmap(copyPic,matrix,paint);

//        setImageBitmap(copyPic);

        //触摸事件
    }

    /**
     * 绘制底图
     */
    private void drawOld() {
        // 给画笔设置默认的颜色，在画画的过程中会使用原图的颜色来画画
        paint.setColor(Color.BLACK);

        // 处理图形
        matrix = new Matrix();
        // 5、使用画笔在画板上画画
        // 参看原图画画
        // srcPic 原图
        // matrix 表示图形的矩阵对象,封装了处理图形的api
        // paint 画画时使用的画笔



        //canvas.drawBitmap(srcPic, matrix, paint);
    }


    /**
     * 创建画布
     */
    private void generatorBit() {
        mDrawBit = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        mPaintCanvas = new Canvas(mDrawBit);

        this.copyPic = mDrawBit;

        drawPic(null);
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    /**
     * 复制一支信息相同的画笔
     */
    private Paint copyPaint() {
        Paint paint = new Paint();
        paint.setColor(mPaint.getColor());
        paint.setAntiAlias(mPaint.isAntiAlias());
        paint.setStrokeJoin(mPaint.getStrokeJoin());
        paint.setStrokeCap(mPaint.getStrokeCap());
        paint.setStyle(mPaint.getStyle());
        paint.setStrokeWidth(mPaint.getStrokeWidth());
        return paint;
    }

    public void setColor(int color) {
        this.mPaint.setColor(color);
        this.color = color;
    }

    public void setWidth(float width) {
        this.mPaint.setStrokeWidth(width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDrawBit != null) {
            canvas.drawBitmap(mDrawBit, 0, 0, null);
        }
    }

    private void drawGuiji() {
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        drawOld();
        drawOldLine();
        for (Shapes sp : shapes) {//画历史轨迹
            paint.setColor(sp.color);
            paint.setStrokeWidth(sp.width);
            if (sp.circle == 1) {
                paint.setStyle(Paint.Style.STROKE);//设置边框
                canvas.drawRect(sp.startX, sp.startY, sp.endX, sp.endY, paint);// 正方形
            } else if (sp.circle == 0) {
                paint.setStyle(Paint.Style.STROKE);//设置边框
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//api21之后的方法
                    canvas.drawOval(sp.startX, sp.startY, sp.endX, sp.endY, paint);//椭圆
                }
            } else if (sp.circle == 2) {
                paint.setStyle(Paint.Style.FILL);//设置边框
                drawArrow(sp.startX, sp.startY, sp.endX, sp.endY, sp.width, paint);//箭头
            }
        }
        setImageBitmap(copyPic);
    }

    private void drawOldLine() {
        draw(mPaintCanvas, mUndoStack);
        invalidate();
    }

    private void setImageBitmap(Bitmap bitmap) {
        canvas.drawBitmap(bitmap,matrix,paint);
    }

    /**
     * 画箭头
     *
     * @param sx
     * @param sy
     * @param ex
     * @param ey
     * @param paint
     */
    private void drawArrow(float sx, float sy, float ex, float ey, int width, Paint paint) {
        int size = 5;
        int count = 20;
        switch (width) {
            case 0:
                size = 5;
                count = 20;
                break;
            case 5:
                size = 8;
                count = 30;
                break;
            case 10:
                size = 11;
                count = 40;
                break;
        }
        float x = ex - sx;
        float y = ey - sy;
        double d = x * x + y * y;
        double r = Math.sqrt(d);
        float zx = (float) (ex - (count * x / r));
        float zy = (float) (ey - (count * y / r));
        float xz = zx - sx;
        float yz = zy - sy;
        double zd = xz * xz + yz * yz;
        double zr = Math.sqrt(zd);
        Path triangle = new Path();
        triangle.moveTo(sx, sy);
        triangle.lineTo((float) (zx + size * yz / zr), (float) (zy - size * xz / zr));
        triangle.lineTo((float) (zx + size * 2 * yz / zr), (float) (zy - size * 2 * xz / zr));
        triangle.lineTo(ex, ey);
        triangle.lineTo((float) (zx - size * 2 * yz / zr), (float) (zy + size * 2 * xz / zr));
        triangle.lineTo((float) (zx - size * yz / zr), (float) (zy + size * xz / zr));
        triangle.close();
        canvas.drawPath(triangle, paint);
    }



    private float endY;
    private float endX;
    private float startX;
    private float startY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //外部通过isOperation来控制是否要对此涂鸦view进行操作
        if (!isOperation) {
            return isOperation;
        }

        boolean ret = super.onTouchEvent(event);
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mPaintType != Shape.Line) { // 绘制图形
                    setShapeType();
                    startX = event.getX();
                    startY = event.getY();
                    drawGuiji();
                }else { //自由画线

                    ////////////
                    // 每次down下去重新new一个Path
                    mPath = new Path();
                    Log.i("wangyanjing", "新创建了一个mPath" + mPath.hashCode());
                    mPath.moveTo(x, y);
                    last_x = x;
                    last_y = y;
                }
                ret = true;

                break;
            case MotionEvent.ACTION_MOVE:
                if (mPaintType != Shape.Line) { // 绘制图形
                    // 得到结束位置的坐标点
                    endX = event.getX();
                    endY = event.getY();
                    // 清除之前轨迹
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                    canvas.drawPaint(paint);
                    drawGuiji();
                    paint.setStrokeWidth(width);
                    paint.setColor(color);
                    if (circle == 1) {
                        paint.setStyle(Paint.Style.STROKE);//设置边框
                        canvas.drawRect(startX, startY, endX, endY, paint);// 正方形
                    } else if (circle == 0) {
                        paint.setStyle(Paint.Style.STROKE);//设置边框
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            canvas.drawOval(startX, startY, endX, endY, paint);
                        }
                    } else if (circle == 2) {
                        paint.setStyle(Paint.Style.FILL);//设置边框
                        drawArrow(startX, startY, endX, endY, width, paint);
                    }
                    setImageBitmap(copyPic);
                }else {  //自由画线
                    // 从x1,y1到x2,y2画一条贝塞尔曲线，更平滑(直接用mPath.lineTo也是可以的)
                    mPath.lineTo(x, y);
                    mPaintCanvas.drawPath(mPath, mPaint);
//                mPaintCanvas.drawLine(last_x, last_y, x, y, mPaint);
                    last_x = x;
                    last_y = y;
                }
                ret = true;
                this.postInvalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mPaintType != Shape.Line) {
//                    shapes.add(new Shapes(startX, startY, endX, endY, width, paint.getColor(), circle));//保存历史轨迹
                    Shapes shapes = new Shapes(startX, startY, endX, endY, width, paint.getColor(), circle);
                    this.shapes.add(shapes);//保存历史轨迹

                    mUndoStackList.add(shapes);
                }else { //自由画线
                    PaintPath paintPath = new PaintPath(mPath, copyPaint());
                    mUndoStack.add(paintPath);
                    Log.i("wangyanjing", "数组里面加入了一个mPath" + mPath.hashCode() + "====" + mUndoStack.size());
                    mPaintCanvas.drawPath(mPath, mPaint);

                    mUndoStackList.add(paintPath);
                }


                ret = false;
                this.postInvalidate();
                break;
            default:
                break;
        }
        return ret;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mDrawBit != null && !mDrawBit.isRecycled()) {
            mDrawBit.recycle();
        }
    }

    public Bitmap getPaintBit() {
        return mDrawBit;
    }

    public void reset() {
        resetCanvas();
        //清空保存操作的栈容器
        mUndoStack.clear();
        shapes.clear();

        mUndoStackList.clear();
    }

    private void resetCanvas() {
        if (mDrawBit != null && !mDrawBit.isRecycled()) {
            mDrawBit.recycle();
        }
        invalidate();
        generatorBit();
    }

    @Override
    public void setIsOperation(boolean isOperation) {
        this.isOperation = isOperation;
    }

    @Override
    public Boolean getIsOperation() {
        return null;
    }



    /**
     * 图形的 单步撤销
     *
     */
    public void one() {
        int size = shapes.size();
        if (size > 0) {
            shapes.remove(size - 1);
            resetCanvas();
            drawGuiji();
            invalidate();
        }
    }

    /**
     * 图形 的 全部撤销
     *
     */
    public void all() {
        shapes.clear();
        resetCanvas();
        drawGuiji();
        invalidate();
    }


    /**
     * 撤销
     */
    public void undo() {
        if (mUndoStackList.size() > 0) {
            Object object = mUndoStackList.remove(mUndoStackList.size() -1);
            if (object instanceof PaintPath) {
                PaintPath undoable = mUndoStack.remove(mUndoStack.size() - 1);
                Log.i("wangyanjing", "撤销了一个mPath"+undoable.hashCode()+"===="+mUndoStack.size());
                resetCanvas();
                draw(mPaintCanvas, mUndoStack);
                drawGuiji();
                invalidate();
            }else {
                one();
            }

        }
        // 图形一步一步撤销
//        one();
    }
// /**
//     * 撤销
//     */
//    public void undo() {
//        if (mUndoStack.size() > 0) {
//            PaintPath undoable = mUndoStack.remove(mUndoStack.size() - 1);
//            Log.i("wangyanjing", "撤销了一个mPath"+undoable.hashCode()+"===="+mUndoStack.size());
//            resetCanvas();
//            draw(mPaintCanvas, mUndoStack);
//            invalidate();
//        }
//        // 图形一步一步撤销
//        one();
//    }

    private void draw(Canvas mPaintCanvas, CopyOnWriteArrayList<PaintPath> mUndoStack) {
        for (PaintPath paintPath : mUndoStack) {
            mPaintCanvas.drawPath(paintPath.getPath(), paintPath.getPaint());
        }
    }
}//end class
