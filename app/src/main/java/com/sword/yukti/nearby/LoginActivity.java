package com.sword.yukti.nearby;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText login_id, password;
    Button signInButton;
    TextView forget_password, register_button;
    CheckBox rem_me;
    ProgressDialog loading;
    SessionManager sessionManager;
    SQLiteDatabase db;
    Cursor cursor;
    DBhelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbHelper= new DBhelper(this);
        db = dbHelper.getReadableDatabase();

        sessionManager = new SessionManager(this);


        if (Build.VERSION.SDK_INT >= 21)
        {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.titlebar_color));
        }

        login_id = (EditText) findViewById(R.id.signin_email);
        password = (EditText) findViewById(R.id.signin_pass);
        signInButton = (Button) findViewById(R.id.btnSignIn);
        forget_password = (TextView) findViewById(R.id.forget_pass);
        register_button = (TextView) findViewById(R.id.register);
        rem_me = (CheckBox) findViewById(R.id.checkBox);

        Boolean savelogin = sessionManager.getUserLogginIn();
        if (!savelogin) {
            if (sessionManager.getLoginDetails()) {
                SessionDetails_GetSet sessionDetails_getSet = sessionManager.getUserDetails();
                login_id.setText(sessionDetails_getSet.getUsername());
                password.setText(sessionDetails_getSet.getPassword());
                rem_me.setChecked(true);
            }
            register_button.setOnClickListener(this);
        }


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String emailInsert=login_id.getText().toString();
              String  passwordInsert=password.getText().toString();
                cursor = db.rawQuery("SELECT *FROM User_info_details"+" WHERE "+Database.USER_EMAIL+"=? AND "+Database.USER_PASSWORD+"=?",new String[] {emailInsert,passwordInsert});
                if (cursor != null) {
                    if(cursor.getCount() > 0) {

                        cursor.moveToFirst();
                        //Retrieving User FullName and Email after successfull login and passing to LoginSucessActivity
                      //  String _fname = cursor.getString(cursor.getColumnIndex(Database.USER_NAME));
                        String _email= cursor.getString(cursor.getColumnIndex(Database.USER_EMAIL));
                        Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this,MapsActivity.class);
                       // intent.putExtra("fullname",_fname);
                        intent.putExtra("email",_email);
                        startActivity(intent);
                        //Removing MainActivity[Login Screen] from the stack for preventing back button press.
                        finish();
                    }
                    else {

                        //I am showing Alert Dialog Box here for alerting user about wrong credentials
                        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setTitle("Alert");
                        builder.setMessage("Username or Password is wrong.");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                dialogInterface.dismiss();

                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();
                        //-------Alert Dialog Code Snippet End Here
                    }
                }
            }

        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.register:
                sessionManager.close_REquestPassword();
                ConnectivityManager Cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo ninfo = Cm.getActiveNetworkInfo();
                if (ninfo != null && ninfo.isConnected()) {
                    Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(registerIntent);
                }
                else {
                    Toast.makeText(this, "please switch on the net", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnSignIn:
                signInButton.setEnabled(true);

                Cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                assert Cm != null;
                ninfo = Cm.getActiveNetworkInfo();
                if (ninfo != null && ninfo.isConnected()) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(login_id.getWindowToken(), 0);
                    }

                    String pass =password.getText().toString().trim();
                    String email = login_id.getText().toString().trim();

                    if (TextUtils.isEmpty(email) || TextUtils.isEmpty((pass))) {
                        signInButton.setEnabled(true);
                        Toast.makeText(LoginActivity.this, "Username or password can't be empty.", Toast.LENGTH_SHORT).show();
                    } else {

                        // TODO: No need to delete database just before login

                    }
                }
                else
                {
                    Toast.makeText(LoginActivity.this, "Network connection not available.", Toast.LENGTH_LONG).show();
                }




        }
    }
}
