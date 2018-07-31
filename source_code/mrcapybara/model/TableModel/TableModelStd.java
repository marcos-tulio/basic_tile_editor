package mrcapybara.model.TableModel;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class TableModelStd extends AbstractTableModel {

    private List<String> header = new ArrayList();
    private List<List<Object>> lines = new ArrayList();
    private List<Class> classes = new ArrayList();

    public TableModelStd(ArrayList<String> header, ArrayList<Class> classes) {
        this.header = (List<String>) header.clone();
        this.classes = (List<Class>) classes.clone();
    }

    @Override
    public int getRowCount() {
        return lines.size();
    }

    @Override
    public int getColumnCount() {
        return header.size();
    }

    @Override
    public Object getValueAt(int row, int col) {
        return lines.get(row).get(col);
    }

    @Override
    public void setValueAt(Object data, int row, int column) {
        List<Object> _list = lines.get(row);
        _list.set(column, data);
    }

    @Override
    public Class getColumnClass(int _column) {
        return classes.get(_column);
    } 

    public void addRow(ArrayList<? extends Object> values) {
        ArrayList<Object> _line = new ArrayList();
        values.forEach((value) -> {
            _line.add(value);
        });
        lines.add(_line);
        fireTableDataChanged();
    }

    public void addColumn(String title, Class classe) {
        header.add(title);
        classes.add(classe);
        lines.forEach((valores) -> {
            try {
                valores.add(classe.newInstance());
            } catch (ReflectiveOperationException ex) {
                System.err.println("Houston, temos um problema!");
            }
        });
        fireTableStructureChanged();
    }

    public void removeAllColumns() {
        header.clear();
        classes.clear();
        fireTableStructureChanged();
    }

    public void removeAll() {
        lines.clear();
        fireTableRowsDeleted(0, lines.size());
    }

}
