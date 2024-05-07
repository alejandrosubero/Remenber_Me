package com.me.remenber.services;

import android.content.Context;
import com.me.remenber.dao.SettingDao;
import com.me.remenber.entitys.Settings;
import com.me.remenber.pojo.SettingsPojo;
import com.me.remenber.repositorys.SettingDataBase;
import com.me.remenber.security.EncryptAES;

import java.util.List;

public class SettingsService {

    private SettingsPojo pojo;
    private Settings entity;
    private Context context;
    private EncryptAES encryptAES;

    public SettingsService(Context context) {
        this.context = context;
    }

    public SettingsPojo startedSetting(String userCode) {
        SettingDao settingDao = SettingDataBase.getDBIstance(context).settingDao();
        List<Settings> entitys = settingDao.findByCode(userCode);
        if (entitys.size() > 0) {
            SettingsPojo pojo = new SettingsPojo(entitys.get(0));
            return pojo;
        }
        return new SettingsPojo();
    }

    public void saveSetting(Settings newSetting) {
        SettingDao settingDao = SettingDataBase.getDBIstance(context).settingDao();
        List<Settings> entitys = settingDao.findByCode(newSetting.getCodeUser());
        if (entitys.size() > 0) {
            settingDao.update(newSetting);
        }else {
            settingDao.insert(newSetting);
        }
    }

    public Settings resetSetting(Settings setting){

        if( setting.getCodeUser() != null && !setting.getCodeUser().equals("DEFAULT")){
            SettingDao settingDao = SettingDataBase.getDBIstance(context).settingDao();
            Settings newSetting = new Settings();
            newSetting.setSettingsId(setting.getSettingsId());
            newSetting.setCodeUser(setting.getCodeUser());
            settingDao.update(newSetting);
            return newSetting;
        }
        return setting;
    }

    public void deleteSetting(Settings newSetting) {
        SettingDao settingDao = SettingDataBase.getDBIstance(context).settingDao();
        List<Settings> entitys = settingDao.findByCode(newSetting.getCodeUser());
        if (entitys.size() > 0) {
            settingDao.delete(newSetting);
        }
    }



}
