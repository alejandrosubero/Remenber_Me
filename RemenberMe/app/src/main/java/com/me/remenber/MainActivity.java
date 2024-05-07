package com.me.remenber;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.me.remenber.fragments.ViewStartedFragment;
import com.me.remenber.security.Cryptography;

import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Context context;

    private boolean permissionGranted;
    private int REQUEST_CODE = 200;
    private static int CODE = 1;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        verificarPermisos();
        this.startFramet1();
    }

    private void startFramet1() {
        ViewStartedFragment viewStartedFragment = new ViewStartedFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.frame1, viewStartedFragment).commit();
    }

//    @Override
//    public void onBackPressed() {
//        int count = getFragmentManager().getBackStackEntryCount();
//        if (count == 0) {
//            super.onBackPressed();
//            this.startFramet1();
//        } else {
//            getFragmentManager().popBackStack();
//        }
//    }


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

//            Toast.makeText(context, "the unit name alredy exists", Toast.LENGTH_SHORT).show();
//                Log.d("******** Actiones ===> ", ""+name);


//                Intent intent = new Intent(this, BaseActivity.class);
////                intent.putExtra("valor", "new");
//                startActivity(intent);