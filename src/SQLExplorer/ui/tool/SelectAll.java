package SQLExplorer.ui.tool;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

class SelectAll extends JCheckBox implements TableCellRenderer {

	private static final long serialVersionUID = 1L;
    private JTable table;
    private TableModel tableModel;
    private JTableHeader header;
    private TableColumnModel tcm;
    private int targetColumn;
    private int viewColumn;

    public SelectAll(JTable table, int targetColumn) {
        super();
        this.table = table;
        this.tableModel = table.getModel();
        this.targetColumn = targetColumn;
        this.header = table.getTableHeader();
        this.tcm = table.getColumnModel();        
        this.addItemListener(new ItemHandler());
        header.addMouseListener(new MouseHandler());
        tableModel.addTableModelListener(new ModelHandler());
    }

    @Override
    public Component getTableCellRendererComponent(
        JTable table, Object value, boolean isSelected,
        boolean hasFocus, int row, int column) {
        return this;
    }

    private class ItemHandler implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            boolean state = e.getStateChange() == ItemEvent.SELECTED;            
            for (int i = 0; i < table.getRowCount(); i++) {
                table.setValueAt(state, i, viewColumn);
            }
        }
    }

    @Override
    public void updateUI() {
        super.updateUI();        
    }    

    private class MouseHandler extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            viewColumn = header.columnAtPoint(e.getPoint());
            int modelColumn = tcm.getColumn(viewColumn).getModelIndex();
            if (modelColumn == targetColumn) {
                doClick();
            }
        }
    }

    private class ModelHandler implements TableModelListener {

        @Override
        public void tableChanged(TableModelEvent e) {
            if (needsToggle()) {
                doClick();
                header.repaint();
            }
        }
    }

    private boolean needsToggle() {
        boolean allTrue = true;
        boolean allFalse = true;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            boolean b = (Boolean) tableModel.getValueAt(i, targetColumn);
            allTrue &= b;
            allFalse &= !b;
        }
        return allTrue && !isSelected() || allFalse && isSelected();
    }
}
