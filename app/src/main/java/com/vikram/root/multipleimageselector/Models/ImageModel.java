package com.vikram.root.multipleimageselector.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by vkramsingh on 29/10/15.
 */
public class ImageModel implements Parcelable {

    private String id;
    private String path;

    /**
     * Standard basic constructor for non-parcel
     * object creation
     */
    public ImageModel(String id, String path) {
        this.id = id;
        this.path = path;
    }

    /**
     * Constructor to use when re-constructing object
     * from a parcel
     *
     * @param in a parcel from which to read this object
     */
    public ImageModel(Parcel in) {
        readFromParcel(in);
    }

    public String getId() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static boolean containsId(ArrayList<ImageModel> mList, String id) {
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public static void removeObjectWithId(ArrayList<ImageModel> mList, String id) {
        Iterator<ImageModel> modelIterator = mList.iterator();
        while (modelIterator.hasNext()) {
            if (modelIterator.next().getId().equals(id)) {
                modelIterator.remove();
                break;
            }
        }
    }

    public static void removeObjectWithPath(ArrayList<ImageModel> mList, String path) {
        Iterator<ImageModel> modelIterator = mList.iterator();
        while (modelIterator.hasNext()) {
            if (modelIterator.next().getPath().equals(path)) {
                modelIterator.remove();
                break;
            }
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // We just need to write each field into the
        // parcel. When we read from parcel, they
        // will come back in the same order
        dest.writeString(id);
        dest.writeString(path);
    }

    /**
     * Called from the constructor to create this
     * object from a parcel.
     *
     * @param in parcel from which to re-create object
     */
    private void readFromParcel(Parcel in) {

        // We just need to read back each
        // field in the order that it was
        // written to the parcel
        this.id = in.readString();
        this.path = in.readString();
    }


    /**
     * This field is needed for Android to be able to
     * create new objects, individually or as arrays.
     */
    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public ImageModel createFromParcel(Parcel in) {
                    return new ImageModel(in);
                }

                public ImageModel[] newArray(int size) {
                    return new ImageModel[size];
                }
            };

}
