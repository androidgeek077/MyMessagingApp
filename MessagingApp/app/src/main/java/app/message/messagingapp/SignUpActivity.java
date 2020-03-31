package app.message.messagingapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kaopiz.kprogresshud.KProgressHUD;

import Models.UserModel;


public class SignUpActivity extends AppCompatActivity {

    KProgressHUD progressDialog;

    Button mSelectedImgBtn;
    ImageView profileImageView;
    String downloadUri;
    LinearLayout view;

    private StorageReference mProfilePicStorageReference;
    private static final int RC_PHOTO_PICKER = 1;
    private Uri selectedProfileImageUri;


    DatabaseReference databaseReference;
    FirebaseAuth auth;
    StorageReference profilePicRef;
    UserModel userModel;

    DatabaseReference registerStudent;
    FirebaseAuth mAuth;
    EditText edName, edEmail, edPassword, edPhone, edLong;
    ImageView mProfilePic;
    private Button btnSignUp;




    String name, email, password, Phone, ImgUrl;

    private Button mSelectImgBtn;
    private String userIdStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();
        auth=FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("user");
        mProfilePicStorageReference= FirebaseStorage.getInstance().getReference("profilePic");
        mAuth = FirebaseAuth.getInstance();

        edName = findViewById(R.id.ed_signup_name);
        view = findViewById(R.id.ll);
        edEmail = findViewById(R.id.edt_txt_email);
        edPassword = findViewById(R.id.ed_signup_password);

        edPhone = findViewById(R.id.ed_phone);
        mProfilePic = findViewById(R.id.selectedImg);
        mSelectImgBtn = findViewById(R.id.btn_selectimg);
        mSelectImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProfilePicture();
            }
        });


        btnSignUp = findViewById(R.id.btn_signup);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {


                name = edName.getText().toString();
                email = edEmail.getText().toString();
                password = edPassword.getText().toString();
                Phone = edPhone.getText().toString();


                if (name.isEmpty()) {
                    edName.setError("Please Enter Name First");
                } else if (email.isEmpty()) {
                    edEmail.setError("please fill email");
                } else if (password.isEmpty()) {
                    edPassword.setError("Please enter Password");
                } else if (password.length() < 8) {
                    edPassword.setError("Password must contain at least 8 characters ");
                } else {

                    progressDialog = KProgressHUD.create(SignUpActivity.this)
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setAnimationSpeed(2)
                            .setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark))
                            .setLabel("Authenticating")
                            .setDetailsLabel("Please Wait...")
                            .setDimAmount(0.3f)
                            .show();


                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                userIdStr=auth.getCurrentUser().getUid();
//                                uploadProduct("https://firebasestorage.googleapis.com/v0/b/mechabot-d487d.appspot.com/o/32566846_1783747771719216_5113386920709193728_n.jpg?alt=media&token=dd05209b-b72d-45bb-8ca2-cf3c8d76175f");
                                progressDialog.dismiss();
                                profilePicRef = mProfilePicStorageReference.child(selectedProfileImageUri.getLastPathSegment());
                                profilePicRef.putFile(selectedProfileImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        profilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                downloadUri = uri.toString();
                                                uploadProduct(downloadUri, userIdStr);
                                            }
                                        });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });


                }

            }
        });


    }

    public void getProfilePicture() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri selectedImageUri = data.getData();
            selectedProfileImageUri = selectedImageUri;
            mProfilePic.setImageURI(selectedImageUri);
            mProfilePic.setVisibility(View.VISIBLE);
        }

    }

    public void uploadProduct(String ImageUrl, String Userid) {

        userModel = new UserModel(Userid ,name, email, password, Phone, ImageUrl, "user");
        databaseReference.child(FirebaseAuth.getInstance().getUid()).setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressDialog.dismiss();
                mProfilePic.setVisibility(View.GONE);
                final Snackbar snackbar = Snackbar.make(view, "User Added Successfully", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Ok", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                        snackbar.dismiss();
                    }
                });
                snackbar.show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }





}
