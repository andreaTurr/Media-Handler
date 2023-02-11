package it.unimib.exercise.andrea.mediahandler.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import it.unimib.exercise.andrea.mediahandler.R;
import it.unimib.exercise.andrea.mediahandler.databinding.ActivityMainBinding;

public class ActivityMain extends AppCompatActivity {
    private static final String TAG = ActivityMain.class.getSimpleName();
    private ActivityMainBinding binding;
    private MaterialToolbar topAppbar;
    private BottomNavigationView bottomNav;
    private boolean inVideoPlayer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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