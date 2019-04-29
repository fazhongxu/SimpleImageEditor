package com.yjing.imageeditandroid;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Build;
import android.os.Environment;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by xxl on 19/4/29.
 * <p>
 * Description 自定义 可以绘制箭头 方形框 圆形框的 view
 **/
public class CustomImageView extends AppCompatImageView {

    private ArrayList<Shapes> shapes = new ArrayList<Shapes>();

//    private ImageView iv;//展示图片
    private Bitmap copyPic;//编辑图片
    private Canvas canvas;//画板
    private Paint paint;//画笔
    private Matrix matrix;//矩阵
    private Bitmap srcPic;//原图
    private int color = Color.BLACK;//画笔颜色
    private int width = 0;//画笔大小
    private int circle;//形状

    private String camera_path;
    //图片保存路径
    public static final String filePath = Environment.getExternalStorageDirectory() + "/PictureTest/";
    private int screenWidth;

    public CustomImageView(Context context) {
        this(context, null);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setImage(String photoPath) {
//        this.camera_path = photoPath;
        Bitmap bitmap = compressionFiller(photoPath, this);
        this.camera_path = saveBitmap(bitmap, "saveTemp");
        drawPic(camera_path);
    }


    private float endY;
    private float endX;
    private float startX;
    private float startY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:// 按下的事件类型
                startX = event.getX();
                startY = event.getY();
                drawGuiji();
                break;
            case MotionEvent.ACTION_MOVE:// 移动的事件类型
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
                break;

            case MotionEvent.ACTION_UP:// 移动的事件类型
                shapes.add(new Shapes(startX, startY, endX, endY, width, paint.getColor(), circle));//保存历史轨迹
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 画画
     */
    public void drawPic(String camera_path) {
        srcPic = BitmapFactory.decodeFile(camera_path);
        copyPic = Bitmap.createBitmap(srcPic.getWidth(), srcPic.getHeight(),
                srcPic.getConfig());
        canvas = new Canvas(copyPic);
        paint = new Paint();
        paint.setAntiAlias(true);
        //绘制原图
        drawOld();
        setImageBitmap(copyPic);

        //触摸事件
    }

    private void drawGuiji() {
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        drawOld();
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
        canvas.drawBitmap(srcPic, matrix, paint);
    }

    /**
     * 红色按钮
     *
     * @param view
     */
    public void red(View view) {
        color = Color.RED;
    }

    /**
     * 绿色按钮
     *
     * @param view
     */
    public void green(View view) {
        color = Color.GREEN;
    }

    /**
     * 蓝色按钮
     *
     * @param view
     */
    public void blue(View view) {
        color = Color.BLUE;
    }

    public void small(View view) {
        //改变刷子的宽度
        width = 1;
    }

    public void zhong(View view) {
        //改变刷子的宽度
        width = 5;
    }

    public void big(View view) {
        //改变刷子的宽度
        width = 10;
    }

    /**
     * 圆形
     *
     * @param view
     */
    public void circle(View view) {
        circle = 0;
    }

    /**
     * 矩形
     *
     * @param view
     */
    public void fang(View view) {
        circle = 1;
    }

    /**
     * 矩形
     *
     * @param view
     */
    public void arrow(View view) {
        circle = 2;
    }


    /**
     * 单步撤销
     *
     * @param view
     */
    public void one(View view) {
        int size = shapes.size();
        if (size > 0) {
            shapes.remove(size - 1);
            drawGuiji();
        }
    }

    /**
     * 全部撤销
     *
     * @param view
     */
    public void all(View view) {
        shapes.clear();
        drawGuiji();
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

    /**
     * 根据时间戳生成文件名
     *
     * @return
     */
    public static String getNewFileName() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

    /**
     * 将生成的图片保存到内存中
     *
     * @param bitmap
     * @param name
     * @return
     */
    public String saveBitmap(Bitmap bitmap, String name) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File dir = new File(filePath);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File file = new File(filePath + name + ".jpg");
            FileOutputStream out;
            try {
                out = new FileOutputStream(file);
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)) {
                    out.flush();
                    out.close();
                }
                return file.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 根据路径获取图片并且压缩，适应view
     *
     * @param filePath    图片路径
     * @param contentView 适应的view
     * @return Bitmap 压缩后的图片
     */
    public Bitmap compressionFiller(String filePath, View contentView) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, opt);
        int layoutHeight = contentView.getHeight();
        float scale = 0f;
        int bitmapHeight = bitmap.getHeight();
        int bitmapWidth = bitmap.getWidth();
        scale = bitmapHeight > bitmapWidth
                ? layoutHeight / (bitmapHeight * 1f)
                : screenWidth / (bitmapWidth * 1f);
        Bitmap resizeBmp;
        if (scale != 0) {
            int bitmapheight = bitmap.getHeight();
            int bitmapwidth = bitmap.getWidth();
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale); // 长和宽放大缩小的比例
            resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmapwidth,
                    bitmapheight, matrix, true);
        } else {
            resizeBmp = bitmap;
        }
        return resizeBmp;
    }


}
