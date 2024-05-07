package com.me.remenber.fragments.user;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.me.remenber.R;
import com.me.remenber.entitys.User;
import com.me.remenber.security.EncryptAES;
import com.me.remenber.services.UserService;


public class RecoverUserFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private Button btnMailRecover;
    private EditText mailRecoverLoging;
    private Context context;
    private UserService userService;

    private int contador;


    public RecoverUserFragment() {
        // Required empty public constructor
    }


    public static RecoverUserFragment newInstance(String param1) {
        RecoverUserFragment fragment = new RecoverUserFragment();
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
        View view = inflater.inflate(R.layout.fragment_recover_user, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();
        userService = new UserService(context);
        btnMailRecover = (Button) view.findViewById(R.id.btn_mail_recover);
        mailRecoverLoging = (EditText) view.findViewById(R.id.mail_recover_Loging);
        contador = 0;
        this.clickListener();

    }

    private void clickListener() {
        btnMailRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = EncryptAES.encryptAES(mailRecoverLoging.getText().toString());
                User user = userService.findByMail(mail);

                if (user != null) {
                    User user2 = userService.restore(user);
                    if (user2 != null) {
                        navegateToFragment(user2);
                    }
                } else {
                    if (contador > 3) {
                        System.exit(1);
                        Toast.makeText(requireContext(), "Mail incorrect...", Toast.LENGTH_LONG).show();
                        return;
                    }
                    contador++;
                    Toast.makeText(requireContext(), "Mail incorrect...", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void navegateToFragment(User user) {
        Gson gson = new Gson();
        String userSend = gson.toJson(user);
        RecoverUserQuestionFragment fragment = RecoverUserQuestionFragment.newInstance(userSend);
        getParentFragmentManager().beginTransaction().replace(R.id.frame_recover, fragment).addToBackStack(null).commit();

    }


}