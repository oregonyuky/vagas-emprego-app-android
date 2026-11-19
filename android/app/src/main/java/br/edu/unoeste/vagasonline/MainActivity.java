package br.edu.unoeste.vagasonline;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import br.edu.unoeste.vagasonline.model.InteresseRequest;
import br.edu.unoeste.vagasonline.model.Vaga;
import br.edu.unoeste.vagasonline.network.ApiClient;
import br.edu.unoeste.vagasonline.network.VagasApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "VagasOnline";
    private VagasApi api;
    private VagaAdapter adapter;
    private TextInputEditText inputCidade;
    private TextInputEditText inputEstado;
    private ProgressBar progress;
    private TextView empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        api = ApiClient.create(getString(R.string.api_base_url));
        inputCidade = findViewById(R.id.inputCidade);
        inputEstado = findViewById(R.id.inputEstado);
        progress = findViewById(R.id.progress);
        empty = findViewById(R.id.textEmpty);

        RecyclerView recycler = findViewById(R.id.recyclerVagas);
        adapter = new VagaAdapter(this::mostrarDetalhes);
        recycler.setAdapter(adapter);
        findViewById(R.id.buttonBuscar).setOnClickListener(view -> buscarVagas());
        buscarVagas();
    }

    private void buscarVagas() {
        setLoading(true);
        String cidade = text(inputCidade);
        String estado = text(inputEstado).toUpperCase(Locale.ROOT);
        api.listarVagas(cidade.isBlank() ? null : cidade, estado.isBlank() ? null : estado)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Vaga>> call,
                                           @NonNull Response<List<Vaga>> response) {
                        setLoading(false);
                        if (response.isSuccessful() && response.body() != null) {
                            adapter.setVagas(response.body());
                            empty.setVisibility(response.body().isEmpty() ? View.VISIBLE : View.GONE);
                        } else {
                            showError("Não foi possível carregar as vagas (HTTP " + response.code() + ").");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Vaga>> call, @NonNull Throwable throwable) {
                        setLoading(false);
                        Log.e(TAG, "Falha ao carregar vagas", throwable);
                        showError("Falha ao conectar com a API. Verifique se o backend está em execução.");
                    }
                });
    }

    private void mostrarDetalhes(Vaga vaga) {
        NumberFormat currency = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        String details = vaga.getEmpresa().getNomeFantasia() + "\n"
                + vaga.getCidade() + "/" + vaga.getEstado() + " • " + vaga.getRegime() + "\n"
                + vaga.getJornada() + " • " + currency.format(vaga.getRemuneracao()) + "\n\n"
                + "Formação\n" + vaga.getFormacao() + "\n\n"
                + "Requisitos\n" + vaga.getRequisitos() + "\n\n"
                + "Conhecimentos\n" + vaga.getConhecimentos();

        new AlertDialog.Builder(this)
                .setTitle(vaga.getCargo().getNome())
                .setMessage(details)
                .setNegativeButton("Fechar", null)
                .setPositiveButton("Tenho interesse", (dialog, which) -> mostrarFormularioInteresse(vaga))
                .show();
    }

    private void mostrarFormularioInteresse(Vaga vaga) {
        int padding = (int) (20 * getResources().getDisplayMetrics().density);
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setPadding(padding, 0, padding, 0);

        EditText nome = input("Nome completo", InputType.TYPE_CLASS_TEXT);
        EditText cpf = input("CPF (somente números)", InputType.TYPE_CLASS_NUMBER);
        EditText email = input("E-mail", InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        EditText telefone = input("Telefone", InputType.TYPE_CLASS_PHONE);
        container.addView(nome);
        container.addView(cpf);
        container.addView(email);
        container.addView(telefone);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Tenho interesse")
                .setView(container)
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Enviar", null)
                .create();

        dialog.setOnShowListener(ignored -> dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(view -> {
                    if (text(nome).isBlank() || text(cpf).length() != 11
                            || text(email).isBlank() || text(telefone).isBlank()) {
                        Toast.makeText(this, "Preencha todos os campos e informe um CPF com 11 dígitos.",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    enviarInteresse(vaga, new InteresseRequest(
                            text(nome), text(cpf), text(email), text(telefone)), dialog);
                }));
        dialog.show();
    }

    private void enviarInteresse(Vaga vaga, InteresseRequest request, AlertDialog dialog) {
        api.registrarInteresse(vaga.getId(), request).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, "Interesse registrado com sucesso!",
                            Toast.LENGTH_LONG).show();
                } else {
                    showError("Confira os dados informados (HTTP " + response.code() + ").");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable throwable) {
                Log.e(TAG, "Falha ao registrar interesse", throwable);
                showError("Não foi possível enviar o interesse. Verifique a conexão com a API.");
            }
        });
    }

    private EditText input(String hint, int inputType) {
        EditText editText = new EditText(this);
        editText.setHint(hint);
        editText.setInputType(inputType);
        editText.setSingleLine(true);
        return editText;
    }

    private String text(EditText editText) {
        return editText.getText() == null ? "" : editText.getText().toString().trim();
    }

    private void setLoading(boolean loading) {
        progress.setVisibility(loading ? View.VISIBLE : View.GONE);
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
