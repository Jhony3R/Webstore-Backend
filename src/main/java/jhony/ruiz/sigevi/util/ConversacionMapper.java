package jhony.ruiz.sigevi.util;

import com.marketplaceautopartes.marketplaceapi.dto.ConversacionDTO;
import com.marketplaceautopartes.marketplaceapi.model.Conversacion;
import com.marketplaceautopartes.marketplaceapi.model.Mensajechat;
import com.marketplaceautopartes.marketplaceapi.model.Usuario;
import com.marketplaceautopartes.marketplaceapi.repository.IMensajechatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ConversacionMapper {
    private final IMensajechatRepository mensajechatRepository;

    public ConversacionDTO toDTO(Conversacion conversacion, Integer idUsuarioActual) {
        ConversacionDTO dto = new ConversacionDTO();

        dto.setIdConversacion(conversacion.getIdConversacion());
        dto.setFechaCreacion(conversacion.getFechaCreacion());
        dto.setFechaUltimoMensaje(conversacion.getFechaUltimoMensaje());
        dto.setActiva(conversacion.getActiva());

        // Usuario remitente
        Usuario remitente = conversacion.getUsuarioRemitente();
        dto.setIdUsuarioRemitente(remitente.getIdUsuario());
        dto.setNombreUsuarioRemitente(remitente.getUsernombres());
        dto.setFotoUsuarioRemitente(
                remitente.getEntidadespersonas() != null &&
                        remitente.getEntidadespersonas().getPersona() != null
                        ? remitente.getEntidadespersonas().getPersona().getUrlfoto()
                        : null
        );

        // Usuario destinatario
        Usuario destinatario = conversacion.getUsuarioDestinatario();
        dto.setIdUsuarioDestinatario(destinatario.getIdUsuario());
        dto.setNombreUsuarioDestinatario(destinatario.getUsernombres());
        dto.setFotoUsuarioDestinatario(
                destinatario.getEntidadespersonas() != null &&
                        destinatario.getEntidadespersonas().getPersona() != null
                        ? destinatario.getEntidadespersonas().getPersona().getUrlfoto()
                        : null
        );

        // Determinar el "otro" usuario (el que NO es el usuario actual)
        boolean esRemitente = remitente.getIdUsuario().equals(idUsuarioActual);
        Usuario otroUsuario = esRemitente ? destinatario : remitente;

        dto.setIdOtroUsuario(otroUsuario.getIdUsuario());
        dto.setNombreOtroUsuario(otroUsuario.getUsernombres());
        dto.setFotoOtroUsuario(
                otroUsuario.getEntidadespersonas() != null &&
                        otroUsuario.getEntidadespersonas().getPersona() != null
                        ? otroUsuario.getEntidadespersonas().getPersona().getUrlfoto()
                        : null
        );
        dto.setRolOtroUsuario(
                !otroUsuario.getRoles().isEmpty()
                        ? otroUsuario.getRoles().get(0).getDescripcion()
                        : "Sin rol"
        );

        // Último mensaje
        if (conversacion.getMensajes() != null && !conversacion.getMensajes().isEmpty()) {
            Mensajechat ultimoMensaje = conversacion.getMensajes().get(conversacion.getMensajes().size() - 1);
            dto.setUltimoMensaje(ultimoMensaje.getContenido());
        }

        // Contar mensajes no leídos
        long noLeidos = mensajechatRepository.countMensajesNoLeidos(conversacion, idUsuarioActual);
        dto.setMensajesNoLeidos((int) noLeidos);

        return dto;
    }

    public List<ConversacionDTO> toDTOList(List<Conversacion> conversaciones, Integer idUsuarioActual) {
        return conversaciones.stream()
                .map(c -> toDTO(c, idUsuarioActual))
                .collect(Collectors.toList());
    }
}
