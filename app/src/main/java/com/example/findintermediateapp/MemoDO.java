package com.example.findintermediateapp;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBIndexRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBRangeKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.List;
import java.util.Map;
import java.util.Set;

@DynamoDBTable(tableName = "memo")

public class MemoDO {
    private String _androidId;
    private String _location;
    private String _image;

    @DynamoDBHashKey(attributeName = "android_id")
    @DynamoDBAttribute(attributeName = "android_id")
    public String getAndroidId() {
        return _androidId;
    }

    public void setAndroidId(final String _androidId) {
        this._androidId = _androidId;
    }
    @DynamoDBRangeKey(attributeName = "location")
    @DynamoDBAttribute(attributeName = "location")
    public String getLocation() {
        return _location;
    }

    public void setLocation(final String _location) {
        this._location = _location;
    }
    @DynamoDBAttribute(attributeName = "image")
    public String getImage() {
        return _image;
    }

    public void setImage(final String _image) {
        this._image = _image;
    }

}
