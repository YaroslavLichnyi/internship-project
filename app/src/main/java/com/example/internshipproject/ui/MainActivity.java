package com.example.internshipproject.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.internshipproject.FilmRepository;
import com.example.internshipproject.R;

public class MainActivity extends AppCompatActivity {
    private NavController navController;
    //private FilmViewModel viewModel;
   //private FragmentRecyclerViewBinding fragmentRecyclerViewBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //viewModel = ViewModelProviders.of(this).get(FilmViewModel.class);
        setContentView(R.layout.activity_main);
        //new FilmRepository();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //fragmentRecyclerViewBinding = FragmentRecyclerViewBinding.inflate(getLayoutInflater());
        //navController.navigate(R.id.recyclerViewFragment2);
    }

}