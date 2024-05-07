package com.me.remenber.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.me.remenber.R;
import com.me.remenber.entitys.Settings;
import com.me.remenber.entitys.User;
import com.me.remenber.fragments.user.RecoverNewUserDataFragment;
import com.me.remenber.fragments.user.UpdateUserDataFragment;
import com.me.remenber.pojo.SettingsPojo;
import com.me.remenber.services.FragmentService;
import com.me.remenber.services.ManagementService;
import com.me.remenber.services.SettingsService;
import com.me.remenber.services.UserService;


public class SettingFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private Context context;
    private ManagementService managementService;
    private SettingsService settingsService;
    private User user;
    private SettingsPojo settingsUser;
    private String userName;
    private androidx.appcompat.widget.SwitchCompat encryptNotes, ecryptImage, endebleFakeImage, endebleBlock, isErasableNote, isErasableImage, showAll;
    private TextView userNameTitle, reset, userSetting;
    private int contador;
    private Dialog dialogMenuShareImage;
    private TextView textViewShareTiTles2, textViewSareTiTles1, itemDetailNameTitleShare, itemDetailUseTitleShare;
    private EditText passKeyShare;
    private Button btn_share;
    private ImageView btn_share_close;
    private FragmentService fragmentService;


    public SettingFragment() {}

    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();
        managementService = ManagementService.getInstance();
        settingsService = new SettingsService(context);
        settingsUser = managementService.getSettingsUser();
        user = managementService.getUser();
        startDialogFakeImage(context);
        this.setButton(view);
        this.setSettings();
        this.changeListener();
    }

    private void setButton(View view){
        userNameTitle = view.findViewById(R.id.user_textView);
        userSetting = view.findViewById(R.id.user_setting);
        userSetting.setVisibility(View.GONE);
        reset = view.findViewById(R.id.reset_setting);
        ecryptImage = view.findViewById(R.id.switch2);
        endebleFakeImage = view.findViewById(R.id.switch3);
        endebleBlock = view.findViewById(R.id.switch4);
        isErasableImage = view.findViewById(R.id.switch5);
        encryptNotes = view.findViewById(R.id.switch1);
        isErasableNote = view.findViewById(R.id.switch6);
        showAll= view.findViewById(R.id.switch7);
        contador = 0;
    }

    private void setCheck(SettingsPojo settings){
        encryptNotes.setChecked(settings.isEcryptNotes());
        ecryptImage.setChecked(settings.isEcryptImage());
        endebleFakeImage.setChecked(settings.isEndebleFakeImage());
        endebleBlock.setChecked(settings.isEndebleBlock());
        isErasableNote.setChecked(settings.isErasableNote());
        isErasableImage.setChecked(settings.isErasableImage());
        showAll.setChecked(settings.isShowAll());
    }
    private void setSettings() {
        if (settingsUser != null) {
            if (settingsUser.getCodeUser() != null && !settingsUser.getCodeUser().equals("DEFAULT")) {
                if (user != null) {
                    userName = user.getName();
                    userNameTitle.setText(userName);
                }
                this.setCheck(settingsUser);
            } else {
                userName = "Default Setting";
                userNameTitle.setText(userName);
            }
        }
    }

    private void checkUser() {
        if (settingsUser != null) {
            if (settingsUser.getCodeUser() ==null || settingsUser.getCodeUser().equals("DEFAULT")) {
                settingsUser.setCodeUser(user.getCodeUser());
            }
            settingsService.saveSetting(settingsUser.convertSettingsPojoToSettings());
        }
    }

    private void changeListener() {
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings setting = settingsService.resetSetting(settingsUser.convertSettingsPojoToSettings());
                settingsUser = null;
                settingsUser = new SettingsPojo(setting);
                setSettings();
            }
        });

        showAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settingsUser.setShowAll(isChecked);
                checkUser();
                setSettings();
            }
        });

        encryptNotes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settingsUser.setEcryptNotes(isChecked);
                checkUser();
                setSettings();
            }
        });

        ecryptImage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settingsUser.setEcryptImage(isChecked);
                checkUser();
                setSettings();
            }
        });

        endebleFakeImage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settingsUser.setEndebleFakeImage(isChecked);
                checkUser();
                setSettings();
            }
        });

        endebleBlock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //TODO: HACER UNA VENTANA QUE EXPLIQUE QUE HAY ELEMENTOS BLOQUEDOS Y MOSTRARLOS PAR QUE LOS CAMBIE DE ESTTUS.
                settingsUser.setEndebleBlock(isChecked);
                checkUser();
                setSettings();
            }
        });

        isErasableImage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settingsUser.setErasableImage(isChecked);
                checkUser();
                setSettings();
            }
        });

        isErasableNote.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                settingsUser.setErasableNote(isChecked);
                checkUser();
                setSettings();
            }
        });

        userNameTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contador++;
                if(contador == 6 ){
                    Toast.makeText(getActivity(), "Continua para activar user Setting "+ contador, Toast.LENGTH_SHORT).show();
                }else if(contador == 8){
                    Toast.makeText(getActivity(), "User Setting is Active"+ contador, Toast.LENGTH_SHORT).show();
                    userSetting.setVisibility(View.VISIBLE);
                }
            }
        });

        userSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateUserDataFragment fragment = new UpdateUserDataFragment();
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
            }
        });
    }


    private void startDialogFakeImage(Context cont) {

        dialogMenuShareImage = new Dialog(cont);
        dialogMenuShareImage.setContentView(R.layout.share_dialog);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialogMenuShareImage.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.background));
        }
        dialogMenuShareImage.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogMenuShareImage.setCancelable(false);

        btn_share_close = (ImageView) dialogMenuShareImage.findViewById(R.id.btn_share_close);
        btn_share = (Button) dialogMenuShareImage.findViewById(R.id.btn_share);
        passKeyShare = (EditText) dialogMenuShareImage.findViewById(R.id.passKeyShare);

        textViewSareTiTles1 = (TextView) dialogMenuShareImage.findViewById(R.id.textViewSareTiTles1);
        textViewShareTiTles2 = (TextView) dialogMenuShareImage.findViewById(R.id.textViewShareTiTles2);
        itemDetailNameTitleShare = (TextView) dialogMenuShareImage.findViewById(R.id.itemDetailNameTitleShare);
        itemDetailUseTitleShare = (TextView) dialogMenuShareImage.findViewById(R.id.itemDetailUseTitleShare);


        textViewSareTiTles1.setText("Set ");
        textViewShareTiTles2.setText("Attemps");
        itemDetailNameTitleShare.setText("Number Of Attemps");
        itemDetailUseTitleShare.setText("Number's: ");
        this.eventListenerDialogFake();
    }

    private void eventListenerDialogFake() {
        btn_share_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogMenuShareImage.dismiss();
            }
        });
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               int number = Integer.parseInt(passKeyShare.getText().toString());
                settingsUser.setNumberOfAttemps(number);
                checkUser();
                setSettings();
                dialogMenuShareImage.dismiss();
            }
        });
    }


}




