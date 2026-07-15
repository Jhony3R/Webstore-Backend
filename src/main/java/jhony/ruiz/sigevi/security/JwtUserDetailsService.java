package jhony.ruiz.sigevi.security;

import jhony.ruiz.sigevi.exception.InvalidCredentialsException;
import jhony.ruiz.sigevi.exception.UserDisabledException;
import jhony.ruiz.sigevi.model.Usuario;
import jhony.ruiz.sigevi.repository.IUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final IUsuarioRepository repo;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario user = repo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // "ROLE_" es el prefijo que Spring Security exige para que funcione hasRole("ADMINISTRADOR")
        List<GrantedAuthority> roles = List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getRol().name())
        );

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isActivo(),  // enabled
                true,             // accountNonExpired
                true,             // credentialsNonExpired
                true,             // accountNonLocked
                roles
        );
    }

    public JwtResponse login(JwtRequest jwtRequest) {
        Usuario usuario = repo.findByUsername(jwtRequest.getUsername())
                .orElseThrow(() -> new InvalidCredentialsException("INVALID_CREDENTIALS"));

        if (!usuario.isActivo()) {
            throw new UserDisabledException("USER_DISABLED");
        }

        if (!passwordEncoder.matches(jwtRequest.getPassword(), usuario.getPassword())) {
            throw new InvalidCredentialsException("INVALID_CREDENTIALS");
        }

        final UserDetails userDetails = loadUserByUsername(jwtRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails);

        return new JwtResponse(token, usuario.getRol().name());
    }
}