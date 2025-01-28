package santiago.barr.befit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private AuthManager authManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializa AuthManager
        authManager = new AuthManager(this);

        // Botones de la pantalla inicial
        ImageView googleButton = findViewById(R.id.google_login);
        ImageView appleButton = findViewById(R.id.apple_login);
        Button emailButton = findViewById(R.id.login_button);

        // Acciones para cada botón
        googleButton.setOnClickListener(v -> authManager.signInWithGoogle(this));
        appleButton.setOnClickListener(v -> authManager.signInWithApple(this));
        emailButton.setOnClickListener(v -> {
            // Abre la actividad de registro por correo
            startActivity(new Intent(this, EmailRegisterActivity.class));
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        authManager.handleActivityResult(requestCode, data, this);
    }
}
