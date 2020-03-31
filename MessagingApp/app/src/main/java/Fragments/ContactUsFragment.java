package Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kaopiz.kprogresshud.KProgressHUD;

import Models.ContactusModel;
import app.message.messagingapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactUsFragment extends Fragment {
    EditText mNameET, mPhoneET, mCommentET;
    String NameStr, PhoneStr, CommentStr;
    Button mSubmitBtn;
    KProgressHUD progressDialog;


    DatabaseReference myCommentRef;

    public ContactUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        myCommentRef = FirebaseDatabase.getInstance().getReference("UserComments");
        getEditText(view);
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                getStrings();
                if (NameStr.isEmpty()) {
                    mNameET.setError("Enter Name");
                } else if (PhoneStr.isEmpty()) {
                    mPhoneET.setError("Enter Phone");
                } else if (CommentStr.isEmpty()) {
                    mCommentET.setError("Enter Comment");
                } else {

                    progressDialog = KProgressHUD.create(getContext())
                            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                            .setAnimationSpeed(2)
                            .setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark))
                            .setLabel("Authenticating")
                            .setDetailsLabel("Please Wait...")
                            .setDimAmount(0.3f)
                            .show();


                    ContactusModel model = new ContactusModel(NameStr, PhoneStr, CommentStr);
                    myCommentRef.push().setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();

                                Toast.makeText(getContext(), "Query Submitted Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();

                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        return view;
    }

    void getEditText(View view) {
        mNameET = view.findViewById(R.id.mCommentET);
        mPhoneET = view.findViewById(R.id.mPhoneET);
        mCommentET = view.findViewById(R.id.mCommentET);
        mSubmitBtn = view.findViewById(R.id.mSubmitBtn);

    }

    void getStrings() {
        NameStr = mNameET.getText().toString();
        PhoneStr = mPhoneET.getText().toString();
        CommentStr = mCommentET.getText().toString();
    }

}
