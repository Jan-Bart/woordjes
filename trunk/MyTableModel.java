/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package woordjes;

import javax.swing.table.AbstractTableModel;

/**
 *
 * @author janbart
 */
class MyTableModel extends AbstractTableModel {

    // Create the table
    String[] columnNames = {"", "",};
    Object[][] rowData;

    public void setRowData(Object[][] rowDataInput) {
        rowData = rowDataInput;

    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    @Override
    public void setValueAt(Object value, int row, int col) {
        rowData[row][col] = value;
        fireTableCellUpdated(row, col);
    }


    public int getColumnCount() {
        return columnNames.length;
    }

    public int getRowCount() {
        return rowData.length;
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return rowData[row][col];
    }
}
