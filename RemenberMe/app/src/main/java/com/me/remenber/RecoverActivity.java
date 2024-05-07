package com.me.remenber;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.google.gson.Gson;
import com.me.remenber.entitys.User;
import com.me.remenber.fragments.user.RecoverNewUserDataFragment;
import com.me.remenber.fragments.user.RecoverUserFragment;
import com.me.remenber.pojo.SettingsPojo;
import com.me.remenber.services.ManagementService;

public class RecoverActivity extends AppCompatActivity {

    private boolean permissionGranted;
    private int REQUEST_CODE = 200;

    private Context context;

    private String type;
    private ManagementService managementService;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover);
        this.verificarPermisos();
        managementService = ManagementService.getInstance();
        user = managementService.getUser();
        Intent intent = getIntent();
        type = (String) intent.getSerializableExtra("objectType");
        this.startFramet();
    }

    private  void startFramet(){
        if(type == null){
            RecoverUserFragment recoverUserFragment =  new RecoverUserFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.frame_recover, recoverUserFragment).commit();
        }else{
            RecoverNewUserDataFragment fragment = RecoverNewUserDataFragment.newInstance(userString(user), type);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_recover, fragment).addToBackStack(null).commit();
        }
    }

    private String userString(User user){
        Gson gson = new Gson();
        String userSend = gson.toJson(user);
        return userSend;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void verificarPermisos() {
        int permissionWRITE_EXTERNAL = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE);
        int permissionREAD_EXTERNAL = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permissionWRITE_EXTERNAL == PackageManager.PERMISSION_GRANTED && permissionREAD_EXTERNAL == PackageManager.PERMISSION_GRANTED) {
            permissionGranted = true;
        } else {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
        }
    }
}