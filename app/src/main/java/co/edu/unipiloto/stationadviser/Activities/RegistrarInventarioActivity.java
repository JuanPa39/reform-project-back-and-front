package co.edu.unipiloto.stationadviser.Activities;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import co.edu.unipiloto.stationadviser.R;
import co.edu.unipiloto.stationadviser.network.ApiClient;
import co.edu.unipiloto.stationadviser.network.ApiService;
import co.edu.unipiloto.stationadviser.network.TokenManager;
import co.edu.unipiloto.stationadviser.network.models.InventarioRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrarInventarioActivity extends AppCompatActivity {

    private Spinner spinnerTipo;
    private EditText editCantidad;
    private Button buttonGuardar;
    private ProgressBar progressBar;

    private ApiService apiService;
    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_inventario);

        tokenManager = new TokenManager(this);
        apiService = ApiClient.getClientWithToken(tokenManager.getToken()).create(ApiService.class);

        spinnerTipo = findViewById(R.id.spinnerTipo);
        editCantidad = findViewById(R.id.editCantidad);
        buttonGuardar = findViewById(R.id.buttonGuardar);
        progressBar = findViewById(R.id.progressBar);

        String[] tipos = {"ACPM", "Gasolina Corriente", "Gasolina Extra"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, tipos);
        spinnerTipo.setAdapter(adapter);

        buttonGuardar.setOnClickListener(v -> registrarInventario());
    }

    private void registrarInventario() {
        String tipo = spinnerTipo.getSelectedItem().toString();
        String cantidadStr = editCantidad.getText().toString();

        if (cantidadStr.isEmpty()) {
            Toast.makeText(this, "Ingrese cantidad", Toast.LENGTH_SHORT).show();
            return;
        }

        int cantidad = Integer.parseInt(cantidadStr);

        mostrarLoading(true);

        InventarioRequest request = new InventarioRequest(tipo, cantidad);
        Call<Void> call = apiService.registrarInventario(request);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                mostrarLoading(false);
                if (response.isSuccessful()) {
                    Toast.makeText(RegistrarInventarioActivity.this, "Inventario guardado", Toast.LENGTH_SHORT).show();
                    editCantidad.setText("");
                } else {
                    Toast.makeText(RegistrarInventarioActivity.this, "Error al guardar inventario", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                mostrarLoading(false);
                Toast.makeText(RegistrarInventarioActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarLoading(boolean mostrar) {
        progressBar.setVisibility(mostrar ? android.view.View.VISIBLE : android.view.View.GONE);
        buttonGuardar.setEnabled(!mostrar);
    }
}