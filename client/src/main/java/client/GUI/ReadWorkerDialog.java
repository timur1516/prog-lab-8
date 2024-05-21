package client.GUI;

import client.Exceptions.ValueParsingException;
import client.GUI.calendar.Calendar;
import client.Parsers.WorkerParsers;
import client.Readers.CalendarReader;
import client.Readers.ComboBoxReader;
import client.Readers.TextFieldReader;
import common.Collection.*;
import common.Collection.Color;
import common.Exceptions.InvalidDataException;
import common.Validators.WorkerValidators;
import common.net.requests.ClientRequest;
import common.utils.CommonConstants;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.FontUIResource;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

public class ReadWorkerDialog extends JDialog {
    private JPanel contentPane;
    private JPanel headerPanel;
    private JPanel mainPanel;
    private JPanel menuPanel;
    private JLabel headerLabel;
    private JButton saveButton;
    private JButton cancelButton;
    private JTextField nameTextField;
    private JTextField startDateTextField;
    private JComboBox statusComboBox;
    private JLabel nameLabel;
    private JLabel salaryLabel;
    private JLabel startDateLabel;
    private JLabel endDateLabel;
    private JLabel statusLabel;
    private JLabel heightLabel;
    private JLabel eyeColorLabel;
    private JLabel nationalityLabel;
    private JTextField salaryTextField;
    private JTextField heightTextField;
    private JComboBox eyeColorComboBox;
    private JComboBox nationalityComboBox;
    private JLabel mainInfoLabel;
    private JCheckBox noPersonCheckBox;
    private JLabel coordinatesLabel;
    private JLabel xLabel;
    private JTextField xTextField;
    private JLabel yLabel;
    private JTextField yTextField;
    private JPanel personPanel;
    private JLabel personLabel;
    private Calendar endDateCalendar;
    private Calendar startDateCalendar;

    private Worker worker;

    public ReadWorkerDialog() {
        $$$setupUI$$$();

        setContentPane(contentPane);
        setModal(true);
        setSize(400, 650);
        setResizable(false);
        revalidate();
        getRootPane().setDefaultButton(saveButton);

        saveButton.addActionListener(e -> onSave());

        cancelButton.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        noPersonCheckBox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                boolean ieEnabled = !noPersonCheckBox.isSelected();
                heightTextField.setEnabled(ieEnabled);
                eyeColorComboBox.setEnabled(ieEnabled);
                nationalityComboBox.setEnabled(ieEnabled);
            }
        });
    }

    public void fillFields(Worker worker) {
        LinkedHashMap<String, String> workerList = worker.getAsStringMap();

        this.nameTextField.setText(workerList.get("name"));
        this.salaryTextField.setText(workerList.get("salary"));

        LocalDateTime startDate = LocalDateTime.parse(workerList.get("startDate"), CommonConstants.formatter);
        this.startDateCalendar.getModel().setDate(startDate.getYear(), startDate.getMonthValue(), startDate.getDayOfMonth());
        this.startDateCalendar.getModel().setSelected(true);

        if (!workerList.get("endDate").isEmpty()) {
            LocalDateTime endDate = LocalDateTime.parse(workerList.get("endDate"), CommonConstants.formatter);
            this.endDateCalendar.getModel().setDate(endDate.getYear(), endDate.getMonthValue(), endDate.getDayOfMonth());
            this.endDateCalendar.getModel().setSelected(true);
        }

        this.statusComboBox.setSelectedItem(workerList.get("status"));

        this.xTextField.setText(workerList.get("x"));
        this.yTextField.setText(workerList.get("y"));

        if (workerList.get("height").isEmpty()) {
            noPersonCheckBox.doClick();
        } else {
            this.heightTextField.setText(workerList.get("height"));
            this.eyeColorComboBox.setSelectedItem(workerList.get("eyeColor"));
            this.nationalityComboBox.setSelectedItem(workerList.get("nationality"));
        }
    }

    private void onSave() {
        try {
            worker = readWorker();
        } catch (Exception e) {
            GUIController.getInstance().showErrorMessage(e);
            return;
        }
        setVisible(false);
        dispose();
    }

    private void onCancel() {
        worker = null;
        setVisible(false);
        dispose();
    }

    public Worker showDialog() {
        setVisible(true);
        return worker;
    }

    private Worker readWorker() throws InvalidDataException, ValueParsingException {
        String name = TextFieldReader.readValue(nameTextField, "name",
                WorkerValidators.nameValidator, WorkerParsers.stringParser);

        Integer salary = TextFieldReader.readValue(salaryTextField, "salary",
                WorkerValidators.salaryValidator, WorkerParsers.integerParser);

        LocalDateTime startDate = CalendarReader.readValue(startDateCalendar, WorkerValidators.startDateValidator);

        LocalDateTime endDate = CalendarReader.readValue(endDateCalendar, WorkerValidators.endDateValidator);

        Status status = ComboBoxReader.readValue(statusComboBox, "status",
                WorkerValidators.statusValidator, WorkerParsers.statusParser);

        double x = TextFieldReader.readValue(xTextField, "x",
                WorkerValidators.xValidator, WorkerParsers.doubleParser);

        double y = TextFieldReader.readValue(yTextField, "y",
                WorkerValidators.yValidator, WorkerParsers.doubleParser);

        Coordinates coordinates = new Coordinates(x, y);

        Person person = null;

        if (!noPersonCheckBox.isSelected()) {
            Long height = TextFieldReader.readValue(heightTextField, "height",
                    WorkerValidators.heightValidator, WorkerParsers.longParser);

            Color eyeColor = ComboBoxReader.readValue(eyeColorComboBox, "eye color",
                    WorkerValidators.eyeColorValidator, WorkerParsers.eyeColorParser);

            Country nationality = ComboBoxReader.readValue(nationalityComboBox, "nationality",
                    WorkerValidators.nationalityValidator, WorkerParsers.nationalityParser);

            person = new Person(height, eyeColor, nationality);
        }

        return new Worker(0, name, coordinates, null, salary, startDate, endDate, status, person, ClientRequest.getUser().userName());
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
        headerLabel.setText("Worker's form");
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
        nameTextField = new JTextField();
        Font nameTextFieldFont = this.$$$getFont$$$(null, Font.PLAIN, 16, nameTextField.getFont());
        if (nameTextFieldFont != null) nameTextField.setFont(nameTextFieldFont);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        mainPanel.add(nameTextField, gbc);
        nameLabel = new JLabel();
        Font nameLabelFont = this.$$$getFont$$$(null, Font.PLAIN, 16, nameLabel.getFont());
        if (nameLabelFont != null) nameLabel.setFont(nameLabelFont);
        nameLabel.setText("Name:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 0, 0);
        mainPanel.add(nameLabel, gbc);
        salaryLabel = new JLabel();
        Font salaryLabelFont = this.$$$getFont$$$(null, Font.PLAIN, 16, salaryLabel.getFont());
        if (salaryLabelFont != null) salaryLabel.setFont(salaryLabelFont);
        salaryLabel.setText("Salary:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 0, 0);
        mainPanel.add(salaryLabel, gbc);
        startDateLabel = new JLabel();
        Font startDateLabelFont = this.$$$getFont$$$(null, Font.PLAIN, 16, startDateLabel.getFont());
        if (startDateLabelFont != null) startDateLabel.setFont(startDateLabelFont);
        startDateLabel.setText("Start date:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 0, 0);
        mainPanel.add(startDateLabel, gbc);
        endDateLabel = new JLabel();
        Font endDateLabelFont = this.$$$getFont$$$(null, Font.PLAIN, 16, endDateLabel.getFont());
        if (endDateLabelFont != null) endDateLabel.setFont(endDateLabelFont);
        endDateLabel.setText("End date:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 0, 0);
        mainPanel.add(endDateLabel, gbc);
        statusLabel = new JLabel();
        Font statusLabelFont = this.$$$getFont$$$(null, Font.PLAIN, 16, statusLabel.getFont());
        if (statusLabelFont != null) statusLabel.setFont(statusLabelFont);
        statusLabel.setText("Status:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 0, 0);
        mainPanel.add(statusLabel, gbc);
        heightLabel = new JLabel();
        Font heightLabelFont = this.$$$getFont$$$(null, Font.PLAIN, 16, heightLabel.getFont());
        if (heightLabelFont != null) heightLabel.setFont(heightLabelFont);
        heightLabel.setText("Height:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 0, 0);
        mainPanel.add(heightLabel, gbc);
        eyeColorLabel = new JLabel();
        Font eyeColorLabelFont = this.$$$getFont$$$(null, Font.PLAIN, 16, eyeColorLabel.getFont());
        if (eyeColorLabelFont != null) eyeColorLabel.setFont(eyeColorLabelFont);
        eyeColorLabel.setText("Eye color:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 0, 0);
        mainPanel.add(eyeColorLabel, gbc);
        nationalityLabel = new JLabel();
        Font nationalityLabelFont = this.$$$getFont$$$(null, Font.PLAIN, 16, nationalityLabel.getFont());
        if (nationalityLabelFont != null) nationalityLabel.setFont(nationalityLabelFont);
        nationalityLabel.setText("Nationality:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 0, 0);
        mainPanel.add(nationalityLabel, gbc);
        salaryTextField = new JTextField();
        Font salaryTextFieldFont = this.$$$getFont$$$(null, Font.PLAIN, 16, salaryTextField.getFont());
        if (salaryTextFieldFont != null) salaryTextField.setFont(salaryTextFieldFont);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        mainPanel.add(salaryTextField, gbc);
        heightTextField = new JTextField();
        Font heightTextFieldFont = this.$$$getFont$$$(null, Font.PLAIN, 16, heightTextField.getFont());
        if (heightTextFieldFont != null) heightTextField.setFont(heightTextFieldFont);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        mainPanel.add(heightTextField, gbc);
        Font statusComboBoxFont = this.$$$getFont$$$(null, Font.PLAIN, 16, statusComboBox.getFont());
        if (statusComboBoxFont != null) statusComboBox.setFont(statusComboBoxFont);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        mainPanel.add(statusComboBox, gbc);
        Font eyeColorComboBoxFont = this.$$$getFont$$$(null, Font.PLAIN, 16, eyeColorComboBox.getFont());
        if (eyeColorComboBoxFont != null) eyeColorComboBox.setFont(eyeColorComboBoxFont);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 11;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        mainPanel.add(eyeColorComboBox, gbc);
        Font nationalityComboBoxFont = this.$$$getFont$$$(null, Font.PLAIN, 16, nationalityComboBox.getFont());
        if (nationalityComboBoxFont != null) nationalityComboBox.setFont(nationalityComboBoxFont);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 12;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        mainPanel.add(nationalityComboBox, gbc);
        mainInfoLabel = new JLabel();
        Font mainInfoLabelFont = this.$$$getFont$$$(null, Font.BOLD, 16, mainInfoLabel.getFont());
        if (mainInfoLabelFont != null) mainInfoLabel.setFont(mainInfoLabelFont);
        mainInfoLabel.setText("Main information");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 10, 5, 0);
        mainPanel.add(mainInfoLabel, gbc);
        coordinatesLabel = new JLabel();
        Font coordinatesLabelFont = this.$$$getFont$$$(null, Font.BOLD, 16, coordinatesLabel.getFont());
        if (coordinatesLabelFont != null) coordinatesLabel.setFont(coordinatesLabelFont);
        coordinatesLabel.setText("Coordinates");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 10, 5, 0);
        mainPanel.add(coordinatesLabel, gbc);
        xLabel = new JLabel();
        Font xLabelFont = this.$$$getFont$$$(null, Font.PLAIN, 16, xLabel.getFont());
        if (xLabelFont != null) xLabel.setFont(xLabelFont);
        xLabel.setText("X:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 0, 0);
        mainPanel.add(xLabel, gbc);
        xTextField = new JTextField();
        Font xTextFieldFont = this.$$$getFont$$$(null, Font.PLAIN, 16, xTextField.getFont());
        if (xTextFieldFont != null) xTextField.setFont(xTextFieldFont);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        mainPanel.add(xTextField, gbc);
        yLabel = new JLabel();
        Font yLabelFont = this.$$$getFont$$$(null, Font.PLAIN, 16, yLabel.getFont());
        if (yLabelFont != null) yLabel.setFont(yLabelFont);
        yLabel.setText("Y:");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 10, 0, 0);
        mainPanel.add(yLabel, gbc);
        yTextField = new JTextField();
        Font yTextFieldFont = this.$$$getFont$$$(null, Font.PLAIN, 16, yTextField.getFont());
        if (yTextFieldFont != null) yTextField.setFont(yTextFieldFont);
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        mainPanel.add(yTextField, gbc);
        personPanel = new JPanel();
        personPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        mainPanel.add(personPanel, gbc);
        personLabel = new JLabel();
        Font personLabelFont = this.$$$getFont$$$(null, Font.BOLD, 16, personLabel.getFont());
        if (personLabelFont != null) personLabel.setFont(personLabelFont);
        personLabel.setText("Person");
        personPanel.add(personLabel);
        noPersonCheckBox = new JCheckBox();
        Font noPersonCheckBoxFont = this.$$$getFont$$$(null, Font.PLAIN, 16, noPersonCheckBox.getFont());
        if (noPersonCheckBoxFont != null) noPersonCheckBox.setFont(noPersonCheckBoxFont);
        noPersonCheckBox.setText("no person");
        personPanel.add(noPersonCheckBox);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        mainPanel.add(endDateCalendar, gbc);
        gbc = new GridBagConstraints();
        gbc.gridx = 2;
        gbc.gridy = 3;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        mainPanel.add(startDateCalendar, gbc);
        menuPanel = new JPanel();
        menuPanel.setLayout(new GridBagLayout());
        contentPane.add(menuPanel, BorderLayout.SOUTH);
        saveButton = new JButton();
        Font saveButtonFont = this.$$$getFont$$$(null, Font.PLAIN, 16, saveButton.getFont());
        if (saveButtonFont != null) saveButton.setFont(saveButtonFont);
        saveButton.setText("Save");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20, 10, 10, 10);
        menuPanel.add(saveButton, gbc);
        cancelButton = new JButton();
        Font cancelButtonFont = this.$$$getFont$$$(null, Font.PLAIN, 16, cancelButton.getFont());
        if (cancelButtonFont != null) cancelButton.setFont(cancelButtonFont);
        cancelButton.setText("Cancel");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20, 10, 10, 10);
        menuPanel.add(cancelButton, gbc);
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
        List<String> statusItems = Stream.of(Status.values()).map(Enum::toString).toList();
        statusComboBox = new JComboBox<String>();
        statusItems.forEach(statusComboBox::addItem);

        List<String> eyeColorItems = Stream.of(Color.values()).map(Enum::toString).toList();
        eyeColorComboBox = new JComboBox<String>();
        eyeColorComboBox.addItem("");
        eyeColorItems.forEach(eyeColorComboBox::addItem);

        List<String> nationalityItems = Stream.of(Country.values()).map(Enum::toString).toList();
        nationalityComboBox = new JComboBox<String>();
        nationalityComboBox.addItem("");
        nationalityItems.forEach(nationalityComboBox::addItem);

        endDateCalendar = new Calendar();
        startDateCalendar = new Calendar();
    }
}
