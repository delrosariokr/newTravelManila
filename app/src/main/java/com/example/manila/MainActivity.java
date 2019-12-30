package com.example.manila;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelperForLandmarks mydb;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter myAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Landmarks> landmarks;
    private ArrayList<String> landmarks2 = new ArrayList<>();
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_mnlist:
                    selectedFragment = new ListFragment();
                    break;
                case R.id.navigation_landmarks:
                    selectedFragment = new LandmarkFragment();
                    break;
                case R.id.navigation_rewards:
                    selectedFragment = new RewardsFragment();
                    break;
                case R.id.navigation_about:
                    selectedFragment = new AboutFragment();
                    break;
                case R.id.navigation_profile:
                    selectedFragment = new ProfileFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListFragment listFragment = new ListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, listFragment).commit();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loaddata();


    }

    public void saveData() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(landmarks2);
        editor.putString("landmarks", json);
        editor.apply();

    }
    public void loaddata() {
        SharedPreferences sharedPreferences = this.getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("landmarks", null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        landmarks2 = gson.fromJson(json, type);
        if (landmarks2 == null) {
            landmarks2 = new ArrayList<String>();
            landmarks2.add("Rizal");
            landmarks2.add("National Museum of History");
            landmarks2.add("National Museum of Arts");

        }
    }

    public void onCLickAdd(View view) {
        int count = 0;
        final String[] sequences = new String[landmarks2.size()];
        if (landmarks2.isEmpty()) {
            Toast.makeText(this, "Empty Choices", Toast.LENGTH_SHORT).show();
        }
        for (String temp : landmarks2) {
            sequences[count] = temp;
            count++;
        }
        final String[] dd = {"Rizal", "National Museum of History", "National Museum of Arts"};
        SingleAlgo(sequences, view);
    }

    public void SingleAlgo(final CharSequence[] sequences, final View view) {
        mydb = new DatabaseHelperForLandmarks(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Landmarks");
        builder.setItems(sequences, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Boolean inserted = mydb.insertData(sequences[which].toString().trim());
                if (inserted) {
                    loadDatabase(view);
                    landmarks2.remove(sequences[which]);
                    saveData();
                    Toast.makeText(MainActivity.this, "Successfully Inserted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNeutralButton("Select Multiple Items", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MultipleAlgo(sequences, view);
            }
        });
        builder.create();
        builder.show();
    }

    public void MultipleAlgo(final CharSequence[] sequences, final View view) {
        final ArrayList selectedItem = new ArrayList();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Landmarks");
        builder.setMultiChoiceItems(sequences, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked == true) {
                    if (!selectedItem.contains(sequences[which])) {
                        selectedItem.add(sequences[which]);
                        landmarks2.remove(sequences[which]);
                    } else {
                        selectedItem.remove(sequences[which]);
                    }
                } else if (isChecked == false) {
                    selectedItem.remove(sequences[which]);
                }
            }
        });
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < selectedItem.size(); i++) {
                    String task = selectedItem.get(i).toString().trim();
                    Boolean inserted = mydb.insertData(task);
                    if (inserted) {
                        loadDatabase(view);
                        saveData();
                        Toast.makeText(MainActivity.this, "Successfull", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        builder.setNeutralButton("Back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SingleAlgo(sequences, view);
            }
        });
        builder.create();
        builder.show();
    }

    public void onClickDelete(View view) {
        System.out.println("HERE AT DELETE");
        mydb = new DatabaseHelperForLandmarks(this);
        View parent = (View) view.getParent();
        TextView taskTextView = (TextView) parent.findViewById(R.id.TextViewTitle);
        String task = String.valueOf(taskTextView.getText());
        landmarks2.add(task);
        int deleterow = mydb.DeleteData(task);
        if (deleterow > 0) {
            Toast.makeText(this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
            loadDatabase(view);
            saveData();
        } else {
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
        }

    }

    public void loadDatabase(View view) {
       View parent= (View) ListFragment.passdata.getParent();
        recyclerView = parent.findViewById(R.id.List);
        mydb = new DatabaseHelperForLandmarks(this);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        landmarks = new ArrayList<Landmarks>();
        Cursor res = mydb.getAllData();
        if (res.getCount() == 0) {
            landmarks.clear();
        } else {
            while (res.moveToNext()) {
                landmarks.add(new Landmarks(res.getString(1), "Add Details", res.getString(1)));
            }
        }

        myAdapter = new LandmarksAdapter(this, landmarks);
        recyclerView.setAdapter(myAdapter);
    }
}
