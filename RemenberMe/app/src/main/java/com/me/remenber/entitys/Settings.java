package com.me.remenber.entitys;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Settings")
public class Settings implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int settingsId;

    @ColumnInfo(name = "ecryptNotes")
    private boolean ecryptNotes;

    @ColumnInfo(name = "codeUser")
    private String codeUser;

    @ColumnInfo(name = "ecryptImage")
    private boolean ecryptImage;

    @ColumnInfo(name = "endebleFakeImage")
    private boolean endebleFakeImage;

    @ColumnInfo(name = "endebleBlock")
    private boolean endebleBlock;

    @ColumnInfo(name = "numberOfAttemps")
    private int numberOfAttemps;

    @ColumnInfo(name = "isErasableNote")
    private boolean isErasableNote;

    @ColumnInfo(name = "isErasableImage")
    private boolean isErasableImage;

    @ColumnInfo(name = "showAll")
    private boolean showAll;

    @ColumnInfo(name = "deletePictureAfterAdd")
    private boolean deletePictureAfterAdd;

    public Settings() {
        this.ecryptNotes = false;
        this.ecryptImage = false;
        this.endebleFakeImage = false;
        this.endebleBlock = false;
        this.isErasableNote = false;
        this.isErasableImage = false;
        this.showAll = false;
        this.numberOfAttemps = 5;
        this.deletePictureAfterAdd =false;
    }


    public Settings( String codeUser, int numberOfAttemps) {
        this.ecryptNotes = false;
        this.ecryptImage = false;
        this.endebleFakeImage = false;
        this.endebleBlock = false;
        this.isErasableNote = false;
        this.isErasableImage = false;
        this.showAll = false;
        this.numberOfAttemps = numberOfAttemps;
        this.codeUser = codeUser;
        this.deletePictureAfterAdd =false;
    }

    public int getSettingsId() {
        return settingsId;
    }

    public void setSettingsId(int settingsId) {
        this.settingsId = settingsId;
    }

    public boolean isEcryptNotes() {
        return ecryptNotes;
    }

    public void setEcryptNotes(boolean ecryptNotes) {
        this.ecryptNotes = ecryptNotes;
    }

    public String getCodeUser() {
        return codeUser;
    }

    public void setCodeUser(String codeUser) {
        this.codeUser = codeUser;
    }

    public boolean isEcryptImage() {
        return ecryptImage;
    }

    public void setEcryptImage(boolean ecryptImage) {
        this.ecryptImage = ecryptImage;
    }

    public boolean isEndebleFakeImage() {
        return endebleFakeImage;
    }

    public void setEndebleFakeImage(boolean endebleFakeImage) {
        this.endebleFakeImage = endebleFakeImage;
    }

    public boolean isEndebleBlock() {
        return endebleBlock;
    }

    public void setEndebleBlock(boolean endebleBlock) {
        this.endebleBlock = endebleBlock;
    }

    public int getNumberOfAttemps() {
        return numberOfAttemps;
    }

    public void setNumberOfAttemps(int numberOfAttemps) {
        this.numberOfAttemps = numberOfAttemps;
    }

    public boolean isErasableNote() {
        return isErasableNote;
    }

    public void setErasableNote(boolean erasableNote) {
        isErasableNote = erasableNote;
    }

    public boolean isErasableImage() {
        return isErasableImage;
    }

    public void setErasableImage(boolean erasableImage) {
        isErasableImage = erasableImage;
    }

    public boolean isShowAll() {
        return showAll;
    }

    public void setShowAll(boolean showAll) {
        this.showAll = showAll;
    }

    public boolean isDeletePictureAfterAdd() {
        return deletePictureAfterAdd;
    }

    public void setDeletePictureAfterAdd(boolean deletePictureAfterAdd) {
        this.deletePictureAfterAdd = deletePictureAfterAdd;
    }
}
