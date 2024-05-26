package client.GUI;

import client.Commands.RemoveByIdCommand;
import client.Commands.UpdateByIdCommand;
import client.Controllers.CollectionController;
import client.Commands.AddCommand;
import client.Controllers.LocaleController;
import client.Controllers.ResourceBundlesController;
import client.GUI.visualization.VisualizationFrom;
import common.Collection.Worker;
import common.Exceptions.AccessDeniedException;
import common.Exceptions.LocalizedException;
import common.net.requests.ClientRequest;
import common.net.requests.ServerResponse;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class MainForm {
    private static MainForm MAIN_FORM = null;

    public static synchronized MainForm getInstance() {
        if (MAIN_FORM == null) {
            MAIN_FORM = new MainForm();
        }
        return MAIN_FORM;
    }

    private JPanel mainRootPanel;
    private JPanel menuPanel;
    private JPanel dataPanel;
    private JPanel controlPanel;
    private JButton createButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton visualizeButton;
    private JButton commandsButton;
    private JPanel editPanel;
    private JPanel visualizePanel;
    private JPanel commandsPanel;
    private JTable dataTable;
    private JLabel currentUserLabel;
    private JLabel numberOfWorkersLabel;
    private JScrollPane dataScrollPane;
    private JLabel workerLabel;
    private JPanel collectionInfoPanel;
    private JPanel userInfoPanel;
    private JPanel mainMenuPanel;
    private JPanel currentUserPanel;
    private JLabel usernameLabel;
    private JComboBox filterComboBox;
    private JTextField filterTextField;
    private JLabel filterByLabel;
    private JPanel filterPanel;
    private JLabel separatorLabel;
    private JPanel sortPanel;
    private JLabel sortLabel;
    private JComboBox sortComboBox;
    private JPanel localePanel;
    private JComboBox localeComboBox;
    private JLabel localeLabel;

    private VisualizationFrom visualizationFrom;
    public boolean VISUALIZATION_MODE = false;

    private final ArrayList<String> dataTableColumns = new ArrayList<>(Arrays.asList("id", "name", "x", "y", "creationDate", "salary", "startDate", "endDate", "status", "height", "eyeColor", "nationality"));

    private MainForm() {
        $$$setupUI$$$();
        updateLocale();

        usernameLabel.setText(ClientRequest.getUser().userName());

        createButton.addActionListener(new CreateButtonActionListener());
        filterComboBox.addActionListener(new FilterActionListener());
        sortComboBox.addActionListener(new SortActionListener());
        editButton.addActionListener(new EditButtonListener());
        deleteButton.addActionListener(new DeleteButtonListener());
        commandsButton.addActionListener(new CommandsButtonListener());
        visualizeButton.addActionListener(new VisualizationButtonListener());
        localeComboBox.addActionListener(new LocaleComboBoxListener());
    }

    public void resetVisualizationMode() {
        VISUALIZATION_MODE = false;
    }

    public void updateDataTable(Collection<Worker> collection) {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        model.setRowCount(0);
        collection.forEach(worker -> {
            model.addRow(new Vector<>(worker.getAsStringMap().values()));
        });
        dataTable.setModel(model);
        numberOfWorkersLabel.setText(String.valueOf(collection.size()));

        if (VISUALIZATION_MODE) visualizationFrom.update();
    }

    private void updateLocale() {
        ResourceBundle labels = ResourceBundlesController.getInstance().getMainBundle();
        createButton.setText(labels.getString("createButton"));
        editButton.setText(labels.getString("editButton"));
        deleteButton.setText(labels.getString("deleteButton"));
        visualizeButton.setText(labels.getString("visualizeButton"));
        commandsButton.setText(labels.getString("commandsButton"));
        currentUserLabel.setText(labels.getString("currentUserLabel") + ":");
        workerLabel.setText(labels.getString("workerLabel") + ":");
        filterByLabel.setText(labels.getString("filterByLabel") + ":");
        sortLabel.setText(labels.getString("sortLabel") + ":");
        localeLabel.setText(labels.getString("localeLabel") + ":");

        ResourceBundle fields = ResourceBundlesController.getInstance().getFieldsBundle();
        DefaultTableModel tableModel = new DefaultTableModel();
        dataTableColumns.stream().map(fields::getString).forEach(tableModel::addColumn);
        dataTable.setModel(tableModel);
        updateDataTable(CollectionController.getInstance().getProcessedCollection());

        int selectedItemIndex = filterComboBox.getSelectedIndex();
        filterComboBox.removeAllItems();
        filterComboBox.addItem("");
        dataTableColumns.stream().map(fields::getString).forEach(filterComboBox::addItem);
        filterComboBox.setSelectedIndex(selectedItemIndex);

        selectedItemIndex = sortComboBox.getSelectedIndex();
        sortComboBox.removeAllItems();
        sortComboBox.addItem("");
        dataTableColumns.stream().map(fields::getString).forEach(sortComboBox::addItem);
        sortComboBox.setSelectedIndex(selectedItemIndex);
    }

    private class LocaleComboBoxListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            LocaleController.getInstance().setLocale(localeComboBox.getSelectedIndex());
            updateLocale();
        }
    }

    private class CreateButtonActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Worker newWorker = (new ReadWorkerDialog()).showDialog();
            if (newWorker == null) return;
            ServerResponse response = new AddCommand(newWorker).execute();
            GUIController.getInstance().handleServerResponse(response);
        }
    }

    private class FilterActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = filterComboBox.getSelectedIndex();
            if (selectedIndex < 0) return;
            String filterByField = selectedIndex > 0 ? dataTableColumns.get(selectedIndex - 1) : "";
            String value = filterTextField.getText().trim();
            CollectionController.getInstance().setFilter(filterByField, value);
            updateDataTable(CollectionController.getInstance().getProcessedCollection());
        }
    }

    private class SortActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedIndex = sortComboBox.getSelectedIndex();
            if (selectedIndex < 0) return;
            String orderByField = selectedIndex > 0 ? dataTableColumns.get(selectedIndex - 1) : "";
            CollectionController.getInstance().setComparator(orderByField);
            updateDataTable(CollectionController.getInstance().getProcessedCollection());
        }
    }

    private class EditButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (dataTable.getSelectedRowCount() == 0) {
                GUIController.getInstance().showErrorMessage(new LocalizedException("rowToEdit"));
                return;
            }
            if (dataTable.getSelectedRowCount() > 1) {
                GUIController.getInstance().showErrorMessage(new LocalizedException("onlyOneRow"));
                return;
            }

            long selectedId = Long.parseLong((String) dataTable.getValueAt(dataTable.getSelectedRow(), 0));
            Worker workerToEdit = CollectionController.getInstance().getCollection().stream().filter(worker -> worker.getId() == selectedId).findFirst().orElseThrow();
            updateWorker(workerToEdit);
        }
    }

    public void updateWorker(Worker worker) {
        if (!worker.getUsername().equals(ClientRequest.getUser().userName())) {
            GUIController.getInstance().showErrorMessage(new AccessDeniedException("accessDeniedExceptionEdit"));
            return;
        }

        ReadWorkerDialog readWorkerDialog = new ReadWorkerDialog();
        readWorkerDialog.fillFields(worker);
        Worker newWorker = readWorkerDialog.showDialog();
        if (newWorker == null) return;
        ServerResponse response = new UpdateByIdCommand(newWorker, worker.getId()).execute();
        GUIController.getInstance().handleServerResponse(response);
    }

    private class DeleteButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (dataTable.getSelectedRowCount() == 0) {
                GUIController.getInstance().showErrorMessage(new LocalizedException("rowToDelete"));
                return;
            }
            if (dataTable.getSelectedRowCount() > 1) {
                GUIController.getInstance().showErrorMessage(new LocalizedException("onlyOneRow"));
                return;
            }
            long selectedId = Long.parseLong((String) dataTable.getValueAt(dataTable.getSelectedRow(), 0));
            ServerResponse response = new RemoveByIdCommand(selectedId).execute();
            GUIController.getInstance().handleServerResponse(response);
        }
    }

    private class CommandsButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            new CommandsDialog().setVisible(true);
        }
    }

    private class VisualizationButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (VISUALIZATION_MODE) return;
            VISUALIZATION_MODE = true;
            visualizationFrom = new VisualizationFrom();
            visualizationFrom.setVisible(true);
            visualizationFrom.update();
        }
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        mainRootPanel = new JPanel();
        mainRootPanel.setLayout(new BorderLayout(0, 0));
        mainRootPanel.setBackground(new Color(-13947600));
        menuPanel = new JPanel();
        menuPanel.setLayout(new GridBagLayout());
        mainRootPanel.add(menuPanel, BorderLayout.NORTH);
        userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BorderLayout(0, 0));
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        menuPanel.add(userInfoPanel, gbc);
        localePanel = new JPanel();
        localePanel.setLayout(new GridBagLayout());
        userInfoPanel.add(localePanel, BorderLayout.WEST);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 0, 0, 0);
        localePanel.add(localeComboBox, gbc);
        localeLabel = new JLabel();
        Font localeLabelFont = this.$$$getFont$$$(null, Font.PLAIN, 16, localeLabel.getFont());
        if (localeLabelFont != null) localeLabel.setFont(localeLabelFont);
        localeLabel.setText("Language");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 10, 0, 10);
        localePanel.add(localeLabel, gbc);
        currentUserPanel = new JPanel();
        currentUserPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        userInfoPanel.add(currentUserPanel, BorderLayout.CENTER);
        currentUserLabel = new JLabel();
        Font currentUserLabelFont = this.$$$getFont$$$(null, Font.PLAIN, 16, currentUserLabel.getFont());
        if (currentUserLabelFont != null) currentUserLabel.setFont(currentUserLabelFont);
        currentUserLabel.setText("Current user:");
        currentUserPanel.add(currentUserLabel);
        usernameLabel = new JLabel();
        Font usernameLabelFont = this.$$$getFont$$$(null, Font.PLAIN, 16, usernameLabel.getFont());
        if (usernameLabelFont != null) usernameLabel.setFont(usernameLabelFont);
        usernameLabel.setText("someUserName");
        currentUserPanel.add(usernameLabel);
        mainMenuPanel = new JPanel();
        mainMenuPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 10, 0, 10);
        menuPanel.add(mainMenuPanel, gbc);
        collectionInfoPanel = new JPanel();
        collectionInfoPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainMenuPanel.add(collectionInfoPanel, gbc);
        workerLabel = new JLabel();
        Font workerLabelFont = this.$$$getFont$$$(null, Font.PLAIN, 16, workerLabel.getFont());
        if (workerLabelFont != null) workerLabel.setFont(workerLabelFont);
        workerLabel.setText("Number of workers:");
        collectionInfoPanel.add(workerLabel);
        numberOfWorkersLabel = new JLabel();
        Font numberOfWorkersLabelFont = this.$$$getFont$$$(null, Font.PLAIN, 16, numberOfWorkersLabel.getFont());
        if (numberOfWorkersLabelFont != null) numberOfWorkersLabel.setFont(numberOfWorkersLabelFont);
        numberOfWorkersLabel.setText("10");
        collectionInfoPanel.add(numberOfWorkersLabel);
        filterPanel = new JPanel();
        filterPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainMenuPanel.add(filterPanel, gbc);
        filterComboBox = new JComboBox();
        Font filterComboBoxFont = this.$$$getFont$$$(null, Font.PLAIN, 16, filterComboBox.getFont());
        if (filterComboBoxFont != null) filterComboBox.setFont(filterComboBoxFont);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        filterPanel.add(filterComboBox, gbc);
        separatorLabel = new JLabel();
        Font separatorLabelFont = this.$$$getFont$$$(null, Font.PLAIN, 16, separatorLabel.getFont());
        if (separatorLabelFont != null) separatorLabel.setFont(separatorLabelFont);
        separatorLabel.setText(":");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 0, 5);
        filterPanel.add(separatorLabel, gbc);
        filterTextField = new JTextField();
        filterTextField.setColumns(10);
        Font filterTextFieldFont = this.$$$getFont$$$(null, Font.PLAIN, 16, filterTextField.getFont());
        if (filterTextFieldFont != null) filterTextField.setFont(filterTextFieldFont);
        gbc = new GridBagConstraints();
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        filterPanel.add(filterTextField, gbc);
        filterByLabel = new JLabel();
        Font filterByLabelFont = this.$$$getFont$$$(null, Font.PLAIN, 16, filterByLabel.getFont());
        if (filterByLabelFont != null) filterByLabel.setFont(filterByLabelFont);
        filterByLabel.setText("Filter by");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 0, 0, 10);
        filterPanel.add(filterByLabel, gbc);
        sortPanel = new JPanel();
        sortPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.VERTICAL;
        mainMenuPanel.add(sortPanel, gbc);
        sortLabel = new JLabel();
        Font sortLabelFont = this.$$$getFont$$$(null, Font.PLAIN, 16, sortLabel.getFont());
        if (sortLabelFont != null) sortLabel.setFont(sortLabelFont);
        sortLabel.setText("Order by");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(0, 0, 0, 10);
        sortPanel.add(sortLabel, gbc);
        sortComboBox = new JComboBox();
        Font sortComboBoxFont = this.$$$getFont$$$(null, Font.PLAIN, 16, sortComboBox.getFont());
        if (sortComboBoxFont != null) sortComboBox.setFont(sortComboBoxFont);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        sortPanel.add(sortComboBox, gbc);
        dataPanel = new JPanel();
        dataPanel.setLayout(new GridBagLayout());
        mainRootPanel.add(dataPanel, BorderLayout.CENTER);
        dataScrollPane = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 0, 10);
        dataPanel.add(dataScrollPane, gbc);
        dataTable.setAutoResizeMode(4);
        dataTable.setEnabled(true);
        dataTable.setFillsViewportHeight(true);
        Font dataTableFont = this.$$$getFont$$$(null, -1, 16, dataTable.getFont());
        if (dataTableFont != null) dataTable.setFont(dataTableFont);
        dataScrollPane.setViewportView(dataTable);
        controlPanel = new JPanel();
        controlPanel.setLayout(new GridBagLayout());
        mainRootPanel.add(controlPanel, BorderLayout.SOUTH);
        editPanel = new JPanel();
        editPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(10, 10, 10, 0);
        controlPanel.add(editPanel, gbc);
        createButton = new JButton();
        Font createButtonFont = this.$$$getFont$$$(null, Font.PLAIN, 16, createButton.getFont());
        if (createButtonFont != null) createButton.setFont(createButtonFont);
        createButton.setText("Create");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 2, 0, 2);
        editPanel.add(createButton, gbc);
        editButton = new JButton();
        Font editButtonFont = this.$$$getFont$$$(null, Font.PLAIN, 16, editButton.getFont());
        if (editButtonFont != null) editButton.setFont(editButtonFont);
        editButton.setText("Edit");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 2, 0, 2);
        editPanel.add(editButton, gbc);
        deleteButton = new JButton();
        Font deleteButtonFont = this.$$$getFont$$$(null, Font.PLAIN, 16, deleteButton.getFont());
        if (deleteButtonFont != null) deleteButton.setFont(deleteButtonFont);
        deleteButton.setText("Delete");
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 2, 0, 2);
        editPanel.add(deleteButton, gbc);
        visualizePanel = new JPanel();
        visualizePanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 0.5;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 0, 10, 0);
        controlPanel.add(visualizePanel, gbc);
        visualizeButton = new JButton();
        Font visualizeButtonFont = this.$$$getFont$$$(null, Font.PLAIN, 16, visualizeButton.getFont());
        if (visualizeButtonFont != null) visualizeButton.setFont(visualizeButtonFont);
        visualizeButton.setText("Visualize");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 2, 0, 2);
        visualizePanel.add(visualizeButton, gbc);
        commandsPanel = new JPanel();
        commandsPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0.4;
        gbc.weighty = 0.4;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(10, 0, 10, 10);
        controlPanel.add(commandsPanel, gbc);
        commandsButton = new JButton();
        Font commandsButtonFont = this.$$$getFont$$$(null, Font.PLAIN, 16, commandsButton.getFont());
        if (commandsButtonFont != null) commandsButton.setFont(commandsButtonFont);
        commandsButton.setText("Commands");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 2, 0, 2);
        commandsPanel.add(commandsButton, gbc);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainRootPanel;
    }

    private void createUIComponents() {
        DefaultTableModel dataTableModel = new DefaultTableModel();
        dataTableColumns.forEach(dataTableModel::addColumn);
        dataTable = new JTable(dataTableModel) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            ;
        };
        dataTable.getTableHeader().setReorderingAllowed(false);
        dataTable.getTableHeader().setFont(this.$$$getFont$$$(null, Font.PLAIN, 16, dataTable.getTableHeader().getFont()));

        localeComboBox = new JComboBox<String>();
        LocaleController.getInstance().getLocaleNames().forEach(localeComboBox::addItem);
        localeComboBox.setSelectedIndex(LocaleController.getInstance().getCurrentLocaleNumber());
    }
}
