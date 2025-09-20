-- Tabal no relacional con los datos de los registros dentro del sistema
CREATE TABLE AccesosUsuarios (
    id_acceso INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT,
    rolUsuario VARCHAR(50),
    fecha_acceso DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    resultado ENUM('éxito', 'fallo') NOT NULL
);
