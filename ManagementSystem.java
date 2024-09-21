import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;

public class ManagementSystem {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Management System");
        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        JButton showInfoButton = new JButton("Show Information");
        showInfoButton.setBounds(50, 50, 150, 30);
        frame.add(showInfoButton);

        JButton addPersonButton = new JButton("Add Person");
        addPersonButton.setBounds(200, 50, 150, 30);
        frame.add(addPersonButton);

        showInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showInformationFrame();
            }
        });

        addPersonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddPersonDialog(frame);
            }
        });

        frame.setVisible(true);
    }

    private static void showAddPersonDialog(JFrame parentFrame) {
        JDialog dialog = new JDialog(parentFrame, "Add Person", true);
        dialog.setSize(400, 400);
        dialog.setLayout(null);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(parentFrame);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(20, 20, 100, 30);
        JTextField nameField = new JTextField();
        nameField.setBounds(150, 20, 200, 30);

        JLabel fatherNameLabel = new JLabel("Father's Name:");
        fatherNameLabel.setBounds(20, 60, 100, 30);
        JTextField fatherNameField = new JTextField();
        fatherNameField.setBounds(150, 60, 200, 30);

        JLabel bloodGroupLabel = new JLabel("Blood Group:");
        bloodGroupLabel.setBounds(20, 100, 100, 30);
        String[] bloodGroups = {"O+", "O-", "A+", "A-", "B+", "B-", "AB+", "AB-"};
        JComboBox<String> bloodGroupCombo = new JComboBox<>(bloodGroups);
        bloodGroupCombo.setBounds(150, 100, 200, 30);

        JLabel phoneLabel = new JLabel("Phone Number:");
        phoneLabel.setBounds(20, 140, 100, 30);
        JTextField phoneField = new JTextField();
        phoneField.setBounds(150, 140, 200, 30);

        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setBounds(20, 180, 100, 30);
        JTextField addressField = new JTextField();
        addressField.setBounds(150, 180, 200, 30);

        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setBounds(20, 220, 100, 30);
        String[] genders = {"Male", "Female", "Other"};
        JComboBox<String> genderCombo = new JComboBox<>(genders);
        genderCombo.setBounds(150, 220, 200, 30);

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(150, 270, 100, 30);
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String fatherName = fatherNameField.getText();
                String bloodGroup = (String) bloodGroupCombo.getSelectedItem();
                String phone = phoneField.getText();
                String address = addressField.getText();
                String gender = (String) genderCombo.getSelectedItem();

                if (savePersonToFile(name, fatherName, bloodGroup, phone, address, gender)) {
                    JOptionPane.showMessageDialog(dialog, "Person added and saved to file.");
                } else {
                    JOptionPane.showMessageDialog(dialog, "Error saving to file.");
                }
                dialog.dispose();
            }
        });

        dialog.add(nameLabel);
        dialog.add(nameField);
        dialog.add(fatherNameLabel);
        dialog.add(fatherNameField);
        dialog.add(bloodGroupLabel);
        dialog.add(bloodGroupCombo);
        dialog.add(phoneLabel);
        dialog.add(phoneField);
        dialog.add(addressLabel);
        dialog.add(addressField);
        dialog.add(genderLabel);
        dialog.add(genderCombo);
        dialog.add(submitButton);
        dialog.setVisible(true);
    }

    private static boolean savePersonToFile(String name, String fatherName, String bloodGroup, String phone, String address, String gender) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("persons.txt", true))) {
            writer.write("Name: " + name + "\n");
            writer.write("Father's Name: " + fatherName + "\n");
            writer.write("Blood Group: " + bloodGroup + "\n");
            writer.write("Phone Number: " + phone + "\n");
            writer.write("Address: " + address + "\n");
            writer.write("Gender: " + gender + "\n");
            writer.write("-------------------------------\n");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void showInformationFrame() {
        JFrame infoFrame = new JFrame("Information");
        infoFrame.setSize(900, 500);
        infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        infoFrame.setLocationRelativeTo(null);
        infoFrame.setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel searchLabel = new JLabel("Search by Keyword:");
        searchLabel.setBounds(10, 10, 150, 30);
        JTextField searchField = new JTextField();
        searchField.setBounds(160, 10, 200, 30);
        JButton searchButton = new JButton("Search");
        searchButton.setBounds(370, 10, 80, 30);
        panel.add(searchLabel);
        panel.add(searchField);
        panel.add(searchButton);

        String[] columnNames = {"Name", "Father's Name", "Blood Group", "Phone Number", "Address", "Gender"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        table.setFont(new Font("Arial", Font.BOLD, 18));
        table.getTableHeader().setFont(new Font("Arial", Font.PLAIN, 20));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 50, 850, 300);
        panel.add(scrollPane);

        loadTableData(tableModel);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBounds(400, 420, 100, 30);
        panel.add(deleteButton);

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String phoneNumber = JOptionPane.showInputDialog(infoFrame, "Enter Phone Number to delete:");

                if (phoneNumber != null && !phoneNumber.isEmpty()) {
                    if (deletePersonFromFile(phoneNumber)) {
                        JOptionPane.showMessageDialog(infoFrame, "Person deleted successfully.");
                        tableModel.setRowCount(0);
                        loadTableData(tableModel);
                    } else {
                        JOptionPane.showMessageDialog(infoFrame, "Error deleting person.");
                    }
                } else {
                    JOptionPane.showMessageDialog(infoFrame, "Phone number cannot be empty.");
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch(tableModel, searchField.getText());
            }
        });

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performSearch(tableModel, searchField.getText());
                }
            }
        });

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        String name = (String) table.getValueAt(selectedRow, 0);
                        String fatherName = (String) table.getValueAt(selectedRow, 1);
                        String bloodGroup = (String) table.getValueAt(selectedRow, 2);
                        String phone = (String) table.getValueAt(selectedRow, 3);
                        String address = (String) table.getValueAt(selectedRow, 4);
                        String gender = (String) table.getValueAt(selectedRow, 5);

                        String details = "Name: " + name + "\n"
                                + "Father's Name: " + fatherName + "\n"
                                + "Blood Group: " + bloodGroup + "\n"
                                + "Phone Number: " + phone + "\n"
                                + "Address: " + address + "\n"
                                + "Gender: " + gender;

                       // Define the font for message dialogs
Font font = new Font("Arial", Font.BOLD, 16);

// Set the font for message dialogs
UIManager.put("OptionPane.messageFont", font);

// Show a message dialog with specified details
JOptionPane.showMessageDialog(infoFrame, details, "Person Details", JOptionPane.PLAIN_MESSAGE);

                    }
                }
            }
        });

        infoFrame.add(panel);
        infoFrame.setVisible(true);
    }

    private static void loadTableData(DefaultTableModel tableModel) {
        try (BufferedReader reader = new BufferedReader(new FileReader("persons.txt"))) {
            String line;
            String[] data = new String[6];

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Name: ")) {
                    data[0] = line.substring(6);
                } else if (line.startsWith("Father's Name: ")) {
                    data[1] = line.substring(15);
                } else if (line.startsWith("Blood Group: ")) {
                    data[2] = line.substring(13);
                } else if (line.startsWith("Phone Number: ")) {
                    data[3] = line.substring(14);
                } else if (line.startsWith("Address: ")) {
                    data[4] = line.substring(9);
                } else if (line.startsWith("Gender: ")) {
                    data[5] = line.substring(8);
                } else if (line.equals("-------------------------------")) {
                    tableModel.addRow(new Object[]{data[0], data[1], data[2], data[3], data[4], data[5]});
                    data = new String[6];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void performSearch(DefaultTableModel tableModel, String keyword) {
        tableModel.setRowCount(0);
        String lowerCaseKeyword = keyword.toLowerCase(); // Convert the search keyword to lowercase
        try (BufferedReader reader = new BufferedReader(new FileReader("persons.txt"))) {
            String line;
            String[] data = new String[6];
            boolean matchFound = false;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Name: ")) {
                    data[0] = line.substring(6);
                    if (data[0].toLowerCase().contains(lowerCaseKeyword)) { // Compare lowercase values
                        matchFound = true;
                    }
                } else if (line.startsWith("Father's Name: ")) {
                    data[1] = line.substring(15);
                    if (data[1].toLowerCase().contains(lowerCaseKeyword)) { // Compare lowercase values
                        matchFound = true;
                    }
                } else if (line.startsWith("Blood Group: ")) {
                    data[2] = line.substring(13);
                    if (data[2].toLowerCase().contains(lowerCaseKeyword)) { // Compare lowercase values
                        matchFound = true;
                    }
                } else if (line.startsWith("Phone Number: ")) {
                    data[3] = line.substring(14);
                } else if (line.startsWith("Address: ")) {
                    data[4] = line.substring(9);
                } else if (line.startsWith("Gender: ")) {
                    data[5] = line.substring(8);
                } else if (line.equals("-------------------------------")) {
                    if (matchFound) {
                        tableModel.addRow(new Object[]{data[0], data[1], data[2], data[3], data[4], data[5]});
                        matchFound = false;
                    }
                    data = new String[6];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean deletePersonFromFile(String phoneNumber) {
        File inputFile = new File("persons.txt");
        File tempFile = new File("persons_temp.txt");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            boolean personFound = false;
            boolean deleteSection = false;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Phone Number: " + phoneNumber)) {
                    personFound = true;
                    deleteSection = true;
                }

                if (deleteSection) {
                    if (line.equals("-------------------------------")) {
                        deleteSection = false;
                    }
                } else {
                    writer.write(line + System.getProperty("line.separator"));
                }
            }

            if (!personFound) {
                tempFile.delete();
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        if (!inputFile.delete()) {
            return false;
        }

        return tempFile.renameTo(inputFile);
    }
}
