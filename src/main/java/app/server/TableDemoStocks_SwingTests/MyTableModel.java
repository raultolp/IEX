package app.server.TableDemoStocks_SwingTests;

import javax.swing.table.AbstractTableModel;

public class MyTableModel extends AbstractTableModel {
  //private boolean CHANGED=false; // LISATUD

    private String[] columnNames = {
            "STOCK",
            "PRICE",
            "PREV.CLOSE",
            "CHANGE 1Y",
            "CHANGE 1M",
            "CHANGE 3M",
            "DIVIDEND YIELD",
            "EPS",
            "PE RATIO",
            "MARKET CAP",
            "SHORT RATIO" };

    private Object[][] data;

    public MyTableModel(Object[][] data) {  //LISATUD
        this.data = data;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        return data[row][col];
    }

    public Object[][] getData() {  //LISATUD
        return data;
    }

    public void setData(Object[][] data) { //LISATUD
        this.data = data;
    }


    /*
     * JTable uses this method to determine the default renderer/
     * editor for each cell.  If we didn't implement this method,
     * then the last column would contain text ("true"/"false"),
     * rather than a check box.
     */
    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }


    @Override
    public boolean isCellEditable(int row, int col) {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        if (col < 2) {
            return false;
        } else {
            return true;
        }
    }

}
