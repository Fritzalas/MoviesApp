package gr.android.moviesapp.common;

public class TimeConversion {
    public static String convertMinutesToTime(int minutes) {
        if (minutes < 0) {
            return null;
        }

        int hours = minutes / 60;
        int remainingMinutes = minutes % 60;

        if (hours > 0 && remainingMinutes > 0) {
            return hours + " hour" + (hours > 1 ? "s" : "") + " " + remainingMinutes + " minute" + (remainingMinutes > 1 ? "s" : "");
        } else if (hours > 0) {
            return hours + " hour" + (hours > 1 ? "s" : "");
        } else {
            return remainingMinutes + " minute" + (remainingMinutes > 1 ? "s" : "");
        }
    }
}
