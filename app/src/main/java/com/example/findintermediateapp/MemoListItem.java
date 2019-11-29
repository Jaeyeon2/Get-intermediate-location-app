package com.example.findintermediateapp;

public class MemoListItem {
    private String memoContent;
    private String[] memoAllImage;
    private String memoFirstImage;
    private int memoImageCount;
    private String memoLocation;
    private String memoAddress;

    public void setMemoContent(String content) {
        this.memoContent = content;
    }

    public void setMemoAllImage(String[] allImage) {
        this.memoAllImage = allImage;
    }

    public void setMemoFirstImage(String image) {
        this.memoFirstImage = image;
    }

    public void setMemoImageCount(int count) {
        this.memoImageCount = count;
    }

    public void setMemoLocation(String location) {
        this.memoLocation = location;
    }

    public void setMemoAddress(String address) {
        this.memoAddress = address;
    }

    public String getMemoContent() {
        return this.memoContent;
    }

    public String[] getMemoAllImage() {
        return this.memoAllImage;
    }

    public String getMemoFirstImage() {
        return this.memoFirstImage;
    }

    public int getMemoImageCount() {
        return this.memoImageCount;
    }

    public String getMemoLocation() {
        return this.memoLocation;
    }

    public String getMemoAddress() {
        return this.memoAddress;
    }
}
