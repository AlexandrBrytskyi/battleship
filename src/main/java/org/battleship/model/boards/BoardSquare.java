package org.battleship.model.boards;


public class BoardSquare {

    private int yPosition;
    private char xPosition;

    public BoardSquare(int yPosition, char xPosition) {
        this.yPosition = yPosition;
        this.xPosition = xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    public char getxPosition() {
        return xPosition;
    }

    public void setxPosition(char xPosition) {
        this.xPosition = xPosition;
    }

    @Override
    public String toString() {
        return "BoardSquare{" +
                "yPosition=" + yPosition +
                ", xPosition='" + xPosition + '\'' +
                '}';
    }
}
