package com.vikram.root.multipleimageselector.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Matrix;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.vikram.root.multipleimageselector.Models.ImageModel;
import com.vikram.root.multipleimageselector.R;


import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by vkramsingh on 29/10/15.
 */


public class ReviewSelectionPagerAdapter extends PagerAdapter implements View.OnClickListener {

    private Context mContext;
    private ArrayList<ImageModel> mSelectedImages;
    private Matrix mMatrix = new Matrix();
    private RequestListener<Uri, GlideDrawable> mRequestListner;

    private HashMap<String,String> tagHash = new HashMap<String,String>();

    private static final String[] TAG_ARRAY = {"Bedroom", "Bathroom", "Hall", "Drawing Room", "Lobby", "House Exterior", "Garden", "Drive Way", "Garage"};


    public ReviewSelectionPagerAdapter(Context context, ArrayList<ImageModel> imageModel) {
        mContext = context;
        mSelectedImages = imageModel;
        mRequestListner = new RequestListener<Uri, GlideDrawable>() {
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
        View imageAndTag = View.inflate(mContext, R.layout.review_selection_pager_view, null);
        ((ViewPager) container).addView(imageAndTag);
        ImageView mImageBig = (ImageView) imageAndTag.findViewById(R.id.imageBig);

        ImageModel currentImageModel = mSelectedImages.get(position);
        Glide.with(mContext).load(currentImageModel.getPath()).dontAnimate().into(mImageBig);
        TextView tagTV = (TextView) imageAndTag.findViewById(R.id.photo_tag);

        if (tagTV != null) {
            tagTV.setTag(position);
            tagTV.setOnClickListener(this);
        }
        tagTV.setText(mSelectedImages.get(position).getTag());
        /*mImageBig.setImageBitmap(ImageUtils.decodeSampledBitmapFromFileWithOrientation(currentImageModel.getPath(), mImageBig.getLayoutParams().width, mImageBig.getLayoutParams().height, currentImageModel.getOrientation()));*/
        return imageAndTag;
    }

    @Override
    public int getCount() {
        return mSelectedImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (View) object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.photo_tag:
                int x = (int) v.getTag();
                getBasicDialog(mContext, "Select Tag", (TextView) v).show();
                break;
        }
    }

    public Dialog getBasicDialog(Context context, String dialogTitle,final TextView target) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_dialog_title_bar);
        dialog.setContentView(R.layout.custom_dialog_for_ppf);
        //dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialog;
        TextView dialogTitleTV = (TextView) dialog.findViewById(R.id.dialogTitle);
        final TextView dialogDone = (TextView) dialog.findViewById(R.id.done);
        dialogTitleTV.setText(dialogTitle);
        dialogDone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        ListView listView = (ListView) dialog.findViewById(R.id.dropDownListInfoType);
        ArrayAdapter<String> simpleArrayAdapter = new ArrayAdapter<String>(context, R.layout.autosuggest_list_item, TAG_ARRAY);
        listView.setAdapter(simpleArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                target.setText(((TextView) view).getText());
                int currentPosition = (int)target.getTag();
                mSelectedImages.get(currentPosition).setTag((String)((TextView) view).getText());
                dialog.dismiss();
            }
        });
        return dialog;
    }
}