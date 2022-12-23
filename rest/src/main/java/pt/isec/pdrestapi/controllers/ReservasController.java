package pt.isec.pdrestapi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import pt.isec.pdrestapi.models.Espetaculo;
import pt.isec.pdrestapi.models.LangConfig;
import pt.isec.pdrestapi.models.Reserva;
import pt.isec.pdrestapi.models.Utilizador;

import java.security.Principal;
import java.util.List;

//Principal class para ver qual o utilizador autenticado

@RestController
@RequestMapping("reservas")
public class ReservasController {
    private final JdbcTemplate jdbcTemplate;

    public ReservasController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    public ResponseEntity getReservas(Principal principal,@RequestParam(value="pago", required=false) Integer pago) {
        if(pago == null)
            pago = 1;

        List<Utilizador> utilizador = jdbcTemplate.query(("SELECT * FROM utilizador WHERE username='" + principal.getName()) + "'",
                (resultSet, rowNum) -> new Utilizador(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("nome"),
                        resultSet.getString("password"),
                        resultSet.getInt("administrador"),
                        resultSet.getInt("autenticado")
                ));

        if (utilizador.isEmpty()) {
            return null;
        }


        List<Reserva> reservas = jdbcTemplate.query(("SELECT * FROM reserva WHERE id_utilizador='" + utilizador.get(0).getId() + "' AND pago='"+ pago +"'"),
                (resultSet, rowNum) -> new Reserva(
                        resultSet.getInt("id"),
                        resultSet.getString("data_hora"),
                        resultSet.getInt("pago"),
                        resultSet.getInt("id_utilizador"),
                        resultSet.getInt("id_espetaculo")
        ));

        return ResponseEntity.ok(reservas.toString());
    }
}
