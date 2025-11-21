package model;

/**
 * classe de objeto (POJO) que armazena as 3 escolhas do usuário.
 */
public class UsuarioEscolha {
    private String area;
    private String especialidade;
    private String nicho;

    public UsuarioEscolha(String area, String especialidade, String nicho) {
        this.area = area;
        this.especialidade = especialidade;
        this.nicho = nicho;
    }

    // Getters
    public String getArea() { return area; }
    public String getEspecialidade() { return especialidade; }
    public String getNicho() { return nicho; }
}