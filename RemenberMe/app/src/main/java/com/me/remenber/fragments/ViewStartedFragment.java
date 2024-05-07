package com.me.remenber.fragments;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.Context.ACCOUNT_SERVICE;
import static android.provider.SyncStateContract.Columns.ACCOUNT_TYPE;

import static androidx.core.content.ContextCompat.getSystemService;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import com.me.remenber.R;

import java.util.Map;


public class ViewStartedFragment extends Fragment {

    private Button btnCreate, btnSing;

    private boolean permissionGranted;
    private Context context;
    private int REQUEST_CODE = 200;

    public ViewStartedFragment() {

    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_started, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity().getApplicationContext();
        btnCreate = view.findViewById(R.id.btnCreate);
        btnSing = view.findViewById(R.id.btnSing);
        verificarPermisos();

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment newUserFragment = new NewUserFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.frame1, newUserFragment).addToBackStack(null).commit();
//                Log.d("******** Actiones ===> ", " Ejecuta New User Fragment");
            }
        });

        btnSing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment loginFragment = new LoginFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.frame1, loginFragment).addToBackStack(null).commit();
//                Log.d("******** Actiones ===> ", " ejecuta sing ");
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void verificarPermisos() {
        int permissionWRITE_EXTERNAL = ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionREAD_EXTERNAL = ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE);
//        int permissionAccount  = ContextCompat.checkSelfPermission(context, Manifest.permission.GET_ACCOUNTS);

        if (permissionWRITE_EXTERNAL == PackageManager.PERMISSION_GRANTED && permissionREAD_EXTERNAL == PackageManager.PERMISSION_GRANTED) {
            permissionGranted = true;
        } else {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
        }

//        if (permissionAccount == PackageManager.PERMISSION_GRANTED) {
//            permissionGranted = true;
//        } else {
//            requestPermissions(new String[]{android.Manifest.permission.GET_ACCOUNTS}, REQUEST_CODE);
//        }
    }

}


