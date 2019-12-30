package com.example.manila;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {
    EditText etName, etPassword;
    Button btnLogin;
    private DatabaseHelper mydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        etName = (EditText) findViewById(R.id.etName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
    }

    public void login(View view) {
//        if(etName.getText().toString().isEmpty()||etPassword.getText().toString().isEmpty()){
//            Toast.makeText(this, "Fill all the TextField", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        mydb= new DatabaseHelper(this);
//       Boolean check= mydb.CheckLogin(etName.getText().toString(),etPassword.getText().toString());
//        if(check){
//            startActivity(new Intent(this,MainActivity.class));
//            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
//        }else{
//            Toast.makeText(this, "You fail to log in", Toast.LENGTH_SHORT).show();
//        }
        startActivity(new Intent(this,MainActivity.class));
    }

    public void LoginRegister(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}

