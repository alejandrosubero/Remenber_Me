package com.me.remenber.entitys;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@Entity(tableName = "User")
public class User implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int userId;

    @ColumnInfo(name = "userName")
    private String name;

    @ColumnInfo(name = "userMail")
    private String userMail;

    @ColumnInfo(name = "userQuestion")
    private String userQuestion;

    @ColumnInfo(name = "userResponse")
    private String userResponse;

    @ColumnInfo(name = "userPass")
    private String userPass;

    @ColumnInfo(name = "keyPrimary")
    private String keyPrimary;

    @ColumnInfo(name = "keyShare")
    private String keyShare;
    @ColumnInfo(name = "codeUser")
    private String codeUser;

    @ColumnInfo(name = "codeUserByte")
    private  byte[] codeUserByte;

    @ColumnInfo(name = "deletePassword")
    private String deletePassword;

    @ColumnInfo(name = "backUpUrl")
    private String backUpUrl;

    @ColumnInfo(name = "backUpUrl2")
    private String backUpUrl2;

    public User() {
    }

    public User(String name, String userMail, String userQuestion, String userResponse, String userPass, String keyPrimary, String keyShare) {
        this.name = name;
        this.userMail = userMail;
        this.userQuestion = userQuestion;
        this.userResponse = userResponse;
        this.userPass = userPass;
        this.keyPrimary = keyPrimary;
        this.keyShare = keyShare;
    }

    public User(String name, String userMail, String userQuestion, String userResponse, String userPass, String keyPrimary, String keyShare, String deletePassword) {
        this.name = name;
        this.userMail = userMail;
        this.userQuestion = userQuestion;
        this.userResponse = userResponse;
        this.userPass = userPass;
        this.keyPrimary = keyPrimary;
        this.keyShare = keyShare;
        this.deletePassword = deletePassword;
    }


    public String getDeletePassword() {
        return deletePassword;
    }

    public void setDeletePassword(String deletePassword) {
        this.deletePassword = deletePassword;
    }

    public byte[] getCodeUserByte() {
        return codeUserByte;
    }

    public void setCodeUserByte(byte[] codeUserByte) {
        this.codeUserByte = codeUserByte;
    }

    public String getCodeUser() {
        return codeUser;
    }

    public void setCodeUser(String codeUser) {
        this.codeUser = codeUser;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getUserQuestion() {
        return userQuestion;
    }

    public void setUserQuestion(String userQuestion) {
        this.userQuestion = userQuestion;
    }

    public String getUserResponse() {
        return userResponse;
    }

    public void setUserResponse(String userResponse) {
        this.userResponse = userResponse;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getKeyPrimary() {
        return keyPrimary;
    }

    public void setKeyPrimary(String keyPrimary) {
        this.keyPrimary = keyPrimary;
    }

    public String getKeyShare() {
        return keyShare;
    }

    public void setKeyShare(String keyShare) {
        this.keyShare = keyShare;
    }

    public String getBackUpUrl() {
        return backUpUrl;
    }

    public void setBackUpUrl(String backUpUrl) {
        this.backUpUrl = backUpUrl;
    }

    public String getBackUpUrl2() {
        return backUpUrl2;
    }

    public void setBackUpUrl2(String backUpUrl2) {
        this.backUpUrl2 = backUpUrl2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId && Objects.equals(name, user.name) && Objects.equals(userMail, user.userMail) && Objects.equals(userQuestion, user.userQuestion) && Objects.equals(userResponse, user.userResponse) && Objects.equals(userPass, user.userPass) && Objects.equals(keyPrimary, user.keyPrimary) && Objects.equals(keyShare, user.keyShare) && Objects.equals(codeUser, user.codeUser) && Arrays.equals(codeUserByte, user.codeUserByte) && Objects.equals(deletePassword, user.deletePassword) && Objects.equals(backUpUrl, user.backUpUrl) && Objects.equals(backUpUrl2, user.backUpUrl2);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(userId, name, userMail, userQuestion, userResponse, userPass, keyPrimary, keyShare, codeUser, deletePassword, backUpUrl, backUpUrl2);
        result = 31 * result + Arrays.hashCode(codeUserByte);
        return result;
    }
}
