package com.me.remenber.services;

import android.content.Context;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.me.remenber.R;
import com.me.remenber.dao.UserDao;
import com.me.remenber.entitys.User;
import com.me.remenber.fragments.LoginFragment;
import com.me.remenber.repositorys.UserDataBase;
import com.me.remenber.security.Cryptography;
import com.me.remenber.security.EncryptAES;

import java.util.List;

public class UserService {

    private User user;
    private Context context;
    private EncryptAES encryptAES;
    public static final String RESET_PASS = "resetPass";
    public static final String RESET_USER_NAME = "resetUserName";
    public static final String RESET_QUESTION = "resetQuestion";
    public static final String RESET_ANSWER = "resetAnswer";
    public static final String RESET_SHARE_PASS = "resetSharePass";
    public static final String  URL_BACKUP = "urlForBackup";

    public static final String GET_BACKUP = "getBackup";
    public static final String  GENERATE_BACKUP = "generateBackup";


    public UserService(Context context) {
        this.context = context;
    }

    public User findByMail(String email) {
        UserDao userRepoDao = UserDataBase.getDBIstance(context).userDao();
        List<User> list = userRepoDao.findByMail(email);
        if (list.size() > 0){
            return list.get(0);
        }else{
            return new User();
        }
    }

    public  List<User> findByCode(String code) {
        UserDao userRepoDao = UserDataBase.getDBIstance(context).userDao();
        List<User> list = userRepoDao.findByCode(code);
        return list;
    }

    public  List<User> findByDeletePassword(String pass) {
        UserDao userRepoDao = UserDataBase.getDBIstance(context).userDao();
        List<User> list = userRepoDao.findByDeletePassword(pass);
        return list;
    }

    public User findBYName(String name){
        UserDao userRepoDao = UserDataBase.getDBIstance(context).userDao();
        List<User> list = userRepoDao.findByName(name);
        return list.get(0);
    }

    public  List<User> findBYNameLis(String name){
        UserDao userRepoDao = UserDataBase.getDBIstance(context).userDao();
        List<User> list = userRepoDao.findByName(name);
        return list;
    }

    public boolean updateUser(User userRecibe) {
        try {
            UserDao userRepoDao = UserDataBase.getDBIstance(context).userDao();
            userRepoDao.update(userRecibe);
            User testUser = findByMail(userRecibe.getUserMail());
            if(testUser.getUserPass().equals(userRecibe.getUserPass())){
                return true;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void deleteAction(List<User> listIsDelete){
        if(listIsDelete.size() > 0){
            UserDao userRepoDao = UserDataBase.getDBIstance(context).userDao();
            listIsDelete.stream().forEach(user ->{
                userRepoDao.delete(user);
            });
        }
    }

    public boolean saveUser(User userRecibe) {
        try {
            UserDao userRepoDao = UserDataBase.getDBIstance(context).userDao();
            userRepoDao.insert(userRecibe);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkUserMail(String email) {
        UserDao userRepoDao = UserDataBase.getDBIstance(context).userDao();
        List<User> list = userRepoDao.findByMail(email);
        if (list.size() > 0) {
            return false;
        } else {
            return true;
        }
    }

    public User restore(User userBase) {
        User userNew = new User();

        userNew.setUserId(userBase.getUserId());

        if(userBase.getBackUpUrl()!=null){
            userNew.setBackUpUrl(userBase.getBackUpUrl());
        }

        if(userBase.getBackUpUrl2()!=null){
            userNew.setBackUpUrl2(userBase.getBackUpUrl2());
        }

        if(userBase.getCodeUserByte()!=null){
            userNew.setCodeUserByte(userBase.getCodeUserByte());
        }

        if (userBase.getName() != null) {
            userNew.setName(userBase.getName());
        }
        if (userBase.getUserMail() != null) {
            String data = EncryptAES.decryptAES(userBase.getUserMail());
            userNew.setUserMail(data.trim());
        }
        if (userBase.getUserQuestion() != null) {
            String data = EncryptAES.decryptAES(userBase.getUserQuestion());
            userNew.setUserQuestion(data.trim());
        }
        if (userBase.getUserResponse() != null) {
            String data = EncryptAES.decryptAES(userBase.getUserResponse());
            userNew.setUserResponse(data.trim());
        }
        if (userBase.getUserPass() != null) {
            String data = EncryptAES.decryptAES(userBase.getUserPass());
            userNew.setUserPass(data.trim());
        }
        if (userBase.getKeyPrimary() != null) {
            String data = EncryptAES.decryptAES(userBase.getKeyPrimary());
            userNew.setKeyPrimary(data.trim());
        }
        if (userBase.getKeyShare() != null) {
            String data = EncryptAES.decryptAES(userBase.getKeyShare());
            userNew.setKeyShare(data.trim());
        }
        if(userBase.getDeletePassword() != null){
            String delete1 = EncryptAES.decryptAES(userBase.getDeletePassword());
            userNew.setDeletePassword(delete1.trim());
        }
        if (userBase.getCodeUser() != null) {
            try {
                String key = userNew.getKeyPrimary().trim();
                String codeUser = userBase.getCodeUser();
                Cryptography cryptography = new Cryptography(key);
                String code = cryptography.decryptAES(codeUser);
                userNew.setCodeUser(code.trim());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return userNew;
    }

    public User setUserForSave(User userBase) {
        User userNew = new User();
        String keyShareT = null;
        String keyPrimaryT = null;
        userNew = userBase;

        if(userBase.getUserId()>0){
            userNew.setUserId(userBase.getUserId());
        }
        if(userBase.getBackUpUrl()!=null){
            userNew.setBackUpUrl(userBase.getBackUpUrl());
        }
        if(userBase.getBackUpUrl2()!=null){
            userNew.setBackUpUrl2(userBase.getBackUpUrl2());
        }

        if(userBase.getCodeUserByte()!=null){
            userNew.setCodeUserByte(userBase.getCodeUserByte());
        }

        if (userBase.getName() != null) {
            userNew.setName(userBase.getName());
        }
        if (userBase.getUserMail() != null) {
            String data = userBase.getUserMail();
            String mail1 = EncryptAES.encryptAES(data);
            userNew.setUserMail(mail1);
        }
        if (userBase.getUserQuestion() != null) {
            String data = userBase.getUserQuestion();
            String mail1 = EncryptAES.encryptAES(data);
            userNew.setUserQuestion(mail1);
        }
        if (userBase.getUserResponse() != null) {
            String data = userBase.getUserResponse();
            String mail1 = EncryptAES.encryptAES(data);
            userNew.setUserResponse(mail1);
        }
        if (userBase.getUserPass() != null) {
            String data = userBase.getUserPass();
            String mail1 = EncryptAES.encryptAES(data);
            userNew.setUserPass(mail1);
        }
        if (userBase.getKeyPrimary() != null) {
            keyPrimaryT = userBase.getKeyPrimary();
            String mail1 = EncryptAES.encryptAES(keyPrimaryT);
            userNew.setKeyPrimary(mail1);
        }
        if (userBase.getKeyShare() != null) {
            keyShareT = userBase.getKeyShare();
            String mail1 = EncryptAES.encryptAES(keyShareT);
            userNew.setKeyShare(mail1);
        }

        if( userBase.getDeletePassword() != null){
            String deleteKeyT =  userBase.getDeletePassword();
            String delete1 = EncryptAES.encryptAES(deleteKeyT);
            userNew.setDeletePassword(delete1);
        }

        if (keyPrimaryT != null && keyShareT != null) {
            String code = userBase.getCodeUser();
            try {
                String pass = keyPrimaryT.trim();
                Cryptography cryptography = new Cryptography(pass);
                String mail1 = cryptography.encryptAES(code);
                userNew.setCodeUser(mail1);
            } catch (Exception e) {
                e.printStackTrace();
                String codeFinal = null;
            }
        }

        return userNew;
    }


}
