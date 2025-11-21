package ui;

import javax.swing.*;
import java.awt.*;
import model.UsuarioEscolha;
import model.Carreira;
import service.GeminiApiService; // Importa o serviço da IA
import service.RecomendacaoService;

public class TelaSelecao extends JFrame {
    private JComboBox<String> cbArea, cbEspecialidade, cbNicho;
    private JButton botaoAlgoritmo; // Botão 1
    private JButton botaoIA;        // Botão 2 (NOVO)
    private boolean updating = false; // evita reentrancia

    public TelaSelecao() {
        setTitle("Seleção de Perfil - Consultor IA");
        setSize(760, 360);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JPanel top = new JPanel(new GridLayout(2, 1));
        JLabel instr = new JLabel("Escolha sua Grande Área, Especialidade e Nicho", SwingConstants.CENTER);
        instr.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        top.add(instr);
        JLabel sub = new JLabel("Use o Algoritmo Local  ou pergunte à IA do Google Gemini.", SwingConstants.CENTER);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        top.add(sub);

        JPanel center = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8,8,8,8);
        c.fill = GridBagConstraints.HORIZONTAL;

        cbArea = new JComboBox<>(RecomendacaoService.getAreas());
        cbEspecialidade = new JComboBox<>();
        cbNicho = new JComboBox<>();

        // --- listeners dos Combos
        cbArea.addActionListener(e -> {
            if (updating) return;
            try {
                updating = true;
                atualizarEspecialidades();
                cbNicho.setModel(new DefaultComboBoxModel<>(new String[0]));
            } finally {
                updating = false;
            }
        });

        cbEspecialidade.addActionListener(e -> {
            if (updating) return;
            try {
                updating = true;
                atualizarNichos();
            } finally {
                updating = false;
            }
        });

        // --- inicialização
        if (cbArea.getItemCount() > 0) {
            cbArea.setSelectedIndex(0);
            atualizarEspecialidades();
            if (cbEspecialidade.getItemCount() > 0) {
                cbEspecialidade.setSelectedIndex(0);
                atualizarNichos();
            }
        }

        // --- layout dos combos
        c.gridx = 0; c.gridy = 0; center.add(new JLabel("Grande Área:"), c);
        c.gridx = 1; center.add(cbArea, c);
        c.gridx = 0; c.gridy = 1; center.add(new JLabel("Especialidade:"), c);
        c.gridx = 1; center.add(cbEspecialidade, c);
        c.gridx = 0; c.gridy = 2; center.add(new JLabel("Nicho:"), c);
        c.gridx = 1; center.add(cbNicho, c);

        // --- painel de botões
        botaoAlgoritmo = new JButton("Gerar (Algoritmo Local)");
        botaoAlgoritmo.addActionListener(e -> gerarResultadoLocal()); // Chama o método local

        botaoIA = new JButton("Perguntar à IA");
        botaoIA.addActionListener(e -> gerarResultadoIA()); // Chama o método da IA

        JButton voltar = new JButton("Voltar");
        voltar.addActionListener(e -> { dispose(); new TelaInicial(); });

        JPanel botoes = new JPanel();
        botoes.add(botaoAlgoritmo);
        botoes.add(botaoIA);
        botoes.add(voltar);

        add(top, BorderLayout.NORTH);
        add(center, BorderLayout.CENTER);
        add(botoes, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // --- funções de atualização dos combos

    private void atualizarEspecialidades() {
        String area = safe((String) cbArea.getSelectedItem());
        String[] especialidades = RecomendacaoService.getEspecialidades(area);
        if (especialidades == null) especialidades = new String[0];

        String prev = (String) cbEspecialidade.getSelectedItem();
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(especialidades);
        cbEspecialidade.setModel(model);

        if (prev != null) {
            for (int i = 0; i < model.getSize(); i++) {
                if (prev.equals(model.getElementAt(i))) {
                    cbEspecialidade.setSelectedIndex(i);
                    return;
                }
            }
        }
        if (model.getSize() > 0) cbEspecialidade.setSelectedIndex(0);
        else cbEspecialidade.setSelectedIndex(-1);
    }

    private void atualizarNichos() {
        String esp = safe((String) cbEspecialidade.getSelectedItem());
        if (esp == null || esp.isEmpty()) {
            cbNicho.setModel(new DefaultComboBoxModel<>(new String[0]));
            return;
        }
        String[] nichos = RecomendacaoService.getNichos(esp);
        if (nichos == null) nichos = new String[0];
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(nichos);
        cbNicho.setModel(model);
        if (model.getSize() > 0) cbNicho.setSelectedIndex(0);
        else cbNicho.setSelectedIndex(-1);
    }

    // --- funções de geração de resultado ---

    /**
     * ação do botão 1: Roda o algoritmo local (RecomendacaoService)
     */
    private void gerarResultadoLocal() {
        setBotoesAtivados(false); // desativa botões

        UsuarioEscolha u = getEscolhaAtual();
        if (u == null) {
            setBotoesAtivados(true);
            return; // vvalidação falhou
        }

        Carreira rec = RecomendacaoService.recomendar(u);

        if (rec == null) {
            rec = RecomendacaoService.recomendarMelhor(u);
        }

        if (rec == null) {
            JOptionPane.showMessageDialog(this,
                    "Não foram encontradas carreiras coerentes no banco.",
                    "Sem sugestão", JOptionPane.INFORMATION_MESSAGE);
            setBotoesAtivados(true);
            return;
        }

        if (!rec.getArea().equalsIgnoreCase(u.getArea())) {
            int escolha = JOptionPane.showConfirmDialog(this,
                    "A sugestão (" + rec.getNome() + ") pertence a outra área: " + rec.getArea() +
                            "\nDeseja visualizar mesmo assim?",
                    "Sugestão fora da área", JOptionPane.YES_NO_OPTION);
            if (escolha != JOptionPane.YES_OPTION) {
                setBotoesAtivados(true);
                return;
            }
        }

        // ok: abrir resultado
        dispose();
        new TelaResultado(rec, u); // Abre a sua tela de resultado
    }

    /**
     * ação do botão 2: Roda a API do Gemini
     */
    private void gerarResultadoIA() {
        setBotoesAtivados(false); // desativa botões

        UsuarioEscolha u = getEscolhaAtual();
        if (u == null) {
            setBotoesAtivados(true);
            return; // validação falhou
        }

        // mostra um "Carregando..."
        JDialog popupCarregando = new JDialog(this, "Aguarde...", true);
        popupCarregando.add(new JLabel("Conectando com a IA do Google..."));
        popupCarregando.pack();
        popupCarregando.setLocationRelativeTo(this);

        // atenção: A chamada de API bloqueia a tela.
        String respostaIA = GeminiApiService.perguntarAoGemini(
                u.getArea(),
                u.getEspecialidade(),
                u.getNicho()
        );

        // mostra a resposta em um Popup grande (JTextArea)
        JTextArea textArea = new JTextArea(15, 50);
        textArea.setText(respostaIA);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        JOptionPane.showMessageDialog(this, scrollPane, "Resposta da IA", JOptionPane.INFORMATION_MESSAGE);

        setBotoesAtivados(true); // reativa botões
    }



    /**
     * Helper que lê os 3 combos e já faz a validação
     */
    private UsuarioEscolha getEscolhaAtual() {
        String area = safe((String) cbArea.getSelectedItem());
        String esp  = safe((String) cbEspecialidade.getSelectedItem());
        String nicho = safe((String) cbNicho.getSelectedItem());

        // valida entradas obrigatorias
        if (area == null || area.isEmpty() || esp == null || esp.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Selecione pelo menos uma Grande Área e uma Especialidade.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        if (nicho == null || nicho.isEmpty()) {
            nicho = "Geral"; // Define um valor padrão se o nicho estiver vazio
        }

        return new UsuarioEscolha(area, esp, nicho);
    }

    /**
     * Helper para desativar/reativar botões durante o processamento
     */
    private void setBotoesAtivados(boolean ativado) {
        botaoAlgoritmo.setEnabled(ativado);
        botaoIA.setEnabled(ativado);

        if (ativado) {
            setCursor(Cursor.getDefaultCursor());
        } else {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        }
    }

    private String safe(String s) {
        if (s == null) return null;
        s = s.trim();
        return s.isEmpty() ? null : s;
    }
}