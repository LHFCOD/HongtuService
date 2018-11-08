package com.hongtu.slice.util;

public class SliceParameter {
    private int level;
    private int x;
    private int y;
    private String path;
    private int thumbnailWidth;

    public SliceParameter(String path,int level, int x, int y) {
        this.path = path;
        this.level=level;
        this.x=x;
        this.y=y;
    }
    public SliceParameter(){}
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
    @Override
    public String toString(){
        return String.format("path:%s,level:%d,x:%d,y:%d",path,level,x,y);
    }

    public int getThumbnailWidth() {
        return thumbnailWidth;
    }

    public void setThumbnailWidth(int thumbnailWidth) {
        this.thumbnailWidth = thumbnailWidth;
    }
}
