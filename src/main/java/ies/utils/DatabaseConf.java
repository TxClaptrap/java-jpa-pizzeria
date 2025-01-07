package ies.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConf {
        public static final String URL = "jdbc:mysql://localhost:3306/pizzeria";
        public static final String USER = "root";
        public static final String PASSWORD = "admin";

        // CREATE TABLE
        public static final String CREATE_TABLE_CLIENTE = "CREATE TABLE IF NOT EXISTS clientes (" +
                        "    id INT PRIMARY KEY AUTO_INCREMENT," +
                        "    dni VARCHAR(255) NOT NULL UNIQUE," +
                        "    nombre VARCHAR(255) NOT NULL," +
                        "    direccion VARCHAR(255) NOT NULL," +
                        "    telefono VARCHAR(255) NULL UNIQUE," +
                        "    email VARCHAR(255) NOT NULL UNIQUE," +
                        "    password VARCHAR(255) NOT NULL," +
                        "    administrador BOOL DEFAULT false" +
                        ");";

        public static final String CREATE_TABLE_PRODUCTOS = "CREATE TABLE IF NOT EXISTS productos (" +
                        "    id INT PRIMARY KEY AUTO_INCREMENT," +
                        "    nombre VARCHAR(255) NOT NULL UNIQUE," +
                        "    precio DOUBLE NOT NULL," +
                        "    tipo VARCHAR(255) NOT NULL," +
                        "    size VARCHAR(255) DEFAULT NULL" +
                        ");";

        public static final String CREATE_TABLE_INGREDIENTES = "CREATE TABLE IF NOT EXISTS ingredientes (" +
                        "    id INT PRIMARY KEY AUTO_INCREMENT," +
                        "    nombre VARCHAR(255) NOT NULL UNIQUE" +
                        ");";

        public static final String CREATE_TABLE_ALERGENOS = "CREATE TABLE IF NOT EXISTS alergenos (" +
                        "    id INT PRIMARY KEY AUTO_INCREMENT," +
                        "    nombre VARCHAR(255) NOT NULL UNIQUE" +
                        ");";

        public static final String CREATE_TABLE_PRODUCTO_INGREDIENTE = "CREATE TABLE IF NOT EXISTS producto_ingrediente ("
                        +
                        "    producto_id INT," +
                        "    ingrediente_id INT," +
                        "    PRIMARY KEY (producto_id, ingrediente_id)," +
                        "    FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE CASCADE ON UPDATE CASCADE," +
                        "    FOREIGN KEY (ingrediente_id) REFERENCES ingredientes(id) ON DELETE CASCADE ON UPDATE CASCADE"
                        +
                        ");";

        public static final String CREATE_TABLE_INGREDIENTE_ALERGENO = "CREATE TABLE IF NOT EXISTS ingrediente_alergeno ("
                        +
                        "    ingrediente_id INT," +
                        "    alergeno_id INT," +
                        "    PRIMARY KEY (ingrediente_id, alergeno_id)," +
                        "    FOREIGN KEY (ingrediente_id) REFERENCES ingredientes(id) ON DELETE CASCADE ON UPDATE CASCADE,"
                        +
                        "    FOREIGN KEY (alergeno_id) REFERENCES alergenos(id) ON DELETE CASCADE ON UPDATE CASCADE" +
                        ");";

        public static final String CREATE_TABLE_PEDIDOS = "CREATE TABLE IF NOT EXISTS pedidos (" +
                        "    id INT PRIMARY KEY AUTO_INCREMENT," +
                        "    fecha DATE NOT NULL," +
                        "    precio_total DOUBLE NOT NULL," +
                        "    cliente_id INT NOT NULL," +
                        "    estado ENUM('PENDIENTE', 'FINALIZADO', 'ENTREGADO', 'CANCELADO') DEFAULT 'PENDIENTE'," +
                        "    pago ENUM('PAGAR_EFECTIVO', 'PAGAR_TARJETA') DEFAULT null," +
                        "    FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE CASCADE ON UPDATE CASCADE" +
                        ");";

        public static final String CREATE_TABLE_LINEAS_PEDIDO = "CREATE TABLE IF NOT EXISTS lineasPedido (" +
                        "    id INT PRIMARY KEY AUTO_INCREMENT," +
                        "    cantidad INT NOT NULL," +
                        "    precio DOUBLE NOT NULL," +
                        "    pedido_id INT NOT NULL," +
                        "    producto_id INT NOT NULL," +
                        "    FOREIGN KEY (pedido_id) REFERENCES pedidos(id) ON DELETE CASCADE ON UPDATE CASCADE," +
                        "    FOREIGN KEY (producto_id) REFERENCES productos(id) ON DELETE CASCADE ON UPDATE CASCADE" +
                        ");";

        // DROP TABLE
        private static final String DROP_TABLE_PRODUCTO_INGREDIENTE = "DROP TABLE IF EXISTS producto_ingrediente";
        private static final String DROP_TABLE_INGREDIENTE_ALERGENO = "DROP TABLE IF EXISTS ingrediente_alergeno";
        private static final String DROP_TABLE_LINEAS_PEDIDO = "DROP TABLE IF EXISTS lineasPedido";
        private static final String DROP_TABLE_PEDIDOS = "DROP TABLE IF EXISTS pedidos";
        private static final String DROP_TABLE_CLIENTES = "DROP TABLE IF EXISTS clientes";
        private static final String DROP_TABLE_PRODUCTOS = "DROP TABLE IF EXISTS productos";
        private static final String DROP_TABLE_INGREDIENTES = "DROP TABLE IF EXISTS ingredientes";
        private static final String DROP_TABLE_ALERGENOS = "DROP TABLE IF EXISTS alergenos";

        public static void createTables() throws SQLException {
                try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                                Statement stmt = conn.createStatement()) {

                        // Creación en orden de dependencias
                        stmt.execute(CREATE_TABLE_CLIENTE);
                        stmt.execute(CREATE_TABLE_PRODUCTOS);
                        stmt.execute(CREATE_TABLE_INGREDIENTES);
                        stmt.execute(CREATE_TABLE_ALERGENOS);
                        stmt.execute(CREATE_TABLE_PEDIDOS);
                        stmt.execute(CREATE_TABLE_LINEAS_PEDIDO);
                        stmt.execute(CREATE_TABLE_PRODUCTO_INGREDIENTE);
                        stmt.execute(CREATE_TABLE_INGREDIENTE_ALERGENO);
                        System.out.println("Tablas creadas correctamente");
                }
        }

        public static void dropTables() throws SQLException {
                try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                                Statement stmt = conn.createStatement()) {

                        // Eliminación en orden inverso de dependencias
                        stmt.execute(DROP_TABLE_PRODUCTO_INGREDIENTE);
                        stmt.execute(DROP_TABLE_INGREDIENTE_ALERGENO);
                        stmt.execute(DROP_TABLE_LINEAS_PEDIDO);
                        stmt.execute(DROP_TABLE_PEDIDOS);
                        stmt.execute(DROP_TABLE_CLIENTES);
                        stmt.execute(DROP_TABLE_PRODUCTOS);
                        stmt.execute(DROP_TABLE_INGREDIENTES);
                        stmt.execute(DROP_TABLE_ALERGENOS);
                        System.out.println("Tablas eliminadas correctamente");
                }
        }

        public static void dropAndCreateTables() throws SQLException {
                dropTables();
                createTables();
        }
}
