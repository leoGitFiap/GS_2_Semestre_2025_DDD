package ui;

import javax.swing.*;
import java.awt.*;
import model.Carreira;
import model.UsuarioEscolha;

public class TelaResultado extends JFrame {
    public TelaResultado(Carreira carreira, UsuarioEscolha u) {
        setTitle("Resultado - Consultor IA");
        setSize(820, 520);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(250, 250, 250));
        JLabel titulo = new JLabel(carreira.getNome(), SwingConstants.LEFT);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        header.add(titulo, BorderLayout.NORTH);

        JLabel subt = new JLabel(
                "Área: " + carreira.getArea() +
                "  •  Especialidade: " + carreira.getEspecialidade() +
                "  •  Nicho: " + carreira.getNicho()
        );
        subt.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subt.setBorder(BorderFactory.createEmptyBorder(0, 12, 12, 12));
        header.add(subt, BorderLayout.SOUTH);

        JPanel center = new JPanel(new GridLayout(1, 2, 10, 10));
        center.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        JPanel left = new JPanel(new BorderLayout(6, 6));
        left.setBackground(new Color(245, 245, 245));
        JLabel sal = new JLabel("Salário Médio: " + carreira.getSalario());
        sal.setFont(new Font("Segoe UI", Font.BOLD, 18));
        left.add(sal, BorderLayout.NORTH);

        JTextArea cursos = new JTextArea("Cursos recomendados:\n" + String.join("\n", carreira.getCursos()));
        cursos.setEditable(false);
        cursos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        left.add(new JScrollPane(cursos), BorderLayout.CENTER);

        JPanel right = new JPanel(new GridLayout(2, 1, 6, 6));
        right.setBackground(new Color(255, 255, 255));
        JTextArea hard = new JTextArea("Hard Skills:\n" + String.join("\n", carreira.getHardSkills()));
        hard.setEditable(false);
        hard.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JTextArea soft = new JTextArea("Soft Skills:\n" + String.join("\n", carreira.getSoftSkills()));
        soft.setEditable(false);
        soft.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        right.add(new JScrollPane(hard));
        right.add(new JScrollPane(soft));

        center.add(left);
        center.add(right);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(255, 255, 255));
        JLabel recomend = new JLabel("Sugestão: comece pelos cursos listados e pratique as hard skills com projetos pequenos.");
        recomend.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        footer.add(recomend, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.setBackground(new Color(255, 255, 255));
        JButton voltar = new JButton("Voltar");
        voltar.addActionListener(e -> { dispose(); new TelaSelecao(); });
        bottom.add(voltar);

        add(header, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);
        add(bottom, BorderLayout.PAGE_END);

        setLocationRelativeTo(null);
        setVisible(true);
    }
}