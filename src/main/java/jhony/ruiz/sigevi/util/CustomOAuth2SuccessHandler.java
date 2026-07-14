package jhony.ruiz.sigevi.util;

import com.marketplaceautopartes.marketplaceapi.dto.CustomOAuth2User;
import com.marketplaceautopartes.marketplaceapi.model.Usuario;
import com.marketplaceautopartes.marketplaceapi.repository.IUsuarioRepository;
import com.marketplaceautopartes.marketplaceapi.security.JwtTokenUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
  private final JwtTokenUtil jwtTokenUtil;
  private final IUsuarioRepository usuarioRepository;
  private final OAuth2AuthorizedClientService authorizedClientService;

  @Value("${intranet.url}")
  private String intranetUrl;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) throws IOException, ServletException {

    String registrationId = "google";
    if (authentication instanceof OAuth2AuthenticationToken token) {
      registrationId = token.getAuthorizedClientRegistrationId();
    }

    CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
    String email = oAuth2User.getEmail();

    Usuario usuario = usuarioRepository.findByUsername(email)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + email));

    //Login normal (OAuth2 con Google)
    if ("google".equals(registrationId)) {
      var userDetails = new org.springframework.security.core.userdetails.User(
          usuario.getUsername(),
          "OAUTH_PASS",
          usuario.getRoles().stream()
              .map(rol -> new SimpleGrantedAuthority(rol.getDescripcion()))
              .toList()
      );

      String token = jwtTokenUtil.generateToken(userDetails);

      Integer idTienda = usuarioRepository.findIdTienda(usuario.getUsername())
          .orElse(null);

      String rol = usuario.getRoles() != null && !usuario.getRoles().isEmpty()
          ? usuario.getRoles().get(0).getDescripcion()
          : "DUEÑO_DE_TIENDA";

      String emailEncoded = URLEncoder.encode(email, StandardCharsets.UTF_8);

      String tokenEncoded = URLEncoder.encode(token, StandardCharsets.UTF_8);
      String rolEncoded = URLEncoder.encode(rol, StandardCharsets.UTF_8);
      String idTiendaEncoded = idTienda != null
              ? URLEncoder.encode(idTienda.toString(), StandardCharsets.UTF_8)
              : "";

//      String tokenEncoded = URLEncoder.encode(token, StandardCharsets.UTF_8);
//      String rolEncoded = URLEncoder.encode(rol, StandardCharsets.UTF_8);
//      String idTiendaEncoded = idTienda != null
//          ? URLEncoder.encode(idTienda.toString(), StandardCharsets.UTF_8)
//          : "";

//      String redirectUrl = String.format(
//          intranetUrl + "/oauth2/callback?token=%s&rol=%s&idTienda=%s",
//          tokenEncoded, rolEncoded, idTiendaEncoded
//      );
      String redirectUrl = String.format(
              intranetUrl + "/login?token=%s&rol=%s&idTienda=%s&email=%s",
              tokenEncoded, rolEncoded, idTiendaEncoded, emailEncoded
      );
      response.sendRedirect(redirectUrl);
      return;
    }

    //OAuth2 con Google Gmail)
    if ("google-gmail".equals(registrationId) && authentication instanceof OAuth2AuthenticationToken token) {
      // Obtener cliente autorizado
      OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
          registrationId, token.getName());

      if (client != null) {
        OAuth2AccessToken accessToken = client.getAccessToken();
        OAuth2RefreshToken refreshToken = client.getRefreshToken();

        usuario.setGmailAccessToken(accessToken.getTokenValue());
        usuario.setGmailTokenExpira(LocalDateTime.ofInstant(
            accessToken.getExpiresAt(), ZoneId.systemDefault()));

        if (refreshToken != null) {
          usuario.setGmailRefreshToken(refreshToken.getTokenValue());
        }

        usuarioRepository.save(usuario);
      }

      String emailEncoded = URLEncoder.encode(email, StandardCharsets.UTF_8);
      String redirectUrl = String.format(
          intranetUrl+"/admin/mensajes/gmail",
          emailEncoded
      );

      response.sendRedirect(redirectUrl);
    }
  }
}
