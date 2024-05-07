package com.me.remenber.services;

import android.content.Context;
import android.os.Bundle;

import com.me.remenber.dao.SortDataDao;
import com.me.remenber.entitys.SortData;
import com.me.remenber.repositorys.SortDataDataBase;

import java.util.ArrayList;
import java.util.List;

public class FragmentService {

    private Context context;


    public FragmentService() {
    }

    public FragmentService(Context context) {
        this.context = context;
    }


    public Bundle codeBundle(String typeData, String nameFragmet, String info){
         Bundle result = new Bundle();
         StringBuilder key = new StringBuilder(typeData);
         key.append(nameFragmet);
         String keyFragment = key.toString();
         result.putString(keyFragment, info);
         return result;
     }
    public List<SortData> getAllData(String userCode){
        List<SortData> lista = new ArrayList<>();
        SortDataDao sortDataDao = SortDataDataBase.getDBIstance(context).sortDataDao();
        lista = sortDataDao.findByCode(userCode);
        return lista;
    }

    public List<SortData> findByKey(String key){
        List<SortData> lista = new ArrayList<>();
        SortDataDao sortDataDao = SortDataDataBase.getDBIstance(context).sortDataDao();
        lista = sortDataDao.findByCode3(key);
        return lista;
    }

     public List<SortData> getdata(String userCode, String type){
         List<SortData> lista = new ArrayList<>();
         SortDataDao sortDataDao = SortDataDataBase.getDBIstance(context).sortDataDao();
         lista = sortDataDao.findBySearch(userCode, type);
         return lista;
     }

     // use for see data block and no block ...
    public List<SortData> getdata2(String userCode, String type, boolean block){
        List<SortData> lista = new ArrayList<>();
        SortDataDao sortDataDao = SortDataDataBase.getDBIstance(context).sortDataDao();
        lista = sortDataDao.findBySearch2(userCode, type,block);
        return lista;
    }

    public Bundle userCodeBundle(String nameFramet, String userCode){
        Bundle result = new Bundle();
        StringBuilder key = new StringBuilder("UserCode");
        key.append(nameFramet);
        String keyFragment = key.toString();
        result.putString(keyFragment, userCode);
        return result;
    }



    public void deleteData(SortData data){
        SortDataDao sortDataDao = SortDataDataBase.getDBIstance(context).sortDataDao();
        sortDataDao.delete(data);
    }


    public  void updateData(SortData data){
        SortDataDao sortDataDao = SortDataDataBase.getDBIstance(context).sortDataDao();
        sortDataDao.update(data);
    }


    public  void saveData(SortData data){
        SortDataDao sortDataDao = SortDataDataBase.getDBIstance(context).sortDataDao();
        sortDataDao.insert(data);
    }


}
