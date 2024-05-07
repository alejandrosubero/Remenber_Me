package com.me.remenber.fragments.user;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.me.remenber.BaseActivity;
import com.me.remenber.R;
import com.me.remenber.RecoverActivity;
import com.me.remenber.entitys.User;
import com.me.remenber.pojo.SettingsPojo;
import com.me.remenber.services.BackupService;
import com.me.remenber.services.FragmentService;
import com.me.remenber.services.ManagementService;
import com.me.remenber.services.SettingsService;
import com.me.remenber.services.UserService;


public class UpdateUserDataFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private Context context;
    private ManagementService managementService;
    private User user;
    private TextView updateUserPassword, updateUserName, updateUserQuestion, updateUserAnswer, updateUserSharePass;
    private TextView  getBackup, generateBackup, urlForBackup;

    private Dialog dialogAdd;
    private Button addItem, addMaterial;
    private ImageView btnIngCloseAdd;
    private TextView textViewAddTiTles, textViewMessageAdd;

    private SettingsPojo settingsUser;
    private SettingsService settingsService;
    private FragmentService fragmentService;

    public UpdateUserDataFragment() {
    }

    public static UpdateUserDataFragment newInstance(String param1, String param2) {
        UpdateUserDataFragment fragment = new UpdateUserDataFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_user_data, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();
        managementService = ManagementService.getInstance();
        settingsUser = managementService.getSettingsUser();
        user = managementService.getUser();
        updateUserPassword = (TextView) view.findViewById(R.id.update_user_password);
        updateUserName = (TextView) view.findViewById(R.id.update_user_name);
        updateUserQuestion= (TextView) view.findViewById(R.id.update_user_question);
        updateUserAnswer= (TextView) view.findViewById(R.id.update_user_answer);
        updateUserSharePass= (TextView) view.findViewById(R.id.update_user_share_pass);
        getBackup = (TextView) view.findViewById(R.id.get_backup);
        generateBackup = (TextView) view.findViewById(R.id.generate_backup);
        urlForBackup = (TextView) view.findViewById(R.id.user_backup_url);
        this.startDialogAdd(view);
        this.listenerButton();
    }

    private String userString(User user){
        Gson gson = new Gson();
        String userSend = gson.toJson(user);
        return userSend;
    }

    private void gotToUpdate(String type){
        Intent intent = new Intent(context, RecoverActivity.class);
        intent.putExtra("objectType", type);
        startActivity(intent);
    }

    private void listenerButton(){
        updateUserPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotToUpdate(UserService.RESET_PASS);
            }
        });
        updateUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotToUpdate(UserService.RESET_USER_NAME);
            }
        });
        updateUserQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotToUpdate(UserService.RESET_QUESTION);
            }
        });
        updateUserAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotToUpdate(UserService.RESET_ANSWER);
            }
        });
        updateUserSharePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotToUpdate(UserService.RESET_SHARE_PASS);
            }
        });
        getBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAdd.show();
            }
        });
        generateBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackupService backupService = new BackupService(context);
                backupService.generateBackup();
                Toast.makeText(context, "The Backup is Ready", Toast.LENGTH_SHORT).show();
                navegate();
            }
        });
        urlForBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotToUpdate(UserService.URL_BACKUP);
            }
        });
    }


    private void startDialogAdd(View view) {
        dialogAdd = new Dialog(view.getContext());
        dialogAdd.setContentView(R.layout.add_new_dialog);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialogAdd.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.background));
        }
        dialogAdd.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogAdd.setCancelable(false);
        addItem = dialogAdd.findViewById(R.id.btn_AddItem);
        addMaterial = dialogAdd.findViewById(R.id.btn_AddMaterial);
        btnIngCloseAdd = dialogAdd.findViewById(R.id.btn_ing_closeAdd);
        textViewAddTiTles = (TextView) dialogAdd.findViewById(R.id.textViewAddTiTles);
        textViewMessageAdd = (TextView) dialogAdd.findViewById(R.id.textViewMessageAdd);

        addItem.setText(R.string.generate_backup);
        addMaterial.setText(R.string.get_backup);
        textViewAddTiTles.setText("Alert Backup");
        StringBuilder description = new StringBuilder("if you have updated information on the device and not in the cloud, you may lose that information.\n");
        description.append("I was able to make a backup so as not to lose the information");
        textViewMessageAdd.setText(description.toString());

        this.eventListenerAddDialog();
    }

    private void eventListenerAddDialog() {
        btnIngCloseAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAdd.dismiss();
            }
        });
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackupService backupService = new BackupService(context);
                backupService.generateBackup();
                dialogAdd.dismiss();
                navegate();
            }
        });
        addMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BackupService backupService = new BackupService(context);
                backupService.getBackup();

                navegate();
//                Intent intent = new Intent(context, BaseActivity.class);
//                String userd= userString(user);
//                intent.putExtra("object", userd);
//                dialogAdd.dismiss();
//                startActivity(intent);
            }
        });
    }


    private void navegate() {
        Intent intent = new Intent(context, BaseActivity.class);
        String userd = userString(user);
        intent.putExtra("object", userd);
        dialogAdd.dismiss();
        startActivity(intent);
    }

}