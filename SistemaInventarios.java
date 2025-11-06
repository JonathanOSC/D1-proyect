
SISTEMA DE INVENTARIOS D1

---

1. Registro de Clientes
@Entity
public class Cliente {
    @Id @GeneratedValue
    private Long id;
    private String nombre;
    private String email;
    // getters/setters
}

@RestController
@RequestMapping("/clientes")
public class ClienteController {
    @Autowired private ClienteRepository repo;

    @PostMapping
    public Cliente crear(@RequestBody Cliente c) {
        return repo.save(c);
    }
}

---

2. Registrar y Procesar Compras
@RestController
@RequestMapping("/compras")
public class CompraController {
    @Autowired private CompraService service;

    @PostMapping
    public ResponseEntity<Compra> procesar(@RequestBody Compra compra) {
        Compra guardada = service.registrarCompra(compra);
        return ResponseEntity.ok(guardada);
    }
}

---

3. Módulo de Inventarios
@Service
public class InventarioService {
    @Autowired private ProductoRepository repo;

    public void actualizarStock(Long idProducto, int cantidad) {
        Producto p = repo.findById(idProducto).orElseThrow();
        p.setStock(p.getStock() + cantidad);
        repo.save(p);
    }
}

---

4. Estructura Base de Datos
CREATE TABLE producto (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   nombre VARCHAR(100),
   precio DECIMAL(10,2),
   stock INT
);

---

5. Módulo de Login y Autenticación
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
            .anyRequest().authenticated()
            .and()
            .httpBasic();
    }
}

---

6. Diseñar Pantalla Principal (Vue.js)
<template>
  <div>
    <h1>Sistema de Inventarios D1</h1>
    <router-link to="/inventario">Inventario</router-link>
    <router-link to="/clientes">Clientes</router-link>
  </div>
</template>

---

7. Definir Casos y Escenarios de Prueba
@SpringBootTest
class InventarioTests {
    @Autowired private InventarioService service;

    @Test
    void testActualizarStock() {
        service.actualizarStock(1L, 10);
        assertEquals(20, service.obtenerStock(1L));
    }
}

---

8. Migración de Datos
@Component
public class DataMigrator implements CommandLineRunner {
    @Autowired private ProductoRepository repo;
    @Override
    public void run(String... args) {
        try (BufferedReader br = Files.newBufferedReader(Paths.get("productos.csv"))) {
            br.lines().forEach(l -> {
                String[] p = l.split(",");
                repo.save(new Producto(p[0], Double.parseDouble(p[1]), Integer.parseInt(p[2])));
            });
        } catch(IOException e){ e.printStackTrace(); }
    }
}

---

9. Integración con Sistema Contable
@Service
public class ContabilidadService {
    private final RestTemplate rest = new RestTemplate();

    public void enviarFactura(Factura f) {
        rest.postForObject("http://contable/api/facturas", f, Void.class);
    }
}

---

10. Gestión de Roles
@Entity
public class Rol {
    @Id @GeneratedValue
    private Long id;
    private String nombre;
}

@Service
public class UsuarioService {
    public boolean tieneRol(User u, String rol) {
        return u.getRoles().stream().anyMatch(r -> r.getNombre().equals(rol));
    }
}

---

11. Filtros y Búsqueda Avanzada
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByNombreContainingAndPrecioBetween(String nombre, double min, double max);
}

---

12. Documentación Técnica (Spring REST Docs)
@AutoConfigureRestDocs
@WebMvcTest(ProductoController.class)
class ProductoControllerDocTest {
    @Test
    void documentarProductoEndpoint() throws Exception {
        mockMvc.perform(get("/productos"))
               .andDo(document("listar-productos"));
    }
}

---

13. Envío de Notificaciones
@Service
public class NotificacionService {
    @Autowired private JavaMailSender mailSender;

    public void enviar(String to, String mensaje) {
        SimpleMailMessage m = new SimpleMailMessage();
        m.setTo(to);
        m.setSubject("Aviso Inventario");
        m.setText(mensaje);
        mailSender.send(m);
    }
}

---

14. Revisar Componentes del Carrito
@RestController
@RequestMapping("/carrito")
public class CarritoController {
    @GetMapping("/{id}")
    public Carrito revisar(@PathVariable Long id) {
        // retorna productos y totales
    }
}

---

15. Generación y Almacenamiento de Facturas Electrónicas
@Service
public class FacturaService {
    public Path generarPDF(Factura f) throws IOException {
        Path path = Paths.get("facturas/" + f.getId() + ".pdf");
        Files.write(path, facturaToPdfBytes(f));
        return path;
    }
}

---

16. Mecanismos de Seguridad
@Bean
public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}

---

17. Pruebas Automatizadas
(Ver ejemplo de JUnit en el punto 7)

---
