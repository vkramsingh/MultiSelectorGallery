package com.vikram.root.multipleimageselector.Adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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

    public ReviewSelectionPagerAdapter(Context context,ArrayList<ImageModel> imageModel){
        mContext = context;
        mSelectedImages = imageModel;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View imageAndTag = View.inflate(mContext, R.layout.review_selection_pager_view,null);
        ((ViewPager)container).addView(imageAndTag);
        ImageView mImageBig = (ImageView)imageAndTag.findViewById(R.id.imageBig);
        mImageBig.setImageBitmap(ImageUtils.decodeSampledBitmapFromFile(mSelectedImages.get(position).getPath(), mImageBig.getLayoutParams().width, mImageBig.getLayoutParams().height));
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