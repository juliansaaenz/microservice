package co.sena.biblioteca.prestamos.controlador;

import co.sena.biblioteca.prestamos.modelo.Prestamo;
import co.sena.biblioteca.prestamos.servicio.PrestamoService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** API propia de préstamos (por si otro servicio o un API Gateway la necesita). */
@RestController
@RequestMapping("/api/prestamos")
public class PrestamoApiController {

    private final PrestamoService service;

    public PrestamoApiController(PrestamoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Prestamo> listar() {
        return service.listar();
    }
}
