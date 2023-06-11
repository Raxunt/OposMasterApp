package com.damdos.oposmasterapp.model;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.damdos.oposmasterapp.MainActivity;
import com.damdos.oposmasterapp.ModificarTema;
import com.damdos.oposmasterapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class RecyclerViewConfiguracion {
    FirebaseAuth firebaseAuth;
    private static FirebaseUser usuario;
    private Context context;
    private TemasAdapter temasAdapter;

    /**
     * Método para inflar el RecyclerView.
     * @param recyclerView recoge el contenedor para alojar los datos de cada tema.
     * @param context recoge el contexto alctual permitiendo la actualización continua al modificarse los datos.
     * @param listaTemas recoge la lista de contactos.
     * @param listaClaves recoge las claves de cada tema.
     */
    public void setConfig(RecyclerView recyclerView, Context context, List<Tema> listaTemas, List<String> listaClaves){
        firebaseAuth = FirebaseAuth.getInstance();
        usuario = firebaseAuth.getCurrentUser();
        this.context = context;
        this.temasAdapter= new TemasAdapter(listaTemas, listaClaves);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(temasAdapter);
    }


    class TemaItemView extends RecyclerView.ViewHolder{
        private TextView nombre, nota, evaluacion, repasos, numero, repasos_label, evaluacion_label;
        private String clave;

        /**
         * Carga los datos del tema en el ItemView.
         * @param parent
         */
        public TemaItemView(ViewGroup parent){
            super(LayoutInflater.from(context).inflate(R.layout.activity_vista_contacto, parent, false));
            numero = itemView.findViewById(R.id.numero_tema);
            nombre = itemView.findViewById(R.id.nombre_tema);
            repasos = itemView.findViewById(R.id.repasos_tema);
            evaluacion = itemView.findViewById(R.id.evaluacion_tema);
            nota = itemView.findViewById(R.id.nota_tema);
            repasos_label = itemView.findViewById(R.id.repasos_label);
            evaluacion_label = itemView.findViewById(R.id.evaluacion_label);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(usuario!=null){
                        Intent intent = new Intent(context, ModificarTema.class);
                        intent.putExtra("clave", clave);
                        intent.putExtra("nombre", nombre.getText().toString());


                        intent.putExtra("numero", numero.getText().toString());
                        intent.putExtra("repasos", repasos.getText().toString());
                        intent.putExtra("evaluacion", evaluacion.getText().toString());
                        intent.putExtra("nota", nota.getText().toString());

                        context.startActivity(intent);
                    }else{
                        context.startActivity(new Intent(context, MainActivity.class));
                    }
                }
            });
        }

        /**
         * Enlace de clave y contacto.
         * @param tema recoge a la clase tema.
         * @param clave recoge un string que será la clave de cada tema.
         */
        public void union(@NonNull Tema tema, String clave){
            nombre.setText(tema.getNombre());
            numero.setText(String.valueOf(tema.getNumero()));
            repasos.setText(String.valueOf(tema.getRepasos()));
            evaluacion.setText(tema.getEvaluacion());
            nota.setText(tema.getNota());


            if (tema.getEvaluacion().equals("Pendiente")) {
                itemView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorPendiente)));
                nombre.setTextColor(ContextCompat.getColor(context, R.color.colorPendiente_texto));
                repasos.setTextColor(ContextCompat.getColor(context, R.color.colorPendiente_texto));
                evaluacion.setTextColor(ContextCompat.getColor(context, R.color.colorPendiente_texto));
                nota.setTextColor(ContextCompat.getColor(context, R.color.colorPendiente_texto));
                repasos_label.setTextColor(ContextCompat.getColor(context, R.color.colorPendiente_texto));
                evaluacion_label.setTextColor(ContextCompat.getColor(context, R.color.colorPendiente_texto));
            } else if (tema.getEvaluacion().equals("Excelente")) {
                itemView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorExcelente)));
                nombre.setTextColor(ContextCompat.getColor(context, R.color.colorExcelente_texto));
                repasos.setTextColor(ContextCompat.getColor(context, R.color.colorExcelente_texto));
                evaluacion.setTextColor(ContextCompat.getColor(context, R.color.colorExcelente_texto));
                nota.setTextColor(ContextCompat.getColor(context, R.color.colorExcelente_texto));
                repasos_label.setTextColor(ContextCompat.getColor(context, R.color.colorExcelente_texto));
                evaluacion_label.setTextColor(ContextCompat.getColor(context, R.color.colorExcelente_texto));
            } else if (tema.getEvaluacion().equals("Bien")) {
                itemView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorMuyBien)));
                nombre.setTextColor(ContextCompat.getColor(context, R.color.colorMuyBien_texto));
                repasos.setTextColor(ContextCompat.getColor(context, R.color.colorMuyBien_texto));
                evaluacion.setTextColor(ContextCompat.getColor(context, R.color.colorMuyBien_texto));
                nota.setTextColor(ContextCompat.getColor(context, R.color.colorMuyBien_texto));
                repasos_label.setTextColor(ContextCompat.getColor(context, R.color.colorMuyBien_texto));
                evaluacion_label.setTextColor(ContextCompat.getColor(context, R.color.colorMuyBien_texto));
            } else if (tema.getEvaluacion().equals("Normal")) {
                itemView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorNormal)));
                nombre.setTextColor(ContextCompat.getColor(context, R.color.colorNormal_texto));
                repasos.setTextColor(ContextCompat.getColor(context, R.color.colorNormal_texto));
                evaluacion.setTextColor(ContextCompat.getColor(context, R.color.colorNormal_texto));
                nota.setTextColor(ContextCompat.getColor(context, R.color.colorNormal_texto));
                repasos_label.setTextColor(ContextCompat.getColor(context, R.color.colorNormal_texto));
                evaluacion_label.setTextColor(ContextCompat.getColor(context, R.color.colorNormal_texto));
            } else if (tema.getEvaluacion().equals("Regular")) {
                itemView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorRegular)));
                nombre.setTextColor(ContextCompat.getColor(context, R.color.colorRegular_texto));
                repasos.setTextColor(ContextCompat.getColor(context, R.color.colorRegular_texto));
                evaluacion.setTextColor(ContextCompat.getColor(context, R.color.colorRegular_texto));
                nota.setTextColor(ContextCompat.getColor(context, R.color.colorRegular_texto));
                repasos_label.setTextColor(ContextCompat.getColor(context, R.color.colorRegular_texto));
                evaluacion_label.setTextColor(ContextCompat.getColor(context, R.color.colorRegular_texto));
            } else if (tema.getEvaluacion().equals("Repasar")) {
                itemView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorRepasar)));
                nombre.setTextColor(ContextCompat.getColor(context, R.color.colorRepasar_texto));
                repasos.setTextColor(ContextCompat.getColor(context, R.color.colorRepasar_texto));
                evaluacion.setTextColor(ContextCompat.getColor(context, R.color.colorRepasar_texto));
                nota.setTextColor(ContextCompat.getColor(context, R.color.colorRepasar_texto));
                repasos_label.setTextColor(ContextCompat.getColor(context, R.color.colorRepasar_texto));
                evaluacion_label.setTextColor(ContextCompat.getColor(context, R.color.colorRepasar_texto));
            }
            this.clave = clave;
        }
    }


    class TemasAdapter extends RecyclerView.Adapter<TemaItemView>{
    private List<Tema> listaTemas;
    private List<String> listaClaves;


        public TemasAdapter(List<Tema> listaTemas, List<String> listaClaves) {
            this.listaTemas = listaTemas;
            this.listaClaves = listaClaves;
        }

        @NonNull
        @Override
        public TemaItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new TemaItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull TemaItemView holder, int position) {
        holder.union(listaTemas.get(position), listaClaves.get(position));
        }

        @Override
        public int getItemCount() {
            return listaTemas.size();
        }
    }


    public static void cerrarSersion(){
        usuario=null;
    }
}
