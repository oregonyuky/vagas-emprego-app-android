package br.edu.unoeste.vagasonline;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.edu.unoeste.vagasonline.model.Vaga;

public class VagaAdapter extends RecyclerView.Adapter<VagaAdapter.VagaViewHolder> {
    public interface OnVagaClickListener {
        void onClick(Vaga vaga);
    }

    private final List<Vaga> vagas = new ArrayList<>();
    private final OnVagaClickListener listener;

    public VagaAdapter(OnVagaClickListener listener) {
        this.listener = listener;
    }

    public void setVagas(List<Vaga> novasVagas) {
        vagas.clear();
        if (novasVagas != null) vagas.addAll(novasVagas);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VagaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vaga, parent, false);
        return new VagaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VagaViewHolder holder, int position) {
        Vaga vaga = vagas.get(position);
        NumberFormat currency = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        holder.cargo.setText(vaga.getCargo().getNome());
        holder.empresa.setText(vaga.getEmpresa().getNomeFantasia());
        holder.resumo.setText(vaga.getCidade() + "/" + vaga.getEstado() + "  •  "
                + vaga.getRegime() + "  •  " + currency.format(vaga.getRemuneracao()));
        holder.itemView.setOnClickListener(view -> listener.onClick(vaga));
    }

    @Override
    public int getItemCount() {
        return vagas.size();
    }

    static class VagaViewHolder extends RecyclerView.ViewHolder {
        final TextView cargo;
        final TextView empresa;
        final TextView resumo;

        VagaViewHolder(@NonNull View itemView) {
            super(itemView);
            cargo = itemView.findViewById(R.id.textCargo);
            empresa = itemView.findViewById(R.id.textEmpresa);
            resumo = itemView.findViewById(R.id.textResumo);
        }
    }
}
