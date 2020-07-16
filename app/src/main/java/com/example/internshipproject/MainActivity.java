package com.example.internshipproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.os.Bundle;

import com.example.internshipproject.databinding.FragmentRecyclerViewBinding;

public class MainActivity extends AppCompatActivity {
    private NavController navController;
    //private FragmentRecyclerViewBinding fragmentRecyclerViewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //fragmentRecyclerViewBinding = FragmentRecyclerViewBinding.inflate(getLayoutInflater());
        navController.navigate(R.id.recyclerViewFragment);*/
    }



}