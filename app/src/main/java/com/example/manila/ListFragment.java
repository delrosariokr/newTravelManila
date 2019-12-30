package com.example.manila;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.plus.PlusOneButton;

import java.util.ArrayList;

public class ListFragment extends Fragment {
    private DatabaseHelperForLandmarks mydb;
    private RecyclerView recyclerView;
    private  RecyclerView.Adapter myAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Landmarks> landmarks;
    private ArrayList<String> landmarks2 = new ArrayList<>();
    private View view;
    public static View passdata;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list, container, false);
        passdata=view;
        mydb = new DatabaseHelperForLandmarks(view.getContext());
        Cursor res = mydb.getAllData();
        if (res.getCount() == 0) {
            Toast.makeText(getActivity(), "Empty", Toast.LENGTH_SHORT).show();
            view=view;
        } else {
            view = loadDatabase(view);
        }
        return view;

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
}
