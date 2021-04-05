package com.zubisoft.noterecorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;


import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        DrawerLayout drawerLayout = findViewById(R.id.drawer);
        NavigationView navigationView = findViewById(R.id.navigationView);
        Toolbar toolbar = findViewById(R.id.toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,(R.string.open),(R.string.close));
        toggle.syncState();
        drawerLayout.addDrawerListener(toggle);

//        Setting Home fragments
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new HomeFragment()).commit();

//        On navigationClick
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container,new HomeFragment()).commit();
                                drawerLayout.closeDrawer(Gravity.LEFT);
                        break;

                        case R.id.favorite:
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container,new FavoritesFragment()).commit();
                              drawerLayout.closeDrawer(Gravity.LEFT);
                            break;

                            case R.id.categories:
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.container,new CategoryFragment()).commit();
                                     drawerLayout.closeDrawer(Gravity.LEFT);
                                break;
                                case R.id.notes:
                                    getSupportFragmentManager().
                                            beginTransaction()
                                            .replace(R.id.container,new NotesFragment()).commit();
                                    drawerLayout.closeDrawer(Gravity.LEFT);
                                    break;

                }
                return true;
            }
        });

    }
}