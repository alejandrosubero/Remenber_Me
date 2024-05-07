package com.me.remenber.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.me.remenber.R;
import com.me.remenber.entitys.SortData;
import com.me.remenber.pojo.SettingsPojo;
import com.me.remenber.services.FragmentService;
import com.me.remenber.services.ItemClickListener;
import com.me.remenber.services.ManagementService;
import com.me.remenber.viewlistrecycle.NoteViewListRecycle;

import java.util.ArrayList;
import java.util.List;


public class ListNoteFragment extends Fragment implements SearchView.OnQueryTextListener, ItemClickListener {

    private FragmentService fragmentService;
    private Context context;
    private String userCode;
    public List<SortData> listaOfNotes = new ArrayList<>();
    private NoteViewListRecycle noteViewListRecycle;
    protected RecyclerView recyclerView;
    private SearchView txtBuscar;

    private ManagementService managementService;
    private SettingsPojo settings;

    private TextView titleViewNoteListView;
    private ImageView noteMenuListView;
    private PopupMenu popupMenu;
    android.view.MenuItem encryptMenu, blockMenu, deleteMenu;

    public ListNoteFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_note, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity().getApplicationContext();
        fragmentService = new FragmentService(context);
        managementService = ManagementService.getInstance();
        titleViewNoteListView = (TextView) view.findViewById(R.id.newItemTitleViewNoteX_list);
        noteMenuListView = (ImageView) view.findViewById(R.id.noteMenu_list);
        settings = managementService.getSettingsUser();
        this.getUserCode();
        this.getlist();
        recyclerView = view.findViewById(R.id.storgeRecyclerView);
        txtBuscar = view.findViewById(R.id.txtBuscar);
        txtBuscar.setOnQueryTextListener(this);
        this.setMenu();

        if (listaOfNotes.size() > 0) {
            this.showList(listaOfNotes);
        }
    }

    private void getlist() {
        if (this.managementService != null) {
            listaOfNotes = managementService.getData2("Note", context, false);
//            listaOfNotes = managementService.getData("Note", context);
//            listaOfNotes = fragmentService.getdata(userCode, "Note");
        }
    }

    private void showList(List<SortData> stordata) {
        noteViewListRecycle = new NoteViewListRecycle(stordata, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(noteViewListRecycle);
    }


    private void getUserCode() {
        Bundle datosRecuperados = getArguments();
        if (datosRecuperados == null) {
            return;
        } else {
            userCode = datosRecuperados.getString("UserCodeNotes");
            if (userCode != null) {
                Log.d("******** Actiones Bundle NOTES  ***** *** ===> ", userCode);
            }
        }
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        noteViewListRecycle.filter(newText);
        return false;
    }


    public void toNote(String noteObject) {
        if (noteObject != null) {
            NewNoteFragment newNoteFragment = new NewNoteFragment();
            Bundle bundleResult = new Bundle();
            bundleResult.putString("ObjectNote", noteObject);
            newNoteFragment.setArguments(bundleResult);
            getChildFragmentManager().beginTransaction().replace(R.id.fragment_container, newNoteFragment).commit();
        }
        Log.d("******** Actiones ===> ", " Ejecuta Note Fragment  Fragment");
        //getParentFragmentManager
    }


    @Override
    public void onItemClick(String objetoSend) {
        NewNoteFragment newNoteFragment = NewNoteFragment.newInstance(objetoSend);
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, newNoteFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private void setMenu() {
        if (settings.isEndebleBlock() || settings.isShowAll()) {
            noteMenuListView.setVisibility(View.VISIBLE);
        } else {
            noteMenuListView.setVisibility(View.GONE);
        }
        noteMenuListView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu = new PopupMenu(context, noteMenuListView);
                popupMenu.getMenuInflater().inflate(R.menu.list_menu, popupMenu.getMenu());
                Menu navMenuLogIn = popupMenu.getMenu();
                navMenuLogIn.findItem(R.id.show_block_list_menu).setVisible(settings.isEndebleBlock());
                navMenuLogIn.findItem(R.id.show_all_list_menu).setVisible(settings.isShowAll());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.show_block_list_menu:
//                                listaOfNotes = managementService.getData2("Note", context, true);
//                                showList(listaOfNotes);
                                showList(managementService.getData2("Note", context, true));
                                break;
                            case R.id.show_all_list_menu:
                                listaOfNotes = managementService.getData("Note", context);
                                showList(listaOfNotes);
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