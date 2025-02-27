package santiago.barr.befit;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import java.util.HashMap;
import java.util.Map;

public class PremiumActivity extends AppCompatActivity {
    private Spinner spinnerPlans;
    private TextView textPlanDetails;
    private ImageView backButton;
    private ConstraintLayout backgroundContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);

        backButton = findViewById(R.id.back_button);
        // Configurar botón de retroceso
        backButton.setOnClickListener(v -> onBackPressed());
        spinnerPlans = findViewById(R.id.spinner_premium);
        textPlanDetails = findViewById(R.id.text_plan_details);

        // Lista de planes
        String[] planNames = {"Plan Gratuito", "Plan Pro", "Plan Premium 👑"};

        // Características de cada plan
        Map<String, String> planDetails = new HashMap<>();
        planDetails.put("Plan Gratuito",
                "✅ Acceso a la aplicación\n" +
                        "✅ Visualización de las actividades\n" +
                        "✅ Acceso a recetas saludables básicas\n" +
                        "✅ Consulta general con nutricionista");

        planDetails.put("Plan Pro",
                "✅ Consulta con nutricionista 1 vez a la semana\n" +
                        "✅ Seguimiento mensual del progreso\n" +
                        "✅ 5 clases online al mes\n" +
                        "✅ Consulta con entrenadores personales 2 veces al mes\n" +
                        "✅ Acceso a todas las recetas saludables\n"+
                        "✅ Seguimiento mensual del progreso\n" +
                        "✅ Acceso a todas las recetas saludables\n" +
                        "✅ 4 actividades en grupo al mes\n" +
                        "✅ Dietas personalizadas\n");

        planDetails.put("Plan Premium 👑",
                "✅ Consulta con nutricionistas ilimitado\n" +
                        "✅ Seguimiento semanal del progreso del usuario\n" +
                        "✅ Clases online ilimitadas\n" +
                        "✅ Consulta con entrenadores personales ilimitado\n" +
                        "✅ Dietas personalizadas\n" +
                        "✅ Acceso a todas las recetas saludables\n" +
                        "✅ Actividades en grupo ilimitadas");

        // Adaptador para el Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, planNames);
        spinnerPlans.setAdapter(adapter);

        // Listener para detectar cambios en la selección del Spinner
        spinnerPlans.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedPlan = planNames[position];
                textPlanDetails.setText(planDetails.get(selectedPlan));

                // Cambiar color de fondo del TextView y del ConstraintLayout según el plan
                if (selectedPlan.equals("Plan Gratuito")) {
                    textPlanDetails.setBackgroundColor(Color.parseColor("#B5B2B2")); // Gris claro
                } else if (selectedPlan.equals("Plan Pro")) {
                    textPlanDetails.setBackgroundColor(Color.parseColor("#4CAF50")); // Verde
                } else if (selectedPlan.equals("Plan Premium 👑")) {
                    textPlanDetails.setBackgroundColor(Color.parseColor("#FFEB3B")); // Amarillo
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                textPlanDetails.setText("Selecciona un plan para ver detalles.");
            }
        });
    }
}
