package com.damdos.oposmasterapp;

import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.damdos.oposmasterapp.model.Tema;

import java.util.List;

public class TemasDialogFragment extends DialogFragment {

    private List<Tema> temas;

    public TemasDialogFragment(List<Tema> temas) {
        this.temas = temas;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialogo_temas, null);

        TextView tituloDialog = view.findViewById(R.id.titulo_dialog_temas);
        LinearLayout contenedorTemas = view.findViewById(R.id.contenedor_temas);
        Button botonCerrarDialog = view.findViewById(R.id.boton_cerrar_dialog);

        tituloDialog.setText("Temas sorteados:");


        for (Tema tema : temas) {
            TextView textView = new TextView(getActivity());
            textView.setText("○ Tema: " + tema.getNumero() + "\n➼ " + tema.getNombre());
            contenedorTemas.addView(textView);
        }

        botonCerrarDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        int dialogStyle = isDarkMode() ? R.style.DialogThemeModoOscuro : R.style.DialogThemeModoClaro;

        Dialog dialog = new Dialog(getActivity(), dialogStyle);
        dialog.setContentView(view);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        return dialog;
    }
    private boolean isDarkMode() {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return currentNightMode == Configuration.UI_MODE_NIGHT_YES;
    }
}

