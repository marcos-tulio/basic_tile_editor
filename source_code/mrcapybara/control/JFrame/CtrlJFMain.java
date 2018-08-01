package mrcapybara.control.JFrame;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import net.miginfocom.swing.MigLayout;
import mrcapybara.view.JFrame.JFrameMain;

/**
 *
 * @author Jakecoll
 */
public class CtrlJFMain extends JFrameMain {

    private final String EXTENSION = ".pal";
    private String local = FileSystemView.getFileSystemView().getHomeDirectory().toString(), fileName = "";
    private byte[] bytes;

    private List<Object[]> listTask;
    private int indexTask = 0;

    private boolean wasChanged = false, paletteChanged = false;

    public CtrlJFMain() {
        initComponents();
    }

    private void initComponents() {
        listTask = new ArrayList();

        this.loadTableWork(null);
        this.addListeners();

        newPalette();
    }

    private void addListeners() {
        itemQuit.addActionListener((ActionEvent e) -> {
            exit();
        });

        super.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                exit();
            }
        });

        spinnerSize.addChangeListener((ChangeEvent e) -> {
            blockSize = (int) spinnerSize.getValue();
            loadTableWork(bytes);
        });

        spinnerColum.addChangeListener((ChangeEvent e) -> {
            columnBlocks = (int) spinnerColum.getValue();
            loadTableWork(bytes);
        });

        spinnerRow.addChangeListener((ChangeEvent e) -> {
            rowBlocks = (int) spinnerRow.getValue();
            loadTableWork(bytes);
        });

        boxTxts.addChangeListener((ChangeEvent e) -> {
            cellRender.setEnableTxt(boxTxts.isSelected());
            refresh();
        });

        itemAdd.addActionListener((e) -> {
            if (btnPlus.isEnabled()) {
                keyAdd(1);
            }
        });

        itemSub.addActionListener((e) -> {
            if (btnMinus.isEnabled()) {
                keySubtra(1);
            }
        });

        itemNext.addActionListener((e) -> {
            if (btnNext.isEnabled()) {
                keyAdd(columnBlocks);
            }
        });

        itemPre.addActionListener((e) -> {
            if (btnPre.isEnabled()) {
                keySubtra(columnBlocks);
            }
        });

        itemSizeA.addActionListener((e) -> {
            if (spinnerSize.getNextValue() != null) {
                spinnerSize.setValue(spinnerSize.getNextValue());
            }
        });

        itemSizeM.addActionListener((e) -> {
            if (spinnerSize.getPreviousValue() != null) {
                spinnerSize.setValue(spinnerSize.getPreviousValue());
            }
        });

        itemColA.addActionListener((e) -> {
            if (spinnerColum.getNextValue() != null) {
                spinnerColum.setValue(spinnerColum.getNextValue());
            }
        });

        itemColM.addActionListener((e) -> {
            if (spinnerColum.getPreviousValue() != null) {
                spinnerColum.setValue(spinnerColum.getPreviousValue());
            }
        });

        itemRowA.addActionListener((e) -> {
            if (spinnerRow.getNextValue() != null) {
                spinnerRow.setValue(spinnerRow.getNextValue());
            }
        });

        itemRowM.addActionListener((e) -> {
            if (spinnerRow.getPreviousValue() != null) {
                spinnerRow.setValue(spinnerRow.getPreviousValue());
            }
        });

        itemOpen.addActionListener(openFile());
        itemSave.addActionListener((ActionEvent e) -> {
            saveFile();
        });
        itemUndo.addActionListener(undoTask());
        txtOffset.addActionListener(actionOffset());

        btnPlus.addMouseListener(btnBehavior());
        btnMinus.addMouseListener(btnBehavior());
        btnPre.addMouseListener(btnBehavior());
        btnNext.addMouseListener(btnBehavior());
        btnLoadP.addMouseListener(btnBehavior());
        btnSaveP.addMouseListener(btnBehavior());
        btnNewP.addMouseListener(btnBehavior());

        tableWork.addMouseListener(tableWorkAction());
        tablePalette.addMouseListener(tablePaletteAction());

        slider.addChangeListener((e) -> {
            int _value = slider.getValue();

            if ((_value + columnBlocks * rowBlocks) >= bytes.length) {
                _value -= columnBlocks * rowBlocks;
            }

            offset = _value;
            keyAdd(0);
        });

        itemAbout.addActionListener((e) -> {
            about();
        });
    }

    private ActionListener openFile() {
        return (ActionEvent e) -> {
            if (wasChanged) {
                if ((JOptionPane.showConfirmDialog(null, "Deseja salvar o arquivo?", "Salvar", JOptionPane.YES_NO_OPTION)
                        == JOptionPane.YES_OPTION)) {
                    saveFile();
                }
            }

            JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                try {
                    bytes = Files.readAllBytes(chooser.getSelectedFile().toPath());

                    if (bytes.length > 0) {
                        loadTableWork(bytes);

                        txtSize.setText(Integer.toHexString(bytes.length - 1).toUpperCase() + "h");

                        txtOffset.setText("0h");
                        txtOffset.setEditable(true);
                        offset = 0;

                        if (bytes.length <= columnBlocks) {
                            btnPlus.setEnabled(true);
                            btnNext.setEnabled(false);
                            btnNext.setBackground(COLOR);
                        } else {
                            btnPlus.setEnabled(true);
                            btnNext.setEnabled(true);
                        }

                        local = chooser.getSelectedFile().getAbsolutePath();
                        fileName = chooser.getSelectedFile().getName();
                        itemSave.setEnabled(true);

                        if (bytes.length > columnBlocks * rowBlocks) {
                            slider.setEnabled(true);
                            slider.setMaximum(bytes.length);
                            slider.setValue(0);
                        } else {
                            slider.setEnabled(false);
                        }
                    } else {
                        btnPlus.setEnabled(false);
                        btnNext.setEnabled(false);
                        btnPlus.setBackground(COLOR);
                        btnNext.setBackground(COLOR);
                    }
                } catch (IOException | java.lang.OutOfMemoryError ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao carregar o arquivo.", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        };
    }

    private void saveFile() {
        if (bytes.length > 0) {
            JFileChooser chooser = new JFileChooser(local) {
                @Override
                public void approveSelection() {
                    if (getSelectedFile().exists() && getDialogType() == SAVE_DIALOG) {
                        int result = JOptionPane.showConfirmDialog(this, "Substituir \"" + getSelectedFile().getName() + "\"?", "Substituir arquivo", JOptionPane.YES_NO_OPTION);
                        switch (result) {
                            case JOptionPane.YES_OPTION:
                                super.approveSelection();
                                return;
                            case JOptionPane.NO_OPTION:
                                return;
                            case JOptionPane.CLOSED_OPTION:
                                return;
                            case JOptionPane.CANCEL_OPTION:
                                cancelSelection();
                                return;
                        }
                    }
                    super.approveSelection();
                }
            };
            chooser.setDialogTitle("Salvar Arquivo");
            chooser.setSelectedFile(new File(fileName));

            if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                try {
                    FileOutputStream fos = new FileOutputStream(chooser.getSelectedFile());
                    fos.write(bytes);
                    fos.flush();
                    fos.close();

                    local = chooser.getSelectedFile().toPath().toString();
                    JOptionPane.showMessageDialog(null, "Arquivo salvo com sucesso!", "Arquivo salvo", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao salvar o arquivo!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            itemSave.setEnabled(false);
        }

    }

    private ActionListener actionOffset() {
        return (ActionEvent e) -> {
            int _offset = 0;
            try {
                _offset = Integer.parseInt(txtOffset.getText().toLowerCase().replace("h", ""), 16);
                if (_offset > bytes.length) {
                    _offset = bytes.length - 1;
                } else if (_offset == bytes.length - 1) {
                    btnPlus.setEnabled(false);
                    btnNext.setEnabled(false);
                    btnPlus.setBackground(COLOR);
                    btnNext.setBackground(COLOR);
                } else if (_offset < 0) {
                    _offset = 0;
                }
            } catch (NumberFormatException exec) {
                JOptionPane.showMessageDialog(null, "Offset inexistente!", "Erro", JOptionPane.ERROR_MESSAGE);
            }

            offset = _offset;
            loadTableWork(bytes);

            if (_offset > 0) {
                btnMinus.setEnabled(true);
                if (_offset - columnBlocks > 0) {
                    btnPre.setEnabled(true);
                } else {
                    btnPre.setEnabled(false);
                    btnPre.setBackground(COLOR);
                }

                if (_offset + columnBlocks <= bytes.length - 1) {
                    btnNext.setEnabled(true);
                }
            } else {
                btnMinus.setEnabled(false);
                btnMinus.setBackground(COLOR);
                btnPre.setEnabled(false);
                btnPre.setBackground(COLOR);
            }

            txtOffset.setText(Integer.toHexString(_offset).toUpperCase() + "h");
            txtOffset.transferFocusBackward();
        };
    }

    private ActionListener undoTask() {
        return (ActionEvent e) -> {
            if (!listTask.isEmpty()) {
                bytes[(int) listTask.get(indexTask)[0]] = (byte) (Integer.parseInt(listTask.get(indexTask)[1].toString(), 16));
                loadTableWork(bytes);

                indexTask--;
                if (indexTask < 0) {
                    indexTask = 0;
                    itemUndo.setEnabled(false);
                }
            } else if (indexTask == 0) {
                itemUndo.setEnabled(false);
            }
        };
    }

    private MouseAdapter btnBehavior() {
        return new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                if (e.getComponent().isEnabled()) {
                    JLabel btn = (JLabel) e.getComponent();
                    btn.setBackground(COLOR_HOVER);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (e.getComponent().isEnabled()) {
                    JLabel btn = (JLabel) e.getComponent();
                    btn.setBackground(COLOR);
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getComponent().isEnabled()) {
                    if (e.getComponent() == btnPlus) {
                        keyAdd(1);
                    } else if (e.getComponent() == btnMinus) {
                        keySubtra(1);
                    } else if (e.getComponent() == btnNext) {
                        keyAdd(columnBlocks);
                    } else if (e.getComponent() == btnPre) {
                        keySubtra(columnBlocks);
                    } else if (e.getComponent() == btnSaveP) {
                        savePalette();
                    } else if (e.getComponent() == btnLoadP) {
                        loadPalette();
                    } else if (e.getComponent() == btnNewP) {
                        newPalette();
                    }
                }
            }
        };
    }

    private MouseAdapter tablePaletteAction() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tablePalette.clearSelection();

                if ((e.getButton() == MouseEvent.BUTTON1) || (e.getButton() == MouseEvent.BUTTON3)) {
                    int _row = tablePalette.rowAtPoint(e.getPoint());
                    int _column = tablePalette.columnAtPoint(e.getPoint());
                    String _txt = tablePalette.getValueAt(_row, _column).toString();
                    Component _component = tablePalette.prepareRenderer(cellRender, _row, _column);

                    if (boxSetPal.isSelected()) {
                        Color _colorB = Color.BLACK;

                        if (cellRender.getListColors().isEmpty()) {
                            for (int i = 0; i < 256; i++) {
                                cellRender.getListColors().add(Color.BLACK);
                            }
                        } else {
                            _colorB = _component.getBackground();
                        }

                        _colorB = JColorChooser.showDialog(getContentPane(), "Selecione uma cor", _colorB);

                        if (_colorB != null) {
                            cellRender.getListColors().set(_column + (_row * 16), _colorB);
                            Color _colorF = cellRender.colorForeground(_colorB);

                            if (lblColor1.getText().equalsIgnoreCase(_txt)) {
                                lblColor1.setBackground(_colorB);
                                lblColor1.setForeground(_colorF);
                            }
                            if (lblColor2.getText().equalsIgnoreCase(_txt)) {
                                lblColor2.setBackground(_colorB);
                                lblColor2.setForeground(_colorF);
                            }
                        }

                        paletteChanged = true;
                        refresh();
                    } else {
                        if (_row >= 0 && _column >= 0) {
                            tablePalette.changeSelection(_row, _column, false, false);

                            if (e.getButton() == MouseEvent.BUTTON1) {
                                lblColor1.setText(_txt);
                                lblColor1.setBackground(_component.getBackground());
                                lblColor1.setForeground(_component.getForeground());
                            } else {
                                lblColor2.setText(_txt);
                                lblColor2.setBackground(_component.getBackground());
                                lblColor2.setForeground(_component.getForeground());
                            }
                        }
                    }
                }
            }
        };
    }

    private MouseAdapter tableWorkAction() {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                tableWork.clearSelection();

                if ((e.getButton() == MouseEvent.BUTTON1) || (e.getButton() == MouseEvent.BUTTON3)) {
                    if (bytes != null && bytes.length > 0 && boxEdit.isSelected()) {
                        int _row = tableWork.rowAtPoint(e.getPoint());
                        int _column = tableWork.columnAtPoint(e.getPoint());
                        int _offset = offset + (((_column - 1) + (_row * columnBlocks)));

                        if (_offset < bytes.length) {
                            Object[] _object = new Object[]{_offset, tableWork.getValueAt(_row, _column).toString()};
                            if (!listTask.contains(_object)) {
                                if (listTask.size() >= 20) {
                                    listTask.remove(0);
                                }

                                listTask.add(_object);
                                itemUndo.setEnabled(true);

                                indexTask = listTask.size() - 1;
                                if (indexTask < 0) {
                                    indexTask = 0;
                                }

                                if (e.getButton() == MouseEvent.BUTTON1) {
                                    bytes[_offset] = (byte) (Integer.parseInt(lblColor1.getText(), 16));
                                } else {
                                    bytes[_offset] = (byte) (Integer.parseInt(lblColor2.getText(), 16));
                                }

                                if (!wasChanged) {
                                    wasChanged = true;
                                }
                            }
                        }
                        loadTableWork(bytes);
                    }
                }
            }
        };
    }

    private void keyAdd(int value) {
        int newOffset = offset + value;
        int _leght = bytes.length - 1;

        if (newOffset <= _leght) {
            offset = newOffset;
            loadTableWork(bytes);

            txtOffset.setText(Integer.toHexString(offset).toUpperCase() + "h");

            if (offset >= _leght) {
                btnPlus.setEnabled(false);
                btnPlus.setBackground(COLOR);
                btnNext.setEnabled(false);
                btnNext.setBackground(COLOR);
            } else if (offset + columnBlocks > _leght) {
                btnNext.setEnabled(false);
                btnNext.setBackground(COLOR);
            }

            btnMinus.setEnabled(true);
            if (offset - columnBlocks >= 0) {
                btnPre.setEnabled(true);
            }
        }
    }

    private void keySubtra(int value) {
        int newOffset = offset - value;

        if (newOffset >= 0) {
            offset = newOffset;
            loadTableWork(bytes);

            txtOffset.setText(Integer.toHexString(offset).toUpperCase() + "h");

            if (offset == 0) {
                btnMinus.setEnabled(false);
                btnPre.setEnabled(false);
                btnMinus.setBackground(COLOR);
                btnPre.setBackground(COLOR);
            } else if (offset - columnBlocks < 0) {
                btnPre.setEnabled(false);
                btnPre.setBackground(COLOR);
            }

            int _leght = bytes.length - 1;
            if (offset < _leght) {
                btnPlus.setEnabled(true);
                if (offset + columnBlocks <= _leght) {
                    btnNext.setEnabled(true);
                }
            }

        }
    }

    private void newPalette() {
        if (!cellRender.getListColors().isEmpty() && paletteChanged) {
            int result = JOptionPane.showConfirmDialog(this, "Deseja salvar a paleta atual?", "Salvar paleta", JOptionPane.YES_NO_CANCEL_OPTION);

            if (JOptionPane.YES_OPTION == result || JOptionPane.NO_OPTION == result) {
                if (JOptionPane.YES_OPTION == result) {
                    savePalette();
                }

                cellRender.getListColors().clear();
                fillPalette();
                refresh();

                lblColor1.setBackground(Color.BLACK);
                lblColor1.setForeground(Color.WHITE);
                lblColor2.setBackground(Color.BLACK);
                lblColor2.setForeground(Color.WHITE);

                paletteChanged = false;
            }
        } else {
            fillPalette();
            refresh();

            lblColor1.setBackground(Color.BLACK);
            lblColor1.setForeground(Color.WHITE);
            lblColor2.setBackground(Color.BLACK);
            lblColor2.setForeground(Color.WHITE);

            paletteChanged = false;
        }
    }

    private void savePalette() {
        for (int i = cellRender.getListColors().size(); i < 256; i++) {
            cellRender.getListColors().add(Color.BLACK);
        }

        JFileChooser chooser = new JFileChooser(local) {
            @Override
            public void approveSelection() {
                String _fileToOpen = getSelectedFile().toString();
                File _file;

                if (_fileToOpen.endsWith(EXTENSION)) {
                    _file = new File(_fileToOpen);
                } else {
                    _file = new File(_fileToOpen + EXTENSION);
                }

                if (_file.exists() && getDialogType() == SAVE_DIALOG) {
                    int result = JOptionPane.showConfirmDialog(this, "Substituir \"" + _file.getName() + "\"?", "Substituir arquivo", JOptionPane.YES_NO_OPTION);
                    switch (result) {
                        case JOptionPane.YES_OPTION:
                            super.approveSelection();
                            return;
                        case JOptionPane.NO_OPTION:
                            return;
                        case JOptionPane.CLOSED_OPTION:
                            return;
                        case JOptionPane.CANCEL_OPTION:
                            cancelSelection();
                            return;
                    }
                }
                super.approveSelection();
            }
        };
        chooser.setDialogTitle("Salvar paleta");
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("Paletas", EXTENSION.replace(".", "")));
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            try {
                String _fileName = chooser.getSelectedFile().toString();

                if (!_fileName.endsWith(EXTENSION)) {
                    _fileName = _fileName + EXTENSION;
                }

                OutputStream stream = new FileOutputStream(_fileName);
                DataOutputStream out = new DataOutputStream(stream);

                out.write(("RIFF").getBytes());
                out.writeInt(Integer.reverseBytes(20 + cellRender.getListColors().size() * 4));
                out.write(("PAL data").getBytes());
                out.writeInt(Integer.reverseBytes(cellRender.getListColors().size() * 4 + 4));
                out.writeShort(Short.reverseBytes((short) 0x0300));
                out.writeShort(Short.reverseBytes((short) cellRender.getListColors().size()));

                for (Color _color : cellRender.getListColors()) {
                    out.writeByte(_color.getRed());
                    out.writeByte(_color.getGreen());
                    out.writeByte(_color.getBlue());
                    out.writeByte(0x00);
                }

                out.flush();
                out.close();
                stream.flush();
                stream.close();

                local = chooser.getSelectedFile().toPath().toString();
                paletteChanged = false;
                JOptionPane.showMessageDialog(null, "Paleta salva com sucesso!", "Paleta salva", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao salvar a paleta!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    private void loadPalette() {
        JFileChooser chooser = new JFileChooser(local);
        chooser.setDialogTitle("Carregar paleta");
        chooser.addChoosableFileFilter(new FileNameExtensionFilter("Paletas", EXTENSION.replace(".", "")));
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            try {
                InputStream stream = new FileInputStream(chooser.getSelectedFile());
                DataInputStream in = new DataInputStream(stream);

                byte[] _riff = new byte[4], _palData = new byte[8];
                List<Object> listInfo = new ArrayList<>();

                in.read(_riff);
                listInfo.add(Integer.reverseBytes(in.readInt()));
                in.read(_palData);
                listInfo.add(Integer.reverseBytes(in.readInt()));
                listInfo.add(Short.reverseBytes(in.readShort()));
                listInfo.add(Short.reverseBytes(in.readShort()));

                if (new String(_riff, "UTF-8").equals("RIFF")
                        && new String(_palData, "UTF-8").equals("PAL data")) {

                    cellRender.getListColors().clear();
                    int r, g, b;

                    for (int i = 0; i < (short) listInfo.get(listInfo.size() - 1); i++) {
                        r = in.readUnsignedByte();
                        g = in.readUnsignedByte();
                        b = in.readUnsignedByte();

                        cellRender.getListColors().add(new Color(r, g, b));
                        in.readByte();//0x00

                        if (cellRender.getListColors().size() >= 256) {
                            break;
                        }
                    }

                    if (cellRender.getListColors().size() >= 0) {
                        Component component = tablePalette.prepareRenderer(cellRender, 0, 0);

                        lblColor1.setText("00");
                        lblColor1.setBackground(component.getBackground());
                        lblColor1.setForeground(component.getForeground());

                        lblColor2.setText("00");
                        lblColor2.setBackground(component.getBackground());
                        lblColor2.setForeground(component.getForeground());
                    }
                    local = chooser.getSelectedFile().toPath().toString();
                    fillPalette();
                    refresh();
                    paletteChanged = true;
                } else {
                    JOptionPane.showMessageDialog(null, "Ops... Paleta incompatível!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException ex) {
                if (JOptionPane.YES_OPTION
                        == JOptionPane.showConfirmDialog(null, "Ops... Paleta incompatível, tentar carrega-la mesmo assim?", "Aviso", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE)) {
                    fillPalette();
                    refresh();
                } else {
                    cellRender.getListColors().clear();
                    newPalette();
                }

                paletteChanged = true;
            }
        }
    }

    private void loadTableWork(byte[] _bytes) {
        tmWork.removeAll();
        tmWork.removeAllColumns();

        ArrayList<Object> rows = new ArrayList();
        tmWork.addColumn("", String.class);

        if (_bytes == null) {
            rows.add("");
            for (int i = 0; i < columnBlocks; i++) {
                tmWork.addColumn(i + "", String.class);
                rows.add("");
            }

            for (int i = 0; i < rowBlocks; i++) {
                rows.set(0, i + "");
                tmWork.addRow(rows);
            }
        } else {
            for (int i = 0; i < columnBlocks; i++) {
                tmWork.addColumn(i + "", String.class);
            }

            for (int i = 0; i < rowBlocks; i++) {
                rows.add(i + "");
                for (int j = 0; j < columnBlocks; j++) {
                    try {
                        rows.add(String.format("%02X", _bytes[(j + (i * columnBlocks) + offset)]));
                    } catch (java.lang.ArrayIndexOutOfBoundsException exception) {
                        rows.add("");
                    }
                }
                tmWork.addRow(rows);
                rows.clear();
            }
        }

        refreshViewTableWork();
    }

    private void refreshViewTableWork() {
        int _width = (blockSize * (columnBlocks + 1)) + 20;

        tableWork.setRowHeight(blockSize);
        tableWork.getTableHeader().setPreferredSize(new Dimension(_width, blockSize));

        for (int i = 0; i < tableWork.getColumnCount(); i++) {
            tableWork.getColumnModel().getColumn(i).setResizable(false);
            tableWork.getColumnModel().getColumn(i).setMinWidth(blockSize);
            tableWork.getColumnModel().getColumn(i).setMaxWidth(blockSize);
        }

        scrollWork.setPreferredSize(new Dimension(_width, (blockSize * (rowBlocks + 1)) + 20));

        refresh();
    }

    private void exit() {
        if (wasChanged) {
            int result = JOptionPane.showConfirmDialog(null, "Deseja salvar o arquivo?", "Salvar", JOptionPane.YES_NO_CANCEL_OPTION);

            switch (result) {
                case JOptionPane.NO_OPTION:
                    System.exit(0);
                    break;

                case JOptionPane.YES_OPTION:
                    saveFile();
                    System.exit(0);
                    break;
            }
        } else {
            System.exit(0);
        }
    }

    private void refresh() {
        super.repaint();
        super.revalidate();
    }

    private void about() {
        String _color;

        switch (new Random().nextInt(3)) {
            case 0:
                _color = "blue";
                break;
            case 1:
                _color = "green";
                break;
            default:
                _color = "red";
        }

        JDialog _dialog = new JDialog(getOwner(), "Sobre");
        _dialog.setSize(350, 300);
        _dialog.setResizable(false);
        _dialog.setLocationRelativeTo(this.getContentPane());
        _dialog.setLayout(new MigLayout("fill"));

        JLabel _label = new JLabel("<html><div align='center'>Disponível em<br><a href='#'>Git Hub</a>");
        _label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        _label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://github.com/mrcapybara/basic_tile_editor"));
                } catch (IOException | URISyntaxException ex) {
                    System.err.println("Houston, temos um problema!");
                }
            }
        });

        _dialog.add(new JLabel("<html><div align='center'>Basic Tile Editor 0.0.2<br>"
                + "Basic Tile Editor é um software de distribuição livre.<br><br>"
                + "É permitida a edição e cópia, parcial ou completa, deste.<br>"
                + "Outrossim, é de inteira responsabilidade do usuário qualquer alteração e/ou prática envolvendo este software, isentando, assim, o autor.<br>"
                + "<br><br><br>Desenvolvido por<br><div style='color:" + _color + ";'>Jakecoll<br><br>"), "center, wrap");
        _dialog.add(_label, "center");

        _dialog.setModal(true);
        _dialog.setVisible(true);
    }

    private void fillPalette() {
        for (int i = cellRender.getListColors().size(); i < 255; i++) {
            cellRender.getListColors().add(Color.WHITE);
        }

        if (cellRender.getListColors().size() == 255) {
            cellRender.getListColors().add(Color.BLACK);
        }
    }

}
