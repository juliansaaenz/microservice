package co.sena.biblioteca.prestamos.repositorio;

import co.sena.biblioteca.prestamos.modelo.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
}
