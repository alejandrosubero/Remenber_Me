package com.me.remenber.viewlistrecycle;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.me.remenber.BaseActivity;
import com.me.remenber.R;
import com.me.remenber.entitys.SortData;
import com.me.remenber.fragments.FullScreenImageFragment;
import com.me.remenber.fragments.ListNoteFragment;
import com.me.remenber.services.ItemClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class NoteViewListRecycle extends RecyclerView.Adapter<NoteListViewHolder>{


    private List<SortData> data;
    protected List<SortData> listaOriginalData;

    private ItemClickListener itemClickListener;

    public NoteViewListRecycle(List<SortData> storages, ItemClickListener itemClickListener) {
       this.data = storages;
        this.itemClickListener = itemClickListener;
        listaOriginalData = new ArrayList<>();
        listaOriginalData.addAll(data);
    }


    @NonNull
    @Override
    public NoteListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.note_item_list_layout, parent, false
        );
        NoteListViewHolder holder = new NoteListViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteListViewHolder holder, int position) {
        SortData sortData = data.get(position);

        if (sortData.getName() != null) {
            holder.cardItemNoteTytle.setText(sortData.getName());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sortData != null) {
                    Gson gson = new Gson();
                    String objetoSend = gson.toJson(sortData);
                    itemClickListener.onItemClick(objetoSend);
                }
            }
        });

//        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                if(sortData != null) {
//                    Gson gson = new Gson();
//                    String objetoSend = gson.toJson(sortData);
//                    itemClickListener.onItemClick(objetoSend);
//                }
//                return false;
//            }
//        });

    }



    @Override
    public int getItemCount() {
        return data.size();
    }



    public void filter(final String txtBuscar) {
        int longitud = txtBuscar.length();
        if (longitud == 0) {
            data.clear();
            data.addAll(listaOriginalData);
        } else {

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                List<SortData> collecion = new ArrayList<SortData>();

                collecion = data.stream()
                        .filter(i -> i.getName().toLowerCase().contains(txtBuscar.toLowerCase()))
                        .collect(Collectors.toList());

                if (collecion.size() > 0) {
                    data.clear();
                    data.addAll(collecion);
                } else {
                    collecion = data.stream()
                            .filter(i -> i.getTypeData().toLowerCase().contains(txtBuscar.toLowerCase()))
                            .collect(Collectors.toList());
                    data.clear();
                    data.addAll(collecion);
                }

            } else {
                data.clear();
                for (SortData nota: listaOriginalData) {
                    if (nota.getName().toLowerCase().contains(txtBuscar.toLowerCase())) {
                        data.add(nota);
                    }
                }
                if (data.size() == 0) {
                    for (SortData notaa : listaOriginalData) {
                        if (notaa.getTypeData().toLowerCase().contains(txtBuscar.toLowerCase())) {
                            data.add(notaa);
                        }
                    }
                }
            }

            notifyDataSetChanged();
        }
    }


    private String formatDate(Date dates) {
        String forDate = "";
        SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        SimpleDateFormat print = new SimpleDateFormat("MMM d, yyyy");
        try {
            Date parsedDate = sdf.parse(String.valueOf(dates));
            forDate = print.format(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return forDate;
    }
}

