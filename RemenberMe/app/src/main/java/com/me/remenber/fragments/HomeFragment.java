package com.me.remenber.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.me.remenber.R;
import com.me.remenber.services.FragmentService;



public class HomeFragment extends Fragment {

    private CardView  cardGalleries, cardListNotes, cardAddImagen, cardAddNote;
    private Context context;
    private FragmentService fragmentService;
    private String userCode;
    public HomeFragment() {
    }

    //  getChildFragmentManager() getParentFragmentManager()

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        if( !getArguments().isEmpty()){
            userCode = getArguments().getString("userCodeBaseActivity");
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity().getApplicationContext();
        fragmentService = new FragmentService(context);
        startedFragment(view);


    }

    private void startedFragment(View view){
        cardGalleries = view.findViewById(R.id.cardGalleries);
        cardListNotes = view.findViewById(R.id.cardlistNotes);
        cardAddImagen = view.findViewById(R.id.cardAddImagen);
        cardAddNote = view.findViewById(R.id.cardAddNote);
        this.listeners();
    }

    private void listeners(){
        this.listenersActionGalleries();
        this.listenersActionListNotes();
        this.listenersActionAddImagen();
        this.listenersActionAddNote();
    }
    private void listenersActionGalleries(){
        cardGalleries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment galleries = new GalleryFragment();
                if(userCode != null){
                    Bundle bundle = fragmentService.userCodeBundle("Galleries", userCode);
//                    Bundle bundle = userCodeBundle("Galleries");
                    galleries.setArguments(bundle);
                }
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, galleries).addToBackStack(null).commit();
//                Log.d("******** Actiones ===> ", " Ejecuta Galleries Fragment");
            }
        });
    }

    private void listenersActionAddImagen(){
        cardAddImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newImageFragment = new NewImageFragment();
                if(userCode != null){
                    Bundle bundle = fragmentService.userCodeBundle("AddImagen", userCode);
//                    Bundle bundle = userCodeBundle("AddImagen");
                    newImageFragment.setArguments(bundle);
                }
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, newImageFragment).addToBackStack(null).commit();
//                Log.d("******** Actiones ===> ", " Ejecuta Add Imagen Fragment");
            }
        });
    }

    private void listenersActionListNotes(){ //UserCodeNotes
        cardListNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment listNoteFragment = new ListNoteFragment();
                if(userCode != null){
                    Bundle bundle =  fragmentService.userCodeBundle("Notes", userCode);
                    listNoteFragment.setArguments(bundle);
                }
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, listNoteFragment).addToBackStack(null).commit();
//                Log.d("******** Actiones ===> ", " Ejecuta Add Note Fragment");
            }
        });
    }

    private void listenersActionAddNote(){//UserCodeAddNote
        cardAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newNoteFragment = new NewNoteFragment();
                if(userCode != null){
                    Bundle bundle = fragmentService.userCodeBundle("AddNote", userCode);
                    newNoteFragment.setArguments(bundle);
                }
                getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, newNoteFragment).addToBackStack(null).commit();
//                Log.d("******** Actiones ===> ", " Ejecuta new Note Fragment");
            }
        });
    }

    private Bundle userCodeBundle(String nameFramet){
        Bundle result = new Bundle();
        StringBuilder key = new StringBuilder("UserCode");
        key.append(nameFramet);
        String keyFragment = key.toString();
        result.putString(keyFragment, userCode);
        return result;
    }

}