package mrcapybara.control;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;
import mrcapybara.control.JFrame.CtrlJFLanguage;
import mrcapybara.control.JFrame.CtrlJFMain;

/**
 *
 * @author Marcos Santos
 */
public class Main {

    public static void main(String[] args) throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeLater(() -> {
            final CtrlJFLanguage sLanguage = new CtrlJFLanguage();
            sLanguage.setVisible(true);
            sLanguage.addWindowListener(new WindowAdapter() {
                @Override
                public void windowDeactivated(WindowEvent e) {
                    super.windowDeactivated(e);
                    new CtrlJFMain().setVisible(true);
                }
            });
        });
    }
}
