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
    private DatabaseHelperForUsers mydb;
    private RecyclerView recyclerView;
    private  RecyclerView.Adapter myAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Landmarks> landmarks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_landmark, container, false);
        mydb = new DatabaseHelperForUsers(view.getContext());
        Cursor res = mydb.getAllDataForLandmarks();
        if (res.getCount() == 0) {
            Toast.makeText(getActivity(), "Empty", Toast.LENGTH_SHORT).show();
            return view;
        } else {
            view = loadDatabase(view);
        }
        return view;

    }
    public View loadDatabase(View view) {
        recyclerView = view.findViewById(R.id.List);
        mydb = new DatabaseHelperForUsers(getActivity());
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        landmarks = new ArrayList<Landmarks>();
        Cursor res = mydb.getAllDataForLandmarks();
        if (res.getCount() == 0) {
            landmarks.clear();
        } else {
            while (res.moveToNext()) {
                landmarks.add(new Landmarks(res.getString(1), res.getString(2), res.getString(1)));
            }
        }

        myAdapter = new LandmarksAdapterDisplayOnly(getActivity(), landmarks);
        recyclerView.setAdapter(myAdapter);
        return view;
    }

}





