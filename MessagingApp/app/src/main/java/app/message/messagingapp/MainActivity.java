package app.message.messagingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;


public class MainActivity extends AppCompatActivity {


    KProgressHUD progressDialog;


    DatabaseReference registerReference;
    FirebaseAuth mAuth;


    TextView mSignupTxt;
    EditText edLoginUserEmail, edLoginPassword;
    Button btnLogin;
    private String email;
    private String password;
    private DatabaseReference databaseReference;
    private String UserType = "";


    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        getSupportActionBar().hide();
        databaseReference = FirebaseDatabase.getInstance().getReference("user");

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(getBaseContext(), BottomNavActivity.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("user");
        registerReference = FirebaseDatabase.getInstance().getReference("Drivers");


        edLoginUserEmail = findViewById(R.id.edt_txt_email);
        edLoginPassword = findViewById(R.id.ed_signup_password);
        btnLogin = findViewById(R.id.btn_signup);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = edLoginUserEmail.getText().toString();
                password = edLoginPassword.getText().toString();

                if (email.isEmpty()) {
                    edLoginUserEmail.setError("Please Enter your Email First");
                } else if (password.isEmpty()) {
                    edLoginPassword.setError("Please enter password First");
                } else {

                    progressDialog = KProgressHUD.create(MainActivity.this)
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setAnimationSpeed(2)
                            .setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark))
                            .setLabel("Authenticating")
                            .setDetailsLabel("Please Wait...")
                            .setDimAmount(0.3f)
                            .show();

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        startActivity(new Intent(getBaseContext(), BottomNavActivity.class));
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        mSignupTxt = findViewById(R.id.signup_txt_vw);
        mSignupTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            }
        });


    }

    void getUserType() {
        if (mAuth.getCurrentUser() != null) {
            databaseReference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Toast.makeText(MainActivity.this, ""+dataSnapshot, Toast.LENGTH_SHORT).show();

                    if (dataSnapshot.exists()) {
                        UserType = dataSnapshot.child("usertype").getValue(String.class);
                    }
                    if (UserType.equalsIgnoreCase("admin")) {
//                        startActivity(new Intent(MainActivity.this, DashboardActivity.class));
                        finish();
                    } else if (UserType.equalsIgnoreCase("user")) {
//                        startActivity(new Intent(MainActivity.this, UserDashboardActivity.class));
                        finish();


                    } else {
//                        startActivity(new Intent(MainActivity.this, ViewComplaintsActivity.class));
                        finish();

                        //                                            startActivity(new Intent(MainActivity.this, PoliceStationDashboardActivity.class));
//                                            finish();
//                                            progressDialog.dismiss();
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
//                if (dataSnapshot.exists()) {
//                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                        StationModel post = postSnapshot.getValue(StationModel.class);
//                        String Name=post.getName();
//                        Toast.makeText(AddComplaintActivity.this, Name, Toast.LENGTH_SHORT).show();
//                    }
//                }


            });
        }

    }
}
