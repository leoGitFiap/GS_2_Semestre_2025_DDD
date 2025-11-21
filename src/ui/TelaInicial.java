package ui;

import javax.swing.*;
import java.awt.*;

public class TelaInicial extends JFrame {
    public TelaInicial() {
        setTitle("Consultor de Carreira IA - PRO");
        setSize(700, 220);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("CONSULTOR DE CARREIRA IA", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titulo.setForeground(new Color(34, 34, 34));

        JLabel subtitulo = new JLabel("Descubra carreiras, salários médios e cursos recomendados", SwingConstants.CENTER);
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitulo.setForeground(new Color(80, 80, 80));

        JPanel center = new JPanel(new BorderLayout());
        center.setBackground(new Color(250, 250, 250));
        center.add(subtitulo, BorderLayout.CENTER);

        JPanel south = new JPanel();
        south.setBackground(new Color(250, 250, 250));

        JButton iniciar = new JButton("Começar");
        iniciar.setPreferredSize(new Dimension(160, 40));
        iniciar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        iniciar.setBackground(new Color(52, 152, 219));
        iniciar.setForeground(Color.white);
        iniciar.addActionListener(e -> {
            dispose();
            new TelaSelecao();
        });
        south.add(iniciar);

        add(titulo, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(south, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }
}