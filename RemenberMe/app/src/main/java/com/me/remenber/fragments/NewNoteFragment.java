package com.me.remenber.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.me.remenber.R;
import com.me.remenber.dao.SortDataDao;
import com.me.remenber.entitys.SortData;
import com.me.remenber.entitys.User;
import com.me.remenber.pojo.SettingsPojo;
import com.me.remenber.repositorys.SortDataDataBase;
import com.me.remenber.security.Cryptography;
import com.me.remenber.services.FragmentService;
import com.me.remenber.services.ManagementService;


public class NewNoteFragment extends Fragment implements View.OnTouchListener, GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private Context context;
    //    private SortDataDao sortDataDao;
    private SortData sortData;
    private String userCode;
    private Button btnCapture;
    private EditText noteTitle, noteNote;

    private TextView newItemTitleView;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private boolean isEdit = false;
    private ManagementService managementService;
//    private FragmentService fragmentService;

    private GestureDetector mGestureDetector;

    private ImageView noteMenu;

    private static final String TAG = "NewNoteFragment";
    private boolean isEncript = false;

    private SettingsPojo settings;

    private PopupMenu popupMenu;
    private android.view.MenuItem encryptMenu, blockMenu, deleteMenu;

    private boolean  tagNote = false;


    public NewNoteFragment() {
        // Required empty public constructor
    }

    public static NewNoteFragment newInstance(String param1) {
        NewNoteFragment fragment = new NewNoteFragment();
        Bundle args = new Bundle();
        if (param1 != null) {
            args.putString(ARG_PARAM1, param1);
        }
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            Gson gson = new Gson();
            sortData = gson.fromJson(mParam1, SortData.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_note, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = view.getContext();
        managementService = ManagementService.getInstance();
        settings = managementService.getSettingsUser();
        this.getBundle();
        this.startedFragment(view);
    }

    private void getBundle() {
        try {
            Bundle datosRecuperados = getArguments();
            if (datosRecuperados == null) {
                return;
            } else {
                userCode = datosRecuperados.getString("UserCodeAddNote");
//                if (userCode != null) {
////                    Log.d("******** Actiones Bundle Add Note ***** *** ===> ", "Recovered");
//                }
                if (!mParam1.equals("param1") && sortData == null) {
                    Gson gson = new Gson();
                    sortData = gson.fromJson(mParam1, SortData.class);
                    managementService.setSortData(sortData);
                    isEdit = true;
                } else if (sortData != null) {
                    managementService.setSortData(sortData);
                    isEdit = true;
                }
            }
        } catch (Exception e) {
//            Log.d("********!!!-> Error in to recover Bundle <-!!!***** *** ===> ", " ");
        }
    }


    private void startedFragment(View view) {
        btnCapture = (Button) view.findViewById(R.id.btnCaptureNote);
        noteTitle = (EditText) view.findViewById(R.id.noteTitle);
        noteNote = (EditText) view.findViewById(R.id.noteNote);
        newItemTitleView = (TextView) view.findViewById(R.id.newItemTitleViewNoteX);
        noteMenu = (ImageView) view.findViewById(R.id.noteMenu);
        newItemTitleView.setOnTouchListener(this);
        mGestureDetector = new GestureDetector(view.getContext(), this);

        if (this.isEdit) {
            btnCapture.setText("Update");
            newItemTitleView.setText("Note: ");
            btnCapture.setVisibility(View.GONE);
            noteMenu.setVisibility(View.GONE);
            if (settings.isEcryptNotes() || settings.isEndebleBlock() || settings.isErasableNote()) {
                noteMenu.setVisibility(View.VISIBLE);
            }
            this.setupOnBackPressed();
        } else {
            btnCapture.setText("Add");
            newItemTitleView.setText("New Note: ");
            btnCapture.setVisibility(View.VISIBLE);
            noteMenu.setVisibility(View.GONE);
        }

        this.listener();
        if (sortData != null) {
            noteTitle.setText(sortData.getName());
            noteNote.setText(sortData.getNote());
        }
//        registerForContextMenu(noteMenu);

        noteMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu = new PopupMenu(context, noteMenu);
                popupMenu.getMenuInflater().inflate(R.menu.image_menu, popupMenu.getMenu());
                Menu navMenuLogIn = popupMenu.getMenu();
                navMenuLogIn.findItem(R.id.encrypt_menu).setVisible(settings.isEcryptNotes());
                navMenuLogIn.findItem(R.id.block_menu).setVisible(settings.isEndebleBlock());
                navMenuLogIn.findItem(R.id.delete_menu).setVisible(settings.isErasableNote());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.encrypt_menu:
                                sortData = managementService.getSortData();
                                if (sortData.isEncripted()) {
                                    sortData.setEncripted(false);
                                    Toast.makeText(requireContext(), "Encrypt disable", Toast.LENGTH_LONG).show();
                                } else {
                                    sortData.setEncripted(true);
                                    Toast.makeText(requireContext(), "Encrypt active", Toast.LENGTH_LONG).show();
                                }
                                managementService.setSortData(sortData);
                                managementService.updateSortData(context);
                                break;

                            case R.id.block_menu:
                                sortData = managementService.getSortData();
                                if (sortData.isIsblock()) {
                                    sortData.setIsblock(false);
                                } else {
                                    sortData.setIsblock(true);
                                }
                                managementService.setSortData(sortData);
                                managementService.updateSortData(context);
                                Toast.makeText(requireContext(), "Block active", Toast.LENGTH_LONG).show();
                                navagateToList();
                                break;

                            case R.id.delete_menu:
                                sortData = managementService.getSortData();
                                managementService.deleteSortData(context);
                                Toast.makeText(requireContext(), "The note is delete.", Toast.LENGTH_LONG).show();
                                navagateToList();
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }


    private void navagateToList() {
        Fragment listNoteFragment = new ListNoteFragment();
        if (sortData.getCodeUser() != null) {
            FragmentService fragmentService = new FragmentService(context);
            Bundle bundle = fragmentService.userCodeBundle("Notes", sortData.getCodeUser());
            listNoteFragment.setArguments(bundle);
        }
        getParentFragmentManager().beginTransaction().replace(R.id.fragment_container, listNoteFragment).addToBackStack(null).commit();
//        Log.d("******** Actiones ===> ", " Ejecuta Add Note Fragment");
    }


    private void setupOnBackPressed() {
        // https://www.youtube.com/watch?v=ubb_qghQYR8
        requireActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isEnabled()) {
                    tagNote = true;
                    updateNote();
                    setEnabled(false);
                    requireActivity().onBackPressed();
                }
            }
        });
    }


    private void listener() {
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit) {
                    updateNote();
                } else {
                    saveNote();
                }
            }
        });

        noteNote.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if( sortData!=null && sortData.getNote()!=null && noteNote != null && (noteNote.getText().toString().length() > sortData.getNote().length()
                        || noteNote.getText().toString().length() < sortData.getNote().length()
                            || (noteNote.getText().toString().length() == sortData.getNote().length()
                                && sortData.getNote().equals(noteNote.getText().toString()))) ){
                    updateNote();
                }
            }
        });


    }


    private void saveNote() {
        SortData newNote = new SortData();
        if (userCode != null) {
            newNote.setTypeData("Note");
            newNote.setUse("Note");
            newNote.setCodeUser(userCode);
            String tytle = noteTitle.getText().toString();
            if (tytle != null) {
                newNote.setName(tytle);
                newNote.setName(tytle);
            }
            String note = noteNote.getText().toString();
            if (note != null) {
                newNote.setNote(note);
            }
            managementService.setSortData(newNote);
            managementService.insertSortData(context);
            Toast.makeText(context, "The Note was Saved...", Toast.LENGTH_SHORT).show();
            this.cleanView();
        } else {
            Toast.makeText(context, "Can't save the Note (Error...)", Toast.LENGTH_SHORT).show();
        }
    }



    private void updateNote() {
        SortData newNote = sortData;
        if (newNote.getCodeUser() != null) {
            String tytle = noteTitle.getText().toString();
            if (tytle != null) {
                newNote.setName(tytle);
                newNote.setName(tytle);
            }
            String note = noteNote.getText().toString();
            if (note != null) {
                newNote.setNote(note);
            }
            managementService.setSortData(newNote);
            managementService.updateSortData(context);

            if(tagNote) {
                Toast.makeText(context, "The Note was update...", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Can't save the Note (Error...)", Toast.LENGTH_SHORT).show();
        }
    }

    private void cleanView() {
        noteTitle.setText("");
        noteNote.setText("");
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
// https://www.youtube.com/watch?v=xHCsL5QOSv4&t=224s
        if (v.getId() == R.id.newItemTitleViewNoteX) {
            mGestureDetector.onTouchEvent(event);
            return true;
        }
        return false;

//        int action = event.getAction();
//
//        switch(action) {
//            case (MotionEvent.ACTION_DOWN) :
//                Log.d(TAG,"Action was DOWN");
//                return true;
//            case (MotionEvent.ACTION_MOVE) :
//                Log.d(TAG,"Action was MOVE");
//                Log.d(TAG, " onTouch (x,y"+ "( "+ event.getX() +", "+ event.getY()+")");
//                return true;
//            case (MotionEvent.ACTION_UP) :
//                Log.d(TAG,"Action was UP");
//                return true;
//            case (MotionEvent.ACTION_CANCEL) :
//                Log.d(TAG,"Action was CANCEL");
//                return true;
//            case (MotionEvent.ACTION_OUTSIDE) :
//                Log.d(TAG,"Movement occurred outside bounds of current screen element");
//                return true;
//            default :
//                return super.getActivity().onTouchEvent(event);
//        }

    }

    /*
     touch events
   */
    @Override
    public boolean onDown(@NonNull MotionEvent e) {
//        Log.d("********!!!-> ACTIONS onDown  <-!!!***** *** ===> ", " ");
        return false;
    }

    @Override
    public void onShowPress(@NonNull MotionEvent e) {
//        Log.d("********!!!-> ACTIONS onShowPress  <-!!!***** *** ===> ", " ");
    }

    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent e) {
//        Log.d("********!!!-> ACTIONS onSingleTapUp  <-!!!***** *** ===> ", " ");
        return false;
    }

    @Override
    public boolean onScroll(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
//        Log.d("********!!!-> ACTIONS onScroll  <-!!!***** *** ===> ", " ");
        return false;
    }

    @Override
    public void onLongPress(@NonNull MotionEvent e) {
//        Log.d("********!!!-> ACTIONS onLongPress  <-!!!***** *** ===> ", " ");
    }

    @Override
    public boolean onFling(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
//        Log.d("********!!!-> ACTIONS onFling  <-!!!***** *** ===> ", " ");
        return false;
    }


    /*
      DoubleTap
    */
    @Override
    public boolean onSingleTapConfirmed(@NonNull MotionEvent e) {
//        Log.d(TAG, " onSingleTapConfirmed <-!!!***** *** ===>");
        return false;
    }

    @Override
    public boolean onDoubleTap(@NonNull MotionEvent e) {
//        Log.d(TAG, " onDoubleTap <-!!!***** *** ===>");
        if (noteNote.getText() != null && noteNote.getText().toString().length() > 0 && sortData != null && sortData.isEncripted()) {
            this.dobleEncript();
        }

        return true;
    }

    @Override
    public boolean onDoubleTapEvent(@NonNull MotionEvent e) {
//        Log.d(TAG, " onDoubleTapEvent <-!!!***** *** ===>");
        return false;
    }


    private void dobleEncript() {
        try {
            SortData newNote = sortData;
            isEncript = newNote.isItsAlreadyEncripted();
            User user = managementService.getUser();
            String key = user.getKeyPrimary().trim();
            Cryptography cryptography = new Cryptography(key);

            String note = "No note...";
            if(newNote.getNote() != null && newNote.getNote().length() > 0){
                note = newNote.getNote();
            }


            if (!isEncript && !note.equals("No note...") && newNote.isEncripted()) {
//                Log.d("DobleTap Encript: ", " " + isEncript);
                String noteEncryp1 = cryptography.encryptAES(note);
                newNote.setNote(noteEncryp1);
                newNote.setItsAlreadyEncripted(true);
                managementService.setSortData(newNote);
                managementService.updateSortData(context);
                noteNote.setText(noteEncryp1);

            } else if (isEncript && !note.equals("No note...") && newNote.isEncripted()) {
//                Log.d("DobleTap Encript: ", " " + isEncript);
                String noteDencryp1 = cryptography.decryptAES(note);
                newNote.setNote(noteDencryp1);
                newNote.setItsAlreadyEncripted(false);
                managementService.setSortData(newNote);
                managementService.updateSortData(context);
                noteNote.setText(noteDencryp1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
