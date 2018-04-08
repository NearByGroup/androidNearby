package com.sword.yukti.nearby;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
EditText registerEmail,registerPassword,confirmPassword,registerAddress,alternatePhone;
Button register;
String email,password,confirm_password,address,alternateNumber;
DBhelper dBhelper;
ProgressDialog loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        dBhelper=new DBhelper(this);

        registerEmail=(EditText)findViewById(R.id.register_email);
        registerPassword=(EditText)findViewById(R.id.register_pass);
        confirmPassword=(EditText)findViewById(R.id.confirm_pass);
        registerAddress=(EditText)findViewById(R.id.address);
        alternatePhone=(EditText)findViewById(R.id.alternate_number);
        register=(Button)findViewById(R.id.btnRegister);

        register.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnRegister:
                Getter_setter getter_setter=new Getter_setter();
                dBhelper=new DBhelper(RegisterActivity.this);
                email=registerEmail.getText().toString().trim();
                password=registerPassword.getText().toString().trim();
                address=registerAddress.getText().toString().trim();
                confirm_password=confirmPassword.getText().toString().trim();
                alternateNumber=alternatePhone.getText().toString().trim();
                 getter_setter.setEmail(email);
                 getter_setter.setPassword(password);
                 getter_setter.setAddress(address);
                 getter_setter.setAlternate_number(alternateNumber);
                 if (confirm_password.equalsIgnoreCase(password))
                 {
                     Intent loginIntent=new Intent(RegisterActivity.this,LoginActivity.class);
                     dBhelper.addRegisterValues(getter_setter);
                     startActivity(loginIntent);
                 }
                 else
                 {
                     Toast.makeText(this, "confirmpassword and password is different", Toast.LENGTH_SHORT).show();
                 }
        }
    }
}
