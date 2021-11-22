package com.alejandroramirez.covidalert;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.annotation.Native;
import java.util.ArrayList;

public class ListaUsuariosAdapter extends RecyclerView.Adapter<ListaUsuariosAdapter.UsuarioViewHolder>{

    public ArrayList<Usuario> listaUsuarios;
    Usuario usuarioInterfaz;

    public ListaUsuariosAdapter(Usuario usuarioInterfaz,ArrayList<Usuario> listaUsuarios){
        this.listaUsuarios=listaUsuarios;
        this.usuarioInterfaz=usuarioInterfaz;
    }

    @NonNull
    @Override
    public  UsuarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_usuario,parent,false);
        RecyclerView.LayoutParams lp=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);

        return new UsuarioViewHolder(usuarioInterfaz,view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull UsuarioViewHolder holder, int position){
        holder.tv_nombre.setText(listaUsuarios.get(position).getNombres()+" "+listaUsuarios.get(position).getApellidos());
        holder.tv_telefono.setText(listaUsuarios.get(position).getCelular());
    }

    @Override
    public int getItemCount(){
        return listaUsuarios.size();
    }

    public class UsuarioViewHolder extends RecyclerView.ViewHolder{
        TextView tv_nombre,tv_telefono;

        public UsuarioViewHolder(Usuario userInterfaz,@NonNull View itemView){
            super(itemView);
            tv_nombre=itemView.findViewById(R.id.tv_nombre_usuario);
            tv_telefono=itemView.findViewById(R.id.tv_telefono_usuario);

            itemView.setOnClickListener(view -> {
                Context contexto = view.getContext();
                Intent intento = new Intent(contexto, VerInfoAlumno.class);
                intento.putExtra("usuario",userInterfaz);
                intento.putExtra("ID", listaUsuarios.get(getAdapterPosition()).getId());
                contexto.startActivity(intento);
            });
        }
    }
}
