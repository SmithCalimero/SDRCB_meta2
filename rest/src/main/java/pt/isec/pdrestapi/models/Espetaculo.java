package pt.isec.pdrestapi.models;

public class Espetaculo {
    private Integer id;
    private String descricao;
    private String tipo;
    private String data_hora;
    private Integer duracao;
    private String local;

    private String localidade;
    private String pais;
    private String classificacao_etaria;
    private Integer visivel;

    public Espetaculo(Integer id,
                      String descricao,
                      String tipo,
                      String data_hora,
                      Integer duracao,
                      String local,
                      String localidade,
                      String pais,
                      String classificacao_etaria,
                      Integer visivel) {
        this.id = id;
        this.descricao = descricao;
        this.tipo = tipo;
        this.data_hora = data_hora;
        this.duracao = duracao;
        this.local = local;
        this.localidade = localidade;
        this.pais = pais;
        this.classificacao_etaria = classificacao_etaria;
        this.visivel = visivel;
    }

    @Override
    public String toString() {
        return  "Id: " + id + "\n" +
                "Descricao: " + descricao + "\n" +
                "Tipo: " + tipo + "\n" +
                "Data: " + data_hora + "\n" +
                "Duração: " + duracao +
                "Local: " + local + "\n" +
                "Localidade: " + localidade + "\n" +
                "País: " + pais + "\n" +
                "Classificação Etária: " + classificacao_etaria + "\n" +
                "Visível: " + visivel + "\n";
    }
}
