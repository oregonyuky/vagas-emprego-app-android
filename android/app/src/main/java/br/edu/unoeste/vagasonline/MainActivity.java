package br.edu.unoeste.vagasonline;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import br.edu.unoeste.vagasonline.model.InteresseRequest;
import br.edu.unoeste.vagasonline.model.Vaga;
import br.edu.unoeste.vagasonline.network.ApiClient;
import br.edu.unoeste.vagasonline.network.VagasApi;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "VagasOnline";
    private static final String TODOS_CARGOS = "Todos os cargos";
    private static final String TODOS_REGIMES = "Todos os regimes";
    private static final String TODAS_FAIXAS = "Todas as faixas";
    private static final String ATE_3000 = "Até R$ 3.000";
    private static final String DE_3000_A_5000 = "R$ 3.001 a R$ 5.000";
    private static final String DE_5000_A_8000 = "R$ 5.001 a R$ 8.000";
    private static final String ACIMA_8000 = "Acima de R$ 8.000";
    private VagasApi api;
    private VagaAdapter adapter;
    private TextInputEditText inputCidade;
    private TextInputEditText inputEstado;
    private AutoCompleteTextView inputCargo;
    private AutoCompleteTextView inputRegime;
    private AutoCompleteTextView inputRemuneracao;
    private ProgressBar progress;
    private TextView empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        api = ApiClient.create(getString(R.string.api_base_url));
        inputCidade = findViewById(R.id.inputCidade);
        inputEstado = findViewById(R.id.inputEstado);
        inputCargo = findViewById(R.id.inputCargo);
        inputRegime = findViewById(R.id.inputRegime);
        inputRemuneracao = findViewById(R.id.inputRemuneracao);
        progress = findViewById(R.id.progress);
        empty = findViewById(R.id.textEmpty);
        configurarFiltrosDeSelecao();

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
        String cargo = inputCargo.getText().toString();
        String regime = inputRegime.getText().toString();
        String faixaRemuneracao = inputRemuneracao.getText().toString();
        api.listarVagas()
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Vaga>> call,
                                           @NonNull Response<List<Vaga>> response) {
                        setLoading(false);
                        if (response.isSuccessful() && response.body() != null) {
                            atualizarOpcoesDeCargo(response.body());
                            List<Vaga> filtradas = new ArrayList<>();
                            for (Vaga vaga : response.body()) {
                                boolean cidadeConfere = cidade.isBlank()
                                        || vaga.getCidade().toLowerCase(Locale.ROOT)
                                        .contains(cidade.toLowerCase(Locale.ROOT));
                                boolean estadoConfere = estado.isBlank()
                                        || vaga.getEstado().equalsIgnoreCase(estado);
                                boolean cargoConfere = TODOS_CARGOS.equals(cargo)
                                        || vaga.getCargo().equalsIgnoreCase(cargo);
                                boolean regimeConfere = TODOS_REGIMES.equals(regime)
                                        || vaga.getRegime().equalsIgnoreCase(regime);
                                boolean remuneracaoConfere = faixaRemuneracaoConfere(
                                        vaga.getRemuneracao(), faixaRemuneracao);
                                if (cidadeConfere && estadoConfere && cargoConfere
                                        && regimeConfere && remuneracaoConfere) {
                                    filtradas.add(vaga);
                                }
                            }
                            adapter.setVagas(filtradas);
                            empty.setVisibility(filtradas.isEmpty() ? View.VISIBLE : View.GONE);
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

    private void configurarFiltrosDeSelecao() {
        definirOpcoes(inputCargo, List.of(TODOS_CARGOS), TODOS_CARGOS);
        definirOpcoes(inputRegime,
                Arrays.asList(TODOS_REGIMES, "CLT", "PJ", "Estágio"), TODOS_REGIMES);
        definirOpcoes(inputRemuneracao,
                Arrays.asList(TODAS_FAIXAS, ATE_3000, DE_3000_A_5000,
                        DE_5000_A_8000, ACIMA_8000), TODAS_FAIXAS);
    }

    private void atualizarOpcoesDeCargo(List<Vaga> vagas) {
        String cargoSelecionado = inputCargo.getText().toString();
        Set<String> cargos = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        for (Vaga vaga : vagas) {
            if (vaga.getCargo() != null && !vaga.getCargo().isBlank()) {
                cargos.add(vaga.getCargo());
            }
        }
        List<String> opcoes = new ArrayList<>();
        opcoes.add(TODOS_CARGOS);
        opcoes.addAll(cargos);
        definirOpcoes(inputCargo, opcoes,
                opcoes.contains(cargoSelecionado) ? cargoSelecionado : TODOS_CARGOS);
    }

    private void definirOpcoes(AutoCompleteTextView campo, List<String> opcoes, String selecao) {
        campo.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, opcoes));
        campo.setText(selecao, false);
    }

    private boolean faixaRemuneracaoConfere(String remuneracao, String faixa) {
        if (TODAS_FAIXAS.equals(faixa)) return true;
        double valor = converterRemuneracao(remuneracao);
        if (Double.isNaN(valor)) return false;
        return switch (faixa) {
            case ATE_3000 -> valor <= 3000;
            case DE_3000_A_5000 -> valor > 3000 && valor <= 5000;
            case DE_5000_A_8000 -> valor > 5000 && valor <= 8000;
            case ACIMA_8000 -> valor > 8000;
            default -> true;
        };
    }

    private double converterRemuneracao(String remuneracao) {
        if (remuneracao == null) return Double.NaN;
        String valor = remuneracao.replaceAll("[^0-9,]", "").replace(',', '.');
        try {
            return Double.parseDouble(valor);
        } catch (NumberFormatException exception) {
            return Double.NaN;
        }
    }

    private void mostrarDetalhes(Vaga vaga) {
        String details = vaga.getEmpresa().getNomeFantasia() + "\n"
                + vaga.getCidade() + "/" + vaga.getEstado() + " • " + vaga.getRegime() + "\n"
                + vaga.getJornadaTrabalho() + " • " + vaga.getRemuneracao() + "\n\n"
                + "Formação\n" + vaga.getFormacao() + "\n\n"
                + "Requisitos\n" + vaga.getPreRequisitos() + "\n\n"
                + "Conhecimentos\n" + vaga.getConhecimentosRequeridos();

        new AlertDialog.Builder(this)
                .setTitle(vaga.getCargo())
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
        aplicarMascaraTelefone(telefone);
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
                    int digitosTelefone = text(telefone).replaceAll("\\D", "").length();
                    if (text(nome).isBlank() || text(cpf).length() != 11
                            || text(email).isBlank()
                            || (digitosTelefone != 10 && digitosTelefone != 11)) {
                        Toast.makeText(this,
                                "Preencha todos os campos, o CPF com 11 dígitos e o telefone com DDD.",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    enviarInteresse(new InteresseRequest(
                            vaga, text(nome), text(cpf), text(email), text(telefone)), dialog);
                }));
        dialog.show();
    }

    private void enviarInteresse(InteresseRequest request, AlertDialog dialog) {
        api.registrarInteresse(request).enqueue(new Callback<>() {
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

    private void aplicarMascaraTelefone(EditText campo) {
        campo.addTextChangedListener(new TextWatcher() {
            private boolean atualizando;

            @Override
            public void beforeTextChanged(CharSequence texto, int inicio, int quantidade, int depois) {
            }

            @Override
            public void onTextChanged(CharSequence texto, int inicio, int antes, int quantidade) {
            }

            @Override
            public void afterTextChanged(Editable texto) {
                if (atualizando) return;

                String numeros = texto.toString().replaceAll("\\D", "");
                if (numeros.length() > 11) {
                    numeros = numeros.substring(0, 11);
                }

                String formatado = formatarTelefone(numeros);
                atualizando = true;
                campo.setText(formatado);
                campo.setSelection(formatado.length());
                atualizando = false;
            }
        });
    }

    private String formatarTelefone(String numeros) {
        if (numeros.isEmpty()) return "";
        if (numeros.length() <= 2) return "(" + numeros;
        if (numeros.length() <= 6) {
            return "(" + numeros.substring(0, 2) + ") " + numeros.substring(2);
        }
        if (numeros.length() <= 10) {
            return "(" + numeros.substring(0, 2) + ") "
                    + numeros.substring(2, 6) + "-" + numeros.substring(6);
        }
        return "(" + numeros.substring(0, 2) + ") "
                + numeros.substring(2, 7) + "-" + numeros.substring(7);
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
