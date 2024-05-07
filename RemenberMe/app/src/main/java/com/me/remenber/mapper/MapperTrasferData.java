package com.me.remenber.mapper;

import android.graphics.Bitmap;

import com.me.remenber.dataModel.DataConverter;
import com.me.remenber.dto.BackupDTO;
import com.me.remenber.entitys.SortData;

public class MapperTrasferData {


    public BackupDTO mapperEntityToDTO(SortData data) {
        BackupDTO dto = null;
        if (data != null) {
            dto = new BackupDTO();
            if (data.getKeyr() != null) {
                dto.setUnbral(data.getKeyr());
            }
            if (data.getName() != null) {
                dto.setMeet(data.getName());
            }
            if (data.getTypeData() != null) {
                dto.setPaprica(data.getTypeData());
            }
            if (data.getUse() != null) {
                dto.setPanela(data.getUse());
            }
            if (data.getNote() != null) {
                dto.setSoup(data.getNote());
            }
            if (data.getImage() != null || data.getImageString() != null) {
                if (data.getImageString() != null) {
                    dto.setRecipes(data.getImageString());
                } else {
                    String imageString = byteToString(data.getImage());
                    dto.setRecipes(imageString);
                }
            }
            if (data.getImageFail() != null) {
                String imageString = byteToString(data.getImageFail());
                dto.setRecipesLasagna(imageString);
            }
        }
        return dto;
    }


    public SortData mapperDTOToEntity(BackupDTO dto) {

        SortData sortData = null;

        if (dto != null) {
            sortData = new SortData();

            if (dto.getUnbral() != null) {
                sortData.setKeyr(dto.getUnbral());
            }
            if (dto.getMeet() != null) {
                sortData.setName(dto.getMeet());
            }
            if (dto.getPaprica() != null) {
                sortData.setTypeData(dto.getPaprica());
            }
            if (dto.getPanela() != null) {
                sortData.setUse(dto.getPanela());
            }
            if (dto.getSoup() != null) {
                sortData.setNote(dto.getSoup());
            }
            if (dto.getRecipes() != null) {
                byte[] bite = stringTobyte(dto.getRecipes());
                if(bite != null){
                    sortData.setImage(bite);
                }
            }
            if (dto.getRecipesLasagna() != null) {
                byte[] bite = stringTobyte(dto.getRecipesLasagna());
                if(bite != null){
                    sortData.setImageFail(bite);
                }
            }
        }
        return sortData;
    }


    private String byteToString(byte[] data) {
        Bitmap bitmap = DataConverter.convertArrayToImage(data);
        String imageString = DataConverter.bitMapToString(bitmap);
        return imageString;
    }

    private byte[] stringTobyte(String data) {
        byte[] image = null;
                Bitmap bitmap = DataConverter.stringToBitMap(data);
        if(bitmap != null){
            image = DataConverter.convertImageToArray(bitmap);
        }
        return image;
    }

}
