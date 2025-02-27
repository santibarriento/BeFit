package santiago.barr.befit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.FirebaseApp;

import android.net.Uri;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    private AuthManager authManager;
    private VideoView videoBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        // Inicializa el VideoView
        videoBackground = findViewById(R.id.video_background);
        playBackgroundVideo();

        // Inicializa AuthManager
        authManager = new AuthManager(this);

        // Botón de Google
        ImageView googleButton = findViewById(R.id.google_login);
        googleButton.setOnClickListener(v -> authManager.signInWithGoogle(this));
    }

    private void playBackgroundVideo() {
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.video_inicio;
        Uri uri = Uri.parse(videoPath);
        videoBackground.setVideoURI(uri);
        videoBackground.start();

        // Reproducir en bucle
        videoBackground.setOnCompletionListener(mp -> videoBackground.start());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        authManager.handleActivityResult(requestCode, data, this);
    }
}
