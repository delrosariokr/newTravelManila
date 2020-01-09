package com.example.manila;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class LandmarkInfoActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView text;
    private DatabaseHelperForUsers mydb;
    private Button AddButton;
    private int UserID;
    private int LandmarksID;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    private String user;
    private ArrayList<String> landmarks2 = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landmark_info);
        mydb = new DatabaseHelperForUsers(this);
        imageView=findViewById(R.id.DesCriptionImage);
        text=findViewById(R.id.Description);
        AddButton=findViewById(R.id.DescriptionButton);
        calendar = Calendar.getInstance();
        String landmarks = getIntent().getStringExtra("Title");
         user = getIntent().getStringExtra("User");
        UserID=mydb.getIDFromTableUser(user);
        LandmarksID=mydb.getIDFromTableLandmarks(landmarks);
        if(mydb.CheckIfUser_ListExist(UserID,LandmarksID)){
        AddButton.setClickable(false);
        AddButton.setText("Included in List");
        }

        Toast.makeText(this, landmarks, Toast.LENGTH_SHORT).show();
        CreateSomething(landmarks);
    }
    public void CreateSomething(String Title){
        mydb = new DatabaseHelperForUsers(this);
        if(Title.equals("Rizal")){
            imageView.setImageResource(R.drawable.rizal);
          text.setText(mydb.Description("Rizal"));
        }else if(Title.equals("National Museum of Arts")){
            imageView.setImageResource(R.drawable.nationalmuseumoffinearts);
            text.setText(mydb.Description("National Museum of Arts"));
        }else if(Title.equals("National Museum of History")){
            imageView.setImageResource(R.drawable.nationalmuseumofhistory);
            text.setText(mydb.Description("National Museum of History"));
        }
    }
    public void onClickAddFromLandmarkInfo(View view){
        mydb = new DatabaseHelperForUsers(this);
         dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        date = dateFormat.format(calendar.getTime());
        Boolean inserted=mydb.insertDataInUserVisit(UserID,LandmarksID,date);
        if(inserted){
            Toast.makeText(this, "Successfully Inserted", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            intent.putExtra("User", user);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Failed to insert", Toast.LENGTH_SHORT).show();
        }
    }




}
