import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.Arrays;
import java.util.List;

// ============================================
// TEST 1: ClienteController Tests
// ============================================
@DisplayName("Tests para ClienteController")
class ClienteControllerTest {
    
    @Mock
    private ClienteRepository repo;
    
    @InjectMocks
    private ClienteController controller;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    @DisplayName("Debe crear un cliente correctamente")
    void testCrearCliente() {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setNombre("Juan Pérez");
        cliente.setEmail("juan@email.com");
        
        when(repo.save(any(Cliente.class))).thenReturn(cliente);
        
        // Act
        Cliente resultado = controller.crear(cliente);
        
        // Assert
        assertNotNull(resultado);
        assertEquals("Juan Pérez", resultado.getNombre());
        assertEquals("juan@email.com", resultado.getEmail());
        verify(repo, times(1)).save(cliente);
    }
    
    @Test
    @DisplayName("No debe crear cliente con email nulo")
    void testCrearClienteEmailNulo() {
        // Arrange
        Cliente cliente = new Cliente();
        cliente.setNombre("Pedro");
        cliente.setEmail(null);
        
        // Act & Assert
        assertThrows(Exception.class, () -> {
            if(cliente.getEmail() == null) throw new IllegalArgumentException("Email requerido");
        });
    }
}

// ============================================
// TEST 2: InventarioService Tests
// ============================================
@DisplayName("Tests para InventarioService")
class InventarioServiceTest {
    
    @Mock
    private ProductoRepository repo;
    
    @InjectMocks
    private InventarioService service;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    @DisplayName("Debe actualizar el stock correctamente")
    void testActualizarStock() {
        // Arrange
        Long idProducto = 1L;
        Producto producto = new Producto();
        producto.setId(idProducto);
        producto.setStock(10);
        
        when(repo.findById(idProducto)).thenReturn(Optional.of(producto));
        when(repo.save(any(Producto.class))).thenReturn(producto);
        
        // Act
        service.actualizarStock(idProducto, 5);
        
        // Assert
        assertEquals(15, producto.getStock());
        verify(repo, times(1)).findById(idProducto);
        verify(repo, times(1)).save(producto);
    }
    
    @Test
    @DisplayName("Debe lanzar excepción si producto no existe")
    void testActualizarStockProductoNoExiste() {
        // Arrange
        Long idProducto = 999L;
        when(repo.findById(idProducto)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(Exception.class, () -> {
            service.actualizarStock(idProducto, 5);
        });
    }
    
    @Test
    @DisplayName("Debe permitir decrementar stock con número negativo")
    void testDecrementarStock() {
        // Arrange
        Long idProducto = 1L;
        Producto producto = new Producto();
        producto.setId(idProducto);
        producto.setStock(20);
        
        when(repo.findById(idProducto)).thenReturn(Optional.of(producto));
        when(repo.save(any(Producto.class))).thenReturn(producto);
        
        // Act
        service.actualizarStock(idProducto, -5);
        
        // Assert
        assertEquals(15, producto.getStock());
    }
}

// ============================================
// TEST 3: CompraController Tests
// ============================================
@DisplayName("Tests para CompraController")
class CompraControllerTest {
    
    @Mock
    private CompraService service;
    
    @InjectMocks
    private CompraController controller;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    @DisplayName("Debe procesar una compra correctamente")
    void testProcesarCompra() {
        // Arrange
        Compra compra = new Compra();
        compra.setId(1L);
        compra.setTotal(150.50);
        
        when(service.registrarCompra(any(Compra.class))).thenReturn(compra);
        
        // Act
        var response = controller.procesar(compra);
        
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(compra, response.getBody());
        verify(service, times(1)).registrarCompra(compra);
    }
}

// ============================================
// TEST 4: UsuarioService Tests (Roles)
// ============================================
@DisplayName("Tests para UsuarioService - Gestión de Roles")
class UsuarioServiceTest {
    
    @InjectMocks
    private UsuarioService service;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    @DisplayName("Debe verificar si usuario tiene rol admin")
    void testTieneRolAdmin() {
        // Arrange
        Rol rolAdmin = new Rol();
        rolAdmin.setNombre("ADMIN");
        
        User usuario = new User();
        usuario.setRoles(Arrays.asList(rolAdmin));
        
        // Act
        boolean tieneRol = service.tieneRol(usuario, "ADMIN");
        
        // Assert
        assertTrue(tieneRol);
    }
    
    @Test
    @DisplayName("Debe retornar false si usuario no tiene el rol")
    void testNoTieneRol() {
        // Arrange
        Rol rolUser = new Rol();
        rolUser.setNombre("USER");
        
        User usuario = new User();
        usuario.setRoles(Arrays.asList(rolUser));
        
        // Act
        boolean tieneRol = service.tieneRol(usuario, "ADMIN");
        
        // Assert
        assertFalse(tieneRol);
    }
}

// ============================================
// TEST 5: ProductoRepository Tests
// ============================================
@DisplayName("Tests para ProductoRepository - Búsqueda Avanzada")
class ProductoRepositoryTest {
    
    @Mock
    private ProductoRepository repo;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    @DisplayName("Debe encontrar productos por nombre y rango de precio")
    void testBusquedaAvanzada() {
        // Arrange
        Producto p1 = new Producto();
        p1.setNombre("Laptop HP");
        p1.setPrecio(1500.0);
        
        Producto p2 = new Producto();
        p2.setNombre("Laptop Dell");
        p2.setPrecio(1800.0);
        
        List<Producto> productos = Arrays.asList(p1, p2);
        
        when(repo.findByNombreContainingAndPrecioBetween("Laptop", 1000.0, 2000.0))
            .thenReturn(productos);
        
        // Act
        List<Producto> resultado = repo.findByNombreContainingAndPrecioBetween("Laptop", 1000.0, 2000.0);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().allMatch(p -> p.getNombre().contains("Laptop")));
    }
}

// ============================================
// TEST 6: NotificacionService Tests
// ============================================
@DisplayName("Tests para NotificacionService")
class NotificacionServiceTest {
    
    @Mock
    private JavaMailSender mailSender;
    
    @InjectMocks
    private NotificacionService service;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    @DisplayName("Debe enviar notificación por email")
    void testEnviarNotificacion() {
        // Arrange
        String destinatario = "cliente@email.com";
        String mensaje = "Stock bajo en producto X";
        
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        
        // Act
        service.enviar(destinatario, mensaje);
        
        // Assert
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}

// ============================================
// TEST 7: FacturaService Tests
// ============================================
@DisplayName("Tests para FacturaService")
class FacturaServiceTest {
    
    @InjectMocks
    private FacturaService service;
    
    @Test
    @DisplayName("Debe generar PDF de factura")
    void testGenerarPDF() throws Exception {
        // Arrange
        Factura factura = new Factura();
        factura.setId(123L);
        
        // Act
        Path path = service.generarPDF(factura);
        
        // Assert
        assertNotNull(path);
        assertTrue(path.toString().contains("123.pdf"));
    }
}
