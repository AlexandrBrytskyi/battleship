package org.battleship.gui;


import org.battleship.AppBuilder;
import org.battleship.GameMode;

import javax.swing.*;
import java.awt.*;

public class StartGuiPage extends JFrame {

    private JPanel mainPanel;
    private JComboBox<GameMode> typeComboBox;
    private JButton submit = new JButton();
    private JPanel addressPanel;
    private JCheckBox connectCheckBox;
    private JTextField host;
    private JTextField port;
    private boolean isShowingAddress = false;

    public StartGuiPage() throws HeadlessException {
        super("Select game type");
        setSize(new Dimension(200, 150));
        setResizable(false);
        initMainPanel();
        add(mainPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void initMainPanel() {

        initAddressPanel();

        typeComboBox = new JComboBox<>(GameMode.values());
        typeComboBox.addActionListener(l -> {
            if (typeComboBox.getSelectedItem().equals(GameMode.USER_USER_TCP_IP)) {
                mainPanel.add(addressPanel, BorderLayout.CENTER);
                isShowingAddress = true;
                submit.setText("Create/Connect");
            } else {
                if (isShowingAddress) {
                    mainPanel.remove(addressPanel);
                    isShowingAddress = false;
                }
                submit.setText("Submit");
            }
        });
        submit = new JButton("Submit");
        submit.addActionListener(l -> {
            switch (((GameMode) typeComboBox.getSelectedItem())) {
                case BOT_BOT: {
                    break;
                }
                case USER_BOT: {
                    break;
                }
                case USER_USER: {
                    AppBuilder.runManMan();
                    StartGuiPage.this.dispose();
                    break;
                }
                case USER_USER_TCP_IP: {
                    break;
                }
            }
        });

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(typeComboBox, BorderLayout.NORTH);
        mainPanel.add(submit, BorderLayout.SOUTH);
    }

    private void initAddressPanel() {
        addressPanel = new JPanel(new GridLayout(2, 1));
        JPanel labelPanel = new JPanel(new BorderLayout());
        labelPanel.add(new JLabel("Connect"), BorderLayout.EAST);
        labelPanel.add(new JLabel("Host"), BorderLayout.CENTER);
        labelPanel.add(new JLabel("Port"), BorderLayout.WEST);
        addressPanel.add(labelPanel);
        JPanel fieldsPanel = new JPanel(new BorderLayout());
        connectCheckBox = new JCheckBox();
        connectCheckBox.setSelected(false);
        connectCheckBox.addActionListener(l -> {
            if (connectCheckBox.isSelected()) {
                host.setEditable(true);
                port.setEditable(true);
            } else {
                host.setEditable(false);
                port.setEditable(false);
            }
        });
        host = new JTextField("localhost");
        port = new JTextField("9999");
        fieldsPanel.add(connectCheckBox, BorderLayout.EAST);
        fieldsPanel.add(host, BorderLayout.CENTER);
        fieldsPanel.add(port, BorderLayout.WEST);
        addressPanel.add(fieldsPanel);
    }

    public static void main(String[] args) {
        new StartGuiPage();
    }
}
