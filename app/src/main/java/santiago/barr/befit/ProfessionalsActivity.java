package santiago.barr.befit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfessionalsActivity extends AppCompatActivity {

    private ImageView backButton;
    private Button contactButton;
    private TextView nameTextView, professionTextView, descriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professionals);  // This links your XML layout
        setupBottomNavigation();  // Activa la barra de navegación
        // Initialize views
        backButton = findViewById(R.id.back_button_pref);
        contactButton = findViewById(R.id.btn_contactar);
        nameTextView = findViewById(R.id.nombre_experto);
        professionTextView = findViewById(R.id.profesion_experto);
        descriptionTextView = findViewById(R.id.descripcion_experto);

        // Set actions for buttons (example: go back when back button is pressed)
        backButton.setOnClickListener(v -> onBackPressed());  // This will go back to the previous activity

        contactButton.setOnClickListener(v -> {
            // Handle contact action (e.g., opening a contact form or sending a message)
            // For now, you can just log or show a toast, for example
            // Toast.makeText(ProfessionalsActivity.this, "Contacting expert", Toast.LENGTH_SHORT).show();
        });

        // Set the data dynamically (you can replace these with dynamic values)
        nameTextView.setText("Daniela Álvarez");
        professionTextView.setText("Nutricionista");
        descriptionTextView.setText("Soy licenciada en Nutrición y apasionada por la salud integral. Mi misión es ayudarte a alcanzar tus metas de bienestar a través de una alimentación equilibrada y adaptada a tus necesidades individuales. Creo firmemente en que cada cuerpo es único y, por ello, mis planes son personalizados, sostenibles y realistas.");
    }
    // Barra inferior
    protected void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setSelectedItemId(R.id.nav_professionals);
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
}
