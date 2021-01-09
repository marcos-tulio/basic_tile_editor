package mrcapybara.model;

import java.util.HashMap;

/**
 *
 * @author Marcos Santos
 */
public final class Constants {

    public static final HashMap<String, String> TEXT = new HashMap<>();

    public static final HashMap<String, String> LANGUAGES = new HashMap<>() {
        {
            put("English", "/languages/en-us.json");
            put("Português Brasileiro", "/languages/pt-br.json");
        }
    };
}
