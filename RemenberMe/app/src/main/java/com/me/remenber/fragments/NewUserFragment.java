package com.me.remenber.fragments;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.renderscript.ScriptGroup;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.me.remenber.R;
import com.me.remenber.dao.UserDao;
import com.me.remenber.entitys.User;
import com.me.remenber.repositorys.UserDataBase;
import com.me.remenber.security.Cryptography;
import com.me.remenber.security.EncryptAES;
import com.me.remenber.services.PasswordGenerator;
import com.me.remenber.services.UserService;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.List;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;


public class NewUserFragment extends Fragment {

    private Context context;
    private ImageView btn_ing_close;
    private EditText userName, userMail, userQuestion, userResponse, userPass, keyPrimary, keyShare, deleteKey;
    private Button btnSaveUser;
    private EncryptAES encryptAES;
    private Dialog dialog;
    private String codeFinal;
    private String keyShareT;
    private String keyPrimaryT;
    private String deleteKeyT;
    private User userNew;
    private UserService userService;
    private ActivityResultLauncher<Intent> resultLauncher;
    private String accountMail;

    public NewUserFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity().getApplicationContext();
        encryptAES = new EncryptAES();
        userService = new UserService(context);
        userName = view.findViewById(R.id.userName);
        userMail = view.findViewById(R.id.userMail);
        userMail.setVisibility(View.GONE);
        userQuestion = view.findViewById(R.id.userQuestion);
        userResponse = view.findViewById(R.id.userResponse);
        userPass = view.findViewById(R.id.userPass);
        keyPrimary = (EditText) view.findViewById(R.id.keyPrimary);
        keyPrimary.setVisibility(View.GONE);
        //  android:inputType="textPassword"
//        keyPrimary.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//        if (keyPrimary.getText().toString().length() < 16) {
//            keyPrimary.setError("The keyPrimary length need equal to 16 character.");
//        }
        this.setKeyPrimary();
        this.keyPrimaryListeners();
        keyShare = view.findViewById(R.id.keyShare);
        deleteKey = (EditText) view.findViewById(R.id.deleteKey);
        btn_ing_close = view.findViewById(R.id.btn_ing_close);
        btnSaveUser = view.findViewById(R.id.btnSaveUser);
        this.activityResultLaunchers();
        String description = "Select the Mail Account for use in app";
        this.checkSelfPermission(description);
        this.dialog(view);
        this.listeners();
    }


    private void setKeyPrimary(){
        char[] passKey =  PasswordGenerator.greek_password(16);
        String passKeyPrimary =  String.valueOf(passKey);
        keyPrimary.setText(passKeyPrimary);

        if (keyPrimary.getText().toString().length() < 16) {
            keyPrimary.setError("The keyPrimary length need equal to 16 character.");
        }
    }

    private void checkSelfPermission(String description) {
        String[] allowableAccountTypes = {"com.google"};
        Intent intent = AccountManager.newChooseAccountIntent(
                null, null, allowableAccountTypes, description,
                null, null, null);
        resultLauncher.launch(intent);

    }

    private void activityResultLaunchers() {
        resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == getActivity().RESULT_OK) {
                    AccountManager accountManager = AccountManager.get(context);
                    Account[] accounts = accountManager.getAccountsByType("com.google");
                    if (accounts != null) {
                        for (Account ac : accounts) {
                            accountMail = ac.name;
                            userMail.setText(accountMail);
//                            Log.e("---->  Accounts : ", "" + accountMail);
                        }
                    }
                }
            }
        });
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
        keyPrimary.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (keyPrimary.getText().toString().length() < 16 || keyPrimary.getText().toString().length() > 16) {
                        keyPrimary.setError("The keyPrimary length need equal to 16 character.");
                        Toast.makeText(context, "The keyPrimary length need equal to 16 character.", Toast.LENGTH_SHORT).show();
                        keyPrimary.setTextColor(R.color.red);
                    } else {
                        keyPrimary.setTextColor(R.color.black);
                    }
                }
            }
        });
    }

    private void listeners() {
        btn_ing_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment viewStartedFragment = new ViewStartedFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.frame1, viewStartedFragment).addToBackStack(null).commit();
            }
        });
        btnSaveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userNameTest = "";
                if (keyPrimary.getText().toString().length() == 16) {
                    if (userName != null) {
                        userNameTest = userName.getText().toString();
                        List<User> list = userService.findBYNameLis(userNameTest);
                        if (list.size() > 0) {
                            setDialog(false);
                            Toast.makeText(context, "The User Name exist Use another user_name", Toast.LENGTH_SHORT).show();
                        } else {
                            setDialog(true);
                            saveUser();
                        }
                    }
                } else {
                    Toast.makeText(context, "The keyPrimary length need equal to 16 character.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void saveUser() {
        userNew = new User();
        keyShareT = null;
        keyPrimaryT = null;

        String userNamec = userName.getText().toString();
        String userMailc = userMail.getText().toString();
        String userQuestionc = userQuestion.getText().toString();
        String userResponsec = userResponse.getText().toString();
        String userPassc = userPass.getText().toString();
        String keyPrimaryTc = keyPrimary.getText().toString();
        String keyShareTc = keyShare.getText().toString();
        String deleteKeyTc = deleteKey.getText().toString();
        this.accountMail = userMailc; // test
        if (userNamec != null && userMailc != null && userMailc.equals(this.accountMail)
                && userQuestionc != null && userResponsec != null && userPassc != null
                && keyPrimaryTc != null && keyShareTc != null && deleteKeyTc != null) {

            if (userName != null) {
                userNew.setName(userNamec);
            }
            if (userMail != null) {
                String mailData = "";
                String data = userMailc;
                if (userService.checkUserMail(data)) {
                    mailData = EncryptAES.encryptAES(data);
                    userNew.setUserMail(mailData);
                } else {
                    Toast.makeText(context, "The User Mail exist All Ready", Toast.LENGTH_SHORT).show();
                }
            }
            if (userQuestion != null) {
                String data = userQuestionc;
                String mail1 = EncryptAES.encryptAES(data);
                userNew.setUserQuestion(mail1);
            }
            if (userResponse != null) {
                String data = userResponsec;
                String mail1 = EncryptAES.encryptAES(data);
                userNew.setUserResponse(mail1);
            }
            if (userPass != null) {
                String data = userPassc;
                String mail1 = EncryptAES.encryptAES(data);
                userNew.setUserPass(mail1);
            }
            if (keyPrimary != null) {
                keyPrimaryT = keyPrimaryTc;
                String mail1 = EncryptAES.encryptAES(keyPrimaryT);
                userNew.setKeyPrimary(mail1);
            }
            if (keyShare != null) {
                keyShareT = keyShareTc;
                String mail1 = EncryptAES.encryptAES(keyShareT);
                userNew.setKeyShare(mail1);
            }
            if (deleteKey != null) {
                deleteKeyT = deleteKeyTc;
                String delete1 = EncryptAES.encryptAES(deleteKeyT);
                userNew.setDeletePassword(delete1);
            }



            Thread thread = new Thread() {
                public void run() {
                    if (keyPrimaryT != null && keyShareT != null) {
                        String code = generateCode("User");
                        try {
                            String pass = keyPrimaryT.trim();
                            Cryptography cryptography = new Cryptography(pass);
                            String mail1 = cryptography.encryptAES(code);
                            userNew.setCodeUser(mail1);
                        } catch (Exception e) {
                            e.printStackTrace();
                            String codeFinal = null;
                        }
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setDialog(false);
                            userService.saveUser(userNew);
                            Toast.makeText(context, "The User is Save", Toast.LENGTH_SHORT).show();
                            Fragment loginFragment = new LoginFragment();
                            getParentFragmentManager().beginTransaction().replace(R.id.frame1, loginFragment).addToBackStack(null).commit();
                        }
                    });
                }
            };
            thread.start();
        }else{
            setDialog(false);
           if(userMailc != null && !userMailc.equals(this.accountMail)) {
               String description = "It is request to select the Mail Account for use in app";
               this.checkSelfPermission(description);
           }else {
               Toast.makeText(context, "The User data no is complete fill all", Toast.LENGTH_SHORT).show();
           }
        }
    }

    private String generateCode(String cod) {
        String code = "UW-" + cod + "*" + UUID.randomUUID().toString();
        List<User> files = userService.findByCode(code);
        if (files != null && files.size() > 0) {
            generateCode(cod);
        }
        return code;
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


}

//        if (getActivity().checkSelfPermission(Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED) {
//            String[] allowableAccountTypes = {"com.google"};
//            Intent intent = AccountManager.newChooseAccountIntent(
//                    null, null, allowableAccountTypes, null,
//                    null, null, null);
//            resultLauncher.launch(intent);
//        }