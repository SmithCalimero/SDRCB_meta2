package pt.isec.pdrestapi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import pt.isec.pdrestapi.models.Espetaculo;
import pt.isec.pdrestapi.models.Reserva;
import pt.isec.pdrestapi.models.Utilizador;

import java.security.Principal;
import java.util.List;

//Principal class para ver qual o utilizador autenticado

@RestController
@RequestMapping("utilizadores")
public class UtilizadoresController {
    private final JdbcTemplate jdbcTemplate;

    public UtilizadoresController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping
    public ResponseEntity getUsers(Principal principal)
    {
        List<Utilizador> admin = jdbcTemplate.query(("SELECT * FROM utilizador WHERE username='" + principal.getName()) + "' AND administrador='1'",
                (resultSet, rowNum) -> new Utilizador(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("nome"),
                        resultSet.getString("password"),
                        resultSet.getInt("administrador"),
                        resultSet.getInt("autenticado")
                ));

        if (admin.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Para executar esta ação tem de ser um admin");
        }

        List<Utilizador> utilizador = jdbcTemplate.query("SELECT * FROM utilizador",
                (resultSet, rowNum) -> new Utilizador(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("nome"),
                        resultSet.getString("password"),
                        resultSet.getInt("administrador"),
                        resultSet.getInt("autenticado")
                ));

        return ResponseEntity.ok(utilizador.toString());
    }

    @PostMapping
    public ResponseEntity addUser(Principal principal,@RequestBody Utilizador utilizador)
    {
        List<Utilizador> admin = jdbcTemplate.query(("SELECT * FROM utilizador WHERE username='" + principal.getName()) + "' AND administrador='1'",
                (resultSet, rowNum) -> new Utilizador(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("nome"),
                        resultSet.getString("password"),
                        resultSet.getInt("administrador"),
                        resultSet.getInt("autenticado")
                ));

        if (admin.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Para executar esta ação tem de ser um admin");
        }

        List<Utilizador> checkUser = jdbcTemplate.query(("SELECT * FROM utilizador WHERE username='" + utilizador.getUsername()) + "'",
                (resultSet, rowNum) -> new Utilizador(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("nome"),
                        resultSet.getString("password"),
                        resultSet.getInt("administrador"),
                        resultSet.getInt("autenticado")
                ));

        if (checkUser.isEmpty()) {
            jdbcTemplate.execute("INSERT INTO utilizador (" +
                    "                           username," +
                    "                           nome," +
                    "                           password," +
                    "                           administrador," +
                    "                           autenticado" +
                    "                       )" +
                    "                       VALUES (" +
                    "                          '"+ utilizador.getUsername() +"'," +
                    "                           '"+ utilizador.getNome() +"'," +
                    "                          '"+ utilizador.getPassword() +"'," +
                    "                           '"+ utilizador.getAdministrador() +"'," +
                    "                           '"+ 0 +"');");

            return ResponseEntity.ok(utilizador.toString());

        }

        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("O utilizador com esse username já existe\n" +
                "Use /utilizadores para ver os utilizador");
    }

    @PostMapping("{type}")
    public ResponseEntity deleteUser(Principal principal,@PathVariable("type") Integer id)
    {
        List<Utilizador> admin = jdbcTemplate.query(("SELECT * FROM utilizador WHERE username='" + principal.getName()) + "' AND administrador='1'",
                (resultSet, rowNum) -> new Utilizador(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("nome"),
                        resultSet.getString("password"),
                        resultSet.getInt("administrador"),
                        resultSet.getInt("autenticado")
                ));

        if (admin.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Para executar esta ação tem de ser um admin");
        }

        List<Utilizador> removeUser = jdbcTemplate.query(("SELECT * FROM utilizador WHERE id='" + id + "'"),
                (resultSet, rowNum) -> new Utilizador(
                        resultSet.getInt("id"),
                        resultSet.getString("username"),
                        resultSet.getString("nome"),
                        resultSet.getString("password"),
                        resultSet.getInt("administrador"),
                        resultSet.getInt("autenticado")
                ));

        if (removeUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O utilizador com esse id não existe\n" +
                    "Use /utilizadores para ver os utilizador que quer remover");
        }

        List<Reserva> reservas = jdbcTemplate.query(("SELECT * FROM reserva WHERE id_utilizador='" + id + "'"),
                (resultSet, rowNum) -> new Reserva(
                        resultSet.getInt("id"),
                        resultSet.getString("data_hora"),
                        resultSet.getInt("pago"),
                        resultSet.getInt("id_utilizador"),
                        resultSet.getInt("id_espetaculo")
                ));

        if (!reservas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("O utilizador com esse id tem reservas associdas\n" + reservas);
        }

        jdbcTemplate.execute("DELETE FROM utilizador" +
                " WHERE id = '" + id + "';");

        return ResponseEntity.ok("Removido com sucesso\n" + removeUser.get(0));
    }
}
