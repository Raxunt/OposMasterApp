package com.damdos.oposmasterapp.model;

import java.util.Timer;
import java.util.TimerTask;

public class PomodoroTimer {
    private static final long MILLIS_IN_MINUTE = 60 * 1000;
    private static final long POMODORO_DURATION = 25 * MILLIS_IN_MINUTE;
    private static final long SHORT_BREAK_DURATION = 5 * MILLIS_IN_MINUTE;
    private static final long LONG_BREAK_DURATION = 15 * MILLIS_IN_MINUTE;
    private static final long LONG_BREAK_INTERVAL = 4;

    private long currentDuration;
    private Timer timer;
    private PomodoroListener listener;
    private int pomodoroCount;
    private boolean isBreak;

    public PomodoroTimer() {
        currentDuration = POMODORO_DURATION;
        pomodoroCount = 0;
        isBreak = false;
    }

    public void setListener(PomodoroListener listener) {
        this.listener = listener;
    }

    public void startTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                currentDuration -= 1000;
                if (listener != null) {
                    listener.onTimerTick(formatTime(currentDuration));
                    listener.onTimerProgress(calculateProgress(currentDuration));
                }

                if (currentDuration <= 0) {
                    timer.cancel();
                    if (isBreak) {
                        pomodoroCount++;
                        if (pomodoroCount % LONG_BREAK_INTERVAL == 0) {
                            currentDuration = LONG_BREAK_DURATION;
                        } else {
                            currentDuration = SHORT_BREAK_DURATION;
                        }
                        isBreak = true;
                    } else {
                        currentDuration = POMODORO_DURATION;
                        isBreak = false;
                    }
                    if (listener != null) {
                        listener.onTimerFinish();
                        listener.onTimerFinished(isBreak, currentDuration);
                    }
                }
            }
        }, 0, 1000);
    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
        if (listener != null) {
            listener.onTimerStopped();
        }
    }

    public void resetTimer() {
        if (timer != null) {
            timer.cancel();
        }
        currentDuration = POMODORO_DURATION;
        pomodoroCount = 0;
        isBreak = false;
        if (listener != null) {
            listener.onTimerReset(currentDuration);
        }
    }

    private String formatTime(long millis) {
        int minutes = (int) (millis / MILLIS_IN_MINUTE);
        int seconds = (int) ((millis % MILLIS_IN_MINUTE) / 1000);
        return String.format("%02d:%02d", minutes, seconds);
    }

    private int calculateProgress(long duration) {
        return (int) (((double) duration / POMODORO_DURATION) * 100);
    }

    public interface PomodoroListener {
        void onTimerTick(String timeRemaining);

        void onTimerFinish();

        void onTimerFinished(boolean isBreak, long nextDuration);

        void onTimerStopped();

        void onTimerReset(long initialDuration);

        void onTimerProgress(int progress);
    }
}