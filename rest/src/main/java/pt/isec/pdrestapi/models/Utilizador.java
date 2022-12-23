package pt.isec.pdrestapi.models;

public class Utilizador {
    private Integer id;
    private String username;
    private String nome;
    private String password;
    private Integer administrador;
    private Integer autenticado;

    public Utilizador(String username) {
        this.username = username;
    }

    public Utilizador(Integer id, String username, String nome, String password, Integer administrador, Integer autenticado) {
        this.id = id;
        this.username = username;
        this.nome = nome;
        this.password = password;
        this.administrador = administrador;
        this.autenticado = autenticado;
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getNome() {
        return nome;
    }

    public String getPassword() {
        return password;
    }

    public Integer getAdministrador() {
        return administrador;
    }

    public Integer getAutenticado() {
        return autenticado;
    }

    @Override
    public String toString() {
        return "Id: " + id + "\n" +
                "Username: " + username + "\n" +
                "Nome: " + nome + "\n" +
                "Password: " + password + "\n" +
                "Administrador: " + administrador + "\n" +
                "Autenticado: " + autenticado + "\n";
    }
}
