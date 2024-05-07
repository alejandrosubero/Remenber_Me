package com.me.remenber.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.me.remenber.dataModel.DataConverter;
import com.me.remenber.entitys.SortData;
import com.me.remenber.pojo.SettingsPojo;
import com.me.remenber.services.ManagementService;

import java.util.ArrayList;
import java.util.List;

public class GalleryAdapter extends BaseAdapter {

    private Context context;

    public List<SortData> listaImagen = new ArrayList<>();

    private SettingsPojo settings;
    private ManagementService managementService;

    public GalleryAdapter(Context context) {
        this.context = context;
        managementService = ManagementService.getInstance();
        settings = managementService.getSettingsUser();
    }

    public GalleryAdapter(Context context, List<SortData> listaImagen) {
        this.context = context;
        this.listaImagen = listaImagen;
        managementService = ManagementService.getInstance();
        settings = managementService.getSettingsUser();
    }


    @Override
    public int getCount() {
        return listaImagen.size();
    }

    @Override
    public Object getItem(int position) {
        return listaImagen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(context);
        Bitmap imageBitmap = null;
        byte[] array = null;
        SortData sotr = listaImagen.get(position);

        if(sotr.getImage() != null){
            if (settings.isEndebleFakeImage() && sotr.getImageFail() != null){
                array = sotr.getImageFail();
            }else{
                array =  sotr.getImage();
            }
            imageBitmap = DataConverter.convertArrayToImage(array);
            imageView.setImageBitmap(imageBitmap);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(340,350));
        }
        return imageView;
    }

    public List<SortData> getListaImagen() {
        return listaImagen;
    }

    public void setListaImagen(List<SortData> listaImagen) {
        this.listaImagen = listaImagen;
    }
}
