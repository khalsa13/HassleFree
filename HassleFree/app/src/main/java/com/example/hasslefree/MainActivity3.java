package com.example.hasslefree;

import static com.example.hasslefree.MainActivity.getKmFromLatLong;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.hasslefree.databinding.ActivityMain3Binding;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingDeque;

public class MainActivity3 extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private TextView name1, dest1, name2, dest2, name3, dest3, name4, dest4, name5, dest5, distanceTravel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Intent intent = getIntent();
        List<Destination> list = (List<Destination>) intent.getSerializableExtra("LIST");
        double distances[][] = new double[6][6];
        for(int i=0;i<6;i++)
        {
            for(int j=0;j<6;j++)
            {
               if(i==j)
               {
                   distances[i][j] = 0.0;
               }else
               {
                   distances[i][j] = getKmFromLatLong(list.get(i).getLat(), list.get(i).getLng(), list.get(j).getLat(), list.get(j).getLng());
               }
            }
        }
        distanceTravel = findViewById(R.id.distanceTravel);
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

        Queue<Integer>q = new LinkedBlockingDeque<>();
        q.add(5);
        boolean visited[] = new boolean[5];
        for(int i=0;i<5;i++)
        {
            visited[i] = false;
        }
        double totalDist = 0.0;
        for(int i=0;i<6;i++)
        {
            for(int j=0;j<6;j++)
            {
                Log.d(""+i+" "+j, distances[i][j]+"");
            }
        }

        List<Integer> ordered = new ArrayList<Integer>();
        while(!q.isEmpty())
        {
            int x = q.peek();
            q.remove();
            double minDist = 1000000.0;
            int index = -1;
            for(int i=0;i<5;i++)
            {
                if(i == x)
                    continue;
                if(distances[x][i] < minDist && !visited[i])
                {
                    minDist = distances[x][i];
                    index = i;
                }
            }
            if(index == -1)
                continue;
            visited[index] = true;
            totalDist += distances[x][index];
            q.add(index);
            Log.d("index", ""+index);
            ordered.add(index);
        }
        name1.setText(list.get(ordered.get(0)).getDestinationName());
        dest1.setText(list.get(ordered.get(0)).getExactLocation());

        name2.setText(list.get(ordered.get(1)).getDestinationName());
        dest2.setText(list.get(ordered.get(1)).getExactLocation());

        name3.setText(list.get(ordered.get(2)).getDestinationName());
        dest3.setText(list.get(ordered.get(2)).getExactLocation());

        name4.setText(list.get(ordered.get(3)).getDestinationName());
        dest4.setText(list.get(ordered.get(3)).getExactLocation());

        name5.setText(list.get(ordered.get(4)).getDestinationName());
        dest5.setText(list.get(ordered.get(4)).getExactLocation());

        String res = String.valueOf(totalDist);
        res = res.substring(0,4);
        res = res + " KM";
        distanceTravel.setText(res);
    }

  /*  @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }*/
}