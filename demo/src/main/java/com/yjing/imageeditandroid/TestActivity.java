package com.yjing.imageeditandroid;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by xxl on 19/4/29.
 * <p>
 * Description
 **/
public class TestActivity extends AppCompatActivity {

    private static final int CAMERA_WITH_DATA = 3023;
    private String photoPath, camera_path, tempPhotoPath;

    //* 用来标识请求gallery的activity *//*
    private static final int PHOTO_PICKED_WITH_DATA = 3021;
    private CustomImageView mIv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mIv = (CustomImageView) findViewById(R.id.iv);
    }


    /**
     * 红色按钮
     *
     * @param view
     */
    public void red(View view) {
        mIv.red(view);
    }

    /**
     * 绿色按钮
     *
     * @param view
     */
    public void green(View view) {
        mIv.green(view);
    }

    /**
     * 蓝色按钮
     *
     * @param view
     */
    public void blue(View view) {
        mIv.blue(view);
    }

    public void small(View view) {
        //改变刷子的宽度
//        width = 1;
        mIv.small(view);
    }

    public void zhong(View view) {
        //改变刷子的宽度
//        width = 5;
        mIv.zhong(view);
    }

    public void big(View view) {
        //改变刷子的宽度
//        width = 10;
        mIv.big(view);
    }

    /**
     * 圆形
     *
     * @param view
     */
    public void circle(View view) {
//        circle = 0;
        mIv.circle(view);
    }

    /**
     * 矩形
     *
     * @param view
     */
    public void fang(View view) {
//        circle = 1;
        mIv.fang(view);
    }

    /**
     * 矩形
     *
     * @param view
     */
    public void arrow(View view) {
//        circle = 2;
        mIv.arrow(view);
    }


    /**
     * 单步撤销
     *
     * @param view
     */
    public void one(View view) {
        mIv.one(view);
    }

    /**
     * 全部撤销
     *
     * @param view
     */
    public void all(View view) {
        mIv.all(view);
    }


    /**
     * 相册
     * @param view
     */
    public void pics(View view) {
        getPictureFromPhoto();
    }

      //* 从相册中获取照片 *//*
    private void getPictureFromPhoto() {
        Intent openphotoIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openphotoIntent, PHOTO_PICKED_WITH_DATA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case PHOTO_PICKED_WITH_DATA:
                Uri selectedImage = data.getData();
                String[] filePathColumns = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                int columnIndex = c.getColumnIndex(filePathColumns[0]);
                c.moveToFirst();
                photoPath = c.getString(columnIndex);
                c.close();
                break;
            default:
                    break;
        }

        mIv.setImage(photoPath);
    }


}
