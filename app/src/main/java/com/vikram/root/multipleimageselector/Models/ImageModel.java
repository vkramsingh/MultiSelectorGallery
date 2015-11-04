package com.vikram.root.multipleimageselector.Models;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by vkramsingh on 29/10/15.
 */
public class ImageModel {

    private String id;
    private String path;

    public ImageModel(String id,String path){
        this.id=id;
        this.path=path;
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

    public static boolean containsId(ArrayList<ImageModel> mList, String id){
        for(int i=0;i<mList.size();i++){
            if(mList.get(i).getId().equals(id)){
                return true;
            }
        }
        return false;
    }

    public static void removeObjectWithId(ArrayList<ImageModel> mList, String id){
        Iterator<ImageModel> modelIterator = mList.iterator();
        while(modelIterator.hasNext()){
            if(modelIterator.next().getId().equals(id)){
                modelIterator.remove();
                break;
            }
        }
    }

    public static void removeObjectWithPath(ArrayList<ImageModel> mList, String path){
        Iterator<ImageModel> modelIterator = mList.iterator();
        while(modelIterator.hasNext()){
            if(modelIterator.next().getPath().equals(path)){
                modelIterator.remove();
                break;
            }
        }
    }
}