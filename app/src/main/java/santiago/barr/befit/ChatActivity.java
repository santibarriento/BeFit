package santiago.barr.befit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private List<Message> chatList;
    private DatabaseReference chatRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        //boton back
        ImageView backButton = findViewById(R.id.back_button_pref);
        backButton.setOnClickListener(v -> finish());

        setupBottomNavigation(); // Barra de navegación

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        chatList = new ArrayList<>();
        chatAdapter = new ChatAdapter(chatList, this);
        recyclerView.setAdapter(chatAdapter);

        chatRef = FirebaseDatabase.getInstance().getReference("chats");
        loadChats();

        // Agrega un mensaje de prueba en Firebase
        addTestMessage();
    }

    // Carga los mensajes desde Firebase
    private void loadChats() {
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for (DataSnapshot chatSnapshot : snapshot.getChildren()) {
                    Message message = chatSnapshot.getValue(Message.class);
                    if (message != null) {
                        chatList.add(message);
                    }
                }
                chatAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error al cargar mensajes", error.toException());
            }
        });
    }

    // Agrega un mensaje de prueba en Firebase
    private void addTestMessage() {
        String chatId = chatRef.push().getKey(); // Genera un ID único para el mensaje

        Message testMessage = new Message(
                chatId,
                "Hola, este es un mensaje de prueba",
                System.currentTimeMillis() / 1000,  // Timestamp en segundos
                "q1X43OrNFUheHISZsa3oZe39ct72",
                "Santiago Barriento",
                "https://lh3.googleusercontent.com/a/ACg8ocI07IAxMzkivQSibHsBiBvfupjsZaI-kaAbV8BFNLX9bDVaAN0=s96-c"
        );

        if (chatId != null) {
            chatRef.child(chatId).setValue(testMessage)
                    .addOnSuccessListener(aVoid ->
                            Log.d("Firebase", "Mensaje agregado correctamente"))
                    .addOnFailureListener(e ->
                            Log.e("Firebase", "Error al agregar mensaje", e));
        }
    }

    // Barra de navegación
    protected void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setItemIconTintList(null);
        if (bottomNavigationView != null) {
            bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int itemId = item.getItemId();
                    if (itemId == R.id.nav_home) {
                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                        return true;
                    } else if (itemId == R.id.nav_trainings) {
                        startActivity(new Intent(getApplicationContext(), TrainingsActivity.class));
                        return true;
                    } else if (itemId == R.id.nav_professionals) {
                        startActivity(new Intent(getApplicationContext(), ProfessionalsActivity.class));
                        return true;
                    } else if (itemId == R.id.nav_chat) {
                        startActivity(new Intent(getApplicationContext(), ChatActivity.class));
                        return true;
                    } else if (itemId == R.id.nav_profile) {
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class)); // Abre perfil
                        return true;
                    }
                    return false;
                }
            });
        }
    }
}
