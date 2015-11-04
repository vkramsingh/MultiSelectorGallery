package com.vikram.root.multipleimageselector.Fragments;


import android.app.Activity;
import android.database.Cursor;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.vikram.root.multipleimageselector.Adapters.ImageCursorAdapter;
import com.vikram.root.multipleimageselector.R;

import java.util.ArrayList;

public class AlbumFragment extends android.support.v4.app.Fragment implements LoaderManager.LoaderCallbacks<Cursor>{


    private static final String[] PROJECTION_BUCKET = {
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
    private static final String BUCKET_GROUP_BY =
            "1) GROUP BY 1,(2";
    private static final String BUCKET_ORDER_BY = "MAX(datetaken) DESC";



    public interface OnAlbumSelectedListener{
        void onAlbumSelected(String bucketId);
    }

    private OnAlbumSelectedListener mAlbumSelectedListener;
    private GridView mAlbumsGrid;
    private ArrayList<String> mSelectedAlbums;
    private ImageCursorAdapter mAlbumCursorAdapter;


    public static AlbumFragment newInstance(ArrayList<String> mSelectedAlbums) {
        AlbumFragment f = new AlbumFragment();
        Bundle args = new Bundle();
        args.putSerializable("albumList",mSelectedAlbums);
        f.setArguments(args);
        return f;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        mSelectedAlbums = (ArrayList<String>)getArguments().getSerializable("albumList");

        mAlbumCursorAdapter= new ImageCursorAdapter(getActivity(),null,null,null,true);

        getLoaderManager().initLoader(0,null,this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mAlbumSelectedListener = (OnAlbumSelectedListener)activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(container ==null){
            return null;
        }

        View rootView = inflater.inflate(R.layout.albumcontents,container,false);
        mAlbumsGrid = (GridView)rootView.findViewById(R.id.albumGrid);
        mAlbumsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (view != null) {
                    openAlbum((Cursor)parent.getItemAtPosition(position));
                }
            }
        });

        mAlbumsGrid.setAdapter(mAlbumCursorAdapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        /*getActivity().findViewById(R.id.multiselect_on).setVisibility(View.GONE);*/

        mAlbumCursorAdapter.setSize(size);
    }

    public void openAlbum(Cursor mLocalCursor){
        mAlbumSelectedListener.onAlbumSelected(mLocalCursor.getString(mLocalCursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_ID)));
    }




    public Cursor getAlbumNames(){

        // Get the base URI for the People table in the Contacts content provider.
        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        Cursor cur = getActivity().getContentResolver().query(
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d("@vikram","Create loader called Album");
        return new CursorLoader(getActivity(),MediaStore.Images.Media.EXTERNAL_CONTENT_URI,PROJECTION_BUCKET,BUCKET_GROUP_BY,null,BUCKET_ORDER_BY);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("@vikram","Load finished called Album");

        mAlbumCursorAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d("@vikram","Loader reset called Album");

        mAlbumCursorAdapter.changeCursor(null);
    }
}