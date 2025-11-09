import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneradorFactura {
    
    public static void main(String[] args) {
        
        Tienda miTienda = new Tienda();
        
        System.out.println("🛒 Iniciando simulación de compra...");

        miTienda.calcularSubtotal("Laptop", 1); 
        miTienda.calcularSubtotal(" Mouse Inalámbrico ", 2);
        miTienda.calcularSubtotal("Disco SSD 1TB", 3);
        miTienda.calcularSubtotal("Cable HDMI", 5); // Producto que no existe
        miTienda.calcularSubtotal("Auriculares Gaming", 1);

        miTienda.mostrarResumenCompra();
    }
    
    private static class Tienda { 

        private final Map<String, Double> preciosProductos;
        
        private final List<Producto> carritoCompra;

        public Tienda() {
            // Inicialización del catálogo con 10 items
            preciosProductos = new HashMap<>();
            preciosProductos.put("Laptop", 1200.00);
            preciosProductos.put("Mouse Inalámbrico", 25.50);
            preciosProductos.put("Teclado Mecánico", 85.99);
            preciosProductos.put("Monitor 27 pulgadas", 350.75);
            preciosProductos.put("Impresora Multifunción", 150.00);
            preciosProductos.put("Webcam HD", 45.90);
            preciosProductos.put("Disco SSD 1TB", 110.50);
            preciosProductos.put("Auriculares Gaming", 70.00);
            preciosProductos.put("Router Wi-Fi 6", 99.99);
            preciosProductos.put("Memoria USB 64GB", 15.20);
            
            carritoCompra = new ArrayList<>();
        }

        public double calcularSubtotal(String nombreProducto, int cantidad) {
            String nombreNormalizado = nombreProducto.trim();

            if (preciosProductos.containsKey(nombreNormalizado)) {
                double precio = preciosProductos.get(nombreNormalizado);
                double subtotal = precio * cantidad;
                
                // Añadir el producto al carrito de compra
                carritoCompra.add(new Producto(nombreNormalizado, cantidad, precio, subtotal));
                
                return subtotal;
            } else {
                System.out.println("\n⚠️ El producto '" + nombreNormalizado + "' no se encuentra en la lista.");
                return 0.00;
            }
        }

        public void mostrarResumenCompra() {
            System.out.println("\n--- Recibo de Compra ---");
            
            if (carritoCompra.isEmpty()) {
                System.out.println("El carrito de compra está vacío.");
                return;
            }
            
            System.out.printf("%-25s %-10s %-15s %-15s%n", 
                "PRODUCTO", "CANTIDAD", "PRECIO UNITARIO", "SUBTOTAL");
            System.out.println("-----------------------------------------------------------------");
            
            double totalCompra = 0.00;
            
            for (Producto item : carritoCompra) {
                System.out.printf("%-25s %-10d %-15.2f %-15.2f%n",
                    item.getNombre(),
                    item.getCantidad(),
                    item.getPrecioUnitario(),
                    item.getSubtotal()
                );
                totalCompra += item.getSubtotal();
            }
            
            System.out.println("-----------------------------------------------------------------");
            System.out.printf("%-50s %15.2f%n", "**TOTAL A PAGAR**", totalCompra);
        }
    }
    

    private static class Producto {
        private String nombre;
        private int cantidad;
        private double precioUnitario;
        private double subtotal;

        public Producto(String nombre, int cantidad, double precioUnitario, double subtotal) {
            this.nombre = nombre;
            this.cantidad = cantidad;
            this.precioUnitario = precioUnitario;
            this.subtotal = subtotal;
        }

        public String getNombre() {
            return nombre;
        }

        public int getCantidad() {
            return cantidad;
        }

        public double getPrecioUnitario() {
            return precioUnitario;
        }

        public double getSubtotal() {
            return subtotal;
        }
    }
}