package Fragments;


import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import app.message.messagingapp.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private static final int RC_PHOTO_PICKER = 1;
    CircleImageView mProfilePic;
    ImageView mBgPic, editContactIV, editNameIV, updateEditContactIV, updateEditNameIV;
    ArrayList mLocationList, mLongList;
    Button uploadimageBtn;
    Button mEditProfile;
    EditText mnameET, mPhoneET, mEmailET;
    DatabaseReference UserRef;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    private Dialog dialog;
    private StorageReference mProfilePicStorageReference;
    // 03002578829
    private TextView mNameTV, mPhoneTV, mEmailTV;
    private String Username, UserPhone, UserEmail, UserImgUrl;
    private Uri selectedProfileImageUri;
    private ImageView UserImage;
    private StorageReference profilePicRef;
    private String downloadUri;
    EditText EditContactET;
    private EditText EditNameET;
    private ImageView updateeditNameIV;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        getActivity().setTitle("Profile");

        auth=FirebaseAuth.getInstance();
        UserRef = FirebaseDatabase.getInstance().getReference().child("user");
        databaseReference = FirebaseDatabase.getInstance().getReference("user");
        mProfilePicStorageReference= FirebaseStorage.getInstance().getReference("profilePic");
        mNameTV = view.findViewById(R.id.nameTV);
        mPhoneTV = view.findViewById(R.id.contactTV);
        mEmailTV = view.findViewById(R.id.emailTV);
        mBgPic = view.findViewById(R.id.bg_img);
        uploadimageBtn = view.findViewById(R.id.uploadimageBtn);
        EditContactET = view.findViewById(R.id.EditContactET);
        EditNameET = view.findViewById(R.id.EditNameET);
        updateeditNameIV = view.findViewById(R.id.updateeditNameIV);
        updateEditContactIV = view.findViewById(R.id.updateEditContactIV);
        uploadimageBtn = view.findViewById(R.id.uploadimageBtn);
        updateeditNameIV = view.findViewById(R.id.updateeditNameIV);
        updateeditNameIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditNameET.setVisibility(View.VISIBLE);
                mNameTV.setVisibility(View.GONE);
                updateEditNameIV.setVisibility(View.VISIBLE);
                updateeditNameIV.setVisibility(View.GONE);
            }
        });
        updateEditNameIV = view.findViewById(R.id.updateEditNameIV);

        updateEditNameIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserRef.child(auth.getCurrentUser().getUid()).child("name").setValue(EditNameET.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        EditNameET.setVisibility(View.GONE);
                        mNameTV.setVisibility(View.VISIBLE);
                        updateEditNameIV.setVisibility(View.GONE);
                        updateeditNameIV.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(), "Name updated successfully", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });
        editContactIV = view.findViewById(R.id.editContactIV);
        editContactIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditContactET.setVisibility(View.VISIBLE);
                mPhoneTV.setVisibility(View.GONE);
                updateEditContactIV.setVisibility(View.VISIBLE);
                editContactIV.setVisibility(View.GONE);

            }
        });
        updateEditContactIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserRef.child(auth.getCurrentUser().getUid()).child("phone").setValue(EditContactET.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        EditContactET.setVisibility(View.GONE);
                        mPhoneTV.setVisibility(View.VISIBLE);
                        updateEditContactIV.setVisibility(View.GONE);
                        editContactIV.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(), "Phone updated successfully", Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });

        mProfilePic = view.findViewById(R.id.profilepic);
        mProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getProfilePicture();


            }
        });
        uploadimageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profilePicRef = mProfilePicStorageReference.child(selectedProfileImageUri.getLastPathSegment());
                profilePicRef.putFile(selectedProfileImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        profilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                downloadUri = uri.toString();
                                UserRef.child(auth.getCurrentUser().getUid()).child("imageUrl").setValue(downloadUri);
                                Toast.makeText(getContext(), "Image changes successfully", Toast.LENGTH_SHORT).show();
                                uploadimageBtn.setVisibility(View.GONE);
                            }
                        });

                    }
                });
            }
        });

        getStudentInfo();
        return view;
    }


    private void getStudentInfo() {
//        mLocationList = new ArrayList<>();
//        mLongList = new ArrayList<>();
        UserRef.child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Username = dataSnapshot.child("name").getValue().toString();
                UserPhone = dataSnapshot.child("phone").getValue().toString();
                UserEmail = dataSnapshot.child("email").getValue().toString();
                UserImgUrl = dataSnapshot.child("imageUrl").getValue().toString();

                mNameTV.setText(Username);
                mPhoneTV.setText(UserPhone);
                mEmailTV.setText(UserEmail);
                Glide.with(getContext())
                        .load(UserImgUrl)
                        .into(mProfilePic);
                Glide.with(getContext())
                        .load(UserImgUrl)
                        .into(mBgPic);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void FragmentLoadinManagerNoBackStack(Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();


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
            uploadimageBtn.setVisibility(View.VISIBLE);


        }

    }

}
