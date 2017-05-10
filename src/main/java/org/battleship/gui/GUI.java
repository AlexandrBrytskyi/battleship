package org.battleship.gui;


import org.battleship.controller.PlayerController;
import org.battleship.model.bits.BitResult;
import org.battleship.model.boards.BoardSquare;
import org.battleship.model.ships.StraightShip;
import org.battleship.service.UserService;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * linked with {@link PlayerController} in order to manage user input and controller output
 */
public class GUI extends JFrame {

    public static final int BOARD_DIMENSION = 10;
    public static final String BOARD_TOP = "RESPUBLIKA";
    private PlayerController playerController;
    private JPanel mainPanel;
    private JPanel myBoardPanel;
    private JPanel opponentBoardPanel;
    private JTextArea messagesArea;
    private JButton sendMessageButton;
    private java.util.List<MyBoardButton> myBoardButtons;
    private List<MyBoardButton> opponentBoardButtons;
    private JTextField messageField;
    private JPanel chatPanel;
    private AtomicBoolean canBeatMyButton = new AtomicBoolean(false);
    private AtomicBoolean isWaitingForBit = new AtomicBoolean(false);
    private AtomicBoolean isChoosingShips = new AtomicBoolean(false);
    private int neededShipLength = 0;
    private ExecutorService waitingService = Executors.newFixedThreadPool(4);
    private AtomicBoolean selectedShip = new AtomicBoolean(false);
    private List<BoardSquare> selectedSquares = new ArrayList<>();
    private MyBoardButton bittedMyButton = null;


    public GUI() throws HeadlessException {
        super("Best BattleShip Ever :D");
        setSize(1100, 500);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void askedForAddingShips() {
        showDialog("Add Ships now", "Adding Ships", JOptionPane.INFORMATION_MESSAGE);
        addShips();
    }

    private void addShips() {
        for (int i = 0; i < 4; i++) {
            for (int j = 3; j >= i; j--) {
                tryToSelectShip(i + 1, j + 1);
            }
        }
    }

    private void tryToSelectShip(int shipSize, int amountShipsNeeded) {
        appendMessage("Need to choose " + amountShipsNeeded + " with size " + shipSize +
                "\nclick mouse left for horisontal alignment and right for vertical");
        isChoosingShips.set(true);
        neededShipLength = shipSize;
        Future<List<BoardSquare>> selectedSquaresFuture = waitingService.submit(new Callable<List<BoardSquare>>() {
            @Override
            public List<BoardSquare> call() throws Exception {
                while (!selectedShip.get() && selectedSquares != null) {
                    Thread.sleep(500);
                }
                return selectedSquares;
            }
        });
        try {
            List<BoardSquare> res = selectedSquaresFuture.get(1, TimeUnit.DAYS);

            selectedSquares = new ArrayList<>();
            selectedShip.set(false);
            playerController.addShip(new StraightShip(String.valueOf(shipSize), shipSize), res);
            markSquaresAsMyShips(res);
            isChoosingShips.set(false);
        } catch (Throwable e) {
            showDialog("Sorry, have problem: " + e.getMessage(),
                    "Adding Ships error", JOptionPane.INFORMATION_MESSAGE);
            e.printStackTrace();
            tryToSelectShip(shipSize, amountShipsNeeded);
        }
    }

    public void notifiedGameStarts() {

        showDialog("Ok, game is now started", "Come on", JOptionPane.INFORMATION_MESSAGE);

        for (MyBoardButton myBoardButton : myBoardButtons) {
            myBoardButton.setEnabled(false);
        }
        for (MyBoardButton opponentBoardButton : opponentBoardButtons) {
            opponentBoardButton.setEnabled(true);
        }
    }

    public void askedForBit() {
        try {
            appendMessage("Your turn to bitOpponentBoardSquare");
            canBeatMyButton.set(true);
            isWaitingForBit.set(true);
            MyBoardButton bitted = waitingService.submit(new Callable<MyBoardButton>() {
                @Override
                public MyBoardButton call() throws Exception {
                    while (isWaitingForBit.get() && bittedMyButton == null) {
                        Thread.sleep(300);
                    }
                    return bittedMyButton;
                }
            }).get();
            bittedMyButton = null;
            canBeatMyButton.set(false);
            playerController.bitOpponentBoardSquare(bitted.getXVal(), bitted.getYVal());
        } catch (Throwable e) {
            appendMessage("Sorry, have problem: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void markSquaresAsMyShips(List<BoardSquare> boardSquares) {
        for (BoardSquare boardSquare : boardSquares) {
            MyBoardButton button = myBoardButtons.get(myBoardButtons.indexOf(boardSquare));
            button.setAsMyShip();
        }
    }

    public void updateMySquare(BoardSquare mySquare) {
        MyBoardButton button = myBoardButtons.get(myBoardButtons.indexOf(mySquare));
        button.setBitted(mySquare.isCanBit(), mySquare.isUnderShip());
    }

    public void updateOpponentSquare(BoardSquare missedBorderSquare) {
        MyBoardButton button = opponentBoardButtons.get(opponentBoardButtons.indexOf(missedBorderSquare));
        button.setBitted(missedBorderSquare.isCanBit(), missedBorderSquare.isUnderShip());
    }

    public void appendMessage(String mess) {
        messagesArea.append(mess + "\n");
    }

    public void showWinner(boolean won) {
        appendMessage("I am " + (won ? "winner" : "loser"));
        showDialog(won ?
                        "You are winner, Congratulations!!!" : "You are loser, sorry :(",
                "Game ended", JOptionPane.INFORMATION_MESSAGE);
    }

    public UserService getPlayerController() {
        return playerController;
    }

    public void setPlayerController(PlayerController playerController) {
        this.playerController = playerController;
        initComponents();
    }

    private void initComponents() {
        initBoards();
        initChatPanel();
        mainPanel = new JPanel(new GridLayout(1, 3));
        mainPanel.add(myBoardPanel);
        mainPanel.add(opponentBoardPanel);
        mainPanel.add(chatPanel);
        this.add(mainPanel);
        mainPanel.updateUI();
    }

    public void showDialog(String message, String tittle, int type) {
        JOptionPane.showMessageDialog(mainPanel, message, tittle, type);
    }

    private void initChatPanel() {
        chatPanel = new JPanel(new BorderLayout());
        chatPanel.setMinimumSize(new Dimension(300, 400));
        chatPanel.setPreferredSize(new Dimension(300, 400));
        chatPanel.setMaximumSize(new Dimension(300, 400));
        messagesArea = new JTextArea("Hi man\n");
        chatPanel.add(new JScrollPane(messagesArea));
        JPanel messFieldButtonPanel = new JPanel(new GridLayout(2, 1));
        messageField = new JTextField("");
        sendMessageButton = new JButton("Send");
        sendMessageButton.addActionListener(l -> {
            if (!messageField.getText().isEmpty()) {
                playerController.sendMessage(messageField.getText());
                messageField.setText("");
            }
        });
        messFieldButtonPanel.add(messageField);
        messFieldButtonPanel.add(sendMessageButton);
        chatPanel.add(messagesArea, BorderLayout.CENTER);
        chatPanel.add(messFieldButtonPanel, BorderLayout.SOUTH);
    }

    private void initBoards() {
        int dimension = BOARD_DIMENSION + 1;
        myBoardPanel = new JPanel(new GridLayout(dimension, dimension));
        myBoardPanel.setMinimumSize(new Dimension(400, 400));
        myBoardPanel.setPreferredSize(new Dimension(400, 400));
        myBoardPanel.setMaximumSize(new Dimension(400, 400));
        myBoardPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        opponentBoardPanel = new JPanel(new GridLayout(dimension, dimension));
        opponentBoardPanel.setMinimumSize(new Dimension(400, 400));
        opponentBoardPanel.setPreferredSize(new Dimension(400, 400));
        opponentBoardPanel.setMaximumSize(new Dimension(400, 400));
        opponentBoardPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        myBoardButtons = new ArrayList<>();
        opponentBoardButtons = new ArrayList<>();
        for (int i = 0; i < dimension - 1; i++) {
            for (int j = 0; j < dimension - 1; j++) {
                myBoardButtons.add(new MyBoardButton(BOARD_TOP.charAt(j), i + 1));

                MyBoardButton oppButt = new MyBoardButton(BOARD_TOP.charAt(j), i + 1);
                oppButt.setEnabled(false);
                oppButt.addActionListener(e -> {
                    if (isWaitingForBit.get()) {
                        bittedMyButton = oppButt;
                        isWaitingForBit.set(false);
                    }
                });
                opponentBoardButtons.add(oppButt);
            }
        }
        myBoardPanel.add(new JLabel("/"));
        opponentBoardPanel.add(new JLabel("/"));
        for (char c : BOARD_TOP.toCharArray()) {
            myBoardPanel.add(new JLabel(String.valueOf(c)));
            opponentBoardPanel.add(new JLabel(String.valueOf(c)));
        }

        int counter = 0;
        for (int i = 1; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (j == 0) {
                    myBoardPanel.add(new JLabel(String.valueOf(i)));
                    opponentBoardPanel.add(new JLabel(String.valueOf(i)));
                } else {
                    myBoardPanel.add(myBoardButtons.get(counter));
                    opponentBoardPanel.add(opponentBoardButtons.get(counter++));
                }
            }
        }
    }

    public class MyBoardButton extends JButton {
        private char xVal;
        private int yVal;
        private Color currentColor;
        private Color prevColor;
        private Lock mouseOverLock = new ReentrantLock();
        private Lock mouseExitedLock = new ReentrantLock();

        public MyBoardButton(char x, int y) {
            super();
            this.xVal = x;
            this.yVal = y;


            addMouseListener(new MouseAdapter() {

                private Set<MyBoardButton> buttonsToChangeColour = new HashSet<>();
                List<MyBoardButton> availableDown;
                List<MyBoardButton> availableRight;

                @Override
                public void mouseClicked(MouseEvent e) {
                    if (isChoosingShips.get()) {
                        super.mouseClicked(e);
                        if (!selectedShip.get()) {
//                            lkm
                            if (e.getButton() == 1) {
                                selectedSquares = copyFrom(availableRight);
                                availableRight = null;
                                availableDown = null;
                            } else {
//                               others
                                selectedSquares = copyFrom(availableDown);
                                availableRight = null;
                                availableDown = null;
                            }
                            mouseExited(e);
                            selectedShip.set(true);
                        }
                    }
                }


                private List<BoardSquare> copyFrom(List<MyBoardButton> availableLeft) {
                    return availableLeft.stream().map(b -> new BoardSquare(b.getYVal(), b.getXVal())).collect(Collectors.toList());
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    try {
                        mouseOverLock.lock();
                        super.mouseEntered(e);
                        if (isEnabled() && isChoosingShips.get()) {
                            fillWithGreenPossibleVars();
                        }
                    } finally {
                        mouseOverLock.unlock();
                    }

                }

                private void fillWithGreenPossibleVars() {

                    availableDown = getAvailableDown();
                    availableRight = getAvailableRight();

                    buttonsToChangeColour.addAll(availableDown);
                    buttonsToChangeColour.addAll(availableRight);

                    for (MyBoardButton button : buttonsToChangeColour) {
                        button.setBackground(Color.GREEN);
                    }
                    buttonsToChangeColour.forEach((b) -> setBackground(Color.GREEN));

                }

                private List<MyBoardButton> getAvailableRight() {
                    List<MyBoardButton> res = new ArrayList<>();
                    char myChar = getXVal();
                    int myY = getYVal();
                    int beginIndex = BOARD_TOP.indexOf(myChar);
                    int end = beginIndex + neededShipLength;
                    char[] chars = BOARD_TOP.substring(beginIndex, end <= BOARD_TOP.length() ? end : BOARD_TOP.length()).toCharArray();
                    for (int i = 0; i < chars.length; i++) {
                        res.add(myBoardButtons.get(myBoardButtons.indexOf(new MyBoardButton(chars[i], myY))));
                    }
                    return res;
                }

                private List<MyBoardButton> getAvailableDown() {
                    List<MyBoardButton> res = new ArrayList<>();
                    char myChar = getXVal();
                    int myY = getYVal();
                    for (int i = myY; i < myY + neededShipLength; i++) {
                        if (i <= BOARD_DIMENSION)
                            res.add(myBoardButtons.get(myBoardButtons.indexOf(new MyBoardButton(myChar, i))));
                    }
                    return res;
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    try {
                        mouseExitedLock.lock();
                        if (isChoosingShips.get() || !buttonsToChangeColour.isEmpty()) {
                            buttonsToChangeColour.forEach(MyBoardButton::restoreColor);
                            buttonsToChangeColour.clear();
                        }
                        super.mouseExited(e);
                    } finally {
                        mouseExitedLock.unlock();
                    }
                }

            });
        }

        public void setBitted(boolean canBit, boolean underShip) {
            if (canBit) return;
            if (underShip) {
                setBackground(Color.RED);
            } else {
                setBackground(Color.BLUE);
            }
            setEnabled(false);
            setOpaque(true);
            mainPanel.updateUI();
        }

        public void setAsMyShip() {
            setBackground(Color.ORANGE);
            setEnabled(false);
            setOpaque(true);
            mainPanel.updateUI();
        }

        @Override
        public void setBackground(Color bg) {
            if (currentColor != null && currentColor.equals(bg)) return;
            prevColor = currentColor;
            currentColor = bg;
            super.setBackground(bg);
        }

        private void restoreColor() {
            currentColor = prevColor;
            prevColor = null;
            super.setBackground(currentColor);
        }

        public char getXVal() {
            return xVal;
        }

        public void setXVal(char x) {
            this.xVal = x;
        }

        public int getYVal() {
            return yVal;
        }

        public void setYVal(int y) {
            this.yVal = y;
        }

        @Override
        public boolean equals(Object o) {

            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MyBoardButton button = (MyBoardButton) o;

            if (xVal != button.xVal) return false;
            return yVal == button.yVal;
        }

        @Override
        public int hashCode() {
            int result = (int) xVal;
            result = 31 * result + yVal;
            return result;
        }

        @Override
        public String toString() {
            return "MyBoardButton{" +
                    "xVal=" + xVal +
                    ", yVal=" + yVal +
                    "} ";
        }
    }


}
