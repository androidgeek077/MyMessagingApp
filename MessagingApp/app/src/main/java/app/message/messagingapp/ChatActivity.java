package app.message.messagingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Adapters.ConversationAdapter;
import Models.Message;
import Models.UserModel;


public class ChatActivity extends AppCompatActivity {
    private static final int CONTEXT_MENU_DELETE_FOR_ME =22 ;
    private static final int CONTEXT_MENU_DELETE_FOR_EVERYONE = 33;
    private static final int RC_PHOTO_PICKER = 44;

    private Uri selectedFileUri;
    private String selectedFileType;

    UserModel user;
    private List<Message> messages;
    RecyclerView rcConversation;

    EditText edMessageText;
    ImageButton btnSendMsg,btnCamera;
    ProgressBar pbLoading;

    private DatabaseReference conversationDatabaseRef;
    private StorageReference conversationFilesStorage;

    private ChildEventListener getAllConversationChildListener;
    private ConversationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        rcConversation = findViewById(R.id.rv_messages_view);
        edMessageText = findViewById(R.id.edt_txt_message);
        btnSendMsg =findViewById(R.id.btn_send_message);
        btnCamera = findViewById(R.id.btn_send_image);
        pbLoading = findViewById(R.id.pb_img_upload);
        messages=new ArrayList<>();

        Intent intent=getIntent();
        user= (UserModel) intent.getSerializableExtra("ConversationUser");

        conversationDatabaseRef= FirebaseDatabase.getInstance().getReference().child("Conversations");


        ActionBar actionBar = getSupportActionBar();
        assert  actionBar!=null;
        actionBar.setTitle(user.getName());
//        actionBar.setDisplayHomeAsUpEnabled(true);


        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);

        rcConversation.setLayoutManager(layoutManager);
        rcConversation.setHasFixedSize(true);

        edMessageText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() >0){
                    btnSendMsg.setEnabled(true);
                }else {
                    btnSendMsg.setEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }

        });

        btnSendMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtMessage=edMessageText.getText().toString();
                String currentUser= FirebaseAuth.getInstance().getCurrentUser().getUid();
                if (txtMessage.isEmpty() && selectedFileUri == null){
                    Toast.makeText(ChatActivity.this, "Please enter text or chose file", Toast.LENGTH_SHORT).show();
                }else{

                    final String msgId=conversationDatabaseRef.push().getKey();

                    Message message=new Message(msgId,txtMessage,null,null,getCurrentTimeDate(),null,null,currentUser,user.getUserid(),false);


                    conversationDatabaseRef.child(msgId).setValue(message).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            edMessageText.setText("");
                        }
                    });



                }
            }
        });


        getAllConversationChildListener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message message=dataSnapshot.getValue(Message.class);
                if (message.getSenderUid().equals(FirebaseAuth.getInstance().getUid())&& message.getReceiverUid().equals(user.getUserid())
                        || message.getSenderUid().equals(user.getUserid()) && message.getReceiverUid().equals(FirebaseAuth.getInstance().getUid()))
                {
                    messages.add(message);
                }


                adapter=new ConversationAdapter(messages,ChatActivity.this);

                rcConversation.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                messages=new ArrayList<>();
//                Message message=dataSnapshot.getValue(Message.class);
//                if (message.getSenderUID().equals(FirebaseAuth.getInstance().getUid())&& message.getReceiverUID().equals(user.getUID())
//                        || message.getSenderUID().equals(user.getUID()) && message.getReceiverUID().equals(FirebaseAuth.getInstance().getUid()))
//                {
//                    if (message.getMsgStatus().equals("sent")){
//                        messages.add(message);
//                    }
//
//
//                }
//
//                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        conversationDatabaseRef.addChildEventListener(getAllConversationChildListener);


    }








    private String getCurrentTimeDate(){

        Calendar calendar=Calendar.getInstance();

        Date currentDateTime=calendar.getTime();

        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String formatedDateTime=dateFormat.format(currentDateTime);

        return formatedDateTime;
    }


}
