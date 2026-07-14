package jhony.ruiz.sigevi.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Component
@RequiredArgsConstructor
public class EmailUtil {
    private final JavaMailSender emailSender;
    private final SpringTemplateEngine templateEngine;

    public void sendEmail(Mail mail) throws MessagingException {
        try {
            System.out.println("=== INICIANDO ENVÍO DE CORREO ===");
            System.out.println("De: " + mail.getFrom());
            System.out.println("Para: " + mail.getTo());
            System.out.println("Asunto: " + mail.getSubject());

            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED);

            Context context = new Context();
            context.setVariables(mail.getModel());
            System.out.println("Contexto creado con variables: " + mail.getModel());

            System.out.println("Procesando template...");
            String html = templateEngine.process("email/email-template", context);
            System.out.println("Template procesado exitosamente");

            helper.setTo(mail.getTo());
            helper.setText(html, true);
            helper.setSubject(mail.getSubject());
            helper.setFrom(mail.getFrom());

            System.out.println("Enviando mensaje...");
            emailSender.send(message);
            System.out.println("=== CORREO ENVIADO EXITOSAMENTE ===");

        } catch (Exception e) {
            System.out.println("=== ERROR EN EMAILUTIL ===");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public void sendPedidoEstadoEmail(Mail mail) throws MessagingException {
        try {
            System.out.println("=== INICIANDO ENVÍO DE CORREO DE PEDIDO ===");
            System.out.println("De: " + mail.getFrom());
            System.out.println("Para: " + mail.getTo());
            System.out.println("Asunto: " + mail.getSubject());

            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED);

            Context context = new Context();
            context.setVariables(mail.getModel());
            System.out.println("Contexto creado con variables del pedido: " + mail.getModel());

            System.out.println("Procesando template de pedido...");
            String html = templateEngine.process("email/pedido-notificacion", context);
            System.out.println("Template procesado exitosamente");

            helper.setTo(mail.getTo());
            helper.setText(html, true);
            helper.setSubject(mail.getSubject());
            helper.setFrom(mail.getFrom());

            System.out.println("Enviando correo de pedido...");
            emailSender.send(message);
            System.out.println("=== CORREO DE PEDIDO ENVIADO EXITOSAMENTE ===");

        } catch (Exception e) {
            System.out.println("=== ERROR EN ENVÍO DE CORREO DE PEDIDO ===");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public void sendNuevaReservaEmail(Mail mail) throws MessagingException {
        try {
            System.out.println("=== INICIANDO ENVÍO DE NOTIFICACIÓN DE NUEVA RESERVA ===");
            System.out.println("De: " + mail.getFrom());
            System.out.println("Para: " + mail.getTo());
            System.out.println("Asunto: " + mail.getSubject());

            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED);

            Context context = new Context();
            context.setVariables(mail.getModel());
            System.out.println("Contexto creado con variables: " + mail.getModel());

            System.out.println("Procesando template de nueva reserva...");
            String html = templateEngine.process("email/nueva-reserva", context);
            System.out.println("Template procesado exitosamente");

            helper.setTo(mail.getTo());
            helper.setText(html, true);
            helper.setSubject(mail.getSubject());
            helper.setFrom(mail.getFrom());

            System.out.println("Enviando correo de nueva reserva...");
            emailSender.send(message);
            System.out.println("=== CORREO DE NUEVA RESERVA ENVIADO EXITOSAMENTE ===");

        } catch (Exception e) {
            System.out.println("=== ERROR EN ENVÍO DE CORREO DE NUEVA RESERVA ===");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // MÉTODO CON TRY-CATCH Y LOGS
    public void sendVerificationEmail(Mail mail) throws MessagingException {
        try {
            System.out.println("=== ENVIANDO CÓDIGO DE VERIFICACIÓN ===");
            System.out.println("De: " + mail.getFrom());
            System.out.println("Para: " + mail.getTo());
            System.out.println("Asunto: " + mail.getSubject());

            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(mail.getTo());
            helper.setSubject(mail.getSubject());
            helper.setFrom(mail.getFrom());

            // Crear HTML para el correo de verificación
            String code = (String) mail.getModel().get("code");
            System.out.println("Código a enviar: " + code);

            String htmlContent = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "    <meta charset='UTF-8'>" +
                    "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                    "</head>" +
                    "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;'>" +
                    "    <div style='max-width: 600px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 5px rgba(0,0,0,0.1);'>"
                    +
                    "        <div style='text-align: center; margin-bottom: 30px;'>" +
                    "            <h2 style='color: #333; margin: 0;'>🛒 Meerkadito</h2>" +
                    "        </div>" +
                    "        <h2 style='color: #333; text-align: center;'>Verificación de Correo Electrónico</h2>" +
                    "        <p style='color: #666; font-size: 16px; line-height: 1.6;'>Hola,</p>" +
                    "        <p style='color: #666; font-size: 16px; line-height: 1.6;'>Gracias por registrarte en Meerkadito. Para completar tu registro, usa el siguiente código de verificación:</p>"
                    +
                    "        <div style='background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); padding: 20px; text-align: center; border-radius: 8px; margin: 30px 0;'>"
                    +
                    "            <span style='color: white; font-size: 32px; font-weight: bold; letter-spacing: 8px; font-family: monospace;'>"
                    + code + "</span>" +
                    "        </div>" +
                    "        <div style='background-color: #fff3cd; border-left: 4px solid #ffc107; padding: 15px; margin: 20px 0;'>"
                    +
                    "            <p style='color: #856404; margin: 0; font-size: 14px;'>" +
                    "                <strong>⏰ Importante:</strong> Este código expira en <strong>3 minutos</strong>." +
                    "            </p>" +
                    "        </div>" +
                    "        <p style='color: #666; font-size: 14px; line-height: 1.6;'>Si no solicitaste este código, puedes ignorar este mensaje de forma segura.</p>"
                    +
                    "        <div style='border-top: 1px solid #eee; margin-top: 30px; padding-top: 20px; text-align: center;'>"
                    +
                    "            <p style='color: #999; font-size: 12px; margin: 0;'>© 2026 Meerkadito. Todos los derechos reservados.</p>"
                    +
                    "            <p style='color: #999; font-size: 12px; margin: 5px 0 0 0;'>¡Lo mejor del mercado, cerca de ti! 🛍️</p>"
                    +
                    "        </div>" +
                    "    </div>" +
                    "</body>" +
                    "</html>";

            helper.setText(htmlContent, true);

            System.out.println("Enviando correo de verificación...");
            emailSender.send(message);
            System.out.println("Correo de verificación enviado exitosamente");

        } catch (Exception e) {
            System.out.println(" ERROR ENVIANDO CÓDIGO DE VERIFICACIÓN");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}