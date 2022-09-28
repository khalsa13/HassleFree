package com.example.hasslefree;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.hasslefree.databinding.ActivityMain3Binding;

public class MainActivity3 extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMain3Binding binding;
    private TextView name1, dest1, name2, dest2, name3, dest3, name4, dest4, name5, dest5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMain3Binding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_main3);
        name1 = findViewById(R.id.nameLoc1);
        dest1 = findViewById(R.id.addressLoc1);

        name2 = findViewById(R.id.nameLoc2);
        dest2 = findViewById(R.id.addressLoc2);

        name3 = findViewById(R.id.nameLoc3);
        dest3 = findViewById(R.id.addressLoc3);

        name4 = findViewById(R.id.nameLoc4);
        dest4 = findViewById(R.id.addressLoc4);

        name5 = findViewById(R.id.nameLoc5);
        dest5 = findViewById(R.id.addressLoc5);
        // add set text on these fields after djikstra

/*

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
*/

    }

  /*  @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }*/
}