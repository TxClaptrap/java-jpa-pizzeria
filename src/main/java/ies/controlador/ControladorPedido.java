package ies.controlador;

import ies.modelo.Pedido;
import ies.modelo.LineaPedido;
import ies.modelo.Pagable;
import ies.modelo.Producto;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ies.controlador.dao.PedidoDao;
import ies.controlador.dao.impl.JdbcPedidoDao;
import ies.modelo.Cliente;
import ies.modelo.EstadoPedido;

public class ControladorPedido {
    private PedidoDao pedidoDao = new JdbcPedidoDao();
    private Pedido pedidoActual;
    //private List<Pedido> listaFinalizados;

    public ControladorPedido(Pedido pedido) {
        pedidoActual = pedido;
        //listaFinalizados = new ArrayList<Pedido>();
    }

    public void registrarPedido(Pedido pedido) throws SQLException {
        pedidoDao.insertPedido(pedido);
    }

    public void actualizarPedido(Pedido pedido) throws SQLException {
        pedidoDao.updatePedido(pedido);
    }

    public void borrarPedido(Pedido pedido) throws SQLException {
        pedidoDao.deletePedido(pedido);
    }

    public Pedido encontrarPedidoById(int idPedido) throws SQLException {
        return pedidoDao.findPedidoById(idPedido);
    }

    public List<Pedido> encontrarPedidosByClienteId(int clienteId) throws SQLException {
        return pedidoDao.findPedidosByClienteId(clienteId);
    }

    public List<Pedido> encontrarPedidosByEstado(EstadoPedido estado) throws SQLException {
        return pedidoDao.findPedidosByEstado(estado);
    }

    public List<LineaPedido> encontrarLineasPedidoByPedidoId(int pedidoId) throws SQLException {
        return pedidoDao.findLineasPedidoByPedidoId(pedidoId);
    }

    // El agregarLineaPedido, pero con base de datos
    public void agregarLineaPedido(Producto producto, int cantidad) throws IllegalAccessException, SQLException {
        if (pedidoActual.getCliente() == null) {
            throw new IllegalAccessException("No se puede agregar un pedido sin un cliente logeado.");
        }

        if (pedidoActual.getEstado() != EstadoPedido.PENDIENTE) {
            pedidoActual = new Pedido(pedidoActual.getCliente());
        }
        try {
            // El pedido existe en la base de datos?
            Pedido pedidoExistente = pedidoDao.findPedidoById(pedidoActual.getId());
            if (pedidoExistente == null) {
                // Insertar el pedido si no
                pedidoDao.insertPedido(pedidoActual);
            } else {
                // Actualizar las líneas del pedido actual con las líneas existentes
                pedidoActual.setListaLineaPedidos(pedidoExistente.getListaLineaPedidos());
            }

            // Crear la nueva línea
            LineaPedido nuevaLinea = new LineaPedido(cantidad, producto);

            pedidoActual.getListaLineaPedidos().add(nuevaLinea);

            // Persistir las líneas actualizadas del pedido
            pedidoDao.updatePedido(pedidoActual);

            System.out.println("Producto " + producto.getNombre() + " agregado al pedido. Cantidad: " + cantidad);
        
        } catch (SQLException e) {
            System.err.println("Error al agregar la línea de pedido: " + e.getMessage());
            throw e;
        }
    }

    /*
     * //agregarLineaPedido (Versión sin base de datos, cuando lineaPedido tenía
     * atributo pedido)
     * public void agregarLineaPedido(Producto producto, int cantidad, SIZE tamaño)
     * throws IllegalAccessException {
     * if (pedidoActual.getCliente() == null) {
     * throw new
     * IllegalAccessException("No se puede agregar pedido sin estar logeado.");
     * }
     * pedidoActual.getListaLineaPedidos().add(new
     * LineaPedido(pedidoActual.getListaLineaPedidos().size() + 1, cantidad,
     * pedidoActual, producto));
     * 
     * System.out.println("Producto " + producto.getNombre() +
     * " agregado al pedido. Cantidad: " + cantidad);
     * 
     * if (pedidoActual.getEstado()!=EstadoPedido.PENDIENTE) {
     * pedidoActual.setEstado(EstadoPedido.PENDIENTE);
     * }
     * }
     */

     public void finalizarPedido(Pagable metodo) throws IllegalAccessException, SQLException {
    
        if (pedidoActual.getEstado() == EstadoPedido.PENDIENTE) {
            // Calcular el precio total del pedido
            double precioTotal = 0;
            for (LineaPedido linea : pedidoActual.getListaLineaPedidos()) {
                precioTotal += (linea.getProducto().getPrecio() * linea.getCantidad());
            }
            pedidoActual.setPrecioTotal(precioTotal);
    
            // Realizar el pago utilizando el método proporcionado
            pedidoActual.setMetodoPago(metodo);
            metodo.pagar(pedidoActual.getPrecioTotal());
    
            // Cambiar el estado del pedido a FINALIZADO
            pedidoActual.setEstado(EstadoPedido.FINALIZADO);
    
            // Actualizar el pedido en la base de datos
            PedidoDao pedidoDao = new JdbcPedidoDao();
            pedidoDao.updatePedido(pedidoActual);
    
            // Confirmación final
            System.out.println("Pedido finalizado, se entregará en breve. Ya no se pueden realizar cambios o cancelar el pedido.");
        } else {
            System.out.println("No se pudo finalizar el pedido.");
        }
    }
    
    /* 
    // finalizarPedido()
    public void finalizarPedido(Pagable metodo) throws IllegalAccessException {
        if (pedidoActual.getCliente() == null) {
            throw new IllegalAccessException("No se puede agregar pedido sin estar logeado.");
        }
        if (pedidoActual.getEstado() == EstadoPedido.PENDIENTE) {
            for (LineaPedido linea : pedidoActual.getListaLineaPedidos()) {
                pedidoActual.setPrecioTotal(
                        pedidoActual.getPrecioTotal() + (linea.getProducto().getPrecio() * linea.getCantidad()));
            }
            metodo.pagar(pedidoActual.getPrecioTotal());
            pedidoActual.setEstado(EstadoPedido.FINALIZADO);
            System.out.println(
                    "Pedido finalizado, se entregará en breve. Ya no se pueden realizar cambios o cancelar el pedido");
            listaFinalizados.add(pedidoActual);
            pedidoActual.setListaLineaPedidos(null);
        } else {
            System.out.println("No se pudo finalizar el pedido.");
        }
    }

    */

    public void cancelarPedido(Cliente clienteActual) throws IllegalAccessException, SQLException {
        if (clienteActual == null) {
            throw new IllegalAccessException("No se puede agregar pedido sin estar logeado.");
        }
    
        // Verificar si el pedido es cancelable
        if (pedidoActual.getEstado() == EstadoPedido.PENDIENTE) {
            // Cambiar el estado del pedido a CANCELADO
            pedidoActual.setEstado(EstadoPedido.CANCELADO);
    
            // Actualiza 
            pedidoDao.updatePedido(pedidoActual);
    
            System.out.println("El pedido ha sido cancelado y eliminado de la base de datos.");
        } else {
            System.out.println("No se puede cancelar el pedido en el estado actual.");
        }
    }

    /* 
    // cancelarPedido(antiguo)
    public void cancelarPedido() throws IllegalAccessException {
        if (pedidoActual.getCliente() == null) {
            throw new IllegalAccessException("No se puede agregar pedido sin estar logeado.");
        }
        if (pedidoActual.getEstado() != EstadoPedido.FINALIZADO || pedidoActual.getEstado() == EstadoPedido.ENTREGADO) {
            pedidoActual.setEstado(EstadoPedido.CANCELADO);
            System.out.println("El pedido ha sido cancelado.");
        } else {
            System.out.println("No se puede cancelar el pedido en el estado actual.");
        }
    }
        */

        public Pedido entregarPedido(int idPedido) throws SQLException {
            // Obtener el pedido de la base de datos
            Pedido pedido = pedidoDao.findPedidoById(idPedido);
        
            if (pedido != null) {
                // Verificar si es FINALIZADO
                if (pedido.getEstado() == EstadoPedido.FINALIZADO) {
                    pedido.setEstado(EstadoPedido.ENTREGADO);
        
                    // Actualizart en la base de datos
                    pedidoDao.updatePedido(pedido);
        
                    System.out.println("El pedido ha sido entregado.");
                    return pedido;
                } else {
                    System.out.println("No se puede entregar un pedido que no ha sido finalizado.");
                    return pedido;
                }
            } else {
                System.out.println("El pedido con ID " + idPedido + " no existe.");
                return null;
            }
        }
        

    /* 
    // entregarPedido()
    public void entregarPedido(int idPedido) {
        for (Pedido pedido : listaFinalizados) {
            if (idPedido == pedido.getId()) {
                pedidoActual.setEstado(EstadoPedido.ENTREGADO);
                System.out.println("El pedido ha sido entregado.");
            } else {
                System.out.println("No se puede entregar un pedido que no ha sido finalizado.");
            }
        }
    }
        */

}