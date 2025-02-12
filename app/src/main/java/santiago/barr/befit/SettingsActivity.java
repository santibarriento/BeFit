package santiago.barr.befit;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsActivity extends AppCompatActivity {

    private TextView userName, userEmail, userPhone, userAddress;
    private ImageView userProfileImage;
    private Switch silentModeSwitch, darkModeSwitch;
    private SharedPreferences sharedPreferences;
    private RequestQueue requestQueue;  // Declaramos requestQueue como un atributo de la clase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingsactivity);

        // Inicializamos Volley request queue
        requestQueue = Volley.newRequestQueue(this);

        // Inicializa vistas
        userName = findViewById(R.id.username_text_google);
        userEmail = findViewById(R.id.email_text_google);
        userPhone = findViewById(R.id.phone_text_google);
        userProfileImage = findViewById(R.id.profile_image);
        silentModeSwitch = findViewById(R.id.silent_mode_switch);
        darkModeSwitch = findViewById(R.id.dark_mode_switch);

        // Cargar datos desde Firebase (si el usuario ya está logueado)
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userName.setText(user.getDisplayName());
            userEmail.setText(user.getEmail());
        }

        // Cargar datos del usuario desde Google Sign-In (nombre, correo y teléfono)
        loadGoogleUserInfo();

        // Aquí tus preferencias de la aplicación
        sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        silentModeSwitch.setChecked(sharedPreferences.getBoolean("silent_mode", false));
        darkModeSwitch.setChecked(sharedPreferences.getBoolean("dark_mode", false));
    }

    private void loadGoogleUserInfo() {
        // Obtenemos la cuenta de Google autenticada
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        if (account != null) {
            // Nombre de usuario
            userName.setText(account.getDisplayName());
            // Email
            userEmail.setText(account.getEmail());

            // Teléfono: No está disponible directamente en GoogleSignInAccount
            userPhone.setText("Número no disponible");

            // Foto de perfil
            if (account.getPhotoUrl() != null) {
                String photoUrl = account.getPhotoUrl().toString();
                // Aquí agregas el código para cargar la foto de perfil (lo mismo que ya tienes)
                ImageRequest imageRequest = new ImageRequest(photoUrl,
                        response -> userProfileImage.setImageBitmap(response),
                        0, 0, ImageView.ScaleType.CENTER_CROP, null, error -> userProfileImage.setImageResource(R.drawable.perfilbefit));
                requestQueue.add(imageRequest);
            }
        }
    }
}
