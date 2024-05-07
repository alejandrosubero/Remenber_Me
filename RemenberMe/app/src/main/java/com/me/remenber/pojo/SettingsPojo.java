package com.me.remenber.pojo;

import androidx.room.ColumnInfo;
import androidx.room.PrimaryKey;

import com.me.remenber.entitys.Settings;

import java.io.Serializable;

public class SettingsPojo implements Serializable {


    private int settingsId;
    private boolean ecryptNotes;
    private String codeUser;
    private boolean ecryptImage;
    private boolean endebleFakeImage;
    private boolean endebleBlock;
    private int numberOfAttemps;
    private boolean isErasableNote;
    private boolean isErasableImage;
    private boolean showAll;
    private boolean deletePictureAfterAdd;


    public SettingsPojo() {
        this.setSettingsDefaulPrameters();
    }

    public SettingsPojo(int settingsId, boolean ecryptNotes, String codeUser, boolean ecryptImage, boolean endebleFakeImage, boolean endebleBlock, int numberOfAttemps, boolean isErasableNote, boolean isErasableImage) {
        this.settingsId = settingsId;
        this.ecryptNotes = ecryptNotes;
        this.codeUser = codeUser;
        this.ecryptImage = ecryptImage;
        this.endebleFakeImage = endebleFakeImage;
        this.endebleBlock = endebleBlock;
        this.numberOfAttemps = numberOfAttemps;
        this.isErasableNote = isErasableNote;
        this.isErasableImage = isErasableImage;
        this.showAll = false;
        this.deletePictureAfterAdd =false;
    }

    public SettingsPojo(Settings settings) {
        this.settingsId = settings.getSettingsId();
        this.ecryptNotes = settings.isEcryptNotes();
        this.ecryptImage = settings.isEcryptImage();
        this.endebleFakeImage = settings.isEndebleFakeImage();
        this.endebleBlock = settings.isEndebleBlock();
        this.isErasableNote = settings.isErasableNote();
        this.isErasableImage = settings.isErasableImage();
        this.showAll = settings.isShowAll();
        this.deletePictureAfterAdd = settings.isDeletePictureAfterAdd();

        if (settings.getCodeUser() != null) {
            this.codeUser = settings.getCodeUser();
        } else {
            this.codeUser = "DEFAULT";
        }

        if (settings.getNumberOfAttemps() > 0) {
            this.numberOfAttemps = settings.getNumberOfAttemps();
        } else {
            this.numberOfAttemps = 5;
        }
    }

    private void setSettingsDefaulPrameters() {
        this.ecryptNotes = false;
        this.ecryptImage = false;
        this.endebleFakeImage = false;
        this.endebleBlock = false;
        this.isErasableNote = false;
        this.isErasableImage = false;
        this.showAll = false;
        this.numberOfAttemps = 5;
        this.codeUser = "DEFAULT";
        this.deletePictureAfterAdd = false;
    }


    public Settings convertSettingsPojoToSettings() {
        Settings newSettings = new Settings();

        if (this.getCodeUser() != null && !this.getCodeUser().equals("DEFAULT")) {

            newSettings.setSettingsId(this.getSettingsId());
            newSettings.setEcryptImage(this.isEcryptImage());
            newSettings.setEcryptNotes(this.isEcryptNotes());
            newSettings.setEndebleFakeImage(this.isEndebleFakeImage());
            newSettings.setEndebleBlock(this.isEndebleBlock());
            newSettings.setErasableNote(this.isErasableNote());
            newSettings.setErasableImage(this.isErasableImage());
            newSettings.setCodeUser(this.getCodeUser());
            newSettings.setShowAll(this.showAll);
            newSettings.setNumberOfAttemps(this.numberOfAttemps);
            newSettings.setDeletePictureAfterAdd(this.deletePictureAfterAdd);
        }
        return newSettings;
    }

    public SettingsPojo setSettingPojo(Settings settings) {
        SettingsPojo newSetting = new SettingsPojo();

        if (settings.getSettingsId() > 0){
            newSetting.setSettingsId(settings.getSettingsId());
        }

        newSetting.setEcryptNotes(settings.isEcryptNotes());
        newSetting.setEcryptImage(settings.isEcryptImage());
        newSetting.setEndebleFakeImage(settings.isEndebleFakeImage());
        newSetting.setEndebleBlock(settings.isEndebleBlock());
        newSetting.setErasableNote(settings.isErasableNote());
        newSetting.setErasableImage(settings.isErasableImage());
        newSetting.setShowAll(settings.isShowAll());
        newSetting.setDeletePictureAfterAdd(settings.isDeletePictureAfterAdd());

        if (settings.getCodeUser() != null) {
            newSetting.setCodeUser(settings.getCodeUser());
        } else {
            newSetting.setCodeUser("DEFAULT");
        }

        if (settings.getNumberOfAttemps() > 0) {
            newSetting.setNumberOfAttemps(settings.getNumberOfAttemps());
        } else {
            newSetting.setNumberOfAttemps(5);
        }
        return newSetting;
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
