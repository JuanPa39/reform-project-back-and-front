package co.edu.unipiloto.stationadviser.Activities;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import co.edu.unipiloto.stationadviser.R;
import co.edu.unipiloto.stationadviser.network.ApiClient;
import co.edu.unipiloto.stationadviser.network.ApiService;
import co.edu.unipiloto.stationadviser.network.TokenManager;
import co.edu.unipiloto.stationadviser.network.models.VentaRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrarVentaActivity extends AppCompatActivity {

    private Spinner spinnerTipo;
    private EditText editLitros, editPrecio;
    private Button buttonGuardar;
    private ProgressBar progressBar;

    private ApiService apiService;
    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_venta);

        tokenManager = new TokenManager(this);
        apiService = ApiClient.getClientWithToken(tokenManager.getToken()).create(ApiService.class);

        spinnerTipo = findViewById(R.id.spinnerTipo);
        editLitros = findViewById(R.id.editLitros);
        editPrecio = findViewById(R.id.editPrecio);
        buttonGuardar = findViewById(R.id.buttonGuardar);
        progressBar = findViewById(R.id.progressBar);

        String[] tipos = {"ACPM", "Gasolina Corriente", "Gasolina Extra"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, tipos);
        spinnerTipo.setAdapter(adapter);

        buttonGuardar.setOnClickListener(v -> registrarVenta());
    }

    private void registrarVenta() {
        String tipo = spinnerTipo.getSelectedItem().toString();
        String litrosStr = editLitros.getText().toString();
        String precioStr = editPrecio.getText().toString();

        if (litrosStr.isEmpty() || precioStr.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        double litros = Double.parseDouble(litrosStr);
        double precio = Double.parseDouble(precioStr);
        double montoTotal = litros * precio;

        mostrarLoading(true);

        VentaRequest request = new VentaRequest(tipo, litros, precio, montoTotal);
        Call<Void> call = apiService.registrarVenta(request);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                mostrarLoading(false);
                if (response.isSuccessful()) {
                    Toast.makeText(RegistrarVentaActivity.this, "Venta registrada", Toast.LENGTH_SHORT).show();
                    editLitros.setText("");
                    editPrecio.setText("");
                } else {
                    Toast.makeText(RegistrarVentaActivity.this, "Error al registrar venta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                mostrarLoading(false);
                Toast.makeText(RegistrarVentaActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarLoading(boolean mostrar) {
        progressBar.setVisibility(mostrar ? android.view.View.VISIBLE : android.view.View.GONE);
        buttonGuardar.setEnabled(!mostrar);
    }
}