package santiago.barr.befit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;

public class MainActivity extends AppCompatActivity {

    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        // Inicializa AuthManager
        authManager = new AuthManager(this);

        // Botón de Google
        ImageView googleButton = findViewById(R.id.google_login);

        // Acción para el botón de Google
        googleButton.setOnClickListener(v -> authManager.signInWithGoogle(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        authManager.handleActivityResult(requestCode, data, this);
    }
}
