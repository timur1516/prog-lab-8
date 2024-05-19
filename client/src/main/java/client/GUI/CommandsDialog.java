package client.GUI;

import client.Commands.*;
import client.Exceptions.ValueParsingException;
import client.Parsers.WorkerParsers;
import client.Readers.TextFieldReader;
import common.Collection.Worker;
import common.Exceptions.InvalidDataException;
import common.Validators.WorkerValidators;
import common.net.requests.ResultState;
import common.net.requests.ServerResponse;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.util.Locale;

public class CommandsDialog extends JDialog {
    private JPanel contentPane;
    private JPanel headerPanel;
    private JPanel mainPanel;
    private JLabel headerLaber;
    private JButton clearCommandButton;
    private JButton executeScriptCommandButton;
    private JButton filterLessThanEndDateCommandButton;
    private JButton minBySalaryCommandButton;
    private JButton removeFirstCommandButton;
    private JButton removeGreaterCommandButton;
    private JButton removeLowerCommandButton;
    private JPanel commandsWithInputPanel;
    private JTextField endDateTextField;
    private JButton removeByIdCommandButton;
    private JTextField idTextField;

    public CommandsDialog() {
        setContentPane(contentPane);
        setModal(false);
        setSize(450, 600);
        setResizable(false);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        clearCommandButton.addActionListener(new ClearCommandListener());
        minBySalaryCommandButton.addActionListener(new MinBySalaryCommandListener());
        filterLessThanEndDateCommandButton.addActionListener(new FilterLessThanEndDateCommandListener());
        removeByIdCommandButton.addActionListener(new RemoveByIdCommandListener());
        removeFirstCommandButton.addActionListener(new RemoveFirstCommandListener());
        removeGreaterCommandButton.addActionListener(new RemoveGreaterCommandListener());
        removeLowerCommandButton.addActionListener(new RemoveLowerCommandListener());
    }

    private class ClearCommandListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            GUIController.getInstance().handleServerResponse(new ClearCommand().execute());
        }
    }

    private class MinBySalaryCommandListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
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
            //TODO add normal execution (i don't know how( )
            try {
                LocalDateTime endDate = TextFieldReader.readValue(endDateTextField, "end Date", WorkerValidators.endDateValidator, WorkerParsers.localDateTimeParser);
                GUIController.getInstance().handleServerResponse(new FilterLessThanEndDateCommand(endDate).execute());
            } catch (ValueParsingException | InvalidDataException ex) {
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
            } catch (ValueParsingException | InvalidDataException ex) {
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
            Worker worker = new ReadWorkerDialog().showDialog();
            if (worker == null) return;
            GUIController.getInstance().handleServerResponse(new RemoveGreaterCommand(worker).execute());
        }
    }

    private class RemoveLowerCommandListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Worker worker = new ReadWorkerDialog().showDialog();
            if (worker == null) return;
            GUIController.getInstance().handleServerResponse(new RemoveLowerCommand(worker).execute());
        }
    }


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(0, 0));
        headerPanel = new JPanel();
        headerPanel.setLayout(new GridBagLayout());
        contentPane.add(headerPanel, BorderLayout.NORTH);
        headerLaber = new JLabel();
        Font headerLaberFont = this.$$$getFont$$$(null, Font.BOLD, 24, headerLaber.getFont());
        if (headerLaberFont != null) headerLaber.setFont(headerLaberFont);
        headerLaber.setText("Additional commands");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 0, 10, 0);
        headerPanel.add(headerLaber, gbc);
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
        endDateTextField = new JTextField();
        endDateTextField.setColumns(30);
        Font endDateTextFieldFont = this.$$$getFont$$$(null, Font.PLAIN, 16, endDateTextField.getFont());
        if (endDateTextFieldFont != null) endDateTextField.setFont(endDateTextFieldFont);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 10);
        commandsWithInputPanel.add(endDateTextField, gbc);
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

}
