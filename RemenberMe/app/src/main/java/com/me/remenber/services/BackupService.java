package com.me.remenber.services;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.gson.Gson;
import com.me.remenber.dataModel.DataConverter;
import com.me.remenber.entitys.SortData;
import com.me.remenber.entitys.User;
import com.me.remenber.pojo.JSONResponse;
import com.me.remenber.pojo.SettingsPojo;
import com.me.remenber.security.Cryptography;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BackupService {

    private Context context;
    private List<JSONObject> listNoKey = new ArrayList<>();
    private HashMap<String, JSONObject> hashMapSend = new HashMap<>();
    private ManagementService managementService;
    private DTOServices dtoServices;
    private User user;
    private SortData sortData;
    private int countReload = 0;
    private int countReloadServer = 0;
    Gson gson ;

    private SettingsPojo settings;
    private UserService userService;


    public BackupService(Context context) {
        this.context = context;
        this.startInstance();
    }

    private void startInstance() {
        countReload++;
        managementService = ManagementService.getInstance();
        gson = new Gson();
        if (managementService != null) {
            user = managementService.getUser();
            if (user != null) {
                dtoServices = new DTOServices(user);
            } else {
                if (countReload < 3) {
                    this.startInstance();
                }
            }
        }
    }

    private  List<SortData> workImagenEncrypted(List<SortData> listaTemp) {

        List<SortData> listaImagens = new ArrayList<SortData>();

        if(listaTemp != null && listaTemp.size()>0){

            User user = managementService.getUser();
            String key = user.getKeyPrimary().trim();
            Cryptography cryptography = new Cryptography(key);

            List<SortData> finalListaImagens = listaImagens;
            listaTemp.stream().forEach(sortDataTemp -> {
                if (sortDataTemp.isEncripted() && sortDataTemp.isItsAlreadyEncripted()) {
                    String imagenEncryp = cryptography.decryptAES(sortDataTemp.getImageString());
                    Bitmap imageBitmap = DataConverter.stringToBitMap(imagenEncryp);
                    byte[] image = DataConverter.convertImageToArray(imageBitmap);
                    sortDataTemp.setImage(image);
                    finalListaImagens.add(sortDataTemp);
                } else {
                    finalListaImagens.add(sortDataTemp);
                }
            });
            return finalListaImagens;
        }
       return listaImagens;

    }


    private void generateDTOList() {
        if(user.getBackUpUrl() != null && user.getBackUpUrl().length() >2){
            List<SortData> listaTempAll = managementService.getAllData(context);
            List<SortData> listaTempAll2 = workImagenEncrypted(listaTempAll);

            listaTempAll2.stream().forEach(sortData1 -> {
                StringBuilder builderUrl = new StringBuilder(user.getBackUpUrl());
                builderUrl.append(user.getName());
                String url = builderUrl.toString();
                user.setBackUpUrl2(url);
                JSONPostRequest request = new JSONPostRequest(sortData1, user, context);
                request.execute();
            });

//            for (SortData sortData1: listaTempAll2) {
//                StringBuilder builderUrl = new StringBuilder(user.getBackUpUrl());
//                builderUrl.append(user.getName());
//                String url = builderUrl.toString();
//                user.setBackUpUrl2(url);
//                JSONPostRequest request = new JSONPostRequest(sortData1, user, context);
//                request.execute();
//            }
        }

    }


    public void generateBackup() {
//        this.generateDTOList();
        Thread thread = new Thread() {
            public void run() {
                generateDTOList();
            }
        };
        thread.start();
    }


    public void getBackup() {
        if(user.getBackUpUrl() != null && user.getBackUpUrl().length() >2){
            StringBuilder builderUrl = new StringBuilder(user.getBackUpUrl());
            builderUrl.append(user.getName());
            builderUrl.append(".json");
            String url = builderUrl.toString();
            user.setBackUpUrl2(url);
            SendRequestGet sentest = (SendRequestGet) new SendRequestGet(context, dtoServices);
            sentest.execute(user.getBackUpUrl2());
        }


    }

    private boolean getServerResponse(SendRequestGet sentest){
        if(sentest.checkServerResponse()){
            String response = sentest.getServerResponse();
          List <SortData> listSortData = dtoServices.getData(response);
            if (listSortData != null && listSortData.size()>0) {
                listSortData.stream().forEach(sortData1 -> {
                    managementService.setSortData(sortData1);
                    managementService.insertSortData(context);
                    Log.e("TAG", "save the id into sortData from JSON POST: ");
                });
            }
        }
    return false;
    }

    public boolean getServerResponses(String text){

        if(text != null && managementService.checkServerResponse()){
            String response = managementService.getServerResponse();
            List <SortData> listSortData = dtoServices.getData(response);
            if (listSortData != null && listSortData.size()>0) {
                listSortData.stream().forEach(sortData1 -> {
                    managementService.setSortData(sortData1);
                    managementService.insertSortData(context);
                    Log.e("TAG", "save the id into sortData from JSON POST: ");
                });
            }
        }else {
            if(countReloadServer < 7){
                getServerResponses(text);
            }
            countReloadServer++;
        }
        return false;
    }
}
