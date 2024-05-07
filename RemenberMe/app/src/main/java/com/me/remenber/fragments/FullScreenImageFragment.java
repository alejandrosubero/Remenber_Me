package com.me.remenber.fragments;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.me.remenber.R;
import com.me.remenber.dao.SortDataDao;
import com.me.remenber.dataModel.DataConverter;
import com.me.remenber.entitys.SortData;
import com.me.remenber.entitys.User;
import com.me.remenber.pojo.SettingsPojo;
import com.me.remenber.repositorys.SortDataDataBase;
import com.me.remenber.security.Cryptography;
import com.me.remenber.services.FragmentService;
import com.me.remenber.services.ManagementService;
import com.me.remenber.componet.ZoomableImageView;

public class FullScreenImageFragment extends Fragment {
    private Context context;
    private Bitmap imageBitmap;
    private ImageView imageDetail;
    private ZoomableImageView touch;
    private SortData sotr;
    private ManagementService managementService;
    private FragmentService fragmentService;
    private Dialog dialogMenu;
    private Dialog dialogMenuDetaillImage;
    private ImageView closeDialogMenu;
    private TextView textViewAddTiTles;

    private int textViewAddTiTlesTouchCount;
    private ImageView closeDialog;
    private TextView itemDetailName, itemDetailUse;
    private Dialog dialogMenuShareImage;
    private Button btn_share;
    private EditText passKeyShare;
    private ImageView btn_share_close;
    private TextView textViewShareTiTles2;
    private Dialog dialogMenuDeleteImage;
    private Button btn_delete;
    private TextView textViewDeleteTiTles2;
    private ImageView btn_Delete_close;
    private LinearLayout selected_1, selected_2, selected_3, selected_4, selected_5;
    private SettingsPojo settings;


    public FullScreenImageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_full_screen_image, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.context = getActivity().getApplicationContext();
        this.touch = (ZoomableImageView) view.findViewById(R.id.IMAGEID);
        this.managementService = ManagementService.getInstance();
        settings = this.managementService.getSettingsUser();
        textViewAddTiTlesTouchCount = 0;
        this.getObject();
        this.startDialogsMenu(view);
    }

    private void getObject() {
        Bundle datosRecuperados = getArguments();
        if (datosRecuperados == null) {
            return;
        } else {
            String objeto = datosRecuperados.getString("ImagenGalleries");
            if (objeto != null) {
                Gson gson = new Gson();
                sotr = gson.fromJson(objeto, SortData.class);

                if (sotr != null && sotr.getImage() != null) {

                    if(settings.isEndebleFakeImage() && sotr.getImageFail() != null){
                        imageBitmap = DataConverter.convertArrayToImage(sotr.getImageFail());
                    }else{
                        imageBitmap = DataConverter.convertArrayToImage(sotr.getImage());
                    }
                    touch.setImageBitmap(imageBitmap);
                    managementService.setSortData(sotr);
                }
            }
        }
    }

    private void startDialogsMenu(View view) {
        this.startDialogMenu(view);
        this.startDialogDetail(view);
        this.startDialogShare(view);
        this.startDialogDelete(view);
        this.eventClick();
    }

    private void startDialogMenu(View view) {
        dialogMenu = new Dialog(view.getContext());
        dialogMenu.setContentView(R.layout.dialogo_menu_image);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialogMenu.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.background));
        }
        dialogMenu.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogMenu.setCancelable(false);
        closeDialogMenu = (ImageView) dialogMenu.findViewById(R.id.btn_ing1_closeMenu);
        textViewAddTiTles = (TextView) dialogMenu.findViewById(R.id.textViewAddTiTles);
        selected_1 = (LinearLayout) dialogMenu.findViewById(R.id.selected_1);
        selected_2 = (LinearLayout) dialogMenu.findViewById(R.id.selected_2);
        selected_3 = (LinearLayout) dialogMenu.findViewById(R.id.selected_3);
        selected_4 = (LinearLayout) dialogMenu.findViewById(R.id.selected_4);
        selected_5 = (LinearLayout) dialogMenu.findViewById(R.id.selected_5);

        if (settings.isErasableImage()) {
            selected_3.setVisibility(View.VISIBLE);
        } else {
            selected_3.setVisibility(View.GONE);
        }

        if (settings.isEndebleBlock()) {
            selected_4.setVisibility(View.VISIBLE);
        } else {
            selected_4.setVisibility(View.GONE);
        }

        if (settings.isEcryptImage()) {
            selected_5.setVisibility(View.VISIBLE);
        } else {
            selected_5.setVisibility(View.GONE);
        }

        this.eventListenerDialogMune();
    }

    private void resetCountFakeImage(){
        textViewAddTiTlesTouchCount = 0;
    }

    private void eventListenerDialogMune() {
        textViewAddTiTles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textViewAddTiTlesTouchCount ++;
                if (textViewAddTiTlesTouchCount >= settings.getNumberOfAttemps()){
                    imageBitmap = DataConverter.convertArrayToImage(sotr.getImage());
                    touch.setImageBitmap(imageBitmap);
                }
            }
        });

        closeDialogMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetCountFakeImage();
                dialogMenu.dismiss();
            }
        });
        selected_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetCountFakeImage();
                dialogMenu.dismiss();
                dialogMenuDetaillImage.show();
            }
        });
        selected_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetCountFakeImage();
                dialogMenuShareImage.show();
                dialogMenu.dismiss();
            }
        });
        selected_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetCountFakeImage();
                dialogMenu.dismiss();
                dialogMenuDeleteImage.show();
            }
        });
        selected_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetCountFakeImage();
                try {
                    sotr.setIsblock(true);
                    managementService.setSortData(sotr);
                    if (managementService.updateSortData(context)) {
                        dialogMenu.dismiss();
                        Toast.makeText(context, "The Image was Block...", Toast.LENGTH_SHORT).show();
                        navegateToGalleries(sotr);
                    } else {
                        dialogMenu.dismiss();
                        Toast.makeText(context, "The Image Block Fail...", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        selected_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(settings.isEcryptImage()){
                    User user = managementService.getUser();
                    Bitmap imageEncryptBitmap = DataConverter.convertArrayToImage(sotr.getImage());
                    String imagen = DataConverter.bitMapToString(imageEncryptBitmap);
                    String key = user.getKeyPrimary().trim();
                    Cryptography cryptography = new Cryptography(key);
                    String imagenEncryp = cryptography.encryptAES(imagen);

                    sotr.setImageString(imagenEncryp);
                    sotr.setEncripted(true);
                    sotr.setItsAlreadyEncripted(true);
                    sotr.setIsblock(true);
                    managementService.setSortData(sotr);

                    if (managementService.updateSortData(context)) {
                        dialogMenu.dismiss();
                        Toast.makeText(context, "The Image was encrypted.", Toast.LENGTH_SHORT).show();
                        navegateToGalleries(sotr);
                    }else{
                        Toast.makeText(context, "Error occurred can not encrypt the image.", Toast.LENGTH_LONG).show();
                        dialogMenu.dismiss();
                    }

                }
            }
        });

    }

    private void startDialogDetail(View view) {
        String name = "";
        String use = "";
        SortData sotr2 = null;

        dialogMenuDetaillImage = new Dialog(view.getContext());
        dialogMenuDetaillImage.setContentView(R.layout.dialog_detaill_image);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialogMenuDetaillImage.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.background));
        }
        dialogMenuDetaillImage.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogMenuDetaillImage.setCancelable(false);
        closeDialog = (ImageView) dialogMenuDetaillImage.findViewById(R.id.btn_ing1_closeAdd);
        itemDetailName = (TextView) dialogMenuDetaillImage.findViewById(R.id.itemDetailName);
        itemDetailUse = (TextView) dialogMenuDetaillImage.findViewById(R.id.itemDetailUse);

        if (sotr != null) {
            name = sotr.getName();
            itemDetailName.setText(name);
            use = sotr.getUse();
            itemDetailUse.setText(use);
        }

        closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogMenuDetaillImage.dismiss();
            }
        });
    }

    private void startDialogShare(View view) {

        dialogMenuShareImage = new Dialog(view.getContext());
        dialogMenuShareImage.setContentView(R.layout.share_dialog);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialogMenuShareImage.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.background));
        }
        dialogMenuShareImage.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogMenuShareImage.setCancelable(false);

        btn_share_close = (ImageView) dialogMenuShareImage.findViewById(R.id.btn_share_close);
        btn_share = (Button) dialogMenuShareImage.findViewById(R.id.btn_share);
        passKeyShare = (EditText) dialogMenuShareImage.findViewById(R.id.passKeyShare);
        textViewShareTiTles2 = (TextView) dialogMenuShareImage.findViewById(R.id.textViewShareTiTles2);
        textViewShareTiTles2.setText("Image");
        this.eventListenerDialogShare();
    }

    private void eventListenerDialogShare() {
        btn_share_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogMenuShareImage.dismiss();
            }
        });
        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareImage(view);
            }
        });
    }

    private void shareImage(View view) {
        SortData sortData3 = managementService.getSortData();
        String pass = passKeyShare.getText().toString();

        if (pass.equals(managementService.getUser().getKeyShare())) {
            if (sortData3.getTypeData().equals("Image")) {
                Bitmap bitmap = DataConverter.convertArrayToImage(sortData3.getImage());
                this.shareImageBit(bitmap);
            } else {
                // logica para compartir nota...
            }

        }
    }

    private void shareImageBit(Bitmap bitmap) {
        String bitmapPath = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "title", null);
        Uri bitmapUri = Uri.parse(bitmapPath);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/png");
        intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
        dialogMenuShareImage.dismiss();
        startActivity(Intent.createChooser(intent, "Share"));
    }

    private void startDialogDelete(View view) {
        dialogMenuDeleteImage = new Dialog(view.getContext());
        dialogMenuDeleteImage.setContentView(R.layout.delete_dialog);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialogMenuDeleteImage.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.background));
        }
        dialogMenuDeleteImage.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogMenuDeleteImage.setCancelable(false);
        btn_Delete_close = (ImageView) dialogMenuDeleteImage.findViewById(R.id.btn_Delete_close);
        btn_delete = (Button) dialogMenuDeleteImage.findViewById(R.id.btn_delete);
        textViewDeleteTiTles2 = (TextView) dialogMenuDeleteImage.findViewById(R.id.textViewDeleteTiTles2);
        textViewShareTiTles2.setText("Image");
        this.eventListenerDialogDelete();
    }

    private void eventListenerDialogDelete() {

        btn_Delete_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogMenuDeleteImage.dismiss();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (managementService.deleteSortData(context)) {
                        Toast.makeText(context, "The image was Delete...", Toast.LENGTH_SHORT).show();
                        dialogMenuDeleteImage.dismiss();
                        navegateToGalleries(sotr);
                    } else {
                        Toast.makeText(context, "The Delete Fail...", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void eventClick() {
        touch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dialogMenu.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void navegateToGalleries(SortData sotrh) {
        if (sotrh != null) {
            String code = sotrh.getCodeUser();
            Fragment galleries = new GalleryFragment();
            fragmentService = new FragmentService(context);
            Bundle bundle = fragmentService.userCodeBundle("Galleries", code);
            galleries.setArguments(bundle);
            getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, galleries).addToBackStack(null).commit();
        }
    }


}

//                Toast.makeText(context, "The Image was Saved...", Toast.LENGTH_SHORT).show();