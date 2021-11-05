package com.alejandroramirez.covidalert;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListaClasesAdapter extends RecyclerView.Adapter<ListaClasesAdapter.ClaseViewHolder> {

    public ArrayList<Clase> listaClases;
    Usuario usuario;

    public ListaClasesAdapter(Usuario usuario,ArrayList<Clase> listaClases) {
        this.listaClases = listaClases;
        this.usuario=usuario;
    }

    @NonNull
    @Override
    public ClaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_clases, null);
        return new ClaseViewHolder(usuario,view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClaseViewHolder holder, int position) {
        //Clases clase = listaClases.get(position);
        holder.tv_nombre.setText(listaClases.get(position).getNombre());
        holder.tv_host.setText(listaClases.get(position).getHost());
        holder.tv_fecha.setText(listaClases.get(position).getFecha());
        holder.tv_hora.setText(listaClases.get(position).getHora());
        holder.tv_lugar.setText(listaClases.get(position).getLugar());
    }

    @Override
    public int getItemCount() { return listaClases.size(); }

    public class ClaseViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nombre, tv_host, tv_lugar, tv_hora, tv_fecha;

        public ClaseViewHolder(Usuario user,@NonNull View itemView) {
            super(itemView);
            tv_nombre = itemView.findViewById(R.id.tv_item_nombre);
            tv_host = itemView.findViewById(R.id.tv_item_prop);
            tv_lugar = itemView.findViewById(R.id.tv_item_lugar);
            tv_hora = itemView.findViewById(R.id.tv_item_hora);
            tv_fecha = itemView.findViewById(R.id.tv_item_fecha);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context contexto = view.getContext();
                    Intent intento = new Intent(contexto, VerInfoClaseAlumno.class);
                    intento.putExtra("usuario",user);
                    intento.putExtra("ID", listaClases.get(getAdapterPosition()).getId());
                    contexto.startActivity(intento);


                }
            });
        }
    }
}
