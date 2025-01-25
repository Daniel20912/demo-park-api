package com.danieloliveira.demo_park_api.jwt;

import com.danieloliveira.demo_park_api.entities.Usuario;
import com.danieloliveira.demo_park_api.sevices.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JwtUserDetailsService implements UserDetailsService { // a classe UserDetailsService é usada para localizar o usuário no banco de dados

    private final UsuarioService usuarioService;

    @Override
    /*
        faz a consulta pelo username do usuário, se o usuário for encontrado vai retornar esse Usuario no formato de um UserDetails
        dessa forma o Spring consegue colocar na sessão, que ele vai gerenciar, o usuário que está logado na aplicação
    */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Usuario usuario = usuarioService.buscarPorUsername(username);

        return new JwtUserDetails(usuario);
    }

    // metodo para gerar o token jwt
    // esse metodo é usado para quando o cliente for autenticar na aplicação
    public JwtToken getTokenAuthenticated(String username) {
        // retorna o perfil do usuário
        Usuario.Role role = usuarioService.buscarRolePorUsername(username);

        return JwtUtils.createToken(username, role.name().substring("ROLE_".length()));

    }
}
