package co.edu.unipiloto.stationadviser.Activities;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.List;

import co.edu.unipiloto.stationadviser.R;
import co.edu.unipiloto.stationadviser.network.ApiClient;
import co.edu.unipiloto.stationadviser.network.ApiService;
import co.edu.unipiloto.stationadviser.network.TokenManager;
import co.edu.unipiloto.stationadviser.network.models.NormativaResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerNormativasActivity extends AppCompatActivity {

    private LinearLayout contenedorNormativas;
    private ProgressBar progressBar;
    private ApiService apiService;
    private TokenManager tokenManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_normativas);

        tokenManager = new TokenManager(this);
        apiService = ApiClient.getClientWithToken(tokenManager.getToken()).create(ApiService.class);

        contenedorNormativas = findViewById(R.id.contenedorNormativas);
        progressBar = findViewById(R.id.progressBar);

        cargarNormativas();
    }

    private void cargarNormativas() {
        mostrarLoading(true);
        contenedorNormativas.removeAllViews();

        Call<List<NormativaResponse>> call = apiService.getNormativas();
        call.enqueue(new Callback<List<NormativaResponse>>() {
            @Override
            public void onResponse(Call<List<NormativaResponse>> call, Response<List<NormativaResponse>> response) {
                mostrarLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    List<NormativaResponse> normativas = response.body();

                    if (normativas.isEmpty()) {
                        TextView tvVacio = new TextView(VerNormativasActivity.this);
                        tvVacio.setText("No hay normativas registradas.");
                        tvVacio.setTextColor(0xFFCCCCCC);
                        tvVacio.setPadding(16, 16, 16, 16);
                        contenedorNormativas.addView(tvVacio);
                        return;
                    }

                    for (NormativaResponse n : normativas) {
                        CardView card = new CardView(VerNormativasActivity.this);
                        LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        cardParams.setMargins(0, 0, 0, 16);
                        card.setLayoutParams(cardParams);
                        card.setCardBackgroundColor(0xFF1A2D42);
                        card.setRadius(16f);
                        card.setCardElevation(4f);

                        LinearLayout layout = new LinearLayout(VerNormativasActivity.this);
                        layout.setOrientation(LinearLayout.VERTICAL);
                        layout.setPadding(32, 24, 32, 24);

                        TextView tvTitulo = new TextView(VerNormativasActivity.this);
                        tvTitulo.setText(n.getNombre());
                        tvTitulo.setTextColor(0xFFFFFFFF);
                        tvTitulo.setTextSize(15f);
                        tvTitulo.setPadding(0, 0, 0, 8);

                        TextView tvDescripcion = new TextView(VerNormativasActivity.this);
                        tvDescripcion.setText(n.getDescripcion());
                        tvDescripcion.setTextColor(0xFFAAAAAA);
                        tvDescripcion.setTextSize(13f);
                        tvDescripcion.setPadding(0, 0, 0, 8);

                        TextView tvFecha = new TextView(VerNormativasActivity.this);
                        tvFecha.setText("Vigente desde: " + n.getFechaInicio());
                        tvFecha.setTextColor(0xFF2196F3);
                        tvFecha.setTextSize(12f);

                        layout.addView(tvTitulo);
                        layout.addView(tvDescripcion);
                        layout.addView(tvFecha);
                        card.addView(layout);
                        contenedorNormativas.addView(card);
                    }
                } else {
                    TextView tvError = new TextView(VerNormativasActivity.this);
                    tvError.setText("Error al cargar normativas");
                    tvError.setTextColor(0xFFEF5350);
                    contenedorNormativas.addView(tvError);
                }
            }

            @Override
            public void onFailure(Call<List<NormativaResponse>> call, Throwable t) {
                mostrarLoading(false);
                TextView tvError = new TextView(VerNormativasActivity.this);
                tvError.setText("Error de conexión: " + t.getMessage());
                tvError.setTextColor(0xFFEF5350);
                contenedorNormativas.addView(tvError);
            }
        });
    }

    private void mostrarLoading(boolean mostrar) {
        progressBar.setVisibility(mostrar ? android.view.View.VISIBLE : android.view.View.GONE);
    }
}