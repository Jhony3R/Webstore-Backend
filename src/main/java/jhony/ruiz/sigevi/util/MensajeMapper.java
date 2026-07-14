package jhony.ruiz.sigevi.util;

import com.marketplaceautopartes.marketplaceapi.dto.MensajechatDTO;
import com.marketplaceautopartes.marketplaceapi.model.Mensajechat;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MensajeMapper {
    public MensajechatDTO toDTO(Mensajechat mensaje, Integer idUsuarioActual) {
        MensajechatDTO dto = new MensajechatDTO();

        dto.setIdMensajechat(mensaje.getIdMensajechat());
        dto.setIdConversacion(mensaje.getConversacion().getIdConversacion());

        // Usuario remitente
        dto.setIdUsuarioRemitente(mensaje.getUsuarioRemitente().getIdUsuario());
        dto.setNombreUsuarioRemitente(mensaje.getUsuarioRemitente().getUsernombres());
        dto.setFotoUsuarioRemitente(
                mensaje.getUsuarioRemitente().getEntidadespersonas() != null &&
                        mensaje.getUsuarioRemitente().getEntidadespersonas().getPersona() != null
                        ? mensaje.getUsuarioRemitente().getEntidadespersonas().getPersona().getUrlfoto()
                        : null
        );

        // Contenido y fecha
        dto.setContenido(mensaje.getContenido());
        dto.setFechaEnvio(mensaje.getFechaEnvio());
        dto.setLeido(mensaje.getLeido());
        dto.setFechaLeido(mensaje.getFechaLeido());

        // Determinar si el mensaje es del usuario actual
        dto.setEsMio(mensaje.getUsuarioRemitente().getIdUsuario().equals(idUsuarioActual));

        return dto;
    }

    public List<MensajechatDTO> toDTOList(List<Mensajechat> mensajes, Integer idUsuarioActual) {
        return mensajes.stream()
                .map(m -> toDTO(m, idUsuarioActual))
                .collect(Collectors.toList());
    }
}
