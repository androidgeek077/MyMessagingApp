package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import Models.Message;
import app.message.messagingapp.R;


public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ChatItemViewHolder> {

    private List<Message> messageList;
    private Context context;
    private int LayoutForListItem;



    public ConversationAdapter(List<Message> messageList, Context context) {
        this.messageList = messageList;
        this.context = context;

    }

    @Override
    public int getItemViewType(int position) {


        Message message = messageList.get(position);
        String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if (message.getSenderUid().equals(currentUserUid)) {
            return LayoutForListItem = R.layout.right_chat_item;
        } else if (!message.getSenderUid().equals(currentUserUid)) {
            return LayoutForListItem = R.layout.left_chat_item;
        }
        return LayoutForListItem = R.layout.left_chat_item;
    }


    @NonNull
    @Override
    public ChatItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(LayoutForListItem, viewGroup, false);

        return new ChatItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatItemViewHolder chatItemViewHolder, int i) {
        chatItemViewHolder.bind(messageList.get(i));
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ChatItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessageText, tvDateTime;
        FrameLayout frameVideoImage;
        ImageView imgConv;
        VideoView videoConv;

        public ChatItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tvMessageText = itemView.findViewById(R.id.txt_user_message_single);
            tvDateTime = itemView.findViewById(R.id.txt_user_time_single);
            frameVideoImage = itemView.findViewById(R.id.frame_video_image);
            imgConv = itemView.findViewById(R.id.img_send);
            videoConv = itemView.findViewById(R.id.vdo_send);

        }


        void bind(Message message) {


            if (message.getReceiverUid().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                setSeenMessage(message);
            }

            tvMessageText.setText(message.getmText());


            tvDateTime.setText(getTimeFromCalender(message.getmDate()));




        }

        private void setSeenMessage(Message message) {

            FirebaseDatabase.getInstance().getReference().child("Conversations").child(message.getmID()).child("seen").setValue(true);
        }

        private String getTimeFromCalender(String strCal) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            SimpleDateFormat showDateFormat = new SimpleDateFormat("K:mm a");

            String showTime = null;

            try {
                Date date = dateFormat.parse(strCal);
                showTime = showDateFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return showTime;
        }
    }
}
