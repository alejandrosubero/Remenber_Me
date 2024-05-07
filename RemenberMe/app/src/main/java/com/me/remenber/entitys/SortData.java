package com.me.remenber.entitys;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

@Entity(tableName = "SortData")
public class SortData implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "typeData")
    private String typeData;

    @ColumnInfo(name = "use")
    private String use;

    @ColumnInfo(name = "note")
    private String note;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] image;

    @ColumnInfo(name = "itemImageString")
    private String imageString;

    @ColumnInfo(name = "itemImageBase64String")
    private String imageBase64String;

    @ColumnInfo(name = "codeUser")
    private String codeUser;

    @ColumnInfo(name = "imageFail", typeAffinity = ColumnInfo.BLOB)
    private byte[] imageFail;

    @ColumnInfo(name = "isblock")
    private boolean isblock;

    @ColumnInfo(name = "isEncripted")
    private boolean isEncripted;

    @ColumnInfo(name = "isEresable")
    private boolean isEresable;

    @ColumnInfo(name = "itsAlreadyEncripted")
    private boolean itsAlreadyEncripted;

    @ColumnInfo(name = "keyr")
    private String keyr;

    public SortData() {
        itsAlreadyEncripted = false;
        isEncripted = false;
        isblock = false;
        isEresable = false;
    }

    public boolean isItsAlreadyEncripted() {
        return itsAlreadyEncripted;
    }

    public void setItsAlreadyEncripted(boolean itsAlreadyEncripted) {
        this.itsAlreadyEncripted = itsAlreadyEncripted;
    }

    public byte[] getImageFail() {
        return imageFail;
    }

    public void setImageFail(byte[] imageFail) {
        this.imageFail = imageFail;
    }

    public boolean isIsblock() {
        return isblock;
    }

    public void setIsblock(boolean isblock) {
        this.isblock = isblock;
    }

    public boolean isEncripted() {
        return isEncripted;
    }

    public void setEncripted(boolean encripted) {
        isEncripted = encripted;
    }

    public boolean isEresable() {
        return isEresable;
    }

    public void setEresable(boolean eresable) {
        isEresable = eresable;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeData() {
        return typeData;
    }

    public void setTypeData(String typeData) {
        this.typeData = typeData;
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageString() {
        return imageString;
    }

    public void setImageString(String imageString) {
        this.imageString = imageString;
    }

    public String getImageBase64String() {
        return imageBase64String;
    }

    public void setImageBase64String(String imageBase64String) {
        this.imageBase64String = imageBase64String;
    }

    public String getCodeUser() {
        return codeUser;
    }

    public void setCodeUser(String codeUser) {
        this.codeUser = codeUser;
    }

    public String getKeyr() {
        return keyr;
    }

    public void setKeyr(String keyr) {
        this.keyr = keyr;
    }
}