package co.edu.unipiloto.stationadviser.Activities;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import co.edu.unipiloto.stationadviser.R;
import co.edu.unipiloto.stationadviser.network.ApiClient;
import co.edu.unipiloto.stationadviser.network.ApiService;
import co.edu.unipiloto.stationadviser.network.TokenManager;
import co.edu.unipiloto.stationadviser.network.models.DisponibilidadResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsultarDisponibilidadActivity extends AppCompatActivity {

    Spinner spinnerTipo, spinnerTipoVehiculo;
    EditText editLitros;
    Button buttonConsultar;
    TextView textResultado;
    ProgressBar progressBar;

    private ApiService apiService;
    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_disponibilidad);

        tokenManager = new TokenManager(this);
        apiService = ApiClient.getClientWithToken(tokenManager.getToken()).create(ApiService.class);

        spinnerTipo = findViewById(R.id.spinnerTipo);
        spinnerTipoVehiculo = findViewById(R.id.spinnerTipoVehiculo);
        editLitros = findViewById(R.id.editLitros);
        buttonConsultar = findViewById(R.id.buttonConsultar);
        textResultado = findViewById(R.id.textResultado);
        progressBar = findViewById(R.id.progressBar);

        String[] tiposCombustible = {"ACPM", "Gasolina Corriente", "Gasolina Extra"};
        ArrayAdapter<String> adapterCombustible = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, tiposCombustible);
        spinnerTipo.setAdapter(adapterCombustible);

        String[] tiposVehiculo = {"particular", "publico", "carga", "oficial", "diplomatico"};
        ArrayAdapter<String> adapterVehiculo = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, tiposVehiculo);
        spinnerTipoVehiculo.setAdapter(adapterVehiculo);

        buttonConsultar.setOnClickListener(v -> consultarDisponibilidad());
    }

    private void consultarDisponibilidad() {
        String combustible = spinnerTipo.getSelectedItem().toString();
        String litrosStr = editLitros.getText().toString();

        if (litrosStr.isEmpty()) {
            Toast.makeText(this, "Ingrese litros", Toast.LENGTH_SHORT).show();
            return;
        }

        mostrarLoading(true);

        Call<DisponibilidadResponse> call = apiService.getDisponibilidad(combustible);
        call.enqueue(new Callback<DisponibilidadResponse>() {
            @Override
            public void onResponse(Call<DisponibilidadResponse> call, Response<DisponibilidadResponse> response) {
                mostrarLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    double litros = Double.parseDouble(litrosStr);
                    double disponible = response.body().getCantidadDisponible();
                    boolean subsidio = response.body().isAplicaSubsidio();

                    String resultado = "Disponible: " + disponible + " litros\n";
                    if (disponible >= litros) {
                        resultado += "✔ Puede comprar\n";
                    } else {
                        resultado += "❌ No hay suficiente combustible\n";
                    }
                    resultado += subsidio ? "💰 Aplica subsidio según Decreto 1428/2025" : "🚫 No aplica subsidio";
                    textResultado.setText(resultado);
                } else {
                    textResultado.setText("Error al consultar disponibilidad");
                }
            }

            @Override
            public void onFailure(Call<DisponibilidadResponse> call, Throwable t) {
                mostrarLoading(false);
                textResultado.setText("Error de conexión: " + t.getMessage());
            }
        });
    }

    private void mostrarLoading(boolean mostrar) {
        progressBar.setVisibility(mostrar ? android.view.View.VISIBLE : android.view.View.GONE);
        buttonConsultar.setEnabled(!mostrar);
    }
}