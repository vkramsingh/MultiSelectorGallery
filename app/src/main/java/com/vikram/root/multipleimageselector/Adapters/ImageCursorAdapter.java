package com.vikram.root.multipleimageselector.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.support.v4.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.vikram.root.multipleimageselector.Models.ImageModel;
import com.vikram.root.multipleimageselector.R;


import java.util.ArrayList;

public class ImageCursorAdapter extends CursorAdapter {

    private ArrayList<ImageModel> mSelectedImagesArray;
    private Context mContext;
    private int mScreenWidth;
    private boolean isAlbum;



    private Point mSize;

    /*
        private LruCache<String, Bitmap> mMemoryCache;
    */
    private LruCache<String, Bitmap> mBlurredMemoryCache;



    public ImageCursorAdapter(Context context,Cursor c, ArrayList<ImageModel> selectedImages, Point size, boolean isAlbum) {
        super(context,c,0);
        mContext = context;
        this.isAlbum=isAlbum;
        this.mSize = size;
        mSelectedImagesArray =selectedImages;
        setMemoryCache();
    }

    public Point getSize() {
        return mSize;
    }

    public void setSize(Point mSize) {
        this.mSize = mSize;
        this.mScreenWidth = mSize.x;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        final ViewHolder holder;
        View view = View.inflate(mContext, R.layout.gallery_cell, null);
        if(view !=null){
            holder = new ViewHolder();


            holder.cbox = (RelativeLayout)view.findViewById(R.id.chkBox);
            holder.image = (ImageView)view.findViewById(R.id.imageView1);
            holder.mCount = (TextView) view.findViewById(R.id.cellCountwe);
            if(isAlbum){
                holder.image.setLayoutParams(new RelativeLayout.LayoutParams(mScreenWidth / 2, mScreenWidth / 2));
                holder.cbox.setLayoutParams(new RelativeLayout.LayoutParams(mScreenWidth / 2, mScreenWidth / 2));
                view.findViewById(R.id.albumDetails).setVisibility(View.VISIBLE);
                holder.mName = (TextView)view.findViewById(R.id.cellName);
            } else {
                holder.image.setLayoutParams(new RelativeLayout.LayoutParams(mScreenWidth / 3, mScreenWidth / 3));
                holder.cbox.setLayoutParams(new RelativeLayout.LayoutParams(mScreenWidth / 3, mScreenWidth / 3));
            }
            view.setTag(holder);
        }
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder holder = null;


        if(view !=null){
            holder = (ViewHolder)view.getTag();
        }



        if(holder != null){

            if (!isAlbum) {
                Boolean flag = ImageModel.containsId(mSelectedImagesArray,cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media._ID)));
                if (!flag) {
                    holder.cbox.setVisibility(View.GONE);
                } else {
                    holder.cbox.setVisibility(View.VISIBLE);
                }
            }
            else {
                holder.cbox.setVisibility(View.GONE);
            }
            if(isAlbum){
                holder.mName.setText(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)));
                holder.mCount.setText(cursor.getInt(cursor.getColumnIndex("count()"))+"");
            }

            loadBitmap(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media._ID)), holder.image,cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));


            //MediaStore.Images.Thumbnails.getThumbnail(mContext.getContentResolver(), Long.parseLong(cursor.getString(fileIdIndex)), MediaStore.Images.Thumbnails.MINI_KIND, null);
        }
    }

    static class ViewHolder {
        RelativeLayout cbox;
        ImageView image;
        TextView mName;
        TextView mCount;
    }

    /*public boolean getMultipleSelectionMode() {
        return multipleSelectionMode;
    }

    public void setMultipleSelectionMode(boolean multipleSelectionMode) {
        this.multipleSelectionMode = multipleSelectionMode;
    }*/

  /*  class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {

        ImageView mImageView;
        ContentResolver mContentResolver;
        String mThumbId;
        Boolean mAddToBlurredCache;

        public BitmapWorkerTask(ImageView imageView,ContentResolver contentResolver,String id,Boolean addToBlurredCache){
            mImageView=imageView;
            mContentResolver=contentResolver;
            mThumbId=id;
            mAddToBlurredCache = addToBlurredCache;
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(String... params) {

            final Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(mContentResolver, Long.parseLong(params[0]), MediaStore.Images.Thumbnails.MINI_KIND, null);
//= decodeSampledBitmapFromResource(mContext.getResources(), params[0], 100, 100);
            if(bitmap !=null){
                addBitmapToMemoryCache(String.valueOf(params[0]), bitmap,mAddToBlurredCache);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Log.e("sdf","postexec");
            if(bitmap != null && mImageView.getTag().equals(mThumbId)){
                mImageView.setImageBitmap(bitmap);
            }
        }
    }*/

    private void setMemoryCache(){
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        /*mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {

            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };*/

        mBlurredMemoryCache = new LruCache<String, Bitmap>(cacheSize) {

            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };

    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap,boolean isBlurred) {
        /*if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key,bitmap);b
        }*/
        if(isBlurred && getBlurredBitmapFromMemCache(key) == null){
            mBlurredMemoryCache.put(key,scaleDown(bitmap,100,false));
        }
    }


    /*public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }
*/
    public Bitmap getBlurredBitmapFromMemCache(String key) {
        return mBlurredMemoryCache.get(key);
    }

    public void loadBitmap(String resId, final ImageView mImageView,String imagePath) {

        final String imageKey = String.valueOf(resId);

        Bitmap mBlurredBitmap = getBlurredBitmapFromMemCache(imageKey);

        if(mBlurredBitmap !=null){
            Drawable mBlurredDrawablePlaceholder = new BitmapDrawable(mContext.getResources(), mBlurredBitmap);
            Glide.with(mContext).load(imagePath).dontAnimate().placeholder(mBlurredDrawablePlaceholder).into(mImageView);
        } else{
            Glide.with(mContext)
                    .load(imagePath)
                    .asBitmap().dontAnimate().placeholder(android.R.color.transparent)
                    .into(new BitmapImageViewTarget(mImageView) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            super.onResourceReady(resource, glideAnimation);
                            addBitmapToMemoryCache(imageKey,resource,true);
                        }
                    });


            /*Glide.with(mContext).loadFromMediaStore(Uri.parse(imagePath)).listener(new RequestListener<Uri, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    Bitmap icon = BitmapFactory.decodeResource(mContext.getResources(),
                            resource.);
                }
            }).placeholder(android.R.color.transparent).into(mImageView);*/
        }

       /* final Bitmap bitmap = getBitmapFromMemCache(imageKey);

        if (bitmap != null) {
            mImageView.setImageBitmap(bitmap);
        } else {

            BitmapWorkerTask task;
            if(mBlurredBitmap != null){
                mImageView.setImageBitmap(mBlurredBitmap);
                task= new BitmapWorkerTask(mImageView,mContext.getContentResolver(),resId,false);
            } else {
                mImageView.setImageResource(android.R.color.transparent);
                task = new BitmapWorkerTask(mImageView,mContext.getContentResolver(),resId,true);
            }
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,resId);
        }*/
    }

    public static Bitmap scaleDown(Bitmap realImage,float maxImageSize,boolean filter){
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }
}