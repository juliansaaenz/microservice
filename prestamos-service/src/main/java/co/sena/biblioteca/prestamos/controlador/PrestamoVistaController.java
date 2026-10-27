package co.sena.biblioteca.prestamos.controlador;

import co.sena.biblioteca.prestamos.cliente.BitacoraSalida;
import co.sena.biblioteca.prestamos.cliente.LibrosClient;
import co.sena.biblioteca.prestamos.cliente.ServicioLibrosException;
import co.sena.biblioteca.prestamos.modelo.Prestamo;
import co.sena.biblioteca.prestamos.modelo.PrestamoForm;
import co.sena.biblioteca.prestamos.servicio.PrestamoService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PrestamoVistaController {

    private final PrestamoService service;
    private final LibrosClient librosClient;
    private final BitacoraSalida bitacora;

    public PrestamoVistaController(PrestamoService service, LibrosClient librosClient, BitacoraSalida bitacora) {
        this.service = service;
        this.librosClient = librosClient;
        this.bitacora = bitacora;
    }

    @ModelAttribute("librosUrl")
    public String librosUrl() {
        return librosClient.getBaseUrl();
    }

    @GetMapping("/")
    public String inicio() {
        return "redirect:/prestamos";
    }

    @GetMapping("/prestamos")
    public String lista(Model model) {
        model.addAttribute("prestamos", service.listar());
        model.addAttribute("traza", bitacora.ultimas());
        return "prestamos/lista";
    }

    @GetMapping("/prestamos/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("form", new PrestamoForm());
        cargarLibros(model);
        return "prestamos/formulario";
    }

    @PostMapping("/prestamos")
    public String crear(@Valid @ModelAttribute("form") PrestamoForm form, BindingResult errores,
                        Model model, RedirectAttributes ra) {
        if (errores.hasErrors()) {
            cargarLibros(model);
            return "prestamos/formulario";
        }
        try {
            Prestamo p = service.crear(form.getLibroId(), form.getEstudiante());
            ra.addFlashAttribute("exito", "Préstamo registrado: " + p.getTituloLibro()
                    + " para " + p.getEstudiante() + ".");
            return "redirect:/prestamos";
        } catch (ServicioLibrosException e) {
            model.addAttribute("errorServicio", e.getMessage());
            cargarLibros(model);
            return "prestamos/formulario";
        }
    }

    @PostMapping("/prestamos/{id}/devolver")
    public String devolver(@PathVariable Long id, RedirectAttributes ra) {
        try {
            Prestamo p = service.devolver(id);
            ra.addFlashAttribute("exito", "Devuelto: " + p.getTituloLibro() + ". El ejemplar volvió al estante.");
        } catch (ServicioLibrosException | IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/prestamos";
    }

    /** Pide la lista de libros a libros-service para llenar el selector. */
    private void cargarLibros(Model model) {
        try {
            model.addAttribute("libros", librosClient.listar());
        } catch (ServicioLibrosException e) {
            model.addAttribute("libros", List.of());
            if (!model.containsAttribute("errorServicio")) {
                model.addAttribute("errorServicio", e.getMessage());
            }
        }
    }
}
