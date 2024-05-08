package client.GUI;

import common.Collection.Worker;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Collection;
import java.util.Vector;

public class MainFormController {
    private final MainForm mainForm;
    private Collection<Worker> collection;

    public MainFormController(){
        mainForm = new MainForm();
    }

    private void updateDataTable(){
        DefaultTableModel model = (DefaultTableModel) mainForm.getDataTable().getModel();
        collection.forEach(worker -> {
            model.addRow(new Vector<>(worker.getAsStringArray()));
        });
        mainForm.getDataTable().setModel(model);
    }

    public void setCollection(Collection<Worker> collection) {
        this.collection = collection;
        updateDataTable();
    }

    public JComponent getRootComponent(){
        return this.mainForm.$$$getRootComponent$$$();
    }
}
