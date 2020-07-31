package com.example.internshipproject.ui;

import android.app.SearchManager;
import android.os.Bundle;
import android.view.Menu;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.navigation.Navigation;

import com.example.internshipproject.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Navigation.findNavController(this, R.id.nav_host_fragment);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }
}