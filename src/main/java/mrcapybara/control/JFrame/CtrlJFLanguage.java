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
        searchLanguages();

        btnOk.addActionListener((e) -> {
            String file = cbLanguages.getSelectedItem().toString();

            if (file != null && !file.isBlank() && !file.isEmpty()) {
                loadLanguage(file);
                dispose();
            }
        });
    }

    private void searchLanguages() {
        try {
            Stream<Path> walk = Files.walk(Paths.get(getClass().getResource("/languages").toURI()), 1);

            for (Iterator<Path> it = walk.iterator(); it.hasNext();) {
                String file = it.next().getFileName().toString();

                if (file.endsWith(Constants.LANGUAGE_EXTESION))
                    cbLanguages.addItem(file.replaceAll(Constants.LANGUAGE_EXTESION, ""));
            }
        } catch (URISyntaxException | IOException ex) {
            Logger.getLogger(CtrlJFLanguage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadLanguage(String language) {
        try (InputStream stream = getClass().getResourceAsStream("/languages/" + language + Constants.LANGUAGE_EXTESION)) {
            String contents = new String(stream.readAllBytes(), Charset.forName("UTF-8"));

            final JSONObject json = new JSONObject(contents);
            for (Object key : json.names())
                Constants.TEXT.put(key.toString(), json.getString(key.toString()));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
