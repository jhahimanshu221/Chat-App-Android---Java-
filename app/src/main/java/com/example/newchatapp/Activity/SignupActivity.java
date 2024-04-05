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

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.newchatapp.R;
import com.example.newchatapp.ModelClass.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SignupActivity extends AppCompatActivity {

    TextView txt_login;
    EditText signup_name,signup_email,signup_pwd,signup_cnfm_pwd;
    Button btn_signup;
    FirebaseAuth auth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseDatabase database;
    FirebaseStorage storage;
    ProgressDialog progressDialog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);


        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        txt_login = findViewById(R.id.txt_login);
        signup_name = findViewById(R.id.signup_name);
        signup_email = findViewById(R.id.signup_email);
        signup_pwd = findViewById(R.id.signup_pwd);
        signup_cnfm_pwd = findViewById(R.id.signup_cnfm_pwd);
        btn_signup = findViewById(R.id.btn_signup);

        txt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String name = signup_name.getText().toString();
                String email = signup_email.getText().toString();
                String password = signup_pwd.getText().toString();
                String cnfm_password = signup_cnfm_pwd.getText().toString();
                String status = "Hey there!!!";

                if (TextUtils.isEmpty(name)||TextUtils.isEmpty(password)||TextUtils.isEmpty(cnfm_password)||TextUtils.isEmpty(email)){

                    signup_email.setError("Fields are Empty");
                    signup_name.setError("Fields are Empty");
                    signup_pwd.setError("Fields are Empty");
                    signup_cnfm_pwd.setError("Fields are Empty");
                    progressDialog.dismiss();
                    Toast.makeText(SignupActivity.this, "Fields are Empty", Toast.LENGTH_SHORT).show();

                } else if (!email.matches(emailPattern)) {
                    signup_email.setError("Enter Valid Email");
                    progressDialog.dismiss();
                    Toast.makeText(SignupActivity.this, "Enter Valid Email", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(cnfm_password)) {
                    progressDialog.dismiss();
                    Toast.makeText(SignupActivity.this, "Password does not match", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6 ) {
                    progressDialog.dismiss();
                    Toast.makeText(SignupActivity.this, "Please Enter 6 Digit Paswword", Toast.LENGTH_SHORT).show();
                } else {
                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){


                                DatabaseReference reference = database.getReference().child("user").child(auth.getUid());
                                StorageReference storageReference = storage.getReference().child("upload").child(auth.getUid());

                                User users = new User(auth.getUid(),name,email,status);
                                reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            progressDialog.dismiss();
                                            if (auth.getCurrentUser() != null) {
                                                startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                            } else {
                                                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                            }
                                            finish(); // Add this line to finish the SignupActivity after redirection
                                        } else {
                                            Toast.makeText(SignupActivity.this, "Error in creating a New User", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });



                                Toast.makeText(SignupActivity.this, "User Created Succesfully", Toast.LENGTH_SHORT).show();
                            }else {
                                progressDialog.dismiss();
                                Toast.makeText(SignupActivity.this, "Unable to create User", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }




            }
        });
    }
}