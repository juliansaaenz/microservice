package co.sena.biblioteca.prestamos.servicio;

import co.sena.biblioteca.prestamos.cliente.LibroDto;
import co.sena.biblioteca.prestamos.cliente.LibrosClient;
import co.sena.biblioteca.prestamos.modelo.Prestamo;
import co.sena.biblioteca.prestamos.repositorio.PrestamoRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PrestamoService {

    private final PrestamoRepository repo;
    private final LibrosClient librosClient;

    public PrestamoService(PrestamoRepository repo, LibrosClient librosClient) {
        this.repo = repo;
        this.librosClient = librosClient;
    }

    public List<Prestamo> listar() {
        return repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    /**
     * Flujo completo de un préstamo:
     *  1. Preguntar a libros-service si el libro existe   (GET)
     *  2. Pedirle que descuente un ejemplar               (PUT .../prestar)
     *  3. Guardar el préstamo en NUESTRA base de datos
     */
    public Prestamo crear(Long libroId, String estudiante) {
        LibroDto libro = librosClient.buscar(libroId);
        librosClient.prestar(libroId);

        Prestamo p = new Prestamo();
        p.setLibroId(libro.getId());
        p.setTituloLibro(libro.getTitulo());
        p.setEstudiante(estudiante.trim());
        p.setFechaPrestamo(LocalDate.now());
        p.setEstado(Prestamo.ACTIVO);
        return repo.save(p);
    }

    /** Avisa a libros-service que el ejemplar volvió y marca el préstamo como devuelto. */
    public Prestamo devolver(Long prestamoId) {
        Prestamo p = repo.findById(prestamoId)
                .orElseThrow(() -> new IllegalArgumentException("No existe el préstamo " + prestamoId + "."));
        if (!p.isActivo()) {
            return p;
        }
        librosClient.devolver(p.getLibroId());
        p.setEstado(Prestamo.DEVUELTO);
        p.setFechaDevolucion(LocalDate.now());
        return repo.save(p);
    }
}
