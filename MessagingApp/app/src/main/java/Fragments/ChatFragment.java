package Fragments;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Models.Message;
import Models.UserModel;
import app.message.messagingapp.ChatActivity;

import app.message.messagingapp.R;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {
    private RecyclerView recyclerViewUsers;

    private DatabaseReference mUsersDbRef;

    public ChatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerViewUsers = view.findViewById(R.id.recyclerChatUsers);
        LinearLayoutManager layoutManager =new LinearLayoutManager(getContext());
        // layoutManager.setStackFromEnd(true);
        recyclerViewUsers.setLayoutManager(layoutManager);

        mUsersDbRef = FirebaseDatabase.getInstance().getReference().child("user");

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<UserModel> options = new FirebaseRecyclerOptions.Builder<UserModel>()
                .setQuery(mUsersDbRef, UserModel.class)
                .build();

        FirebaseRecyclerAdapter<UserModel,UsersViewHolder> adapter = new FirebaseRecyclerAdapter<UserModel, UsersViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UsersViewHolder usersViewHolder, int i, @NonNull final UserModel userModel) {
                if (userModel.getUserid().equals(FirebaseAuth.getInstance().getUid())){
                    usersViewHolder.frameVisibilityItem.setVisibility(View.GONE);
                } else {
                    usersViewHolder.bind(userModel);
                }

                usersViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), ChatActivity.class);
                        intent.putExtra("ConversationUser",userModel);
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.list_item_user,parent,false);
                UsersViewHolder viewHolder = new UsersViewHolder(view);
                return viewHolder;
            }
        };

        recyclerViewUsers.setAdapter(adapter);
        adapter.startListening();
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder{
        CircleImageView profileImage;
        TextView userName,lastMessage,tvConnectionState;
        FrameLayout frameVisibilityItem;
        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage=itemView.findViewById(R.id.profile_image);
            userName=itemView.findViewById(R.id.txtUsername);
            lastMessage=itemView.findViewById(R.id.txtChat);
            tvConnectionState = itemView.findViewById(R.id.txtOnlineStatus);
            frameVisibilityItem = itemView.findViewById(R.id.frameVisibilityUser);
        }

        void bind(UserModel user){
            userName.setText(user.getName());
            setLastMessage(user);
//            if (user.getConnectionState()!=null) {
//                if (user.getConnectionState().equals("online")) {
//                    tvConnectionState.setTextColor(Color.GREEN);
//                } else if (user.getConnectionState().equals("offline")) {
//                    tvConnectionState.setTextColor(Color.RED);
//                }
//
//                tvConnectionState.setText(user.getConnectionState());
//            }

            tvConnectionState.setVisibility(View.INVISIBLE);


            if (user.getImageUrl()!=null){
                Glide.with(profileImage.getContext()).load(user.getImageUrl()).into(profileImage);
            }
        }





        private void setLastMessage(final UserModel user){
            DatabaseReference mMessagesRef = FirebaseDatabase.getInstance().getReference().child("Conversations");
            final List<Message> megList = new ArrayList<>();
            ValueEventListener mValListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        final Message message = data.getValue(Message.class);

                        if (message.getSenderUid().equals(FirebaseAuth.getInstance().getUid())&& message.getReceiverUid().equals(user.getUserid())
                                || message.getSenderUid().equals(user.getUserid()) && message.getReceiverUid().equals(FirebaseAuth.getInstance().getUid()))
                        {
                            megList.add(message);
                        }





                    }
                    if (megList.size()>0){
                        if (megList.get(megList.size()-1).getSenderUid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            // userLastMsg.setCompoundDrawablesWithIntrinsicBounds(R.drawable.last_msg_list_users_tick,0,0,0);
                        }

                        if (megList.get(megList.size()-1).getmText().length()>30){
                            String message = megList.get(megList.size()-1).getmText().substring(0,30)+"...";
                            lastMessage.setTextColor(Color.BLACK);
                            lastMessage.setText(message);
                            lastMessage.setTypeface(null, Typeface.NORMAL);
                        }else {
                            lastMessage.setTextColor(Color.BLACK);
                            lastMessage.setText(megList.get(megList.size()-1).getmText());
                            lastMessage.setTypeface(null,Typeface.NORMAL);
                        }

                    } else {
                        lastMessage.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                        lastMessage.setTextColor(Color.GRAY);
                        lastMessage.setText("");
                        lastMessage.setTypeface(null,Typeface.ITALIC);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };


            mMessagesRef.addValueEventListener(mValListener);
        }
    }

}
