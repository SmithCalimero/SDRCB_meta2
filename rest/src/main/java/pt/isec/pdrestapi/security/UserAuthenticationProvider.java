package pt.isec.pdrestapi.security;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import pt.isec.pdrestapi.models.Reserva;
import pt.isec.pdrestapi.models.Utilizador;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider
{
    private final JdbcTemplate jdbcTemplate;

    public UserAuthenticationProvider(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        List<Utilizador> utilizador = jdbcTemplate.query(("SELECT * FROM utilizador WHERE username='" + username + "' AND password='"+ password + "'"),
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

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(username.toUpperCase(Locale.ROOT)));

        return new UsernamePasswordAuthenticationToken(username, password, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication)
    {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
