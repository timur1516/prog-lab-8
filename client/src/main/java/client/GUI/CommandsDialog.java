package client.GUI;

import client.Commands.*;
import client.Controllers.CollectionController;
import client.Controllers.LocaleController;
import client.GUI.calendar.Calendar;
import client.Parsers.WorkerParsers;
import client.Readers.CalendarReader;
import client.Readers.TextFieldReader;
import common.Collection.Worker;
import common.Exceptions.InvalidDataException;
import common.Exceptions.LocalizedException;
import common.Exceptions.LocalizedMessage;
import common.Validators.WorkerValidators;
import common.net.requests.ResultState;
import common.net.requests.ServerResponse;
import common.utils.CommonConstants;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Locale;
import java.util.ResourceBundle;

public class CommandsDialog extends JDialog {
    private JPanel contentPane;
    private JPanel headerPanel;
    private JPanel mainPanel;
    private JLabel headerLabel;
    private JButton clearCommandButton;
    private JButton executeScriptCommandButton;
    private JButton filterLessThanEndDateCommandButton;
    private JButton minBySalaryCommandButton;
    private JButton removeFirstCommandButton;
    private JButton removeGreaterCommandButton;
    private JButton removeLowerCommandButton;
    private JPanel commandsWithInputPanel;
    private JButton removeByIdCommandButton;
    private JTextField idTextField;
    private Calendar endDateCalendar;

    public CommandsDialog() {
        $$$setupUI$$$();
        updateLocale();
        setContentPane(contentPane);
        setModal(true);
        setSize(450, 600);
        setResizable(false);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        clearCommandButton.addActionListener(new ClearCommandListener());
        executeScriptCommandButton.addActionListener(new ExecuteScriptCommandListener());
        minBySalaryCommandButton.addActionListener(new MinBySalaryCommandListener());
        filterLessThanEndDateCommandButton.addActionListener(new FilterLessThanEndDateCommandListener());
        removeByIdCommandButton.addActionListener(new RemoveByIdCommandListener());
        removeFirstCommandButton.addActionListener(new RemoveFirstCommandListener());
        removeGreaterCommandButton.addActionListener(new RemoveGreaterCommandListener());
        removeLowerCommandButton.addActionListener(new RemoveLowerCommandListener());
    }

    private void updateLocale() {
        ResourceBundle labels = ResourceBundle.getBundle("MainGuiLabels", LocaleController.getInstance().getCurrentLocale());
        headerLabel.setText(labels.getString("commandsHeaderLabel"));
        clearCommandButton.setText(labels.getString("clearCommandButton"));
        executeScriptCommandButton.setText(labels.getString("executeScriptCommandButton"));
        filterLessThanEndDateCommandButton.setText(labels.getString("filterLessThanEndDateCommandButton"));
        minBySalaryCommandButton.setText(labels.getString("minBySalaryCommandButton"));
        removeFirstCommandButton.setText(labels.getString("removeFirstCommandButton"));
        removeGreaterCommandButton.setText(labels.getString("removeGreaterCommandButton"));
        removeLowerCommandButton.setText(labels.getString("removeLowerCommandButton"));
        removeByIdCommandButton.setText(labels.getString("removeByIdCommandButton"));
    }

    private class ClearCommandListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            GUIController.getInstance().handleServerResponse(new ClearCommand().execute());
        }
    }

    private class ExecuteScriptCommandListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

            fileChooser.setFileFilter(new FileNameExtensionFilter("TEXT FILES", "txt", "text"));

            int r = fileChooser.showOpenDialog(CommandsDialog.this);

            PrintStream systemStream = System.out;

            if (r == JFileChooser.APPROVE_OPTION) {
                String logFileName = LocalDateTime.now().format(CommonConstants.formatter).replace(':', '.') + "_script_output.txt";
                try {
                    System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream(logFileName))));
                } catch (FileNotFoundException ex) {
                    GUIController.getInstance().showErrorMessage(new LocalizedException("scriptLogFileError"));
                    return;
                }
                GUIController.getInstance().handleServerResponse(new ExecuteScriptCommand(fileChooser.getSelectedFile().getAbsolutePath()).execute());
                System.out.flush();
                System.out.close();
                System.setOut(systemStream);
            }
        }
    }

    private class MinBySalaryCommandListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (CollectionController.getInstance().getCollection().isEmpty()) {
                GUIController.getInstance().showInfoMessage(new LocalizedMessage("emptyCollectionMessage"));
                return;
            }
            ServerResponse response = new MinBySalaryCommand().execute();
            if (response.state() == ResultState.EXCEPTION) {
                GUIController.getInstance().handleServerResponse(response);
                return;
            }
            MainForm.getInstance().updateWorker((Worker) response.data());
        }
    }

    private class FilterLessThanEndDateCommandListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (CollectionController.getInstance().getCollection().isEmpty()) {
                    GUIController.getInstance().showInfoMessage(new LocalizedMessage("emptyCollectionMessage"));
                    return;
                }

                LocalDateTime endDate = CalendarReader.readValue(endDateCalendar, WorkerValidators.endDateValidator);
                if (endDate == null) {
                    GUIController.getInstance().showErrorMessage(new LocalizedException("noEndDate"));
                    return;
                }
                ServerResponse response = new FilterLessThanEndDateCommand(endDate).execute();
                if (response.state() == ResultState.EXCEPTION) {
                    GUIController.getInstance().showErrorMessage((LocalizedException) response.data());
                    return;
                }
                MainForm.getInstance().updateDataTable((Collection<Worker>) response.data());
            } catch (InvalidDataException ex) {
                GUIController.getInstance().showErrorMessage(ex);
            }
        }
    }

    private class RemoveByIdCommandListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                long id = TextFieldReader.readValue(idTextField, "id", WorkerValidators.idValidator, WorkerParsers.longParser);
                GUIController.getInstance().handleServerResponse(new RemoveByIdCommand(id).execute());
            } catch (InvalidDataException ex) {
                GUIController.getInstance().showErrorMessage(ex);
            }
        }
    }

    private class RemoveFirstCommandListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            GUIController.getInstance().handleServerResponse(new RemoveFirstCommand().execute());
        }
    }

    private class RemoveGreaterCommandListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (CollectionController.getInstance().getCollection().isEmpty()) {
                GUIController.getInstance().showInfoMessage(new LocalizedMessage("emptyCollectionMessage"));
                return;
            }

            Worker worker = new ReadWorkerDialog().showDialog();
            if (worker == null) return;
            GUIController.getInstance().handleServerResponse(new RemoveGreaterCommand(worker).execute());
        }
    }

    private class RemoveLowerCommandListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (CollectionController.getInstance().getCollection().isEmpty()) {
                GUIController.getInstance().showInfoMessage(new LocalizedMessage("emptyCollectionMessage"));
                return;
            }

            Worker worker = new ReadWorkerDialog().showDialog();
            if (worker == null) return;
            GUIController.getInstance().handleServerResponse(new RemoveLowerCommand(worker).execute());
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
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(0, 0));
        headerPanel = new JPanel();
        headerPanel.setLayout(new GridBagLayout());
        contentPane.add(headerPanel, BorderLayout.NORTH);
        headerLabel = new JLabel();
        Font headerLabelFont = this.$$$getFont$$$(null, Font.BOLD, 24, headerLabel.getFont());
        if (headerLabelFont != null) headerLabel.setFont(headerLabelFont);
        headerLabel.setText("Additional commands");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 0, 10, 0);
        headerPanel.add(headerLabel, gbc);
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        contentPane.add(mainPanel, BorderLayout.CENTER);
        clearCommandButton = new JButton();
        Font clearCommandButtonFont = this.$$$getFont$$$(null, Font.PLAIN, 16, clearCommandButton.getFont());
        if (clearCommandButtonFont != null) clearCommandButton.setFont(clearCommandButtonFont);
        clearCommandButton.setText("Clear collection");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        mainPanel.add(clearCommandButton, gbc);
        executeScriptCommandButton = new JButton();
        Font executeScriptCommandButtonFont = this.$$$getFont$$$(null, Font.PLAIN, 16, executeScriptCommandButton.getFont());
        if (executeScriptCommandButtonFont != null) executeScriptCommandButton.setFont(executeScriptCommandButtonFont);
        executeScriptCommandButton.setText("Execute script");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        mainPanel.add(executeScriptCommandButton, gbc);
        minBySalaryCommandButton = new JButton();
        Font minBySalaryCommandButtonFont = this.$$$getFont$$$(null, Font.PLAIN, 16, minBySalaryCommandButton.getFont());
        if (minBySalaryCommandButtonFont != null) minBySalaryCommandButton.setFont(minBySalaryCommandButtonFont);
        minBySalaryCommandButton.setText("Min by salary");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        mainPanel.add(minBySalaryCommandButton, gbc);
        removeFirstCommandButton = new JButton();
        Font removeFirstCommandButtonFont = this.$$$getFont$$$(null, Font.PLAIN, 16, removeFirstCommandButton.getFont());
        if (removeFirstCommandButtonFont != null) removeFirstCommandButton.setFont(removeFirstCommandButtonFont);
        removeFirstCommandButton.setText("Remove first");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        mainPanel.add(removeFirstCommandButton, gbc);
        removeGreaterCommandButton = new JButton();
        Font removeGreaterCommandButtonFont = this.$$$getFont$$$(null, Font.PLAIN, 16, removeGreaterCommandButton.getFont());
        if (removeGreaterCommandButtonFont != null) removeGreaterCommandButton.setFont(removeGreaterCommandButtonFont);
        removeGreaterCommandButton.setText("Remove greater");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        mainPanel.add(removeGreaterCommandButton, gbc);
        removeLowerCommandButton = new JButton();
        Font removeLowerCommandButtonFont = this.$$$getFont$$$(null, Font.PLAIN, 16, removeLowerCommandButton.getFont());
        if (removeLowerCommandButtonFont != null) removeLowerCommandButton.setFont(removeLowerCommandButtonFont);
        removeLowerCommandButton.setText("Remove lower");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 20, 10);
        mainPanel.add(removeLowerCommandButton, gbc);
        commandsWithInputPanel = new JPanel();
        commandsWithInputPanel.setLayout(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(commandsWithInputPanel, gbc);
        filterLessThanEndDateCommandButton = new JButton();
        Font filterLessThanEndDateCommandButtonFont = this.$$$getFont$$$(null, Font.PLAIN, 16, filterLessThanEndDateCommandButton.getFont());
        if (filterLessThanEndDateCommandButtonFont != null)
            filterLessThanEndDateCommandButton.setFont(filterLessThanEndDateCommandButtonFont);
        filterLessThanEndDateCommandButton.setText("Filter less than end date");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 5);
        commandsWithInputPanel.add(filterLessThanEndDateCommandButton, gbc);
        removeByIdCommandButton = new JButton();
        Font removeByIdCommandButtonFont = this.$$$getFont$$$(null, Font.PLAIN, 16, removeByIdCommandButton.getFont());
        if (removeByIdCommandButtonFont != null) removeByIdCommandButton.setFont(removeByIdCommandButtonFont);
        removeByIdCommandButton.setText("Remove by id");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 5);
        commandsWithInputPanel.add(removeByIdCommandButton, gbc);
        idTextField = new JTextField();
        idTextField.setColumns(30);
        Font idTextFieldFont = this.$$$getFont$$$(null, Font.PLAIN, 16, idTextField.getFont());
        if (idTextFieldFont != null) idTextField.setFont(idTextFieldFont);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 10);
        commandsWithInputPanel.add(idTextField, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 10);
        commandsWithInputPanel.add(endDateCalendar, gbc);
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
        return contentPane;
    }

    private void createUIComponents() {
        endDateCalendar = new Calendar();
    }

}
