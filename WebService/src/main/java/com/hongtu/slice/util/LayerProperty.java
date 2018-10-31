package com.hongtu.slice.util;

public class LayerProperty {
    private String folderName;
    private float scaleVal;
    private int rowCount;
    private int colCount;
    private int lastImageWidth;
    private int lastImageHeight;
    private int currentWidth;
    private int currentHeight;
    private int width;
    private int height;

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getColCount() {
        return colCount;
    }

    public void setColCount(int colCount) {
        this.colCount = colCount;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public float getScaleVal() {
        return scaleVal;
    }

    public void setScaleVal(float scaleVal) {
        this.scaleVal = scaleVal;
    }

    public int getCurrentWidth() {
        return currentWidth;
    }

    public void setCurrentWidth(int currentWidth) {
        this.currentWidth = currentWidth;
    }

    public int getCurrentHeight() {
        return currentHeight;
    }

    public void setCurrentHeight(int currentHeight) {
        this.currentHeight = currentHeight;
    }

    public int getLastImageWidth() {
        return lastImageWidth;
    }

    public void setLastImageWidth(int lastImageWidth) {
        this.lastImageWidth = lastImageWidth;
    }

    public int getLastImageHeight() {
        return lastImageHeight;
    }

    public void setLastImageHeight(int lastImageHeight) {
        this.lastImageHeight = lastImageHeight;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
