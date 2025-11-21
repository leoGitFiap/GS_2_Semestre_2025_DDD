package model;

/**
 * classe de objeto (POJO) que armazena dados de UMA carreira.
 * corresponde ao `Recomendacao.java` primeira versão.
 */
public class Carreira {
    private String nome;
    private String area;
    private String especialidade;
    private String nicho;
    private String salario;
    private String[] cursos;
    private String[] hardSkills;
    private String[] softSkills;

    // construtor completo
    public Carreira(String nome, String area, String especialidade, String nicho, String salario,
                    String[] cursos, String[] hardSkills, String[] softSkills) {
        this.nome = nome;
        this.area = area;
        this.especialidade = especialidade;
        this.nicho = nicho;
        this.salario = salario;
        this.cursos = cursos;
        this.hardSkills = hardSkills;
        this.softSkills = softSkills;
    }

    // getters (métodos para acessar os dados)
    public String getNome() { return nome; }
    public String getArea() { return area; }
    public String getEspecialidade() { return especialidade; }
    public String getNicho() { return nicho; }
    public String getSalario() { return salario; }
    public String[] getCursos() { return cursos; }
    public String[] getHardSkills() { return hardSkills; }
    public String[] getSoftSkills() { return softSkills; }
}