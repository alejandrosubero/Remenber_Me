package com.me.remenber;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.me.remenber.dataModel.DataConverter;
import com.me.remenber.entitys.SortData;
import com.me.remenber.entitys.User;
import com.me.remenber.fragments.GalleryFragment;
import com.me.remenber.fragments.HomeFragment;
import com.me.remenber.fragments.ListNoteFragment;
import com.me.remenber.fragments.NewImageFragment;
import com.me.remenber.fragments.NewNoteFragment;
import com.me.remenber.fragments.SettingFragment;
import com.me.remenber.fragments.ViewStartedFragment;
import com.me.remenber.pojo.SettingsPojo;
import com.me.remenber.services.FragmentService;
import com.me.remenber.services.ManagementService;
import com.me.remenber.services.SettingsService;

import java.util.Timer;
import java.util.TimerTask;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    public static User storageUser;
    private String code;
    public Context context;
    private ManagementService managementService;
    private FragmentService fragmentService;
    private NavigationView navigationView;
    private SettingsService settingsService;
    private Dialog dialogMenuShareImage;
    private TextView textViewShareTiTles2, textViewSareTiTles1, itemDetailNameTitleShare;
    private EditText passKeyShare;
    private Button btn_share;
    private ImageView btn_share_close;
    private Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_base);
        allocateActivityTitle("Home");
        context = BaseActivity.this;
        Intent intent = getIntent();
        managementService = ManagementService.getInstance();
        fragmentService = new FragmentService(context);
        try {
            storageUser = (User) intent.getSerializableExtra("object");
        }catch (Exception e){
            e.printStackTrace();
            if(storageUser == null){
                storageUser = managementService.getUser();
            }
        }

        startDialogShare(context);
        this.setCodeUser();
        this.getConfiguration();
        this.addMenue(savedInstanceState);
    }

    private void setCodeUser() {
        if (storageUser != null) {
            code = storageUser.getCodeUser();
            if (managementService != null) {
                managementService.setUser(storageUser);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void getConfiguration() {
        settingsService = new SettingsService(context);
        String code = storageUser.getCodeUser();
        SettingsPojo settings = settingsService.startedSetting(code);
        managementService.setSettingsUser(settings);
    }

    private void addMenue(Bundle savedInstanceState) {
        Toolbar toolbar = findViewById(R.id.toolbar); //Ignore red line errors
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            Fragment homeFragment = new HomeFragment();
            if (code != null) {
                Bundle args = new Bundle();
                args.putString("userCodeBaseActivity", code);
                homeFragment.setArguments(args);
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.nav_home:
                Fragment homeFragment = new HomeFragment();
                if (code != null) {
                    Bundle args = new Bundle();
                    args.putString("userCodeBaseActivity", code);
                    homeFragment.setArguments(args);
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
                break;

            case R.id.nav_galleries:
                Fragment galleries = new GalleryFragment();
                if (code != null) {
                    Bundle bundle = fragmentService.userCodeBundle("Galleries", code);
                    galleries.setArguments(bundle);
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, galleries).addToBackStack(null).commit();
//                Log.d("******** Actiones ===> ", " Ejecuta Galleries Fragment");
                break;

            case R.id.nav_list:
                Fragment listNoteFragment = new ListNoteFragment();
                if (code != null) {
                    Bundle bundle = fragmentService.userCodeBundle("Notes", code);
                    listNoteFragment.setArguments(bundle);
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, listNoteFragment).addToBackStack(null).commit();
//                Log.d("******** Actiones ===> ", " Ejecuta listNote Fragment");
                break;

            case R.id.nav_photo:
                Fragment newImageFragment = new NewImageFragment();
                if (code != null) {
                    Bundle bundle = fragmentService.userCodeBundle("AddImagen", code);
                    newImageFragment.setArguments(bundle);
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, newImageFragment).addToBackStack(null).commit();
//                Log.d("******** Actiones ===> ", " Ejecuta Add Imagen Fragment");
                break;


            case R.id.nav_addNote:
                Fragment newNoteFragment = new NewNoteFragment();
                if (code != null) {
                    Bundle bundle = fragmentService.userCodeBundle("AddNote", code);
                    newNoteFragment.setArguments(bundle);
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, newNoteFragment).addToBackStack(null).commit();
                break;

            case R.id.nav_settings:
                dialogMenuShareImage.show();
                break;

            case R.id.nav_close:
//                System.exit(1);
                this.logout();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void allocateActivityTitle(String titleString) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(titleString);
        }
    }


    private void startDialogShare(Context cont) {

        dialogMenuShareImage = new Dialog(cont);
        dialogMenuShareImage.setContentView(R.layout.share_dialog);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialogMenuShareImage.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background));
        }
        dialogMenuShareImage.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialogMenuShareImage.setCancelable(false);

        btn_share_close = (ImageView) dialogMenuShareImage.findViewById(R.id.btn_share_close);
        btn_share = (Button) dialogMenuShareImage.findViewById(R.id.btn_share);
        passKeyShare = (EditText) dialogMenuShareImage.findViewById(R.id.passKeyShare);
        textViewSareTiTles1 = (TextView) dialogMenuShareImage.findViewById(R.id.textViewSareTiTles1);
        textViewShareTiTles2 = (TextView) dialogMenuShareImage.findViewById(R.id.textViewShareTiTles2);
        itemDetailNameTitleShare = (TextView) dialogMenuShareImage.findViewById(R.id.itemDetailNameTitleShare);
        textViewSareTiTles1.setText("Open ");
        textViewShareTiTles2.setText("Setting");
        itemDetailNameTitleShare.setText("Key for open Setting");
        btn_share.setText("Check and Open");
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
                openSetting(view);
            }
        });
    }

    private void openSetting(View view) {
        String pass = passKeyShare.getText().toString();
        storageUser = managementService.getUser();
        if (pass.equals(storageUser.getKeyShare()) || pass.equals(storageUser.getUserResponse())) {
            this.cleanDialogo();
            Fragment settingFragment = new SettingFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, settingFragment).addToBackStack(null).commit();
        }
    }

    private void cleanDialogo() {
        passKeyShare.setText("");
        passKeyShare.setHint("key for share...");
        dialogMenuShareImage.dismiss();
    }

    private void logout(){
        Intent intent = new Intent(context, MainActivity.class);
        managementService.resetManagementService();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private class LogOutTimerTask extends TimerTask {
        @Override
        public void run() {
            logout();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (timer != null) {
            timer.cancel();
            Log.i("Main", "cancel timer");
            timer = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer = new Timer();
        Log.i("Main", "Invoking logout timer");
        LogOutTimerTask logoutTimeTask = new LogOutTimerTask();
        timer.schedule(logoutTimeTask, 600000); //auto logout in 10 minutes
    }

}

//    private void shareImage(Bitmap bitmap) {
//        String bitmapPath = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "title", null);
//        Uri bitmapUri = Uri.parse(bitmapPath);
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setType("image/png");
//        intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
//        dialogMenuShareImage.dismiss();
//        startActivity(Intent.createChooser(intent, "Share"));
//    }