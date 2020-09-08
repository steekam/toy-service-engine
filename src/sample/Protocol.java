package sample;

import sample.models.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Protocol {

    public static final int REQUEST_TOY_ID = 10;
    public static final int REQUEST_TOY_INFO = 11;
    public static final int REQUEST_TOY_MANUFACTURER = 12;
    public static final int REQUEST_TOY_ALL = 13;
    public static final int REQUEST_THANK_YOU = 14;

    public final HashMap<String, Integer> requestTypesMap = new HashMap<>(
            Map.ofEntries(
                    Map.entry("Toy identification details", REQUEST_TOY_ID),
                    Map.entry("Toy information", REQUEST_TOY_INFO),
                    Map.entry("Toy manufacturer details", REQUEST_TOY_MANUFACTURER),
                    Map.entry("All toy information", REQUEST_TOY_ALL),
                    Map.entry("Thank you message", REQUEST_THANK_YOU)
            )
    );

    private final HashMap<Integer, String[]> toyRelatedRules = new HashMap<>(
            Map.ofEntries(
                    Map.entry(REQUEST_TOY_ID, new String[]{"code", "name"}),
                    Map.entry(REQUEST_TOY_INFO, new String[]{"name", "description", "price", "dateOfManufacture", "batchNumber"}),
                    Map.entry(REQUEST_TOY_MANUFACTURER, new String[]{"manufacturer"}),
                    Map.entry(REQUEST_TOY_ALL, new String[]{"all"})
            )
    );

    public int currentRequestType = 0;

    /**
     * Returns message object to be sent to the client
     *
     * @param inputObject Object received from client [Message | Toy]
     * @param callback A Consumer function that takes a string as parameter
     * @return Message
     */
    public Message processInput(Object inputObject, Consumer<String> callback) {
        callback.accept(inputObject.toString());

        return new Message("Response received");
    }

}
