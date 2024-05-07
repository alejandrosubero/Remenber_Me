package com.me.remenber.fragments.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.me.remenber.BaseActivity;
import com.me.remenber.R;
import com.me.remenber.dao.UserDao;
import com.me.remenber.entitys.User;
import com.me.remenber.repositorys.UserDataBase;
import com.me.remenber.security.Cryptography;
import com.me.remenber.security.EncryptAES;
import com.me.remenber.services.ManagementService;
import com.me.remenber.services.UserService;

import java.util.List;


public class RecoverUserQuestionFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    private String mParam1;
    private User userGloval;
    private User temp;
    private TextView userQuestiontextView;
    private EditText userQuestionAnswer;
    private Button userQuestionBtnLogin;
    private Context context;
    private UserService userService;




    public RecoverUserQuestionFragment() {
    }


    public static RecoverUserQuestionFragment newInstance(String param1) {
        RecoverUserQuestionFragment fragment = new RecoverUserQuestionFragment();
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
            Gson gson = new Gson();
            userGloval = gson.fromJson(mParam1, User.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recover_user_question, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity().getApplicationContext();

        userService = new UserService(context);
        userQuestiontextView = (TextView) view.findViewById(R.id.userQuestiontextView);
        userQuestionAnswer = (EditText) view.findViewById(R.id.userQuestionAnswer);
        userQuestionBtnLogin = (Button) view.findViewById(R.id.userQuestionBtnLogin);
        this.setQuestion();
        this.clickListener();
    }

    private void setQuestion(){
        if(userGloval!=null){
            userQuestiontextView.setText(userGloval.getUserQuestion());
        }
    }



    private void clickListener() {
        userQuestionBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userGloval != null) {
                    String answer = userQuestionAnswer.getText().toString().trim();
                    String userGlovalAnswer = userGloval.getUserResponse().trim();
                    if (userGlovalAnswer.equals(answer)) {
                        ManagementService managementService = ManagementService.getInstance();
                        managementService.setUser(userGloval);
                        Gson gson = new Gson();
                        String user = gson.toJson(userGloval);
                        RecoverNewUserDataFragment fragment =  RecoverNewUserDataFragment.newInstance(user, UserService.RESET_PASS);
                        getParentFragmentManager().beginTransaction().replace(R.id.frame_recover, fragment).addToBackStack(null).commit();
                    }
                }
            }
        });
    }

}