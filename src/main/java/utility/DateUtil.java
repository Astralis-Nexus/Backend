package utility;

import lombok.Getter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    @Getter
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    @Getter
    private static String timestamp = dateFormat.format(new Date());
}
