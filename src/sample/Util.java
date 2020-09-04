package sample;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Util {

    public static String currentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }


    public static List<String> requestTypes() {
        return Arrays.asList(
                "Toy identification details",
                "Toy information",
                "Toy manufacturer details",
                "All toy information",
                "Thank you message"
        );
    }
}
