package org.battleship.gui;


import org.battleship.AppBuilder;
import org.battleship.GameMode;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
/**
 * form to select {@link org.battleship.GameMode}*/
public class StartGuiPage extends JFrame {

    private JPanel mainPanel;
    private JComboBox<GameMode> typeComboBox;
    private JButton submit = new JButton();
    private JPanel addressPanel;

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

        typeComboBox = new JComboBox<>(GameMode.values());
        submit = new JButton("Submit");
        submit.addActionListener(l -> {
            switch (((GameMode) typeComboBox.getSelectedItem())) {
                case BOT_BOT: {
                    AppBuilder.runBotBot(
                            JOptionPane.showConfirmDialog(mainPanel, "Show bot gui?", "Bots board",
                                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) ==
                                    JOptionPane.YES_OPTION ? true : false);
                    StartGuiPage.this.dispose();
                    break;
                }
                case USER_BOT: {
                    AppBuilder.runManBot(
                            JOptionPane.showConfirmDialog(mainPanel, "Show bot gui?", "Bots board",
                                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) ==
                                    JOptionPane.YES_OPTION ? true : false,
                            JOptionPane.showConfirmDialog(mainPanel, "Do you want to create ships?", "Ships generation",
                                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) ==
                                    JOptionPane.NO_OPTION ? true : false);
                    StartGuiPage.this.dispose();
                    break;
                }
                case USER_USER: {
                    AppBuilder.runManMan(
                            JOptionPane.showConfirmDialog(mainPanel, "Do you want to create ships?", "Ships generation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) ==
                                    JOptionPane.NO_OPTION ? true : false);
                    StartGuiPage.this.dispose();
                    break;
                }
                case USER_USER_TCP_IP: {
                    try {
                        boolean isServer = JOptionPane.showConfirmDialog(mainPanel, "Are you main", "Game mode",
                                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) ==
                                JOptionPane.YES_OPTION ? true : false;
                        if (isServer) {
                            AppBuilder.runServer(JOptionPane.showConfirmDialog(mainPanel, "Do you want to create ships?", "Ships generation",
                                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) ==
                                    JOptionPane.NO_OPTION ? true : false);
                        } else {
                            String host = JOptionPane.showInputDialog("Enter host", "localhost");
                            AppBuilder.runClient(host, JOptionPane.showConfirmDialog(mainPanel, "Do you want to create ships?", "Ships generation",
                                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) ==
                                    JOptionPane.NO_OPTION ? true : false);
                        }
                        StartGuiPage.this.dispose();
                    } catch (IOException e) {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(mainPanel, e.getMessage());
                    }
                    break;
                }
            }
        });

        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(typeComboBox, BorderLayout.NORTH);
        mainPanel.add(submit, BorderLayout.SOUTH);
    }


    public static void main(String[] args) {
        new StartGuiPage();
    }
}
