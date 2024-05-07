package com.me.remenber.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.gson.Gson;
import com.me.remenber.R;
import com.me.remenber.adapters.GalleryAdapter;
import com.me.remenber.dataModel.DataConverter;
import com.me.remenber.entitys.SortData;
import com.me.remenber.entitys.User;
import com.me.remenber.pojo.SettingsPojo;
import com.me.remenber.security.Cryptography;
import com.me.remenber.services.FragmentService;
import com.me.remenber.services.JSONPostRequest;
import com.me.remenber.services.ManagementService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class GalleryFragment extends Fragment {

    private GridView gridView;
    private Context context;
    private User user;
    private SortData sortData;
    private String userCode;
    public List<SortData> listaImagens = new ArrayList<>();
    private ManagementService managementService;
    private ImageView galleryMenu;
    private TextView titleViewgalleries;
    private SettingsPojo settings;
    private PopupMenu popupMenu;
    private GalleryAdapter galleryAdapter;
    private static final String ARG_PARAM1 = "param1";
    private String mParam1;
    private static final String BLOCK = "BLOCK";
    private static final String ALL = "ALL";

    public GalleryFragment() {
    }

    public static GalleryFragment newInstance(String param1) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle args = new Bundle();
        if (param1 != null) {
            args.putString(ARG_PARAM1, param1);
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity().getApplicationContext();
        managementService = ManagementService.getInstance();
        settings = managementService.getSettingsUser();
        titleViewgalleries = (TextView) view.findViewById(R.id.item_Title_View_galleries);
        galleryMenu = (ImageView) view.findViewById(R.id.gallery_menu);
        this.getUserCode();
        this.getlist();
        this.showList(view, listaImagens);
        this.viewClickListener();
        this.setMenu();

//        SendRequestGet sentest = (SendRequestGet) new SendRequestGet().execute("https://kanbanfire-9ae89-default-rtdb.firebaseio.com/Users.json");

//        if (listaImagens.size() > 0) {
//            listaImagens.stream().forEach(sortData1 -> testObjecte(sortData1));
//        }
    }

//    private void testObjecte(SortData object) {
//
//        Gson gson = new Gson();
//        String json = gson.toJson(object);
//
//        JSONObject data = null;
//        try {
//            data = new JSONObject(json);
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }
//
//        User user = managementService.getUser();
//        StringBuilder builderUrl = new StringBuilder("https://remember-10ac8-default-rtdb.firebaseio.com/");
//        builderUrl.append(user.getName());
//        builderUrl.append(".json");
//        String url = builderUrl.toString();
//        user.setBackUpUrl(url);
//        JSONPostRequest request = new JSONPostRequest(data, user);
//        request.execute(); //{"name":"-NXeQ5VBbfxZnFqayqNt"}
//    }

    private void showList(View view, List<SortData> list) {
        gridView = view.findViewById(R.id.grid_view_imagen);
        galleryAdapter = new GalleryAdapter(context, listaImagens);
        gridView.setAdapter(galleryAdapter);
    }


    private void workImagenEncrypted(List<SortData> listaTemp) {
        User user = managementService.getUser();
        String key = user.getKeyPrimary().trim();
        Cryptography cryptography = new Cryptography(key);

        listaTemp.stream().forEach(sortDataTemp -> {
            if (sortDataTemp.isEncripted() && sortDataTemp.isItsAlreadyEncripted()) {
                String imagenEncryp = cryptography.decryptAES(sortDataTemp.getImageString());
                Bitmap imageBitmap = DataConverter.stringToBitMap(imagenEncryp);
                byte[] image = DataConverter.convertImageToArray(imageBitmap);
                sortDataTemp.setImage(image);
                listaImagens.add(sortDataTemp);
            } else {
                listaImagens.add(sortDataTemp);
            }
        });
    }

    private void getlist() {
        if (this.managementService != null) {
            if (mParam1 == null) {
                listaImagens = managementService.getData2("Image", context, false);
            }
            if (mParam1 != null && mParam1.equals(BLOCK)) {
                List<SortData> listaTempBlock = managementService.getData2("Image", context, true);
                workImagenEncrypted(listaTempBlock);
            }
            if (mParam1 != null && mParam1.equals(ALL)) {
                List<SortData> listaTempAll = managementService.getData("Image", context);
                workImagenEncrypted(listaTempAll);
            }
        }

    }

    private void viewClickListener() {
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String objetoSend = null;
                SortData selectImage = listaImagens.get(position);
                if (selectImage != null) {
                    Gson gson = new Gson();
                    objetoSend = gson.toJson(selectImage);
                    Fragment fullScreenImage = new FullScreenImageFragment();
                    if (objetoSend != null) {
                        Bundle bundle = new FragmentService(context).codeBundle("Imagen", "Galleries", objetoSend);
                        fullScreenImage.setArguments(bundle);
                    }
                    getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, fullScreenImage).addToBackStack(null).commit();
                }
            }
        });
    }

    private void getUserCode() {
        Bundle datosRecuperados = getArguments();
        if (datosRecuperados == null) {
            return;
        } else {
            userCode = datosRecuperados.getString("UserCodeGalleries");
        }
    }


    private void setMenu() {
        if (settings.isEndebleBlock() || settings.isShowAll()) {
            galleryMenu.setVisibility(View.VISIBLE);
        } else {
            galleryMenu.setVisibility(View.GONE);
        }
        galleryMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu = new PopupMenu(context, galleryMenu);
                popupMenu.getMenuInflater().inflate(R.menu.gallery_menu, popupMenu.getMenu());
                Menu navMenuLogIn = popupMenu.getMenu();
                navMenuLogIn.findItem(R.id.show_block_gallery_menu).setVisible(settings.isEndebleBlock());
                navMenuLogIn.findItem(R.id.show_all_gallery_menu).setVisible(settings.isShowAll());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Fragment galleries = null;
                        switch (item.getItemId()) {
                            case R.id.show_block_gallery_menu:
                                galleries = GalleryFragment.newInstance(BLOCK);
                                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, galleries).addToBackStack(null).commit();
                                break;
                            case R.id.show_all_gallery_menu:
                                galleries = GalleryFragment.newInstance(ALL);
                                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, galleries).addToBackStack(null).commit();
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

}