/*
package com.vikram.root.multipleimageselector;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.vikram.root.multipleimageselector.Adapters.ImageCursorAdapter;
import com.vikram.root.multipleimageselector.Constants.Constants;

import java.util.HashMap;


public class AlbumContents extends Activity {

    private Cursor mCursor;
    private int bucketIdIndex;
    private HashMap<String,String> mImagesReceivedFromIntent;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Create","Called");

        setContentView(R.layout.albumcontents);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        mCursor= getAlbumNames();
        bucketIdIndex = mCursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_ID);

        mImagesReceivedFromIntent = (HashMap<String,String>)getIntent().getSerializableExtra("imageList");

        GridView mAlbumGrid=(GridView)findViewById(R.id.albumGrid);
        ImageCursorAdapter mAlbumCursorAdapter= new ImageCursorAdapter(this,mCursor,null,size,true);
        mAlbumGrid.setAdapter(mAlbumCursorAdapter);

        mAlbumGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (view != null) {
                    openAlbum(position);
                }
            }
        });

    }

    public void openAlbum(int position){
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("bucketId",mCursor.getString(bucketIdIndex));
        intent.putExtra("imageList", mImagesReceivedFromIntent);
        startActivityForResult(intent,Constants.SHOW_ALBUM_CONTENTS);
    }

    public Cursor getAlbumNames(){

        String[] PROJECTION_BUCKET = {
                MediaStore.Images.ImageColumns.BUCKET_ID,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns._ID,
                "count()"};
        // We want to order the albums by reverse chronological order. We abuse the
        // "WHERE" parameter to insert a "GROUP BY" clause into the SQL statement.
        // The template for "WHERE" parameter is like:
        //    SELECT ... FROM ... WHERE (%s)
        // and we make it look like:
        //    SELECT ... FROM ... WHERE (1) GROUP BY 1,(2)
        // The "(1)" means true. The "1,(2)" means the first two columns specified
        // after SELECT. Note that because there is a ")" in the template, we use
        // "(2" to match it.
        String BUCKET_GROUP_BY =
                "1) GROUP BY 1,(2";
        String BUCKET_ORDER_BY = "MAX(datetaken) DESC";

        // Get the base URI for the People table in the Contacts content provider.
        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        Cursor cur = getContentResolver().query(
                images, PROJECTION_BUCKET, BUCKET_GROUP_BY, null, BUCKET_ORDER_BY);

        Log.i("ListingImages", " query count=" + cur.getCount());

        if (cur.moveToFirst()) {
            String bucket;
            String date;
            String data;
            int count;
            int bucketColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME);

            int dateColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.DATE_TAKEN);
            int dataColumn = cur.getColumnIndex(
                    MediaStore.Images.Media.DATA);

            int countColumn = cur.getColumnIndex("count()");

            do {
                // Get the field values
                bucket = cur.getString(bucketColumn);
                date = cur.getString(dateColumn);
                data = cur.getString(dataColumn);
                count = cur.getInt(countColumn);


                // Do something with the values.
                Log.i("ListingImages", " bucket=" + bucket
                        + "  date_taken=" + date
                        + "  _data=" + data+" _count=" + count);
            } while (cur.moveToNext());
        }

        return cur;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch(requestCode){
            case Constants.SHOW_ALBUM_CONTENTS:
                if(resultCode == Constants.FINISH_PHOTO_SELECTION){
                    setResult(Constants.FINISH_PHOTO_SELECTION);
                    finish();
                } else if(resultCode == Constants.ALBUM_CONTENTS_BACK_PRESSED) {
                    mImagesReceivedFromIntent = (HashMap<String,String>)data.getSerializableExtra("imageList");
                }
                break;
            case Constants.REVIEW_SELECTION:
                if(resultCode == Constants.REVIEW_SELECTION_BACK_PRESSED){
                    mImagesReceivedFromIntent = (HashMap<String,String>)data.getSerializableExtra("imageList");
                }
        }
    }

    @Override
    public void onBackPressed() {
        setResult(Constants.FINISH_PHOTO_SELECTION);
        finish();
    }
}*/
