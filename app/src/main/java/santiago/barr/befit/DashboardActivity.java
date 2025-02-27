package santiago.barr.befit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

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

public class DashboardActivity extends Activity {
    private TextView user_nameDBR;
    private ImageView userProfileImage;
    private ImageView actividadesfoto, actividadesfoto2, actividadesfoto3, actividadesfoto4;
    private RequestQueue requestQueue;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);
        setupBottomNavigation();  // Activa la barra de navegación

        // Inicializar Firebase Database
        databaseReference = FirebaseDatabase.getInstance("https://befit-81d9e-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users");

        // Inicializar Volley request queue
        requestQueue = Volley.newRequestQueue(this);

        // Inicialización de los ImageViews
        userProfileImage = findViewById(R.id.user_avatar);
        actividadesfoto = findViewById(R.id.imageViewActividades1);  // Ejemplo de ImageView
        actividadesfoto2 = findViewById(R.id.imageViewActividades2);
        actividadesfoto3 = findViewById(R.id.imageViewActividades3);
        actividadesfoto4 = findViewById(R.id.imageViewActividades4);

        // Asignar un único OnClickListener para todos los ImageViews
        asignarListener(actividadesfoto, "");
        asignarListener(actividadesfoto2, "");
        asignarListener(actividadesfoto3, "");
        asignarListener(actividadesfoto4, "");

        // Inicialización del TextView de nombre de usuario
        user_nameDBR = findViewById(R.id.user_name);

        // Realizar validación de usuario y cargar su información
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            user_nameDBR.setText(firebaseUser.getDisplayName());
            loadGoogleUserInfo();
        }
        // Encuentra la vista y asigna el listener de calendario
        ImageView imageViewCalendario = findViewById(R.id.imageViewCalendario);
        imageViewCalendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });
    }

    // Método genérico para asignar un OnClickListener con mensaje dinámico
    private void asignarListener(ImageView imageView, final String actividadNombre) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarToastDesarrollo(actividadNombre);
            }
        });
    }

    // Método para mostrar un Toast con el nombre de la actividad que está en desarrollo
    private void mostrarToastDesarrollo(String actividad) {
        Toast.makeText(this, actividad + "Funcionalidad en desarrollo", Toast.LENGTH_SHORT).show();
    }

    // Barra inferior
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
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class)); // Abre perfil en vez de settings
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    // Carga de datos del usuario (foto de perfil)
    private void loadGoogleUserInfo() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null && account.getPhotoUrl() != null) {
            String photoUrl = account.getPhotoUrl().toString();
            ImageRequest imageRequest = new ImageRequest(photoUrl,
                    response -> userProfileImage.setImageBitmap(response),
                    0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.ARGB_8888,
                    error -> userProfileImage.setImageResource(R.drawable.perfilbefit));
            requestQueue.add(imageRequest);
        } else {
            // Si no hay foto, usar una imagen predeterminada
            userProfileImage.setImageResource(R.drawable.perfilbefit);
        }
    }
}
