package view;

import model.Carreira;
import model.UsuarioEscolha;
import service.GeminiApiService;
import service.RecomendacaoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

/**
 * Interface Gráfica (JFrame) para o Consultor de Carreira.
 * Usa o RecomendacaoService (local) e o GeminiApiService (API).
 */
public class InterfaceGrafica extends JFrame {

    private JComboBox<String> comboArea;
    private JComboBox<String> comboEspecialidade;
    private JComboBox<String> comboNicho;
    private JButton botaoAlgoritmo;
    private JButton botaoIA;
    private JTextArea areaResultado; // Trocamos a JTable por uma JTextArea

    public InterfaceGrafica() {
        // --- Configuração da Janela ---
        setTitle("Consultor de Carreira Híbrido (Java + IA) - GS");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // --- PAINEL DE CONTROLES (Filtros) ---
        JPanel painelControles = new JPanel();
        painelControles.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));

        painelControles.add(new JLabel("Área:"));
        comboArea = new JComboBox<>(RecomendacaoService.getAreas());
        painelControles.add(comboArea);

        painelControles.add(new JLabel("Especialidade:"));
        comboEspecialidade = new JComboBox<>();
        painelControles.add(comboEspecialidade);

        painelControles.add(new JLabel("Nicho:"));
        comboNicho = new JComboBox<>();
        painelControles.add(comboNicho);

        // Adiciona os botões
        botaoAlgoritmo = new JButton("Gerar (Algoritmo Local)");
        painelControles.add(botaoAlgoritmo);

        botaoIA = new JButton("Perguntar à IA");
        painelControles.add(botaoIA);

        add(painelControles, BorderLayout.NORTH);

        // --- PAINEL DE RESULTADO (TextArea) ---
        // (Usar uma área de texto é mais flexível para mostrar
        // tanto a carreira quanto a resposta da IA)
        areaResultado = new JTextArea("Selecione suas opções e clique em um botão.");
        areaResultado.setEditable(false);
        areaResultado.setFont(new Font("Monospaced", Font.PLAIN, 14));
        areaResultado.setLineWrap(true);
        areaResultado.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(areaResultado);
        add(scrollPane, BorderLayout.CENTER);

        // --- LÓGICA DOS EVENTOS (Listeners) ---

        // Atualiza Especialidades quando a Área muda
        comboArea.addActionListener(e -> atualizarComboEspecialidade());

        // Atualiza Nichos quando a Especialidade muda
        comboEspecialidade.addActionListener(e -> atualizarComboNicho());

        // Ação do Botão 1: Algoritmo Local
        botaoAlgoritmo.addActionListener(e -> executarFiltroLocal());

        // Ação do Botão 2: API do Gemini
        botaoIA.addActionListener(e -> executarFiltroIA());

        // --- INICIALIZAÇÃO ---
        atualizarComboEspecialidade();
        setVisible(true);
    }

    // --- Funções de Atualização dos Combos ---

    private void atualizarComboEspecialidade() {
        String area = (String) comboArea.getSelectedItem();
        comboEspecialidade.removeAllItems();
        for (String s : RecomendacaoService.getEspecialidades(area)) {
            comboEspecialidade.addItem(s);
        }
        // Atualiza o nicho em cascata
        atualizarComboNicho();
    }

    private void atualizarComboNicho() {
        String esp = (String) comboEspecialidade.getSelectedItem();
        comboNicho.removeAllItems();
        if (esp != null) {
            for (String s : RecomendacaoService.getNichos(esp)) {
                comboNicho.addItem(s);
            }
        }
    }

    // --- Funções de Ação dos Botões ---

    /**
     * Ação do Botão 1: Roda o algoritmo local (RecomendacaoService)
     */
    private void executarFiltroLocal() {
        setBotoesAtivados(false); // Desativa botões
        areaResultado.setText("Processando com algoritmo local...");

        UsuarioEscolha escolha = getEscolhaAtual();
        if (escolha == null) return;

        // Chama o algoritmo que NUNCA retorna null
        Carreira c = RecomendacaoService.recomendarMelhor(escolha);

        // Formata a saída para o TextArea
        StringBuilder sb = new StringBuilder();
        sb.append("--- Recomendação (Algoritmo Local) ---\n\n");
        sb.append("Sua escolha: ").append(escolha.getArea()).append(" > ")
                .append(escolha.getEspecialidade()).append(" > ").append(escolha.getNicho()).append("\n\n");

        sb.append("Melhor correspondência encontrada:\n");
        sb.append("Carreira: ").append(c.getNome()).append("\n");
        sb.append("Salário: ").append(c.getSalario()).append("\n\n");

        sb.append("Trilha de Estudos Recomendada:\n");
        sb.append("- Cursos: ").append(Arrays.toString(c.getCursos())).append("\n");
        sb.append("- Hard Skills: ").append(Arrays.toString(c.getHardSkills())).append("\n");
        sb.append("- Soft Skills: ").append(Arrays.toString(c.getSoftSkills())).append("\n");

        areaResultado.setText(sb.toString());
        setBotoesAtivados(true); // Reativa botões
    }

    /**
     * Ação do Botão 2: Roda a API do Gemini
     */
    private void executarFiltroIA() {
        UsuarioEscolha escolha = getEscolhaAtual();
        if (escolha == null) return;

        setBotoesAtivados(false); // Desativa botões
        areaResultado.setText("Conectando com a API do Google Gemini...\n\nIsso pode levar alguns segundos...");

        // ATENÇÃO: A chamada de API bloqueia a tela.
        // O correto seria usar uma SwingWorker (Thread), mas para a GS
        // isso é mais simples e direto.
        String respostaIA = GeminiApiService.perguntarAoGemini(
                escolha.getArea(),
                escolha.getEspecialidade(),
                escolha.getNicho()
        );

        // Formata a saída
        StringBuilder sb = new StringBuilder();
        sb.append("--- Resposta da IA (Google Gemini) ---\n\n");
        sb.append("Sua pergunta: Recomendações para '").append(escolha.getNicho()).append("'\n\n");
        sb.append(respostaIA);

        areaResultado.setText(sb.toString());
        setBotoesAtivados(true); // Reativa botões
    }

    // --- Funções Helper ---

    private UsuarioEscolha getEscolhaAtual() {
        String area = (String) comboArea.getSelectedItem();
        String esp = (String) comboEspecialidade.getSelectedItem();
        String nicho = (String) comboNicho.getSelectedItem();

        if (area == null || esp == null || nicho == null) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione todas as 3 opções.", "Erro", JOptionPane.ERROR_MESSAGE);
            setBotoesAtivados(true);
            return null;
        }
        return new UsuarioEscolha(area, esp, nicho);
    }

    private void setBotoesAtivados(boolean ativado) {
        botaoAlgoritmo.setEnabled(ativado);
        botaoIA.setEnabled(ativado);

        // Muda o cursor para "espera" se estiver desativado
        if (ativado) {
            setCursor(Cursor.getDefaultCursor());
        } else {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        }
    }
}