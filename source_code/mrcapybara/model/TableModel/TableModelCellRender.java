package mrcapybara.model.TableModel;

import java.awt.Color;
import java.awt.Component;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class TableModelCellRender extends DefaultTableCellRenderer {

    private boolean enableTxt = false;
    private boolean isTableRom = false;

    private static List<Color> listColors;

    public TableModelCellRender(List<Color> _listColors) {
        this.setHorizontalAlignment(JLabel.CENTER);
        listColors = _listColors;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        isTableRom = table.getName().equalsIgnoreCase("rom");
        String _str = table.getValueAt(row, column).toString().replaceAll(" ", "");

        if (!isTableRom) {
            setBorder(noFocusBorder);
        }

        if (isTableRom && column == 0) {
            super.setBackground(Color.WHITE);
            super.setForeground(Color.BLACK);
            
        } else if (!_str.isEmpty()) {
            Color _colorB = Color.BLACK;
            if (listColors != null && !listColors.isEmpty()) {
                try {
                    _colorB = listColors.get(Integer.parseInt(_str, 16));
                } catch (java.lang.IndexOutOfBoundsException ex) {
                    System.err.println("Error ao carregar a cor!");
                }
            }

            super.setBackground(_colorB);

            if (!isTableRom || (isTableRom && enableTxt)) {
                super.setForeground(colorForeground(_colorB));
            } else {
                super.setForeground(_colorB);
            }

        } else {
            super.setBackground(Color.white);
            super.setForeground(Color.black);
        }
        return cellComponent;
    }

    public void setEnableTxt(boolean enableTxt) {
        this.enableTxt = enableTxt;
    }

    public List<Color> getListColors() {
        return listColors;
    }

    public Color colorForeground(Color _background) {
        return new Color(255 - _background.getRed(),
                255 - _background.getGreen(), 255 - _background.getBlue());
    }
}
