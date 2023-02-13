package it.unimib.exercise.andrea.mediahandler.ui.main;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.Map;

import it.unimib.exercise.andrea.mediahandler.R;
import it.unimib.exercise.andrea.mediahandler.databinding.ActivityMainBinding;
import it.unimib.exercise.andrea.mediahandler.databinding.FragmentSettingsBinding;
import it.unimib.exercise.andrea.mediahandler.repository.user.IUserRepository;
import it.unimib.exercise.andrea.mediahandler.ui.welcome.ViewModelFactoryUser;
import it.unimib.exercise.andrea.mediahandler.ui.welcome.ViewModelUser;
import it.unimib.exercise.andrea.mediahandler.util.ServiceLocator;

public class ActivityMain extends AppCompatActivity {
    private static final String TAG = ActivityMain.class.getSimpleName();
    private ActivityMainBinding binding;
    private MaterialToolbar topAppbar;
    private BottomNavigationView bottomNav;
    private boolean inVideoPlayer = false;
    private ActivityResultLauncher<String[]> multiplePermissionLauncher;
    private ActivityResultContracts.RequestMultiplePermissions multiplePermissionsContract;
    private ViewModelUser userViewModel;
    private FragmentSettingsBinding fragmentSettingsBinding;

    private final String[] PERMISSIONS = {
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            //Manifest.permission.GET_ACCOUNTS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_MEDIA_VIDEO,
            Manifest.permission.READ_MEDIA_AUDIO
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IUserRepository userRepository = ServiceLocator.getInstance().
                getUserRepository(getApplication());
        userViewModel = new ViewModelProvider(
                this,
                new ViewModelFactoryUser(userRepository)).get(ViewModelUser.class);

        //setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);
        topAppbar = binding.topAppbar;

        setSupportActionBar(topAppbar);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().
                findFragmentById(R.id.nav_host_fragment_main);
        NavController navController = navHostFragment.getNavController();
        bottomNav = findViewById(R.id.bottom_navigation);



        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.fragment_audio, R.id.fragment_video,
                R.id.fragment_settings, R.id.fragment_login_auth,
                R.id.fragment_playlist_list).build();

        // For the Toolbar
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(
                topAppbar, navController, appBarConfiguration);

        // For the BottomNavigationView
        NavigationUI.setupWithNavController(bottomNav, navController);



        //Intent intent = getIntent();
        //todo togliere quando fatto lo spostamento
        /*navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller,
                                             @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if(destination.getId() == R.id.videoPlayer) {
                    //topAppbar.setVisibility(View.GONE);
                    //bottomNav.setVisibility(View.GONE);
                    setBarsBaseOnRotation();
                    inVideoPlayer = true;
                } else {
                    //topAppbar.setVisibility(View.VISIBLE);
                    //bottomNav.setVisibility(View.VISIBLE);
                    inVideoPlayer = false;
                    showSystemBars();
                }
            }
        });*/
        multiplePermissionsContract = new ActivityResultContracts.RequestMultiplePermissions();
        multiplePermissionLauncher = registerForActivityResult(multiplePermissionsContract, isGranted -> {
            for(Map.Entry<String, Boolean> set : isGranted.entrySet()) {
                Log.d(TAG, set.getKey() + " " + set.getValue());
            }
            if (!isGranted.containsValue(false)) {
                Log.d(TAG, "All permission have been granted");
            }
        });
        boolean requirePermissions = false;
        for (int i = 0; i < PERMISSIONS.length; i++){
            if (ContextCompat.checkSelfPermission(
                    getApplication(), PERMISSIONS[i]) !=
                    PackageManager.PERMISSION_GRANTED){
                requirePermissions = true;
            }
        }
        if (requirePermissions){
            multiplePermissionLauncher.launch(PERMISSIONS);
        }

    }

    // Register the permissions callback, which handles the user's response to the
    // system permissions dialog. Save the return value, an instance of
    // ActivityResultLauncher, as an instance variable.
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // feature requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            });
    @Override
    public void onPause() {
        //todo save data when app close
        /*userViewModel.saveYTData().observe(this, result -> {
            if (result.isSuccess()) {
                Log.d(TAG, "savePlaylistList: result success" );
            } else {
                Log.d(TAG, "savePlaylistList: result error" );
            }
        });*/
        super.onPause();
    }

    /*private void setBarsBaseOnRotation() {
        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        int orientation = display.getRotation();
        if (orientation == Surface.ROTATION_90
                || orientation == Surface.ROTATION_270) {
            hideSystemBars();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(!inVideoPlayer)
            return;

        Log.d(TAG, "onConfigurationChanged: ");
        int newOrientation = newConfig.orientation;
        if (newOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            hideSystemBars();
        }else{
            showSystemBars();
        }
    }

    private void hideSystemBars() {
        Log.d(TAG, "onConfigurationChanged: landscape");
        bottomNav.setVisibility(View.GONE);
        topAppbar.setVisibility(View.GONE);
        int flags = View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;

        getWindow().getDecorView().setSystemUiVisibility(flags);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    private void showSystemBars() {
        Log.d(TAG, "onConfigurationChanged: horizontal");
        bottomNav.setVisibility(View.VISIBLE);
        topAppbar.setVisibility(View.VISIBLE);
        View decorView = getWindow().getDecorView();
        Log.d(TAG, "onConfigurationChanged: " + decorView.getSystemUiVisibility());
        int flags = View.SYSTEM_UI_FLAG_VISIBLE;
        getWindow().getDecorView().setSystemUiVisibility(flags);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
        }
    }*/
}