package mrcapybara.control.JFrame;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import mrcapybara.model.Constants;
import mrcapybara.view.JFrame.FrameLanguage;
import org.json.JSONObject;

/**
 *
 * @author Marcos Santos
 */
public final class CtrlJFLanguage extends FrameLanguage {

    public CtrlJFLanguage() {
        super();

        // Carregar os idiomas
        for (String values : Constants.LANGUAGES.keySet())
            cbLanguages.addItem(values);;

        btnOk.addActionListener((e) -> {
            String file = cbLanguages.getSelectedItem().toString();

            if (file != null && !file.isBlank() && !file.isEmpty()) {
                loadTexts(Constants.LANGUAGES.get(file));
                dispose(); 
            }
        });
    }

    private void loadTexts(String languageJson) {
        try (InputStream stream = getClass().getResourceAsStream(languageJson)) {
            String contents = new String(stream.readAllBytes(), Charset.forName("UTF-8"));

            final JSONObject json = new JSONObject(contents);
            for (Object key : json.names())
                Constants.TEXT.put(key.toString(), json.getString(key.toString()));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
