package com.example.productsaleprm.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.productsaleprm.adapter.ChatAdapter;
import com.example.productsaleprm.databinding.ActivityChatBinding;
import com.example.productsaleprm.model.ChatMessage;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;
    private RecyclerView chatRecycler;
    private EditText inputMess;
    private ImageView btnSend;

    private List<ChatMessage> messageList = new ArrayList<>();
    private ChatAdapter chatAdapter;

    private DatabaseReference chatRef;

    private String currentUserId = "shop"; // giải token để lấy id
    private String shopId = "shop"; // id admin
    private String roomId = "room_shop_user_01";
    private boolean firstLoad = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        inputMess = binding.inputMess;
        btnSend = binding.btnSend;
        chatRecycler = binding.chatRecycler;

        chatAdapter = new ChatAdapter(messageList, currentUserId);
        chatRecycler.setLayoutManager(new LinearLayoutManager(this));
        chatRecycler.setAdapter(chatAdapter);

        chatRef = FirebaseDatabase.getInstance().getReference("chatMessages").child(roomId);

        // Show progress when loading
        binding.progressBar.setVisibility(View.VISIBLE);
        chatRecycler.setVisibility(View.GONE);

        preloadCheck(); // NEW: kiểm tra dữ liệu có tồn tại
        listenForMessages();
        binding.imgBack.setOnClickListener(v -> finish());

        btnSend.setOnClickListener(v -> sendMessage());
    }

    private void sendMessage() {
        String text = inputMess.getText().toString().trim();
        if (text.isEmpty()) return;

        ChatMessage msg = new ChatMessage(currentUserId, text, System.currentTimeMillis());
        chatRef.push().setValue(msg);
        inputMess.setText("");
    }

    // ✅ Dùng thêm ValueEventListener để biết có dữ liệu hay không
    private void preloadCheck() {
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    // Không có tin nhắn nào → ẩn progress
                    binding.progressBar.setVisibility(View.GONE);
                    chatRecycler.setVisibility(View.VISIBLE);
                    Toast.makeText(ChatActivity.this, "Chưa có tin nhắn nào", Toast.LENGTH_SHORT).show();
                    firstLoad = false;
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(ChatActivity.this, "Không thể tải dữ liệu: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void listenForMessages() {
        chatRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String prev) {
                ChatMessage msg = snapshot.getValue(ChatMessage.class);
                if (msg != null) {
                    messageList.add(msg);
                    chatAdapter.notifyItemInserted(messageList.size() - 1);
                    chatRecycler.scrollToPosition(messageList.size() - 1);

                    if (firstLoad) {
                        binding.progressBar.setVisibility(View.GONE);
                        chatRecycler.setVisibility(View.VISIBLE);
                        firstLoad = false;
                    }
                }
            }

            @Override public void onCancelled(DatabaseError error) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(ChatActivity.this, "Lỗi Firebase: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override public void onChildChanged(DataSnapshot snapshot, String prev) {}
            @Override public void onChildRemoved(DataSnapshot snapshot) {}
            @Override public void onChildMoved(DataSnapshot snapshot, String prev) {}
        });
    }
}
