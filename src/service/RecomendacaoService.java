package service;

import model.Carreira;
import model.UsuarioEscolha;
import java.util.*;

/*
 * RecomendacaoService - Versão Refinada
 * - O método 'recomendarMelhor' foi atualizado para NUNCA retornar null.
 * - A base de dados (BASE) foi expandida.
 */
public class RecomendacaoService {

    private static final Map<String, String[]> ESPECIALIDADES_BY_AREA;
    private static final Map<String, String[]> NICHOS_BY_ESPECIALIDADE;
    private static final List<Carreira> BASE;

    static {
        // --- MAPA DE ESPECIALIDADES ---
        Map<String, String[]> e = new LinkedHashMap<>();
        e.put("Exatas", new String[]{"Computacao", "Engenharia", "Matematica", "RPA"});
        e.put("Humanas", new String[]{"Administracao", "Psicologia", "Educacao", "Direito"});
        e.put("Artes", new String[]{"Design", "Musica", "ArtesVisuais", "Animacao"});
        e.put("Biologicas", new String[]{"Medicina", "Enfermagem", "Biotecnologia", "SaudePublica"});
        e.put("Negocios", new String[]{"Marketing", "Financas", "Logistica", "Consultoria"});
        ESPECIALIDADES_BY_AREA = Collections.unmodifiableMap(e);

        // --- MAPA DE NICHOS ---
        Map<String, String[]> n = new HashMap<>();
        n.put("Computacao", new String[]{"Data Science", "Desenvolvimento", "Cloud", "QA"});
        n.put("Engenharia", new String[]{"Civil", "Mecanica", "Automacao", "Producao"});
        n.put("Matematica", new String[]{"Estatistica", "Pesquisa", "Modelagem"});
        n.put("RPA", new String[]{"Automacao de Processos", "RPA Corporativo"});
        n.put("Administracao", new String[]{"Operacoes", "Projetos", "Recursos Humanos"});
        n.put("Psicologia", new String[]{"Clinica", "Organizacional"});
        n.put("Educacao", new String[]{"Pedagogia", "Tecnologia Educacional"});
        n.put("Direito", new String[]{"Consultoria", "Compliance"});
        n.put("Design", new String[]{"UX/UI", "Branding", "Motion"});
        n.put("Musica", new String[]{"Producao Musical", "Composicao"});
        n.put("ArtesVisuais", new String[]{"Ilustracao", "Pintura"});
        n.put("Animacao", new String[]{"2D", "3D"});
        n.put("Medicina", new String[]{"Clinica Geral", "Cirurgia"});
        n.put("Enfermagem", new String[]{"Hospitalar", "UTI"});
        n.put("Biotecnologia", new String[]{"Pesquisa", "Laboratorio"});
        n.put("SaudePublica", new String[]{"Epidemiologia", "Vigilancia"});
        n.put("Marketing", new String[]{"Digital", "Performance", "Branding"});
        n.put("Financas", new String[]{"Analise Financeira", "Controladoria", "Investimentos"});
        n.put("Logistica", new String[]{"Supply Chain", "Planejamento"});
        n.put("Consultoria", new String[]{"Transformacao Digital", "Estrategia"});
        NICHOS_BY_ESPECIALIDADE = Collections.unmodifiableMap(n);

        // --- BASE DE DADOS DE CARREIRAS (EXPANDIDA) ---
        BASE = List.of(
                new Carreira("Analista de Dados", "Exatas", "Computacao", "Data Science", "R$ 9.500",
                        new String[]{"Google Data Analytics", "SQL Avancado", "Python para Data Science"},
                        new String[]{"SQL", "Python", "Estatistica"},
                        new String[]{"Pensamento Critico", "Comunicacao"}),

                new Carreira("Cientista de Dados", "Exatas", "Computacao", "Data Science", "R$ 14.000",
                        new String[]{"Machine Learning", "Python Avancado", "Estatistica"},
                        new String[]{"Python", "Machine Learning", "Estatistica"},
                        new String[]{"Curiosidade", "Raciocinio Logico"}),

                new Carreira("Engenheiro de Software", "Exatas", "Computacao", "Desenvolvimento", "R$ 12.500",
                        new String[]{"Formacao Java", "Algoritmos", "Arquitetura"},
                        new String[]{"Java", "Estruturas de Dados", "Design Patterns"},
                        new String[]{"Resolucao de Problemas", "Trabalho em Equipe"}),

                new Carreira("Gerente de Operacoes", "Negocios", "Administracao", "Operacoes", "R$ 11.500",
                        new String[]{"Gestao de Operacoes", "Lean Six Sigma", "PMI"},
                        new String[]{"Gestao de Processos", "Analise de Dados"},
                        new String[]{"Tomada de Decisao", "Lideranca"}),

                new Carreira("Psicologo Organizacional", "Humanas", "Psicologia", "Organizacional", "R$ 8.000",
                        new String[]{"Gestao de Pessoas", "Recrutamento", "Clima Organizacional"},
                        new String[]{"Psicologia", "RH", "Comunicacao"},
                        new String[]{"Empatia", "Escuta Ativa"}),

                new Carreira("Tech Recruiter (RH)", "Humanas", "Administracao", "Recursos Humanos", "R$ 7.500",
                        new String[]{"Recrutamento e Selecao", "Entrevista por Competencias"},
                        new String[]{"Boolean Search", "Networking", "Gestao de Pessoas"},
                        new String[]{"Comunicacao", "Persuasao"}),

                new Carreira("Designer UX/UI", "Artes", "Design", "UX/UI", "R$ 10.500",
                        new String[]{"Figma", "Design Thinking", "User Research"},
                        new String[]{"Figma", "Adobe XD", "Prototipacao"},
                        new String[]{"Criatividade", "Empatia", "Atencao aos Detalhes"}),

                new Carreira("Enfermeiro(a) de UTI", "Biologicas", "Enfermagem", "UTI", "R$ 9.000",
                        new String[]{"Pos em Terapia Intensiva", "Ventilacao Mecanica"},
                        new String[]{"Monitoramento", "Procedimentos Invasivos", "Farmacologia"},
                        new String[]{"Inteligencia Emocional", "Agilidade", "Resiliencia"})
        );
    }

    // --- API PUBLICA (para a Interface) ---
    public static String[] getAreas() {
        return ESPECIALIDADES_BY_AREA.keySet().toArray(new String[0]);
    }

    public static String[] getEspecialidades(String area) {
        if (area == null) return new String[0];
        return ESPECIALIDADES_BY_AREA.getOrDefault(area, new String[0]);
    }

    public static String[] getNichos(String especialidade) {
        if (especialidade == null) return new String[0];
        return NICHOS_BY_ESPECIALIDADE.getOrDefault(especialidade, new String[0]);
    }

    // --- ALGORITMOS DE RECOMENDAÇÃO ---

    // 1. MATCH EXATO
    public static Carreira recomendar(UsuarioEscolha u) {
        if (u == null) return null;
        return BASE.stream().filter(c ->
                equalsIgnoreCase(c.getArea(), u.getArea()) &&
                        equalsIgnoreCase(c.getEspecialidade(), u.getEspecialidade()) &&
                        equalsIgnoreCase(c.getNicho(), u.getNicho())
        ).findFirst().orElse(null);
    }

    // 2. FALLBACK INTELIGENTE (REFINADO)
    public static Carreira recomendarMelhor(UsuarioEscolha u) {
        if (u == null || BASE.isEmpty()) return null;

        // Tenta o match exato primeiro
        Carreira exata = recomendar(u);
        if (exata != null) {
            return exata;
        }

        // Se falhar, calcula o score para TODAS as carreiras e retorna a melhor
        List<Carreira> todosOsCandidatos = new ArrayList<>(BASE);
        return melhorPorScore(todosOsCandidatos, u);
    }

    // --- MÉTODOS PRIVADOS (Helpers) ---

    private static Carreira melhorPorScore(List<Carreira> candidatos, UsuarioEscolha u) {
        candidatos.sort((a, b) -> {
            int sa = score(a, u);
            int sb = score(b, u);
            if (sa != sb) return Integer.compare(sb, sa); // Maior score primeiro
            return Double.compare(parseSalario(b.getSalario()), parseSalario(a.getSalario())); // Maior salário primeiro
        });
        return candidatos.get(0); // Retorna a melhor
    }

    private static int score(Carreira c, UsuarioEscolha u) {
        int s = 0;
        if (equalsIgnoreCase(c.getArea(), u.getArea())) s += 30;
        if (equalsIgnoreCase(c.getEspecialidade(), u.getEspecialidade())) s += 20;
        if (equalsIgnoreCase(c.getNicho(), u.getNicho())) s += 10;
        return s;
    }

    private static boolean equalsIgnoreCase(String a, String b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.equalsIgnoreCase(b);
    }

    private static double parseSalario(String s) {
        try {
            String t = s.replaceAll("[^0-9]", "");
            return t.isEmpty() ? 0 : Double.parseDouble(t);
        } catch (Exception e) {
            return 0;
        }
    }
}