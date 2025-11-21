package ui;

import javax.swing.SwingUtilities;

/**
 * Ponto de entrada do aplicativo.
 * Inicia a primeira tela da interface (TelaInicial).
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TelaInicial();
            }
        });
    }
}