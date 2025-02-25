package santiago.barr.befit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private final List<Message> chatList;
    private final Context context;

    public ChatAdapter(List<Message> chatList, Context context) {
        this.chatList = chatList;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Message message = chatList.get(position);

        holder.userName.setText(message.getSenderName());
        holder.messageText.setText(message.getText());
        holder.messageTime.setText(formatTimestamp(message.getTimestamp()));

        // Cargar la imagen de perfil con Glide
        Glide.with(context)
                .load(message.getSenderProfileImage())
                .placeholder(R.drawable.perfilbefit) // Imagen por defecto si falla
                .into(holder.userImage);
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView userName, messageText, messageTime;
        ImageView userImage;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.chat_user_name);
            messageText = itemView.findViewById(R.id.chat_message_text);
            messageTime = itemView.findViewById(R.id.chat_message_time);
            userImage = itemView.findViewById(R.id.chat_user_image);
        }
    }

    // Formatear la hora del mensaje (HH:mm)
    private String formatTimestamp(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date(timestamp * 1000));
    }
}
