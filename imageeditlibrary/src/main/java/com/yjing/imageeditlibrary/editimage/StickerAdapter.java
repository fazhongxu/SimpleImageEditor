package com.yjing.imageeditlibrary.editimage;

/**
 * Created by wangyanjing on 2018/2/27.
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yjing.imageeditlibrary.R;
import com.yjing.imageeditlibrary.editimage.fragment.StickerFragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xxl on 19/4/30.
 * <p>
 * Description 贴图 adapter
 **/
public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.ViewHolder> {

    public DisplayImageOptions imageOption = new DisplayImageOptions.Builder()
            .cacheInMemory(true).showImageOnLoading(R.drawable.yd_image_tx)
            .build();// 下载图片显示

    StickerFragment mStickerFragment;
    public StickerAdapter(StickerFragment fragment) {
        this.mStickerFragment = fragment;
        for (String path : stickerPath) {
            addStickerImages(path);
        }
    }

    public void addStickerImages(String folderPath) {
        pathList.clear();
        try {
            String[] files = mStickerFragment.getActivity().getAssets()
                    .list(folderPath);
            for (String name : files) {
                pathList.add(folderPath + File.separator + name);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.notifyDataSetChanged();
    }

    private List<String> pathList = new ArrayList<String>();// 图片路径列表

    public static final String[] stickerPath = {"stickers/type1", "stickers/type2", "stickers/type3", "stickers/type4", "stickers/type5", "stickers/type6"};

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.view_sticker_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String path = pathList.get(position);
        ImageLoader.getInstance().displayImage("assets://" + path,
                holder.mImageView, imageOption);
        holder.mImageView.setTag(path);

        holder.mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStickerFragment.selectedStickerItem((String) view.getTag());
            }
        });
    }

    @Override
    public int getItemCount() {
        return pathList == null ? 0 : pathList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView mImageView;
        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = (ImageView) itemView.findViewById(R.id.img);
        }
    }
}

/*

public class StickerAdapter extends BaseAdapter {

    public DisplayImageOptions imageOption = new DisplayImageOptions.Builder()
            .cacheInMemory(true).showImageOnLoading(R.drawable.yd_image_tx)
            .build();// 下载图片显示

    private final StickerFragment mStickerFragment;
    private List<String> pathList = new ArrayList<String>();// 图片路径列表

    public static final String[] stickerPath = {"stickers/type1", "stickers/type2", "stickers/type3", "stickers/type4", "stickers/type5", "stickers/type6"};

    public StickerAdapter(StickerFragment fragment) {
        super();
        this.mStickerFragment = fragment;
        for (String path : stickerPath) {
            addStickerImages(path);
        }
    }

    @Override
    public int getCount() {
        return pathList.size();
    }

    @Override
    public Object getItem(int i) {
        return pathList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = null;
        v = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.view_sticker_item, viewGroup, false);
        ImageHolder imageHoler = new ImageHolder(v);

        String path = pathList.get(i);
        ImageLoader.getInstance().displayImage("assets://" + path,
                imageHoler.image, imageOption);
        v.setTag(path);

        imageHoler.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStickerFragment.selectedStickerItem((String) view.getTag());
            }
        });
        return v;
    }


    public class ImageHolder {
        public ImageView image;

        public ImageHolder(View itemView) {
            this.image = (ImageView) itemView.findViewById(R.id.img);
        }
    }// end inner class

    public void addStickerImages(String folderPath) {
        pathList.clear();
        try {
            String[] files = mStickerFragment.getActivity().getAssets()
                    .list(folderPath);
            for (String name : files) {
                pathList.add(folderPath + File.separator + name);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.notifyDataSetChanged();
    }
}
*/
