package com.example.newchatapp.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.newchatapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText login_edit_email, login_edit_pwd;
    private Button btn_login;
    private TextView txt_signup;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize FirebaseAuth instance
        auth = FirebaseAuth.getInstance();

        // Initialize views
        login_edit_email = findViewById(R.id.login_edit_email);
        login_edit_pwd = findViewById(R.id.login_edit_pwd);
        btn_login = findViewById(R.id.btn_login);
        txt_signup = findViewById(R.id.txt_signup);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        // Set onClick listener for SignUp TextView
        txt_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

        // Set onClick listener for Login button
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });
    }

    // Method to login user
    private void loginUser() {
        progressDialog.show();
        String email = login_edit_email.getText().toString().trim();
        String password = login_edit_pwd.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            progressDialog.dismiss();
            Toast.makeText(LoginActivity.this, "Email or Password is Empty", Toast.LENGTH_SHORT).show();
        } else {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish(); // Finish LoginActivity after successful login
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Error in Login", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}