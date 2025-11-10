import java.util.ArrayList;
import java.util.List;

public class SimulacionMigracion {
    
    public static void main(String[] args) {
        
        FileMigrator migrator = new FileMigrator();
        
        System.out.println("🚀 --- INICIANDO SIMULACIÓN DE MIGRACIÓN DE ARCHIVOS ---");

        List<CasoPrueba> casos = new ArrayList<>();
        casos.add(new CasoPrueba("documento_ok.pdf", 45.0, "EXITO"));     // Dentro del límite
        casos.add(new CasoPrueba("imagen_grande.jpg", 150.5, "FALLO"));  // Demasiado grande
        casos.add(new CasoPrueba("registro_vacio.dat", 0.0, "ERROR"));   // Tamaño cero
        casos.add(new CasoPrueba("temp.log", 1.0, "EXITO"));             // Límite inferior
        casos.add(new CasoPrueba("  ", 10.0, "ERROR"));                  // Sin nombre
        casos.add(new CasoPrueba("mini.dat", 0.5, "ERROR"));             // Demasiado pequeño

        for (int i = 0; i < casos.size(); i++) {
            CasoPrueba caso = casos.get(i);
            
            MigrationResult resultado = migrator.simularMigracionArchivo(caso.nombre, caso.tamano, caso.mensajeEsperado);
            
            System.out.println("\n--- CASO DE PRUEBA #" + (i + 1) + " ---");
            System.out.printf("   Archivo: %s (%.2f KB)%n", caso.nombre.trim().isEmpty() ? "SIN NOMBRE" : caso.nombre, caso.tamano);
            System.out.println("   Resultado Esperado: " + caso.estadoEsperado);
            System.out.println("   Resultado Obtenido: " + resultado.estado());
            System.out.println("   Mensaje: " + resultado.mensaje());
            
            // Validación simple para mostrar si la simulación cumple lo esperado
            if (resultado.estado().equals(caso.estadoEsperado)) {
                System.out.println("   ✅ ESTATUS: Coincide con lo esperado.");
            } else {
                System.out.println("   ❌ ESTATUS: NO coincide con lo esperado.");
            }
        }
    }

    private record CasoPrueba(String nombre, double tamano, String estadoEsperado) {}

    public record MigrationResult(String estado, String mensaje) {}


    private static class FileMigrator { 
        
        private static final double MAX_SIZE_KB = 50.0;
        private static final double MIN_SIZE_KB = 1.0;

        public MigrationResult simularMigracionArchivo(String nombreArchivo, double tamanoKb, String mensajeEsperado) {
            
            // 1. Validación de nombre o tamaño inválido
            if (nombreArchivo == null || nombreArchivo.trim().isEmpty() || tamanoKb <= 0) {
                return new MigrationResult("ERROR", "Nombre o tamaño de archivo inválido.");
            }
            
            // 2. Migración exitosa (tamaño estándar)
            if (tamanoKb >= MIN_SIZE_KB && tamanoKb <= MAX_SIZE_KB) {
                return new MigrationResult("EXITO", 
                    String.format("Archivo '%s' de %.2f KB migrado correctamente.", nombreArchivo, tamanoKb));
            }
            
            // 3. Archivo demasiado pequeño
            if (tamanoKb < MIN_SIZE_KB) {
                 return new MigrationResult("ERROR", 
                    String.format("Archivo '%s' es demasiado pequeño (%.2f KB). Mínimo: %.1f KB.", nombreArchivo, tamanoKb, MIN_SIZE_KB));
            }

            // 4. Archivo demasiado grande (FALLO)
            if (tamanoKb > MAX_SIZE_KB) {
                return new MigrationResult("FALLO", 
                    String.format("Archivo '%s' es demasiado grande (%.2f KB). Límite: %.1f KB.", nombreArchivo, tamanoKb, MAX_SIZE_KB));
            }
            
            return new MigrationResult("ERROR", "Estado de migración desconocido.");
        }
    }
}