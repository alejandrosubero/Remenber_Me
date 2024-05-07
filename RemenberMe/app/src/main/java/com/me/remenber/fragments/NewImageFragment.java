package com.me.remenber.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.RecoverableSecurityException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.me.remenber.R;
import com.me.remenber.dao.SortDataDao;
import com.me.remenber.dataModel.DataConverter;
import com.me.remenber.entitys.SortData;
import com.me.remenber.entitys.User;
import com.me.remenber.pojo.SettingsPojo;
import com.me.remenber.repositorys.SortDataDataBase;
import com.me.remenber.repositorys.UserDataBase;
import com.me.remenber.security.Cryptography;
import com.me.remenber.services.ManagementService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class NewImageFragment extends Fragment {

    private Context context;
    private Bitmap imageBitmap;
    public static final String EXTRA_INFO = "default";
    private Button btnCapture, btnCaptureFake, btnEncriptImage;
    private ImageView itemImage, btnIngCloseAdd;
    private static final int Image_Capture_Code = 1;
    private static final int PICK_IMAGE = 2;
    private Dialog dialogAdd;
    private Button addItem, addMaterial;
    private EditText itemSectionName, itemSectionUse;
    private String userCode;
    private ManagementService managementService;
    private SettingsPojo settings;
    private Bitmap imageBitmapFakeImage;
    private boolean fakeImage;
    private boolean fakeImageControl;
    private ActivityResultLauncher<Intent> resultLauncherCamera;
    private ActivityResultLauncher<Intent> resultLauncherPick;
    private boolean encriptImagen = false;

    private ActivityResultLauncher<Intent> deleteResultLauncher;
    private String picturePath;
    Uri uriImage;
    private TextView textViewAddTiTles;
    private TextView textViewMessageAdd;
    String mediaPath;

    public NewImageFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_image, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity().getApplicationContext();
        managementService = ManagementService.getInstance();
        settings = this.managementService.getSettingsUser();
        Bundle datosRecuperados = getArguments();
        if (datosRecuperados == null) {
            return;
        } else {
            userCode = datosRecuperados.getString("UserCodeAddImagen");
            if (userCode != null) {
                Log.d("******** Actiones Bundle AddImagen ***** *** ===> ", "");
            }
        }
        this.startedFragment(view);
    }

    private void startedFragment(View view) {
        itemImage = (ImageView) view.findViewById(R.id.itemImage);
        btnCapture = (Button) view.findViewById(R.id.btnCapture);
        btnCaptureFake = (Button) view.findViewById(R.id.btnCaptureFake);
        btnEncriptImage = (Button) view.findViewById(R.id.btnEncriptImage);
        itemSectionName = (EditText) view.findViewById(R.id.itemSectionName);
        itemSectionUse = (EditText) view.findViewById(R.id.itemSectionUse);
        fakeImage = false;
        fakeImageControl = false;

        if(settings.isEcryptImage()){
            btnEncriptImage.setVisibility(View.VISIBLE);
        }else {
            btnEncriptImage.setVisibility(View.GONE);
        }

        this.startDialogAdd(view);
        activityResultLaunchers();
        this.listener();
    }

    private void imageReviewReset() {
        if (fakeImage) {
            imageBitmapFakeImage = null;
        } else {
            imageBitmap = null;
        }
    }

    private void activityResultLaunchers() {
        resultLauncherCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                imageReviewReset();
                Bitmap thumbnail = null;
                if (result.getResultCode() == getActivity().RESULT_OK) {
                    Bundle extras = result.getData().getExtras();
                    Bitmap bit = (Bitmap) extras.get("data");
                    if (fakeImage) {
                        imageBitmapFakeImage = bit;
                        thumbnail = (Bitmap) Objects.requireNonNull(Objects.requireNonNull(result.getData()).getExtras()).get("data");
                        imageBitmapFakeImage = thumbnail;

                    } else {
                        imageBitmap = bit;
                        thumbnail = (Bitmap) Objects.requireNonNull(Objects.requireNonNull(result.getData()).getExtras()).get("data");
                        imageBitmap = thumbnail;
                        itemImage.setImageBitmap(thumbnail);
                    }
                } else if (result.getResultCode() == getActivity().RESULT_CANCELED) {
                    Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_LONG).show();
                }
            }
        });

        resultLauncherPick = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                imageReviewReset();
                Bitmap selectedImageBitmap = null;
                Uri selectedImageUri = result.getData().getData();
                int receivedFlags = result.getData().getFlags();

                if ((receivedFlags & Intent.FLAG_GRANT_READ_URI_PERMISSION) == 0) {
                    Log.e("Received Flags... ", "Read URI permission flag not available");
                }

                try {
                    selectedImageBitmap = scaleBitmap(null, selectedImageUri);
                    if (selectedImageUri != null) {
                        if (fakeImage) {
                            imageBitmapFakeImage = selectedImageBitmap;
                        } else {
                            itemImage.setImageBitmap(selectedImageBitmap);
                            imageBitmap = selectedImageBitmap;
                        }

                    } else {
                        Toast.makeText(context, "Bitmap null", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
        addItem.setVisibility(View.GONE);

        this.eventListenerAddDialog();
    }

    private void eventListenerAddDialog() {

        btnIngCloseAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fakeImageControl = false;
                fakeImage = false;
                dialogAdd.dismiss();
            }
        });
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
                dialogAdd.dismiss();
            }
        });
        addMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImage();
                dialogAdd.dismiss();
            }
        });
    }

    private void dispatchTakePictureIntent() {
        if (fakeImageControl) {
            fakeImage = true;
        }
        Intent cInt = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        resultLauncherCamera.launch(cInt);
    }

    private void pickImage() {
        if (fakeImageControl) {
            fakeImage = true;
        }
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        resultLauncherPick.launch(intent);
    }

    public void listener() {
        this.listenerItemImage();
        this.listenerBtnCapture();
        this.listenerAddFakeImage();
        this.listenerEncriptImage();
    }

    private void listenerEncriptImage() {
        btnEncriptImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(settings.isEcryptImage()){
                    if(encriptImagen){
                        encriptImagen = false;
                        Toast.makeText(context, "The Image will not be encrypted.", Toast.LENGTH_SHORT).show();
                    }else {
                        encriptImagen = true;
                        Toast.makeText(context, "The Image will be encrypted.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    public void listenerItemImage() {
        itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAdd.show();
            }
        });
    }

    private void listenerAddFakeImage() {
        btnCaptureFake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fakeImageControl = true;
                dialogAdd.show();
            }
        });
    }

    public void listenerBtnCapture() {
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImagen();
            }
        });
    }

    private void saveImagen() {
        SortData imagenNewSave = new SortData();
        imagenNewSave.setTypeData("Image");
        if (userCode != null) {
            imagenNewSave.setCodeUser(userCode);
            String name = itemSectionName.getText().toString();
            if (name != null) {
                imagenNewSave.setName(name);
            } else {
                imagenNewSave.setName("no data");
            }
            String use = itemSectionUse.getText().toString();
            if (use != null) {
                imagenNewSave.setUse(use);
            } else {
                imagenNewSave.setUse("no data");
            }

            if (imageBitmap != null && !encriptImagen) {
                byte[] image = DataConverter.convertImageToArray(imageBitmap);
                imagenNewSave.setImage(image);
            }

            if(imageBitmap != null && encriptImagen && settings.isEcryptImage()){
                String imagen = DataConverter.bitMapToString(imageBitmap);

                User user = managementService.getUser();
                String key = user.getKeyPrimary().trim();
                Cryptography cryptography = new Cryptography(key);
                String imagenEncryp = cryptography.encryptAES(imagen);

                imagenNewSave.setImageString(imagenEncryp);
                imagenNewSave.setEncripted(true);
                imagenNewSave.setItsAlreadyEncripted(true);
                imagenNewSave.setIsblock(true);
            }


            if (fakeImage && imageBitmapFakeImage != null) {
                byte[] imagefake = DataConverter.convertImageToArray(imageBitmapFakeImage);
                imagenNewSave.setImageFail(imagefake);
            }
            managementService.setSortData(imagenNewSave);
            managementService.insertSortData(context);
            Toast.makeText(context, "The Image was Saved...", Toast.LENGTH_SHORT).show();
            this.cleanView();
        } else {
            Toast.makeText(context, "Can't save the image (Error...)", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap scaleBitmap(Bitmap bitmap, Uri uri) {

        Bitmap selectedImageBitmap = null;
        Uri selectedImageUri = null;

        try {
            if (bitmap != null && uri == null) {
                String bitmapPath = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "title", null);
                selectedImageUri = Uri.parse(bitmapPath);
            } else if (bitmap == null && uri != null) {
                selectedImageUri = uri;
            }
            selectedImageBitmap = DataConverter.reduceBitmap(context, selectedImageUri, 500, 500);
        } catch (Exception e) {
            e.printStackTrace();
            return selectedImageBitmap;
        }
        return selectedImageBitmap;
    }

    private void cleanView() {
        itemSectionUse.setText("");
        itemSectionName.setText("");
        itemImage.setImageResource(R.drawable.photot);
        fakeImage = false;
        fakeImageControl = false;
    }

}

//    public long getFilePathToMediaID(String songPath, Context context) {
//        long id = 0;
//        ContentResolver cr = context.getContentResolver();
//
//        Uri uri = MediaStore.Files.getContentUri("external");
//        String selection = MediaStore.Audio.Media.DATA;
//        String[] selectionArgs = {songPath};
//        String[] projection = {MediaStore.Audio.Media._ID};
//        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
//
//        Cursor cursor = cr.query(uri, projection, selection + "=?", selectionArgs, null);
//
//        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                int idIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
//                id = Long.parseLong(cursor.getString(idIndex));
//            }
//        }
//        return id;
//    }

//    private Bundle userCodeBundle(String nameFramet){
//        Bundle result = new Bundle();
//        StringBuilder key = new StringBuilder("UserCode");
//        key.append(nameFramet);
//        String keyFragment = key.toString();
//        result.putString(keyFragment, userCode);
//        return result;
//    }


//    private void navegateToGallery(){
//        Fragment galleries = new GalleryFragment();
//
//        if(userCode != null){
//            Bundle bundle = userCodeBundle("Galleries");
//            galleries.setArguments(bundle);
//        }
//
//        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, galleries).addToBackStack(null).commit();
////        Log.d("******** Actiones ===> ", " Ejecuta Galleries Fragment");
//    }


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        if (fakeImage) {
//            imageBitmapFakeImage = null;
//        } else {
//            imageBitmap = null;
//        }
//
//        if (requestCode == Image_Capture_Code) {
//            Bitmap thumbnail = null;
//            if (resultCode == getActivity().RESULT_OK) {
//                Bundle extras = data.getExtras();
//                Bitmap bit = (Bitmap) extras.get("data");
//                if (fakeImage) {
//                    imageBitmapFakeImage = bit;
//                    thumbnail = (Bitmap) Objects.requireNonNull(Objects.requireNonNull(data).getExtras()).get("data");
//                    imageBitmapFakeImage = thumbnail;
//                } else {
//                    imageBitmap = bit;
//                    thumbnail = (Bitmap) Objects.requireNonNull(Objects.requireNonNull(data).getExtras()).get("data");
//                    imageBitmap = thumbnail;
//                    itemImage.setImageBitmap(thumbnail);
//                }
//            } else if (resultCode == getActivity().RESULT_CANCELED) {
//                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_LONG).show();
//            }
//        }
//
//        if (requestCode == PICK_IMAGE) {
//            Bitmap selectedImageBitmap = null;
//            Uri selectedImageUri = data.getData();
//            picturePath = getPath( getActivity( ).getApplicationContext( ), selectedImageUri );
//
//            try {
//                selectedImageBitmap = scaleBitmap(null, selectedImageUri);
//                if (selectedImageUri != null) {
//                    if (fakeImage) {
//                        imageBitmapFakeImage = selectedImageBitmap;
//                    } else {
//                        itemImage.setImageBitmap(selectedImageBitmap);
//                        imageBitmap = selectedImageBitmap;
//                    }
//                } else {
//                    Toast.makeText(context, "Bitmap null", Toast.LENGTH_SHORT).show();
//                }
//
//                if (imageBitmap != null) {
////                    String g1  = Environment.getExternalStorageDirectory().getPath();
////                    String g1  = getExternalStorage(context);
//
//                    Log.d("******** Actiones Bundle AddImagen ***** *** ===> ",  picturePath);
//
//                    deleteImage(picturePath);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }


