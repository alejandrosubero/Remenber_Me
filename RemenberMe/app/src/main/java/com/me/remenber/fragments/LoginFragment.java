package com.me.remenber.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.me.remenber.BaseActivity;
import com.me.remenber.R;

import com.me.remenber.RecoverActivity;
import com.me.remenber.dao.UserDao;
import com.me.remenber.entitys.User;
import com.me.remenber.repositorys.UserDataBase;
import com.me.remenber.security.Cryptography;
import com.me.remenber.security.EncryptAES;
import com.me.remenber.services.FragmentService;
import com.me.remenber.services.ManagementService;
import com.me.remenber.services.UserService;

import java.util.List;

import javax.crypto.SecretKey;


public class LoginFragment extends Fragment implements Runnable {

//    private UserDao userRepoDao;
    private Context context;
    private EditText nameUserLoguin, passwordUserLoging;
    private TextView recoveryUser;
    private Button btnLogin;
    private Dialog dialog;
    private User userBase;
    private UserService userService;

    public LoginFragment() {

    }

    //TODO: CONTROLAR EL NUMERO DE INGRESOS FALLIDOS, PANTALLA DE RECUPERACION DE CLAVE Y GENERACION DE PASS

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity().getApplicationContext();
        this.dialog(view);
//        userRepoDao = UserDataBase.getDBIstance(context).userDao();
        userService = new UserService(context);
        nameUserLoguin = view.findViewById(R.id.nameUserLoguin);
        passwordUserLoging = view.findViewById(R.id.passwordUserLoging);
        recoveryUser = (TextView) view.findViewById(R.id.recovery_user_text);
        recoveryUser.setVisibility(View.GONE);
        btnLogin = view.findViewById(R.id.btnLogin);
        this.listenersStarted();
    }

    private void listenersStarted() {
        this.listenerButton();
        this.listenerRecoveryUser();
        this.keyPrimaryListeners();
    }


    private void dialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setView(R.layout.progress);
        dialog = builder.create();
    }

    private void setDialog(boolean show) {
        if (show) dialog.show();
        else dialog.dismiss();
    }

    private void keyPrimaryListeners() {
        nameUserLoguin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    recoveryUser.setVisibility(View.GONE);
                }
            }
        });
    }

    private void listenerRecoveryUser() {
        recoveryUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RecoverActivity.class);
//                intent.putExtra("object", userForSend);
                startActivity(intent);
            }
        });
    }

    private void listenerButton() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singIn();
            }
        });
    }

    private void deleteAction(String pass){
        List<User> listIsDelete = userService.findByDeletePassword(pass);
        if(listIsDelete.size() > 0){
            ManagementService managementService = ManagementService.getInstance();
            managementService.deleteActionSortData(listIsDelete, context);
            userService.deleteAction(listIsDelete);
        }
    }

    private void singIn() {
        this.setDialog(true);
        String userNameTest = nameUserLoguin.getText().toString();
        List<User> list = userService.findBYNameLis(userNameTest);
        String recibePass = passwordUserLoging.getText().toString();
        String passRecib = EncryptAES.encryptAES(recibePass);

        if (list.size() > 0) {
            userBase = list.get(0);
            String passUser = userBase.getUserPass();

            if (passRecib != null && passUser != null && (passRecib.equals(passUser))) {
                Thread hilo = new Thread(this);
                hilo.start();
            } else {
                this.dataIncorrect(passRecib);
            }
        } else {
            this.dataIncorrect(passRecib);
        }
    }

    private void dataIncorrect(String passRecib){
        Toast.makeText(context, "The User or passwork are incorrect", Toast.LENGTH_SHORT).show();
        this.cleanData();
        this.setDialog(false);
        recoveryUser.setVisibility(View.VISIBLE);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(btnLogin.getWindowToken(), 0);
        this.deleteAction(passRecib);
    }

    private void cleanData() {
        nameUserLoguin.setText("");
        passwordUserLoging.setText("");
    }





    @Override
    public void run() {
//        User userForSend = restore(userBase);
        User userForSend = userService.restore(userBase);

        nameUserLoguin.post(new Runnable() {
            @Override
            public void run() {
                nameUserLoguin.setText("");
            }
        });
        passwordUserLoging.post(new Runnable() {
            @Override
            public void run() {
                passwordUserLoging.setText("");
            }
        });

        Intent intent = new Intent(context, BaseActivity.class);
        intent.putExtra("object", userForSend);
        startActivity(intent);
        setDialog(false);
    }
}

//    private User restore(User userBase) {
//        User userNew = new User();
//        userNew.setUserId(userBase.getUserId());
//
//        if (userBase.getName() != null) {
//            userNew.setName(userBase.getName());
//        }
//        if (userBase.getUserMail() != null) {
//            String data = EncryptAES.decryptAES(userBase.getUserMail());
//            userNew.setUserMail(data.trim());
//        }
//        if (userBase.getUserQuestion() != null) {
//            String data = EncryptAES.decryptAES(userBase.getUserQuestion());
//            userNew.setUserQuestion(data.trim());
//        }
//        if (userBase.getUserResponse() != null) {
//            String data = EncryptAES.decryptAES(userBase.getUserResponse());
//            userNew.setUserResponse(data.trim());
//        }
//        if (userBase.getUserPass() != null) {
//            String data = EncryptAES.decryptAES(userBase.getUserPass());
//            userNew.setUserPass(data.trim());
//        }
//        if (userBase.getKeyPrimary() != null) {
//            String data = EncryptAES.decryptAES(userBase.getKeyPrimary());
//            userNew.setKeyPrimary(data.trim());
//        }
//        if (userBase.getKeyShare() != null) {
//            String data = EncryptAES.decryptAES(userBase.getKeyShare());
//            userNew.setKeyShare(data.trim());
//        }
//
//        if (userBase.getCodeUser() != null) {
//            try {
//                String key = userNew.getKeyPrimary().trim();
//                String codeUser = userBase.getCodeUser();
//                Cryptography cryptography = new Cryptography(key);
//                String code = cryptography.decryptAES(codeUser);
//                userNew.setCodeUser(code.trim());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return userNew;
//    }