package com.me.remenber.fragments.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.me.remenber.BaseActivity;
import com.me.remenber.R;
import com.me.remenber.entitys.User;
import com.me.remenber.services.ManagementService;
import com.me.remenber.services.UserService;


public class RecoverNewUserDataFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private User userGloval;
    private String typeData;
    private Context context;
    private UserService userService;
    private EditText campoNewUserData1, campoNewUserData2;
    private TextView newUserDataTitle;
    private Button campoNewUserBtn1;
    private String valor1 = "";
    private String valor2 = "";
    private ManagementService managementService;
    private CardView cardeyes1, cardeyes2;
    private ImageView eyes1, eyes2;

    private int valorChange1 = 0;
    private int valorChange2 = 0;

    public RecoverNewUserDataFragment() {
    }

    public static RecoverNewUserDataFragment newInstance(String param1, String param2) {
        RecoverNewUserDataFragment fragment = new RecoverNewUserDataFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (!getArguments().getString(ARG_PARAM1).isEmpty()) {
                Gson gson = new Gson();
                mParam1 = getArguments().getString(ARG_PARAM1);
               userGloval = gson.fromJson(mParam1, User.class);
            }
            if (!getArguments().getString(ARG_PARAM2).isEmpty()) {
                mParam2 = getArguments().getString(ARG_PARAM2);
                if (mParam2 != null) {
                    typeData = mParam2;
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recover_new_user_data, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();
        userService = new UserService(context);
        managementService = ManagementService.getInstance();
        newUserDataTitle = (TextView) view.findViewById(R.id.NewUserDataTitle);
        campoNewUserBtn1 = (Button) view.findViewById(R.id.campoNewUser_btn_1);
        campoNewUserData1 = (EditText) view.findViewById(R.id.campoNewUserData_1);
        campoNewUserData2 = (EditText) view.findViewById(R.id.campoNewUserData_2);
        cardeyes1 = (CardView) view.findViewById(R.id.cardeyes1);
        cardeyes2 = (CardView) view.findViewById(R.id.cardeyes2);
         eyes1 = (ImageView) view.findViewById(R.id.eyes1);
         eyes2 = (ImageView) view.findViewById(R.id.eyes2);
        userGloval = managementService.getUser();
        this.setViewTextEditHitForData();
        this.clickListener();
    }


    private void setViewTextEditHitForData() {
        switch (typeData) {
            case UserService.RESET_PASS:
                newUserDataTitle.setText("Reset the Password");
                campoNewUserData1.setHint("Password");
                campoNewUserData2.setHint("Confirm Password");
                break;
            case UserService.RESET_USER_NAME:
                newUserDataTitle.setText("Change the UserName");
                campoNewUserData1.setHint("UserName");
                campoNewUserData2.setHint("Confirm UserName");
                break;
            case UserService.RESET_QUESTION:
                newUserDataTitle.setText("Change the Question");
                campoNewUserData1.setHint("Question");
                campoNewUserData2.setHint("Confirm Question");
                break;
            case UserService.RESET_ANSWER:
                newUserDataTitle.setText("Change the Answer");
                campoNewUserData1.setHint("Answer");
                campoNewUserData2.setHint("Confirm Answer");
                break;
            case UserService.RESET_SHARE_PASS:
                newUserDataTitle.setText("Change the Share Password");
                campoNewUserData1.setHint("Share Password");
                campoNewUserData2.setHint("Confirm Share Password");
//                NewUserDataTitle.setTextSize(View.);
                break;

            case UserService.URL_BACKUP:
                newUserDataTitle.setText("Add the user url for backup");
                campoNewUserData1.setHint("Url");
                campoNewUserData2.setHint("Confirm url");
                campoNewUserBtn1.setText("Save Url");
                break;
        }
    }

    private void clickListener() {

        cardeyes1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(valorChange1 == 0){
                    campoNewUserData1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    valorChange1 = 1;
                    eyes1.setImageResource(R.drawable.baseline_remove_red_eye_24);
                }else{
                    campoNewUserData1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    valorChange1 = 0;
                    eyes1.setImageResource(R.drawable.baseline_do_not_disturb_block_24);
                }
            }
        });

        cardeyes2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(valorChange2 == 0){
                    campoNewUserData2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    eyes2.setImageResource(R.drawable.baseline_remove_red_eye_24);
                    valorChange2 = 1;
                }else{
                    campoNewUserData2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    eyes2.setImageResource(R.drawable.baseline_do_not_disturb_block_24);
                    valorChange2 = 0;
                }
            }
        });

        campoNewUserBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valor1 = campoNewUserData1.getText().toString();
                valor2 = campoNewUserData2.getText().toString();
                if (!valor1.equals("") && !valor2.equals("") && valor1.equals(valor2)) {
                    if (userGloval != null) {
                        if (setViewData(valor1, valor2)) {
                            User userForSend = userService.restore(userGloval);
                            managementService.setUser(userForSend);
                            userGloval = userForSend;
                            navigateTo();
                        }
                    }
                }
            }
        });
    }


    private boolean setViewData(String valor1, String valor2) {

        if (typeData.equals(UserService.RESET_PASS)) {
            userGloval.setUserPass(valor1);
            boolean action = saveAction(userGloval, "The Password is Update");
            return action;
        }

        if (typeData.equals(UserService.RESET_USER_NAME)) {
            userGloval.setName(valor1);
            boolean action2 = saveAction(userGloval, "The UserName is Update");
            return action2;
        }

        if (typeData.equals(UserService.RESET_QUESTION)) {
            userGloval.setUserQuestion(valor1);
            boolean action3 = saveAction(userGloval, "The Question is Update");
            return action3;
        }

        if (typeData.equals(UserService.RESET_ANSWER)) {
            userGloval.setUserResponse(valor1);
            boolean action4 = saveAction(userGloval, "The Answer is Update");
            return action4;
        }

        if (typeData.equals(UserService.RESET_SHARE_PASS)) {
            userGloval.setKeyShare(valor1);
            boolean action5 = saveAction(userGloval, "The Share Password is Update");
            return action5;
        }

        if (typeData.equals(UserService.URL_BACKUP)) {
            userGloval.setBackUpUrl(valor1);
            boolean action6 = saveAction(userGloval, "The backup url is Update");
            return action6;
        }
        return false;
    }

    private boolean saveAction(User userSave, String referent) {
        managementService.setUser(userGloval);
        User userFinal = userService.setUserForSave(userGloval);
        boolean result = userService.updateUser(userFinal);
        return result;
    }

    private void navigateTo() {
        Intent intent = new Intent(context, BaseActivity.class);
        intent.putExtra("object", userGloval);
        startActivity(intent);
    }

}