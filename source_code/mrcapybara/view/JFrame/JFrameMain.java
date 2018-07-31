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
import net.miginfocom.swing.MigLayout;
import mrcapybara.model.TableModel.TableModelCellRender;
import mrcapybara.model.TableModel.TableModelStd;

/**
 *
 * @author Jakecoll
 */
public class JFrameMain extends JFrame {

    protected final Color COLOR = new Color(225, 225, 225);
    protected final Color COLOR_HOVER = new Color(173, 173, 173);
    protected final EtchedBorder BORDER = new EtchedBorder();

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

    public JFrameMain() {
        cellRender = new TableModelCellRender(new ArrayList<>());
        initComponents();
    }

    private void initComponents() {
        this.setLayout(new MigLayout());
        this.setTitle("Basic Tile Editor - By Jakecoll");
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

        itemQuit = new JMenuItem("Sair");
        itemAbout = new JMenuItem("Sobre");

        itemOpen = customItem("Carregar ROM...", KeyEvent.VK_O, ActionEvent.CTRL_MASK);
        itemSave = customItem("Salvar", KeyEvent.VK_S, ActionEvent.CTRL_MASK);
        itemAdd = customItem("Próximo Byte", KeyEvent.VK_ADD, 0);
        itemSub = customItem("Byte Anterior", KeyEvent.VK_SUBTRACT, 0);
        itemNext = customItem("Próxima Linha", KeyEvent.VK_ADD, ActionEvent.CTRL_MASK);
        itemPre = customItem("Linha Anterior", KeyEvent.VK_SUBTRACT, ActionEvent.CTRL_MASK);
        itemSizeA = customItem("Aumentar Célula", KeyEvent.VK_ADD, ActionEvent.ALT_MASK);
        itemSizeM = customItem("Diminuir Célula", KeyEvent.VK_SUBTRACT, ActionEvent.ALT_MASK);
        itemColA = customItem("Adicionar Coluna", KeyEvent.VK_RIGHT, ActionEvent.ALT_MASK);
        itemColM = customItem("Retirar Coluna", KeyEvent.VK_LEFT, ActionEvent.ALT_MASK);
        itemRowA = customItem("Adicionar Linha", KeyEvent.VK_DOWN, ActionEvent.ALT_MASK);
        itemRowM = customItem("Retirar Linha", KeyEvent.VK_UP, ActionEvent.ALT_MASK);
        itemUndo = customItem("Desfazer", KeyEvent.VK_Z, ActionEvent.CTRL_MASK);

        itemUndo.setEnabled(false);
        itemSave.setEnabled(false);

        JMenu file = new JMenu("Arquivo");
        file.add(itemOpen);
        file.add(itemSave);
        file.addSeparator();
        file.add(itemQuit);

        JMenu help = new JMenu("Ajuda");
        help.add(itemAbout);

        JMenu edit = new JMenu("Editar");
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

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(file);
        menuBar.add(edit);
        menuBar.add(help);

        return menuBar;
    }

    private JMenuItem customItem(String txt, int key, int modify) {
        JMenuItem _item = new JMenuItem(txt);
        _item.setAccelerator(KeyStroke.getKeyStroke(key, modify));
        return _item;
    }

    private JPanel panelLeft() {
        JPanel _panel = new JPanel(new MigLayout("fill"));
        _panel.add(panelOffset(), "growx, pushx, top, split, wrap");
        _panel.add(panelConfig(), "growx, push, top, wrap");

        _panel.setBorder(BORDER);

        return _panel;
    }

    private JPanel panelConfig() {
        JPanel _panel = new JPanel(new MigLayout());
        _panel.setBorder(new TitledBorder(BORDER, "Tabela", 2, 0));

        boxTxts = new JCheckBox("Exibir textos");
        boxTxts.setFocusable(false);
        
        spinnerSize = new JSpinner(new SpinnerNumberModel(blockSize, 5, 30, 1));
        spinnerColum = new JSpinner(new SpinnerNumberModel(columnBlocks, 1, 50, 1));
        spinnerRow = new JSpinner(new SpinnerNumberModel(rowBlocks, 1, 100, 1));

        _panel.add(new JLabel("Tamanho:"), "cell 0 1");
        _panel.add(spinnerSize, "cell 1 1, growx, pushx");
        _panel.add(new JLabel("Colunas:"), "cell 0 2");
        _panel.add(spinnerColum, "cell 1 2, growx, pushx");
        _panel.add(new JLabel("Linhas:"), "cell 0 3");
        _panel.add(spinnerRow, "cell 1 3, growx, pushx");
        _panel.add(boxTxts, "cell 0 4, span 2 2");

        return _panel;
    }

    private JPanel panelOffset() {
        JPanel _panel = new JPanel(new MigLayout());
        _panel.setBorder(new TitledBorder(BORDER, "Arquivo", 2, 0));

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

        _panel.add(new JLabel("Tam.:"), "cell 0 0");
        _panel.add(txtSize, "cell 1 0, growx, pushx");
        _panel.add(new JLabel("Offset:"), "cell 0 1");
        _panel.add(txtOffset, "cell 1 1, growx, pushx, wrap");
        _panel.add(btnMinus, "split, spanx 2, growx");
        _panel.add(btnPlus, "wrap, growx");
        _panel.add(btnPre, "split, spanx 2, growx");
        _panel.add(btnNext, "growx");

        return _panel;
    }

    private JPanel panelPalette() {
        JPanel _panel = new JPanel(new MigLayout());
        _panel.setBorder(new TitledBorder(BORDER, "Paleta", 1, 0));

        ArrayList<String> _header = new ArrayList<>();
        ArrayList<Class> _classes = new ArrayList<>();

        for (char i = 0; i < 16; i++) {
            _header.add("");
            _classes.add(String.class);
        }

        TableModelStd _tmPalette = new TableModelStd(_header, _classes);

        tablePalette = new JTable(_tmPalette) {
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

        JScrollPane _scrollPane = new JScrollPane(tablePalette);
        _scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        ArrayList<String> _rows = new ArrayList<>();
        for (char i = 0; i < _tmPalette.getColumnCount(); i++) {
            for (char j = 0; j < 16; j++) {
                _rows.add((Integer.toHexString(i) + Integer.toHexString(j)).toUpperCase().replaceAll(" ", ""));
            }
            _tmPalette.addRow(_rows);
            _rows.clear();
        }

        btnNewP = labelToBtn("Nova Paleta");
        btnSaveP = labelToBtn("Salvar Paleta");
        btnLoadP = labelToBtn("Carregar Paleta");

        _panel.add(btnNewP, "wmin 100px, hmin 25px, cell 1 0, pushy");
        _panel.add(btnSaveP, "wmin 100px, hmin 25px, cell 1 1, pushy");
        _panel.add(btnLoadP, "wmin 100px, hmin 25px, cell 1 2, pushy");
        _panel.add(_scrollPane, "width 418px, left, cell 0 0, spany 3");

        return _panel;
    }

    private JPanel panelWorkSpace() {
        JPanel _panel = new JPanel(new MigLayout());
        _panel.setBorder(BORDER);

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

        _panel.add(slider, "cell 0 0, pushy, growy");
        _panel.add(scrollWork, "cell 1 0, top");
        _panel.setSize(420, 445);

        return _panel;
    }

    private JPanel panelEdit() {
        JPanel _panel = new JPanel(new MigLayout());
        _panel.setBorder(new TitledBorder(BORDER, "Edição", 1, 0));

        boxEdit = new JCheckBox("Modo Edição");
        boxEdit.setFocusable(false);
        boxEdit.setToolTipText("Ao clicar em um byte, na tabela da ROM, ele será alterado");

        boxSetPal = new JCheckBox("Editar Paleta");
        boxSetPal.setFocusable(false);

        lblColor1 = new JLabel("00", JLabel.CENTER);
        lblColor1.setOpaque(true);
        lblColor1.setBackground(Color.BLACK);
        lblColor1.setForeground(Color.WHITE);

        lblColor2 = new JLabel("00", JLabel.CENTER);
        lblColor2.setOpaque(true);
        lblColor2.setBackground(Color.BLACK);
        lblColor2.setForeground(Color.WHITE);

        _panel.add(boxEdit, "spanx 2, center, wrap");
        _panel.add(boxSetPal, "spanx 2, center, wrap");
        _panel.add(new JLabel("B. Esquerdo: "), "wmin 25px, hmin 25px");
        _panel.add(lblColor1, "wmin 25px, hmin 25px, wrap");
        _panel.add(new JLabel("B. Direito: "), "wmin 25px, hmin 25px");
        _panel.add(lblColor2, "wmin 25px, hmin 25px");

        return _panel;
    }

    private JLabel labelToBtn(String txt) {
        JLabel _label = new JLabel(txt, JLabel.CENTER);

        _label.setFocusable(false);
        _label.setOpaque(true);
        _label.setBackground(COLOR);
        _label.setBorder(BORDER);

        return _label;
    }

}
