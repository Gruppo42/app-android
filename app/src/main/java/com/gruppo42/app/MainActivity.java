package com.gruppo42.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.gruppo42.app.session.SessionManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sessionManager = new SessionManager(this);
        BottomNavigationView navView = findViewById(R.id.bottomNavigationView);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration;
        if(sessionManager.isLoggedIn()) {
            navView.getMenu().getItem(2).setEnabled(false);
            appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.homeFragment, R.id.searchFragment, R.id.userFragment)
                    .build();
        }
        else {
            navView.getMenu().getItem(2).setEnabled(true);
            appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.homeFragment, R.id.searchFragment)
                    .build();
        }
        NavController navController = ((NavHostFragment)this.getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment)).getNavController();
        NavigationUI.setupWithNavController(navView, navController);

    }

}
