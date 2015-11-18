package com.vikram.root.multipleimageselector.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.vikram.root.multipleimageselector.Models.ImageModel;
import com.vikram.root.multipleimageselector.R;
import com.vikram.root.multipleimageselector.Utils.ImageUtils;

import java.util.ArrayList;


/**
 * Created by vkramsingh on 29/10/15.
 */


public class ReviewSelectionPagerAdapter extends PagerAdapter{

    private Context mContext;
    private ArrayList<ImageModel> mSelectedImages;
    private Matrix mMatrix = new Matrix();
    private RequestListener<Uri,GlideDrawable> mRequestListner;

    public ReviewSelectionPagerAdapter(Context context,ArrayList<ImageModel> imageModel){
        mContext = context;
        mSelectedImages = imageModel;
        mRequestListner= new RequestListener<Uri, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                // easy
                return false;
                // impossible?
            }
        };


    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View imageAndTag = View.inflate(mContext, R.layout.review_selection_pager_view,null);
        ((ViewPager)container).addView(imageAndTag);
        ImageView mImageBig = (ImageView)imageAndTag.findViewById(R.id.imageBig);

        ImageModel currentImageModel = mSelectedImages.get(position);
        Glide.with(mContext).load(currentImageModel.getPath()).dontAnimate().into(mImageBig);
        /*mImageBig.setImageBitmap(ImageUtils.decodeSampledBitmapFromFileWithOrientation(currentImageModel.getPath(), mImageBig.getLayoutParams().width, mImageBig.getLayoutParams().height, currentImageModel.getOrientation()));*/
        return imageAndTag;
    }

    @Override
    public int getCount() {
        return mSelectedImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (View)object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}