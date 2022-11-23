package pkgMultiThread;

import java.awt.*;
import java.util.ArrayList;

public class Acomodador extends Thread {

    private final boolean[][] gameBoard;
    private final ArrayList<Point> celulasobrevivientes;

    public Acomodador(boolean[][] gameBoard,ArrayList<Point> celulasobrevivientes){
        this.gameBoard = gameBoard;
        this.celulasobrevivientes = celulasobrevivientes;
    }

    @Override
    public void run(){
        this.generarPosiciones();
    }

    public void generarPosiciones(){
        for (int i = 1; i < gameBoard.length - 1; i++) {
            for (int j = 1; j < gameBoard[0].length - 1; j++) {
                int surrounding = 0;   //Organismos

                //PREGUNTO EN TODAS LAS POSIBLES DIRECCIONES
                if (gameBoard[i - 1][j - 1]) {
                    surrounding++;
                }
                if (gameBoard[i - 1][j]) {
                    surrounding++;
                }
                if (gameBoard[i - 1][j + 1]) {
                    surrounding++;
                }
                if (gameBoard[i][j - 1]) {
                    surrounding++;
                }
                if (gameBoard[i][j + 1]) {
                    surrounding++;
                }
                if (gameBoard[i + 1][j - 1]) {
                    surrounding++;
                }
                if (gameBoard[i + 1][j]) {
                    surrounding++;
                }
                if (gameBoard[i + 1][j + 1]) {
                    surrounding++;
                }
                if (gameBoard[i][j]) {
                    // Celula esta viva, Pudiendo ser 2 o 3
                    if ((surrounding == 2) || (surrounding == 3)) {
                        //agrego un nuevo punto al Array en tal posicion
                        celulasobrevivientes.add(new Point(i - 1, j - 1));
                    }
                } else {
                    // Celula esta muerta, si sucede que el valor es 3
                    if (surrounding == 3) {
                        celulasobrevivientes.add(new Point(i - 1, j - 1));
                    }
                }
            }
        }

    }
}
