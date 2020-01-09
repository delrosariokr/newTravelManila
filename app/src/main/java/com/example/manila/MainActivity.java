package com.example.manila;

import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelperForUsers mydb;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    private String user;
    private ArrayList<String> landmarks2 = new ArrayList<>();
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            mydb = new DatabaseHelperForUsers(getApplicationContext());
            int PassUser = mydb.getIDFromTableUser(user);
            Bundle bundle = new Bundle();
            bundle.putInt("User", PassUser);
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_mnlist:
                    selectedFragment = new ListFragment();
                    selectedFragment.setArguments(bundle);
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
        mydb = new DatabaseHelperForUsers(this);
        calendar = Calendar.getInstance();
        user = getIntent().getStringExtra("User");
        int PassUser = mydb.getIDFromTableUser(user);
        System.out.println(PassUser);
        Bundle bundle = new Bundle();
        bundle.putInt("User", PassUser);
        ListFragment listFragment = new ListFragment();
        listFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, listFragment).commit();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public void onClickAdd(View view) {
        CharSequence[] sequences = new String[10];
        mydb = new DatabaseHelperForUsers(this);
        int UserID = mydb.getIDFromTableUser(user);
        Cursor res = mydb.getAllDataForLandmarks();
        int i = 0;
        while (res.moveToNext()) {
            if (!mydb.CheckIfUser_ListExist(UserID, res.getInt(0))) {
                sequences[i] = mydb.getLandmarksBaseOnID(res.getInt(0));
                i++;
            }
        }
        SingleAlgo(sequences,UserID);

    }




    public void SingleAlgo(final CharSequence[] sequences, final int userID) {
        mydb = new DatabaseHelperForUsers(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Landmarks");
        builder.setItems(sequences, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                date = dateFormat.format(calendar.getTime());
                int landmarkID = mydb.getIDFromTableLandmarks(sequences[which].toString());
                Boolean CheckInsert = mydb.insertDataInUserVisit(userID, landmarkID, date);
                if (CheckInsert) {
                    Toast.makeText(MainActivity.this, "Successfully Inserted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }

        });
        builder.setNeutralButton("Select Multiple Items", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                MultipleAlgo(sequences, view, userID);
            }
        });
        builder.create();
        builder.show();
    }

    //
//    public void MultipleAlgo(final CharSequence[] sequences, final View view, final int userID) {
//        final ArrayList selectedItem = new ArrayList();
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Landmarks");
//        builder.setMultiChoiceItems(sequences, null, new DialogInterface.OnMultiChoiceClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
//                if (isChecked == true) {
//                    if (!selectedItem.contains(sequences[which])) {
//                        selectedItem.add(sequences[which]);
//                        landmarks2.remove(sequences[which]);
//                    } else {
//                        selectedItem.remove(sequences[which]);
//                    }
//                } else if (isChecked == false) {
//                    selectedItem.remove(sequences[which]);
//                }
//            }
//        });
//        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                 dateFormat = new SimpleDateFormat("MM/dd/yyyy");
//                date = dateFormat.format(calendar.getTime());
//                for (int i = 0; i < selectedItem.size(); i++) {
//                    String task = selectedItem.get(i).toString().trim();
//                    int landmarkID = myDBUSER.getIDFromTableLandmarks(task);
//                    Boolean CheckInsert = myDBUSER.insertDataInUserVisit(userID, landmarkID, date);
//                    if (CheckInsert) {
//                        Cursor res = myDBUSER.getAllDataForList(userID);
//                        while (res.moveToNext()) {
//                            String temp = myDBUSER.getLandmarksBaseOnID(res.getInt(1));
//                            Boolean check = mydb.CheckifLandmarkExist(temp);
//                            if (!check) {
//                                Boolean inserted = mydb.insertData(userID,landmarkID, temp);
//                                if (inserted) {
//                                    loadDatabase(view,userID);
//                                    saveData();
//                                    Toast.makeText(MainActivity.this, "Successfull", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        });
//
//        builder.setNeutralButton("Back", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                SingleAlgo(sequences, view, userID);
//            }
//        });
//        builder.create();
//        builder.show();
//    }
//
    public void onClickStartActvitiy(View view) {
        View parent = (View) view.getParent();
        TextView text = (TextView) parent.findViewById(R.id.TextViewTitleDisplay);
        Intent intent = new Intent(getBaseContext(), LandmarkInfoActivity.class);
        intent.putExtra("Title", text.getText().toString());
        intent.putExtra("User", user);
        startActivity(intent);
    }
//
//    //    public void onClickDelete(View view) {
////        System.out.println("HERE AT DELETE");
////        mydb = new DatabaseHelperForLandmarks(this);
////        View parent = (View) view.getParent();
////        TextView taskTextView = (TextView) parent.findViewById(R.id.TextViewTitle);
////        String task = String.valueOf(taskTextView.getText());
////        landmarks2.add(task);
////        int deleterow = mydb.DeleteData(task);
////        if (deleterow > 0) {
////            Toast.makeText(this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
////            loadDatabase(view);
////            saveData();
////        } else {
////            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
////        }
////
////    }
////
//    public void loadDatabase(View view,int userID) {
//        View parent = (View) ListFragment.passdata.getParent();
//        recyclerView = parent.findViewById(R.id.List);
//        mydb = new DatabaseHelperForLandmarks(this);
//        recyclerView.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        landmarks = new ArrayList<Landmarks>();
//        Cursor res = mydb.getAllData(userID);
//        if (res.getCount() == 0) {
//            landmarks.clear();
//        } else {
//            while (res.moveToNext()) {
//                landmarks.add(new Landmarks(res.getString(2), "Add Details", res.getString(2)));
//            }
//        }
//
//        myAdapter = new LandmarksAdapter(this, landmarks);
//        recyclerView.setAdapter(myAdapter);
//    }
}
