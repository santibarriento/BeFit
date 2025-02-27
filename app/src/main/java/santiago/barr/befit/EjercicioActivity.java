package santiago.barr.befit;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EjercicioActivity extends AppCompatActivity {

    private TextView timerText;
    private ImageView btnPlayPause, btnStop, btnBack;
    private VideoView videoEjercicio;
    private CountDownTimer countDownTimer;
    private boolean isRunning = false;
    private long timeLeftInMillis = 150000; // 2 minutos 30 segundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejercicio);
        setupBottomNavigation();  // Activa la barra de navegación

        // Inicializar vistas
        timerText = findViewById(R.id.timerText);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        btnStop = findViewById(R.id.btnStop);
        btnBack = findViewById(R.id.back_button_pref);
        videoEjercicio = findViewById(R.id.video_ejercicio); // VideoView en el layout

        // Configurar el video
        setupVideo();

        updateTimerText();

        // Botones del temporizador
        btnPlayPause.setOnClickListener(view -> {
            if (isRunning) {
                pauseTimer();
            } else {
                startTimer();
            }
        });

        btnStop.setOnClickListener(view -> resetTimer());

        btnBack.setOnClickListener(view -> finish());
    }

    // Configurar VideoView
    private void setupVideo() {
        // Ruta del video en res/raw
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.flexiones;
        Uri uri = Uri.parse(videoPath);
        videoEjercicio.setVideoURI(uri);

        // Reproducir en bucle
        videoEjercicio.setOnPreparedListener(mp -> {
            mp.setLooping(true);
            videoEjercicio.start();
        });

        // Permitir pausar y reanudar al tocar el video
        videoEjercicio.setOnClickListener(v -> {
            if (videoEjercicio.isPlaying()) {
                videoEjercicio.pause();
            } else {
                videoEjercicio.start();
            }
        });
    }

    // Barra de navegación
    protected void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setSelectedItemId(R.id.nav_trainings);
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

    // Métodos del temporizador
    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            public void onFinish() {
                isRunning = false;
                btnPlayPause.setImageResource(android.R.drawable.ic_media_play);
            }
        }.start();

        isRunning = true;
        btnPlayPause.setImageResource(android.R.drawable.ic_media_pause);
    }

    private void pauseTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        isRunning = false;
        btnPlayPause.setImageResource(android.R.drawable.ic_media_play);
    }

    private void resetTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        timeLeftInMillis = 150000;
        updateTimerText();
        isRunning = false;
        btnPlayPause.setImageResource(android.R.drawable.ic_media_play);
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        timerText.setText(timeFormatted);
    }
}
