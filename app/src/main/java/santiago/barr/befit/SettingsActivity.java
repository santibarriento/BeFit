package santiago.barr.befit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsActivity extends AppCompatActivity {

    private TextView userName, userEmail, userPhone, userAddress;
    private ImageView userProfileImage, changePhone, changeAddress;
    private Switch silentModeSwitch, darkModeSwitch;
    private SharedPreferences sharedPreferences;
    private RequestQueue requestQueue;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingsactivity);

        setupBottomNavigation(); // Barra de navegación

        // Inicializar Firebase Database
        databaseReference = FirebaseDatabase.getInstance("https://befit-81d9e-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users");

        // Inicializar Volley request queue
        requestQueue = Volley.newRequestQueue(this);

        // Inicializa vistas
        userName = findViewById(R.id.username_text);
        userEmail = findViewById(R.id.email_text);
        userPhone = findViewById(R.id.phone_text);
        userAddress = findViewById(R.id.address_text);
        userProfileImage = findViewById(R.id.profile_image);
        changePhone = findViewById(R.id.change_phone);
        changeAddress = findViewById(R.id.change_adress);
        silentModeSwitch = findViewById(R.id.silent_mode_switch);
        darkModeSwitch = findViewById(R.id.dark_mode_switch);
        ImageView backButton = findViewById(R.id.back_button_pref);


        // Cargar el estado de modo guardado
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean("dark_mode", false);
        darkModeSwitch.setChecked(isDarkMode);

        // Listener para cambiar el tema
     /*   darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("dark_mode", isChecked);
            editor.apply(); // Guardar configuración
            // Aplicar el cambio de tema
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }

            // Retrasar recreate() para evitar ANR
            new Handler(Looper.getMainLooper()).postDelayed(this::recreate, 300);
        });*/

        // Listener para el botón de retroceso
        backButton.setOnClickListener(v -> finish());

        // Obtener usuario autenticado en Firebase
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            saveUserToDatabase(firebaseUser); // Guarda o actualiza los datos en Firebase
            userName.setText(firebaseUser.getDisplayName());
            userEmail.setText(firebaseUser.getEmail());
        }

        // Cargar datos del usuario desde Google Sign-In
        loadGoogleUserInfo();

        // Cargar datos desde SharedPreferences
        sharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE);
        userPhone.setText(sharedPreferences.getString("phone", "Número no disponible"));
        userAddress.setText(sharedPreferences.getString("address", "Dirección no disponible"));
        silentModeSwitch.setChecked(sharedPreferences.getBoolean("silent_mode", false));
        darkModeSwitch.setChecked(sharedPreferences.getBoolean("dark_mode", false));

        // Listeners para cambiar datos
        changePhone.setOnClickListener(v -> showEditDialog("Teléfono", userPhone, "phone"));
        changeAddress.setOnClickListener(v -> showEditDialog("Dirección", userAddress, "address"));
    }

    private void saveUserToDatabase(FirebaseUser user) {
        if (user == null) {
            Log.e("Firebase", "Error: Usuario es NULL");
            return;
        }

        String userId = user.getUid();
        String name = user.getDisplayName();
        String email = user.getEmail();
        String phone = user.getPhoneNumber() != null ? user.getPhoneNumber() : "No disponible";
        String profileImageUrl = user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : "";

        Log.d("Firebase", "Guardando usuario: " + userId);

        User newUser = new User(userId, name, email, phone, profileImageUrl);

        databaseReference.child(userId).setValue(newUser)
                .addOnSuccessListener(aVoid -> Log.d("Firebase", "Usuario guardado correctamente en la BD"))
                .addOnFailureListener(e -> Log.e("Firebase", "Error al guardar usuario en Firebase", e));
    }


    private void loadGoogleUserInfo() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            userName.setText(account.getDisplayName());
            userEmail.setText(account.getEmail());

            if (account.getPhotoUrl() != null) {
                String photoUrl = account.getPhotoUrl().toString();
                ImageRequest imageRequest = new ImageRequest(photoUrl,
                        response -> userProfileImage.setImageBitmap(getCircularBitmap(response)),
                        0, 0, ImageView.ScaleType.CENTER_CROP, null,
                        error -> userProfileImage.setImageResource(R.drawable.perfilbefit));
                requestQueue.add(imageRequest);
            }
        }
    }

    private Bitmap getCircularBitmap(Bitmap bitmap) {
        int size = Math.min(bitmap.getWidth(), bitmap.getHeight());
        Bitmap output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(bitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));

        float radius = size / 2f;
        canvas.drawCircle(radius, radius, radius, paint);

        return output;
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
                        startActivity(new Intent(getApplicationContext(), EjercicioActivity.class));
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
    private void showEditDialog(String field, TextView textView, String firebaseField) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Modificar " + field);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(textView.getText().toString());

        builder.setView(input);
        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String newValue = input.getText().toString();
            textView.setText(newValue);
            saveToPreferences(firebaseField, newValue);
            updateUserFieldInDatabase(firebaseField, newValue);
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void saveToPreferences(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private void updateUserFieldInDatabase(String field, String value) {
        if (firebaseUser != null) {
            databaseReference.child(firebaseUser.getUid()).child(field).setValue(value)
                    .addOnSuccessListener(aVoid -> {
                        Log.d("Firebase", "Campo " + field + " actualizado");
                        Toast.makeText(this, "Datos actualizados", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> Log.e("Firebase", "Error al actualizar " + field, e));
        }
    }
}
