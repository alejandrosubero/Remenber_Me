package com.me.remenber.viewlistrecycle;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.me.remenber.R;

public class NoteListViewHolder extends RecyclerView.ViewHolder {

   protected TextView cardItemNoteTytle;


    public NoteListViewHolder(@NonNull View itemView) {
        super(itemView);
        cardItemNoteTytle = (TextView) itemView.findViewById(R.id.cardItemNoteTytle);
    }


}
