package com.example.manila;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {
    EditText etName, etPassword;
    Button btnLogin;
    private DatabaseHelperForUsers mydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        etName = (EditText) findViewById(R.id.etName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
    }

    public void login(View view) {
        if(etName.getText().toString().isEmpty()||etPassword.getText().toString().isEmpty()){
            Toast.makeText(this, "Fill all the TextField", Toast.LENGTH_SHORT).show();
            return;
        }
        mydb= new DatabaseHelperForUsers(this);
       Boolean check= mydb.CheckLogin(etName.getText().toString(),etPassword.getText().toString());
        if(check){
            mydb = new DatabaseHelperForUsers(view.getContext());
            Cursor res = mydb.getAllDataForLandmarks();
            if (res.getCount() == 0) {
                CreateData();
            }
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            intent.putExtra("User", etName.getText().toString());
            startActivity(intent);
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "You fail to log in", Toast.LENGTH_SHORT).show();
        }

    }

    public void LoginRegister(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }
    public void CreateData(){
        mydb.insertDataInLandmarks("Rizal","Rizal here");
        mydb.insertDataInLandmarks("National Museum of Arts","NM of Arts Here");
        mydb.insertDataInLandmarks("National Museum of History","NM if History Here");
        mydb.insertDataInLandmarks("Testing test","test here");
        mydb.insertDataInLandmarks("Testing test2","Test 2 here");
        mydb.insertDataInLandmarks("Testing test2","Test 2 here");
        mydb.insertDataInLandmarks("Testing test2","Test 2 here");
        mydb.insertDataInLandmarks("Testing test2","Test 2 here");
    }

}

