package pt.isec.pdrestapi.models;

public class Reserva {
    private Integer id;
    private String data_hora;
    private Integer pago;
    private Integer id_utilizador;

    private Integer id_espetaculo;

    public Reserva(Integer id, String data_hora, Integer pago, Integer id_utilizador, Integer id_espetaculo) {
        this.id = id;
        this.data_hora = data_hora;
        this.pago = pago;
        this.id_utilizador = id_utilizador;
        this.id_espetaculo = id_espetaculo;
    }

    @Override
    public String toString() {
        return  "Id: " + id + "\n" +
                "Data: " + data_hora + "\n" +
                "Pago: " + pago + "\n" +
                "Id Utilizador: " + id_utilizador + "\n" +
                "Id Espetaculo: " + id_espetaculo + "\n";
    }
}


