package io.edgeperformance.edge.utils;

public interface AppUtils {

    static String convertMillisToTime(long millisUntilFinished) {
        long seconds = millisUntilFinished / 1000;
        long s = seconds % 60;
        long m = (seconds / 60) % 60;
        long h = (seconds / (60 * 60)) % 24;
        if (h != 0) {
            return String.format("%02d:%02d:%02d", h, m, s);
        } else if (m != 0) {
            return String.format("%02d:%02d", m, s);
        }
        return String.format("%02ds", s);
    }
}
