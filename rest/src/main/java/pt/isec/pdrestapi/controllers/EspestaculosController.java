package pt.isec.pdrestapi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import pt.isec.pdrestapi.models.Espetaculo;
import java.util.List;

@RestController
@RequestMapping("espetaculos")
public class EspestaculosController {
    private final JdbcTemplate jdbcTemplate;

    public EspestaculosController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    public ResponseEntity getShows(@RequestParam(value="data-inicio", required=false) String dataInicio,@RequestParam(value="data-fim", required=false) String dataFim)
    {
        if (dataInicio == null && dataFim == null) {
            List<Espetaculo> espetaculos = jdbcTemplate.query("SELECT * FROM espetaculo",
                    (resultSet, rowNum) -> new Espetaculo(
                            resultSet.getInt("id"),
                            resultSet.getString("descricao"),
                            resultSet.getString("tipo"),
                            resultSet.getString("data_hora"),
                            resultSet.getInt("duracao"),
                            resultSet.getString("local"),
                            resultSet.getString("localidade"),
                            resultSet.getString("pais"),
                            resultSet.getString("classificacao_etaria"),
                            resultSet.getInt("visivel"))
            );

            return ResponseEntity.ok(espetaculos.toString());
        } else if (dataInicio != null && dataFim != null) {
            List<Espetaculo> espetaculos = jdbcTemplate.query("SELECT * FROM espetaculo WHERE strftime('%s',data_hora)" +
                            "BETWEEN strftime('%s', '" + dataInicio + " ') AND   strftime('%s', '" + dataFim + "')",
                    (resultSet, rowNum) -> new Espetaculo(
                            resultSet.getInt("id"),
                            resultSet.getString("descricao"),
                            resultSet.getString("tipo"),
                            resultSet.getString("data_hora"),
                            resultSet.getInt("duracao"),
                            resultSet.getString("local"),
                            resultSet.getString("localidade"),
                            resultSet.getString("pais"),
                            resultSet.getString("classificacao_etaria"),
                            resultSet.getInt("visivel"))
            );

            return ResponseEntity.ok(espetaculos.toString());
        }

        if (dataInicio == null) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Introduza uma data-incio do tipo yyyy-MM-dd HH:mm:ss ou faça /espetaculo para ver a lista de todos os espetáculos");
        }

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Introduza uma data-fim do tipo yyyy-MM-dd HH:mm:ss ou faça /espetaculo para ver a lista de todos os espetáculos");
    }
}
