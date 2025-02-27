package santiago.barr.befit;

import android.content.Intent;
import android.net.Uri;
import android.provider.CalendarContract;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

public class CalendarActivity extends AppCompatActivity {

    private ImageView backButton, Befit;
    private Button addEvent1Button, addEvent2Button;
    private TextView eventName1, eventLocation1, eventDate1, eventTime1;
    private TextView eventName2, eventLocation2, eventDate2, eventTime2;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        setupBottomNavigation();  // Activa la barra de navegación

        // Inicializar vistas
        backButton = findViewById(R.id.back_button_pref);
        Befit = findViewById(R.id.befitimagetop);
        addEvent1Button = findViewById(R.id.add_event_1);
        addEvent2Button = findViewById(R.id.add_event_2);
        eventName1 = findViewById(R.id.textViewNombreEvento);
        eventLocation1 = findViewById(R.id.textViewUbi);
        eventDate1 = findViewById(R.id.textViewFechaEvento);
        eventTime1 = findViewById(R.id.textViewHoraEvento);
        eventName2 = findViewById(R.id.textViewNombreEvento2);
        eventLocation2 = findViewById(R.id.textViewUbi2);
        eventDate2 = findViewById(R.id.textViewFechaEvento2);
        eventTime2 = findViewById(R.id.textViewHoraEvento2);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Configurar botón de retroceso
        backButton.setOnClickListener(v -> onBackPressed());

        // Manejo de eventos en botones
        addEvent1Button.setOnClickListener(v -> agregarEventoCalendario(
                eventName1.getText().toString(),
                eventLocation1.getText().toString(),
                eventDate1.getText().toString(),
                eventTime1.getText().toString()
        ));

        addEvent2Button.setOnClickListener(v -> agregarEventoCalendario(
                eventName2.getText().toString(),
                eventLocation2.getText().toString(),
                eventDate2.getText().toString(),
                eventTime2.getText().toString()
        ));

    }

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


    private void agregarEventoCalendario(String titulo, String ubicacion, String fecha, String hora) {
        try {
            String[] fechaParts = fecha.split("/");
            if (fechaParts.length < 3) {
                Toast.makeText(this, "Formato de fecha incorrecto", Toast.LENGTH_SHORT).show();
                return;
            }

            int dia = Integer.parseInt(fechaParts[0]);
            int mes = Integer.parseInt(fechaParts[1]) - 1; // Meses en Calendar empiezan desde 0
            int anio = Integer.parseInt("20" + fechaParts[2]);

            String[] horaParts = hora.split(":");
            if (horaParts.length < 2) {
                Toast.makeText(this, "Formato de hora incorrecto", Toast.LENGTH_SHORT).show();
                return;
            }

            int horaEvento = Integer.parseInt(horaParts[0]);
            int minutoEvento = Integer.parseInt(horaParts[1]);

            Calendar beginTime = Calendar.getInstance();
            beginTime.set(anio, mes, dia, horaEvento, minutoEvento);
            Calendar endTime = (Calendar) beginTime.clone();
            endTime.add(Calendar.HOUR, 2);

            abrirCalendario(titulo, ubicacion, beginTime.getTimeInMillis(), endTime.getTimeInMillis());

        } catch (Exception e) {
            Toast.makeText(this, "Error al agregar evento: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Método para crear un evento en el calendario
    public void abrirCalendario(String title, String location, long begin, long end) {
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, title)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, location)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, begin)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, end);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "No se encontró una aplicación de calendario", Toast.LENGTH_LONG).show();
        }
    }
}
