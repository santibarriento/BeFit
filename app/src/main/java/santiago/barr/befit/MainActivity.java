package santiago.barr.befit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Botones de la pantalla inicial
        ImageView googleButton = findViewById(R.id.google_login);
        ImageView appleButton = findViewById(R.id.apple_login);
        Button emailButton = findViewById(R.id.login_button);

        // Navegación para cada botón (Ejemplo: transiciones a actividades futuras)
        googleButton.setOnClickListener(v -> {
            // Aquí iría el código para el inicio de sesión con Google
        });

        appleButton.setOnClickListener(v -> {
            // Aquí iría el código para el inicio de sesión con Apple
        });

        emailButton.setOnClickListener(v -> {
            // Abre la actividad de registro por correo
            startActivity(new Intent(this, EmailRegisterActivity.class));
        });
    }
}
