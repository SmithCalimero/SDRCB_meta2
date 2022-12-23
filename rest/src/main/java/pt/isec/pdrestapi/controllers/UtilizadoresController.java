package pt.isec.pdrestapi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import pt.isec.pdrestapi.models.Espetaculo;
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
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Para executar esta ação tem de ser um admin");
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
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Para executar esta ação tem de ser um admin");
        }

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
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Para executar esta ação tem de ser um admin");
        }

        jdbcTemplate.execute("DELETE FROM utilizador" +
                " WHERE id = '" + id + "';");

        return ResponseEntity.ok("removido");
    }
}
