package mrcapybara.view.JFrame;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import net.miginfocom.swing.MigLayout;

/**
 *
 * @author Marcos Santos
 */
public class FrameLanguage extends JFrame {

    protected final JButton btnOk = new JButton("OK");
    protected final JComboBox<String> cbLanguages = new JComboBox<>();

    public FrameLanguage() {
        this.setLayout(new MigLayout());
        this.setTitle("Basic Tile Editor");
        
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(300, 140);
        this.setLocationRelativeTo(null);
        
        this.add(new JLabel("Select your language:"), "push, grow, wrap");
        this.add(cbLanguages, "pushx, growx, wrap");
        this.add(btnOk, "pushx, growx");
    }
}
