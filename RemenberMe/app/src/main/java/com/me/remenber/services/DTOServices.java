package com.me.remenber.services;

import android.util.Log;

import com.google.gson.Gson;
import com.me.remenber.dto.BackupDTO;
import com.me.remenber.entitys.SortData;
import com.me.remenber.entitys.User;
import com.me.remenber.mapper.MapperTrasferData;
import com.me.remenber.security.Cryptography;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DTOServices {

    private BackupDTO backupDTO;
    private MapperTrasferData mapperTrasferData;
    private User user;

    public DTOServices() {
        this.mapperTrasferData = new MapperTrasferData();
    }

    public DTOServices(User user) {
        this.mapperTrasferData = new MapperTrasferData();
        this.user = user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public JSONObject generateDTO(SortData sortData) {
        BackupDTO backupDTO = mapperTrasferData.mapperEntityToDTO(sortData);
        if (this.user != null) {
            backupDTO.setCheese(this.user.getKeyPrimary());
        }
        JSONObject generateDTO = this.dtoToString(backupDTO);
        return generateDTO;
    }

    public String generateDTONoKey(SortData sortData) {
        BackupDTO backupDTO = mapperTrasferData.mapperEntityToDTO(sortData);
        if (this.user != null) {
            backupDTO.setCheese(this.user.getKeyPrimary());
        }
        return this.dtoToString2(backupDTO);
    }

    private String dtoToString2(BackupDTO backupDTO) {
        Gson gson = new Gson();
        String data = null;
        backupDTO.backupDTOEncrypt();
        String key = this.generateKey();
        Cryptography cryptography = new Cryptography(key);
        String decoKey = cryptography.encryptAES(backupDTO.getCheese());
        backupDTO.setCheese(decoKey);
        String dto = gson.toJson(backupDTO);
        data = dto;
        return data;
    }

    public JSONObject StringJSONObject(String value) {
        JSONObject data = null;

        try {
            data = new JSONObject(value);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        return data;
    }

    public JSONObject dtoToString(BackupDTO backupDTO) {
        Gson gson = new Gson();
        JSONObject data = null;
        backupDTO.backupDTOEncrypt();
        String key = this.generateKey();
        Cryptography cryptography = new Cryptography(key);
        String decoKey = cryptography.encryptAES(backupDTO.getCheese());
        backupDTO.setCheese(decoKey);
        String dto = gson.toJson(backupDTO);
        if(dto != null){
            try {
                data = new JSONObject(dto);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return data;
    }

    private String generateKey() {
        boolean redFlat = false;
        int decimal = 16;
        String base = user.getUserMail().trim();
        char[] chars = base.toCharArray();
        int starLimit = base.length();
        StringBuilder key = new StringBuilder();
        if (starLimit < decimal) {
            redFlat = true;
        }
        for (int i = 0; i < decimal; i++) {
            if (redFlat && i > starLimit) {
                key.append("o");
            } else {
                key.append(chars[i]);
            }
        }
        return key.toString();
    }


    public  List<SortData> getData(String sdto){
        List<BackupDTO> listBackupDTO = this.stringToDTO(sdto);
        List<SortData> tempSortData = new ArrayList<>();
        listBackupDTO.stream().forEach(backupDTO1 -> {
            SortData sortDataTemp = mapperTrasferData.mapperDTOToEntity(backupDTO1);
            sortDataTemp.setCodeUser(user.getCodeUser());
            tempSortData.add(sortDataTemp);
        });
        return  tempSortData;
    }

    private List<BackupDTO> stringToDTO(String sdto) {
        List<BackupDTO> tempList = new ArrayList<>();
        Gson gson = new Gson();
        String key = this.generateKey();
        Cryptography cryptography = new Cryptography(key);

        try {
            JSONObject object = new JSONObject(sdto);
            JSONArray array = object.names();

            for(int n = 0; n < array.length(); n++) {
                String name = (String) array.get(n);
                String cosa =  object.get(name).toString();
                BackupDTO backupDTO = gson.fromJson(cosa,  BackupDTO.class);

                if(backupDTO != null){
                    if(backupDTO.getCheese() != null) {
                        String decoKey = cryptography.decryptAES(backupDTO.getCheese());
                        BackupDTO backupDTO2 = new BackupDTO();
                        backupDTO2 = backupDTO;
                        backupDTO2.setCheese(decoKey);
                        if (backupDTO.getUnbral() == null) {
                            Cryptography crypto = new Cryptography(backupDTO.getCheese());
                            String keyServer = crypto.encryptAES(name);
                            backupDTO2.setUnbral(keyServer);
                        }
                        backupDTO2.backupDTODencrypt();
                        tempList.add(backupDTO2);
                    }
                }
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return tempList;
    }
}
