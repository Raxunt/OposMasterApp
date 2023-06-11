package com.damdos.oposmasterapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.damdos.oposmasterapp.model.PomodoroTimer;
import com.damdos.oposmasterapp.model.RecyclerViewConfiguracion;
import com.google.firebase.auth.FirebaseAuth;

public class PomodoroActivity extends AppCompatActivity implements PomodoroTimer.PomodoroListener {

    private TextView textTimer;
    private Button buttonStart;
    private Button buttonStop;
    private Button buttonReset;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private PomodoroTimer pomodoroTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pomodoro);
        this.setTitle("Crono");
        firebaseAuth = FirebaseAuth.getInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textTimer = findViewById(R.id.text_timer);
        buttonStart = findViewById(R.id.button_start);
        buttonStop = findViewById(R.id.button_stop);
        buttonReset = findViewById(R.id.button_reset);
        progressBar = findViewById(R.id.progress_bar);

        pomodoroTimer = new PomodoroTimer();
        pomodoroTimer.setListener(this);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pomodoroTimer.startTimer();
                buttonStart.setEnabled(false);
                buttonStop.setEnabled(true);
                buttonReset.setEnabled(false);
            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pomodoroTimer.stopTimer();
                buttonStart.setEnabled(true);
                buttonStop.setEnabled(false);
                buttonReset.setEnabled(true);
            }
        });

        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pomodoroTimer.resetTimer();
                buttonStart.setEnabled(true);
                buttonStop.setEnabled(false);
                buttonReset.setEnabled(false);
            }
        });
    }

    @Override
    public void onTimerTick(final String timeRemaining) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textTimer.setText(timeRemaining);
            }
        });
    }

    @Override
    public void onTimerFinish() {
        textTimer.setText("Inicie el descanso");
    }

    @Override
    public void onTimerFinished(final boolean isBreak, final long nextDuration) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isBreak) {
                    Toast.makeText(PomodoroActivity.this, "Estas en un descanso", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PomodoroActivity.this, "Al trabajo", Toast.LENGTH_SHORT).show();
                }
                textTimer.setText(formatTime(nextDuration));
            }
        });
    }

    @Override
    public void onTimerStopped() {

    }

    @Override
    public void onTimerReset(final long initialDuration) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textTimer.setText(formatTime(initialDuration));
                progressBar.setProgress(0);
            }
        });
    }

    @Override
    public void onTimerProgress(final int progress) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setProgress(progress);
            }
        });
    }

    private String formatTime(long millis) {
        int minutes = (int) (millis / 60000);
        int seconds = (int) ((millis % 60000) / 1000);
        return String.format("%02d:%02d", minutes, seconds);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_menu_app, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@androidx.annotation.NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.cronoMenu:
            case R.id.crono_menu:
                startActivity(new Intent(this, PomodoroActivity.class));
                return true;
            case R.id.calendario_menu:
            case R.id.calendarioMenu:
                startActivity(new Intent(this, Calendario.class));
                return true;
            case R.id.prueba:
            case R.id.pruebasMenu:
                startActivity(new Intent(this, Prueba.class));
                return true;
            case R.id.cerrar_sesion:
                firebaseAuth.signOut();
                invalidateOptionsMenu();
                RecyclerViewConfiguracion.cerrarSersion();

                startActivity(new Intent(this, MainActivity.class));
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return false;
    }
}
