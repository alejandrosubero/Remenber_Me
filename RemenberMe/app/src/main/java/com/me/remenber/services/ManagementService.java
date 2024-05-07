package com.me.remenber.services;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.me.remenber.dataModel.DataConverter;
import com.me.remenber.entitys.SortData;
import com.me.remenber.entitys.User;
import com.me.remenber.pojo.SettingsPojo;
import com.me.remenber.security.Cryptography;

import java.util.ArrayList;
import java.util.List;

public class ManagementService extends AppCompatActivity {

    private static ManagementService instance;
    private SortData sortData;
    private User user;
    private SettingsPojo settingsUser;

    private String serverResponse;

    private ManagementService() {
    }


    public static ManagementService getInstance() {
        if (instance == null) {
            instance = new ManagementService();
            Log.d("******** Actiones instance is create ..... ===> ", "...");
        } else {
            Log.d("******** Actiones instance is exist ..... ===> ", "...");
        }
        return instance;
    }

    public void setSortData(SortData newSortData) {
        this.sortData = newSortData;
        if (this.sortData != null) {
            Log.d("******** Actiones sortData is set..... ===> ", "...");
        }
    }

    public boolean setSortDataString(String newSortData) {
        Gson gson = new Gson();
        this.sortData = gson.fromJson(newSortData, SortData.class);
        Log.d("******** Actiones sortData is set..... ===> ", "...");
        return (this.sortData != null);
    }


    public boolean deleteSortData(Context context) {
        FragmentService fragmentService = new FragmentService(context);
        fragmentService.deleteData(this.sortData);
        return true;
    }


    public boolean insertSortData(Context context) {
        FragmentService fragmentService = new FragmentService(context);
        fragmentService.saveData(this.sortData);
        return true;
    }


    public boolean updateSortData(Context context) {
        FragmentService fragmentService = new FragmentService(context);
        fragmentService.updateData(this.sortData);
        return true;
    }

    /***
     *
     * @param key
     * @param context
     * @return
     */
    public List<SortData> findByKey(String key, Context context){
        String userCode = key;
        FragmentService fragmentService = new FragmentService(context);
        return fragmentService.findByKey(key);
    }

    /**
     *
     * @param type
     * @param context
     * @return list of SortData from specific TYPE..
     */
    public List<SortData> getData(String type, Context context) {
        String userCode = this.user.getCodeUser();
        FragmentService fragmentService = new FragmentService(context);
        return fragmentService.getdata(userCode, type);
    }

    /**
     *
     * @param type
     * @param context
     * @param traerBlock
     * @return list of SortData from specific type but are block o hide...
     */
    public List<SortData> getData2(String type, Context context, boolean traerBlock) {
        List<SortData> listData = new ArrayList<>();
        String userCode = this.user.getCodeUser();
        FragmentService fragmentService = new FragmentService(context);

        if (settingsUser.isEndebleBlock() && traerBlock) {
            listData = fragmentService.getdata2(userCode, type, true);
        } else {
            listData = fragmentService.getdata2(userCode, type, false);
        }
        return listData;
    }

    /**
     *
     * @param context
     * @return list of SortData already decrypted...
     */
    public List<SortData> getAllData(Context context) {
        String userCode = this.user.getCodeUser();
        FragmentService fragmentService = new FragmentService(context);
        List<SortData> getAllDataTemp = fragmentService.getAllData(userCode);
        List<SortData> getAllData = workIndecryptData(getAllDataTemp);
        return getAllData;
    }

    public void deleteActionSortData(List<User> list, Context context) {
        FragmentService fragmentService = new FragmentService(context);
        list.stream().forEach(user -> {
            List<SortData> allData = fragmentService.getAllData(user.getCodeUser());
            allData.stream().forEach(sortData -> fragmentService.deleteData(sortData));
        });
    }


    public void resetManagementService() {
        this.sortData = null;
        this.user = null;
        this.settingsUser = null;
    }

    private List<SortData> workIndecryptData(List<SortData> listaTemp) {
        List<SortData> lista = new ArrayList<SortData>();
        String key = this.user.getKeyPrimary().trim();
        Cryptography cryptography = new Cryptography(key);

        listaTemp.stream().forEach(sortDataTemp -> {
            if (sortDataTemp.isEncripted() && sortDataTemp.isItsAlreadyEncripted()) {
                if (sortDataTemp.getTypeData().equals("Image")) {
                    String imagenEncryp = cryptography.decryptAES(sortDataTemp.getImageString());
                    Bitmap imageBitmap = DataConverter.stringToBitMap(imagenEncryp);
                    byte[] image = DataConverter.convertImageToArray(imageBitmap);
                    sortDataTemp.setImage(image);
                    lista.add(sortDataTemp);
                } else {
                    String noteEncryp = cryptography.decryptAES(sortDataTemp.getNote());
                    sortDataTemp.setNote(noteEncryp);
                    lista.add(sortDataTemp);
                }
            } else {
                lista.add(sortDataTemp);
            }
        });
        return lista;
    }


    public SortData getSortData() {
        return sortData;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SettingsPojo getSettingsUser() {
        return settingsUser;
    }

    public void setSettingsUser(SettingsPojo settingsUser) {
        this.settingsUser = settingsUser;
    }

    public String getServerResponse() {
        return serverResponse;
    }

    public void setServerResponse(String serverResponse) {
        this.serverResponse = serverResponse;
    }

    public boolean checkServerResponse() {
        if (this.serverResponse != null && !this.serverResponse.equals("")) {
            return true;
        } else {
            return false;
        }
    }
}

