package santiago.barr.befit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

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

        userProfileImage = findViewById(R.id.user_avatar);
        user_nameDBR = findViewById(R.id.user_name);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            user_nameDBR.setText(firebaseUser.getDisplayName());
            loadGoogleUserInfo();
        }
    }
//barra inferior
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
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class)); // Abre perfil en vez de settings
                        return true;
                    }
                    return false;
                }
            });
        }
    }
//carga datos de usuario
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
