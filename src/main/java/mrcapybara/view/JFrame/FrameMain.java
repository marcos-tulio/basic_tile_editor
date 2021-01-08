package mrcapybara.view.JFrame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import mrcapybara.model.Constants;
import net.miginfocom.swing.MigLayout;
import mrcapybara.model.TableModel.TableModelCellRender;
import mrcapybara.model.TableModel.TableModelStd;

/**
 *
 * @author Marcos Santos
 */
public class FrameMain extends JFrame {

    protected final Color COLOR = new Color(225, 225, 225);
    protected final Color COLOR_HOVER = new Color(173, 173, 173);
    protected final EtchedBorder DEFAULT = new EtchedBorder();

    protected int blockSize = 22, columnBlocks = 16, rowBlocks = 16, offset = 0;

    protected JMenuItem itemAbout, itemOpen, itemSave, itemQuit, itemUndo, itemNext, itemAdd, itemSub, itemPre,
            itemSizeA, itemSizeM, itemColA, itemColM, itemRowA, itemRowM;
    protected JTextField txtOffset, txtSize;

    protected JSpinner spinnerSize, spinnerColum, spinnerRow;
    protected JCheckBox boxTxts, boxEdit, boxSetPal;
    protected JLabel btnPlus, btnMinus, btnNext, btnPre, btnLoadP, btnSaveP, btnNewP, lblColor1, lblColor2;

    protected JSlider slider;

    protected JScrollPane scrollWork;
    protected JTable tableWork, tablePalette;
    protected TableModelStd tmWork;
    protected TableModelCellRender cellRender;

    public FrameMain() {
        cellRender = new TableModelCellRender(new ArrayList<>());
        initComponents();
    }

    private void initComponents() {
        this.setLayout(new MigLayout());
        this.setTitle(Constants.TEXT.get("APP_TITLE"));
        this.setSize(800, 700);
        this.setMinimumSize(new Dimension(200, 200));
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setJMenuBar(menu());
        this.add(panelLeft(), "growy, pushy, width 1%");
        this.add(panelWorkSpace(), "grow, push, width 99%, wrap");
        this.add(panelEdit(), "growx, pushx, height 200px");
        this.add(panelPalette(), "growx, pushx, height 200px, hmin 200px");
    }

    private JMenuBar menu() {
        // File menu items
        itemQuit = new JMenuItem(Constants.TEXT.get("MENU_FILE_QUIT"));
        itemOpen = customItem(Constants.TEXT.get("MENU_FILE_OPEN"), KeyEvent.VK_O, ActionEvent.CTRL_MASK);
        itemSave = customItem(Constants.TEXT.get("MENU_FILE_SAVE"), KeyEvent.VK_S, ActionEvent.CTRL_MASK);
        itemSave.setEnabled(false);

        // File menu
        final JMenu file = new JMenu(Constants.TEXT.get("MENU_FILE"));
        file.add(itemOpen);
        file.add(itemSave);
        file.addSeparator();
        file.add(itemQuit);

        // Help menu items
        itemAbout = new JMenuItem(Constants.TEXT.get("MENU_HELP_ABOUT"));

        // Help menu
        final JMenu help = new JMenu(Constants.TEXT.get("MENU_HELP"));
        help.add(itemAbout);

        // Edit menu items
        itemAdd = customItem(Constants.TEXT.get("MENU_EDIT_NEXT_BYTE"), KeyEvent.VK_ADD, 0);
        itemSub = customItem(Constants.TEXT.get("MENU_EDIT_PREV_BYTE"), KeyEvent.VK_SUBTRACT, 0);
        itemNext = customItem(Constants.TEXT.get("MENU_EDIT_NEXT_ROW"), KeyEvent.VK_ADD, ActionEvent.CTRL_MASK);
        itemPre = customItem(Constants.TEXT.get("MENU_EDIT_PREV_ROW"), KeyEvent.VK_SUBTRACT, ActionEvent.CTRL_MASK);
        itemSizeA = customItem(Constants.TEXT.get("MENU_EDIT_INC_CELL"), KeyEvent.VK_ADD, ActionEvent.ALT_MASK);
        itemSizeM = customItem(Constants.TEXT.get("MENU_EDIT_DEC_CELL"), KeyEvent.VK_SUBTRACT, ActionEvent.ALT_MASK);
        itemColA = customItem(Constants.TEXT.get("MENU_EDIT_ADD_COL"), KeyEvent.VK_RIGHT, ActionEvent.ALT_MASK);
        itemColM = customItem(Constants.TEXT.get("MENU_EDIT_REM_COL"), KeyEvent.VK_LEFT, ActionEvent.ALT_MASK);
        itemRowA = customItem(Constants.TEXT.get("MENU_EDIT_ADD_ROW"), KeyEvent.VK_DOWN, ActionEvent.ALT_MASK);
        itemRowM = customItem(Constants.TEXT.get("MENU_EDIT_REM_ROW"), KeyEvent.VK_UP, ActionEvent.ALT_MASK);
        itemUndo = customItem(Constants.TEXT.get("MENU_EDIT_UNDO"), KeyEvent.VK_Z, ActionEvent.CTRL_MASK);
        itemUndo.setEnabled(false);

        // Edit menu
        final JMenu edit = new JMenu(Constants.TEXT.get("MENU_EDIT"));
        edit.add(itemUndo);
        edit.addSeparator();
        edit.add(itemAdd);
        edit.add(itemSub);
        edit.addSeparator();
        edit.add(itemNext);
        edit.add(itemPre);
        edit.addSeparator();
        edit.add(itemSizeA);
        edit.add(itemSizeM);
        edit.addSeparator();
        edit.add(itemColA);
        edit.add(itemColM);
        edit.addSeparator();
        edit.add(itemRowA);
        edit.add(itemRowM);

        // Menu bar
        final JMenuBar menuBar = new JMenuBar();
        menuBar.add(file);
        menuBar.add(edit);
        menuBar.add(help);

        return menuBar;
    }

    private JMenuItem customItem(String txt, int key, int modify) {
        JMenuItem item = new JMenuItem(txt);
        item.setAccelerator(KeyStroke.getKeyStroke(key, modify));
        return item;
    }

    private JPanel panelLeft() {
        JPanel panel = new JPanel(new MigLayout("fill, ins 0"));
        panel.add(panelOffset(), "growx, pushx, top, split, wrap");
        panel.add(panelConfig(), "growx, push, top, wrap");

        return panel;
    }

    private JPanel panelConfig() {
        JPanel panel = new JPanel(new MigLayout());
        panel.setBorder(new TitledBorder(DEFAULT, Constants.TEXT.get("PANEL_TABLE_TITLE"), TitledBorder.LEFT, TitledBorder.CENTER));

        boxTxts = new JCheckBox(Constants.TEXT.get("PANEL_TABLE_SHOW_TXT"));
        boxTxts.setFocusable(false);

        spinnerSize = new JSpinner(new SpinnerNumberModel(blockSize, 5, 30, 1));
        spinnerColum = new JSpinner(new SpinnerNumberModel(columnBlocks, 1, 50, 1));
        spinnerRow = new JSpinner(new SpinnerNumberModel(rowBlocks, 1, 100, 1));

        panel.add(new JLabel(Constants.TEXT.get("PANEL_TABLE_SIZE")), "cell 0 1");
        panel.add(spinnerSize, "cell 1 1, growx, pushx");
        panel.add(new JLabel(Constants.TEXT.get("PANEL_TABLE_COLS")), "cell 0 2");
        panel.add(spinnerColum, "cell 1 2, growx, pushx");
        panel.add(new JLabel(Constants.TEXT.get("PANEL_TABLE_ROWS")), "cell 0 3");
        panel.add(spinnerRow, "cell 1 3, growx, pushx");
        panel.add(boxTxts, "cell 0 4, span 2 2");

        return panel;
    }

    private JPanel panelOffset() {
        JPanel panel = new JPanel(new MigLayout());
        panel.setBorder(new TitledBorder(DEFAULT, Constants.TEXT.get("PANEL_FILE_TITLE"), TitledBorder.LEFT, TitledBorder.CENTER));

        Font font = new Font("Arial", 1, 17);

        txtOffset = new JTextField();
        txtOffset.setEditable(false);

        txtSize = new JTextField();
        txtSize.setEditable(false);

        btnPlus = labelToBtn("+");
        btnPlus.setEnabled(false);
        btnPlus.setFont(font);

        btnMinus = labelToBtn("- ");
        btnMinus.setEnabled(false);
        btnMinus.setFont(font);

        btnNext = labelToBtn(">>");
        btnNext.setEnabled(false);
        btnNext.setFont(font);

        btnPre = labelToBtn("<<");
        btnPre.setEnabled(false);
        btnPre.setFont(font);

        panel.add(new JLabel(Constants.TEXT.get("PANEL_FILE_SIZE")), "cell 0 0");
        panel.add(txtSize, "cell 1 0, growx, pushx");
        panel.add(new JLabel(Constants.TEXT.get("PANEL_FILE_OFFSET")), "cell 0 1");
        panel.add(txtOffset, "cell 1 1, growx, pushx, wrap");
        panel.add(btnMinus, "split, spanx 2, growx");
        panel.add(btnPlus, "wrap, growx");
        panel.add(btnPre, "split, spanx 2, growx");
        panel.add(btnNext, "growx");

        return panel;
    }

    private JPanel panelPalette() {
        JPanel panel = new JPanel(new MigLayout());
        panel.setBorder(new TitledBorder(DEFAULT, Constants.TEXT.get("PANEL_PALETTE_TITLE"), TitledBorder.LEFT, TitledBorder.CENTER));

        final ArrayList<String> header = new ArrayList<>();
        final ArrayList<Class> classes = new ArrayList<>();

        for (char i = 0; i < 16; i++) {
            header.add("");
            classes.add(String.class);
        }

        TableModelStd tmPalette = new TableModelStd(header, classes);

        tablePalette = new JTable(tmPalette) {
            @Override
            public boolean getScrollableTracksViewportWidth() {
                return false;
            }
        };
        tablePalette.setName("palette");
        tablePalette.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        for (char i = 0; i < tablePalette.getColumnCount(); i++) {
            tablePalette.getColumnModel().getColumn(i).setResizable(false);
            tablePalette.getColumnModel().getColumn(i).setMinWidth(25);
            tablePalette.getColumnModel().getColumn(i).setMaxWidth(25);
        }

        tablePalette.getTableHeader().setVisible(false);
        tablePalette.getTableHeader().setPreferredSize(new Dimension(0, 0));
        tablePalette.setDefaultRenderer(String.class, cellRender);
        tablePalette.getTableHeader().setReorderingAllowed(false);
        tablePalette.setRowHeight(20);
        tablePalette.setRowSelectionAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tablePalette);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        ArrayList<String> rows = new ArrayList<>();
        for (char i = 0; i < tmPalette.getColumnCount(); i++) {
            for (char j = 0; j < 16; j++) {
                rows.add((Integer.toHexString(i) + Integer.toHexString(j)).toUpperCase().replaceAll(" ", ""));
            }
            tmPalette.addRow(rows);
            rows.clear();
        }

        btnNewP = labelToBtn(Constants.TEXT.get("PANEL_PALETTE_NEW"));
        btnSaveP = labelToBtn(Constants.TEXT.get("PANEL_PALETTE_SAVE"));
        btnLoadP = labelToBtn(Constants.TEXT.get("PANEL_PALETTE_LOAD"));

        panel.add(btnNewP, "wmin 100px, hmin 25px, cell 1 0, pushy");
        panel.add(btnSaveP, "wmin 100px, hmin 25px, cell 1 1, pushy");
        panel.add(btnLoadP, "wmin 100px, hmin 25px, cell 1 2, pushy");
        panel.add(scrollPane, "width 418px, left, cell 0 0, spany 3");

        return panel;
    }

    private JPanel panelWorkSpace() {
        final JPanel panel = new JPanel(new MigLayout());
        panel.setBorder(DEFAULT);

        slider = new JSlider(JSlider.VERTICAL);
        slider.setInverted(true);
        slider.setValue(0);
        slider.setEnabled(false);

        tmWork = new TableModelStd(new ArrayList(), new ArrayList());
        tableWork = new JTable(tmWork) {
            @Override
            public boolean getScrollableTracksViewportWidth() {
                return false;
            }
        };
        tableWork.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        tableWork.setDefaultRenderer(String.class, cellRender);
        tableWork.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tableWork.getTableHeader().setReorderingAllowed(false);
        tableWork.setName("rom");

        scrollWork = new JScrollPane(tableWork);
        scrollWork.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollWork.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        panel.add(slider, "cell 0 0, pushy, growy");
        panel.add(scrollWork, "cell 1 0, top");
        panel.setSize(420, 445);

        return panel;
    }

    private JPanel panelEdit() {
        final JPanel panel = new JPanel(new MigLayout());
        panel.setBorder(new TitledBorder(DEFAULT, Constants.TEXT.get("PANEL_EDIT_TITLE"), TitledBorder.LEFT, TitledBorder.CENTER));

        boxEdit = new JCheckBox(Constants.TEXT.get("PANEL_EDIT_MODE"));
        boxEdit.setFocusable(false);
        boxEdit.setToolTipText(Constants.TEXT.get("PANEL_EDIT_MODE_TOOLTIP"));

        boxSetPal = new JCheckBox(Constants.TEXT.get("PANEL_EDIT_PALETTE"));
        boxSetPal.setFocusable(false);

        lblColor1 = new JLabel("00", JLabel.CENTER);
        lblColor1.setOpaque(true);
        lblColor1.setBackground(Color.BLACK);
        lblColor1.setForeground(Color.WHITE);

        lblColor2 = new JLabel("00", JLabel.CENTER);
        lblColor2.setOpaque(true);
        lblColor2.setBackground(Color.BLACK);
        lblColor2.setForeground(Color.WHITE);

        panel.add(boxEdit, "spanx 2, center, wrap");
        panel.add(boxSetPal, "spanx 2, center, wrap");
        panel.add(new JLabel(Constants.TEXT.get("PANEL_EDIT_LEFT_BUTTON")), "wmin 25px, hmin 25px");
        panel.add(lblColor1, "wmin 25px, hmin 25px, wrap");
        panel.add(new JLabel(Constants.TEXT.get("PANEL_EDIT_RIGHT_BUTTON")), "wmin 25px, hmin 25px");
        panel.add(lblColor2, "wmin 25px, hmin 25px");

        return panel;
    }

    private JLabel labelToBtn(String txt) {
        JLabel label = new JLabel(txt, JLabel.CENTER);

        label.setFocusable(false);
        label.setOpaque(true);
        label.setBackground(COLOR);
        label.setBorder(DEFAULT);

        return label;
    }
}
