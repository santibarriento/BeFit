package santiago.barr.befit;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.OAuthProvider;

public class AuthManager {

    private static final int RC_SIGN_IN = 100; // Código para Google Sign-In
    private final GoogleSignInClient googleSignInClient;
    private final FirebaseAuth firebaseAuth;

    public AuthManager(Activity activity) {
        // Configuración de Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(R.string.default_web_client_id)) // ID del cliente OAuth
                .requestEmail()
                .build();

        this.googleSignInClient = GoogleSignIn.getClient(activity, gso);
        this.firebaseAuth = FirebaseAuth.getInstance();
    }

    // Inicio de sesión con Google
    public void signInWithGoogle(Activity activity) {
        googleSignInClient.signOut().addOnCompleteListener(activity, task -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            activity.startActivityForResult(signInIntent, RC_SIGN_IN);
        });
    }

    // Inicio de sesión con Apple
    public void signInWithApple(Activity activity) {
        OAuthProvider.Builder provider = OAuthProvider.newBuilder("apple.com");
        provider.addCustomParameter("locale", "en"); // Opcional: idioma

        Task<AuthResult> pendingResultTask = firebaseAuth.getPendingAuthResult();
        if (pendingResultTask != null) {
            pendingResultTask.addOnSuccessListener(authResult -> {
                // Inicio de sesión exitoso
                FirebaseUser user = authResult.getUser();
                Toast.makeText(activity, "Inicio de sesión con Apple: " + user.getEmail(), Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                // Manejar errores
                Toast.makeText(activity, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            firebaseAuth.startActivityForSignInWithProvider(activity, provider.build())
                    .addOnSuccessListener(authResult -> {
                        // Inicio de sesión exitoso
                        FirebaseUser user = authResult.getUser();
                        Toast.makeText(activity, "Inicio de sesión con Apple: " + user.getEmail(), Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        // Manejar errores
                        Toast.makeText(activity, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    // Manejo de resultados para Google
    public void handleActivityResult(int requestCode, Intent data, Activity activity) {
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account.getIdToken(), activity);
                }
            } catch (ApiException e) {
                Toast.makeText(activity, "Error al iniciar sesión con Google: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken, Activity activity) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        // Inicio de sesión exitoso
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        Toast.makeText(activity, "Inicio de sesión con Google: " + user.getEmail(), Toast.LENGTH_SHORT).show();

                        // Crear un Intent para abrir la actividad DashboardActivity
                        Intent intent = new Intent(activity, DashboardActivity.class);
                        activity.startActivity(intent);

                        // Si deseas cerrar la actividad actual (por ejemplo, MainActivity), puedes hacerlo:
                        activity.finish();
                    } else {
                        // Manejar errores
                        Toast.makeText(activity, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
