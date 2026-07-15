-- =========================================================
-- Usuarios de prueba para SIGEVI
-- Las contraseñas están hasheadas con BCrypt (compatible con
-- Spring Security BCryptPasswordEncoder, que acepta prefijos
-- $2a$, $2b$ y $2y$).
-- =========================================================

-- Usuario Administrador
-- username: admin
-- password (texto plano, solo para tus pruebas en Postman): admin123
INSERT INTO tbl_usuario
(activo, fecha_creacion, ultimo_acceso, email, nombre_completo, password, username, rol)
VALUES
    (1, NOW(), NULL, 'admin@sigevi.com', 'Administrador General',
     '$2b$10$QyaH14LbZAXh0EaQeqFkNedpf7R/DTxBtNO/yE5RXQ25hIX3acNSi',
     'admin', 'ADMINISTRADOR');

-- Usuario Vendedor
-- username: vendedor1
-- password (texto plano, solo para tus pruebas en Postman): vendedor123
INSERT INTO tbl_usuario
(activo, fecha_creacion, ultimo_acceso, email, nombre_completo, password, username, rol)
VALUES
    (1, NOW(), NULL, 'vendedor1@sigevi.com', 'Vendedor de Prueba',
     '$2b$10$GLSE.2EFgwCtyM.dJCjFeuY1h52AJNca3.s//iPvfcSgO3Txe8c.O',
     'vendedor1', 'VENDEDOR');