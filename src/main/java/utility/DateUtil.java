package utility;

import java.text.SimpleDateFormat;

public class DateUtil {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    public static SimpleDateFormat getDateFormat() {
        return dateFormat;
    }
}
