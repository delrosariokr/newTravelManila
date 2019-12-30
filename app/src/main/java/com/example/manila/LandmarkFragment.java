package com.example.manila;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class LandmarkFragment extends Fragment {

   private DatabaseHelperForLandmarks mydb;
  private  RecyclerView recyclerView;
  private  RecyclerView.Adapter myAdapter;
   private RecyclerView.LayoutManager layoutManager;
   private  ArrayList<Landmarks> landmarks;
    private ArrayList<String> landmarks2 = new ArrayList<>();
    private View view;
    public static View passdata;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_landmark, container, false);
        passdata=view;
        mydb = new DatabaseHelperForLandmarks(view.getContext());
        Cursor res = mydb.getAllData();
        if (res.getCount() == 0) {
            Toast.makeText(getActivity(), "Empty", Toast.LENGTH_SHORT).show();
            view=view;
        } else {
            view = loadDatabase(view);
        }
//        FloatingActionButton add = view.findViewById(R.id.ButtonAdd);
//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int count = 0;
//                final String[] sequences = new String[landmarks2.size()];
//                if (landmarks2.isEmpty()) {
//                    Toast.makeText(getActivity(), "Empty Choices", Toast.LENGTH_SHORT).show();
//                }
//                for (String temp : landmarks2) {
//                    sequences[count] = temp;
//                    count++;
//                }
//                final String[] dd = {"Rizal", "National Museum of History", "National Museum of Arts"};
//                SingleAlgo(sequences,view);
//            }
//        });

//        Button delete = getView().findViewById(R.id.ButtonDelete);
//        delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                System.out.println("HERE AT DELETE");
//                mydb = new DatabaseHelperForLandmarks(getActivity());
//                View parent = (View) v.getParent();
//                TextView taskTextView = (TextView) parent.findViewById(R.id.TextViewTitle);
//                String task = String.valueOf(taskTextView.getText());
//                landmarks2.add(task);
//                int deleterow = mydb.DeleteData(task);
//                if (deleterow > 0) {
//                    Toast.makeText(getActivity(), "Successfully Deleted", Toast.LENGTH_SHORT).show();
//                    loadDatabase(view);
//                    saveData();
//                } else {
//                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        return view;
    }

    public void SingleAlgo(final CharSequence[] sequences, final View view) {
        mydb = new DatabaseHelperForLandmarks(getActivity());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Landmarks");
        builder.setItems(sequences, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Boolean inserted = mydb.insertData(sequences[which].toString().trim());
                if (inserted) {
                    loadDatabase(view);
                    landmarks2.remove(sequences[which]);
                    saveData();
                    Toast.makeText(getActivity(), "Successfully Inserted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                        Toast.makeText(getActivity(), "Successfully Inserted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
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

    public View loadDatabase(View view) {
        recyclerView = view.findViewById(R.id.List);
        mydb = new DatabaseHelperForLandmarks(getActivity());
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
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

        myAdapter = new LandmarksAdapter(getActivity(), landmarks);
        recyclerView.setAdapter(myAdapter);
        return view;
    }

    public void saveData() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(landmarks2);
        editor.putString("landmarks", json);
        editor.apply();

    }




}





