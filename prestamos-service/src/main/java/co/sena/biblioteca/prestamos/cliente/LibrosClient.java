package co.sena.biblioteca.prestamos.cliente;

import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

/**
 * AQUÍ OCURRE LA COMUNICACIÓN ENTRE MICROSERVICIOS.
 * Cada método hace una petición HTTP a libros-service y recibe JSON.
 */
@Component
public class LibrosClient {

    private static final Logger log = LoggerFactory.getLogger(LibrosClient.class);

    private final RestTemplate rest;
    private final BitacoraSalida bitacora;
    private final String baseUrl;

    public LibrosClient(RestTemplate rest, BitacoraSalida bitacora,
                        @Value("${libros.service.url}") String baseUrl) {
        this.rest = rest;
        this.bitacora = bitacora;
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    /** GET /api/libros */
    public List<LibroDto> listar() {
        LibroDto[] libros = llamar(HttpMethod.GET, "/api/libros", LibroDto[].class);
        return libros == null ? List.of() : Arrays.asList(libros);
    }

    /** GET /api/libros/{id} */
    public LibroDto buscar(Long id) {
        return llamar(HttpMethod.GET, "/api/libros/" + id, LibroDto.class);
    }

    /** PUT /api/libros/{id}/prestar */
    public LibroDto prestar(Long id) {
        return llamar(HttpMethod.PUT, "/api/libros/" + id + "/prestar", LibroDto.class);
    }

    /** PUT /api/libros/{id}/devolver */
    public LibroDto devolver(Long id) {
        return llamar(HttpMethod.PUT, "/api/libros/" + id + "/devolver", LibroDto.class);
    }

    private <T> T llamar(HttpMethod metodo, String ruta, Class<T> tipo) {
        String url = baseUrl + ruta;
        long inicio = System.currentTimeMillis();
        log.info("[prestamos-service] --> ENVÍA   {} {}", metodo.name(), url);
        try {
            ResponseEntity<T> respuesta = rest.exchange(url, metodo, null, tipo);
            long ms = System.currentTimeMillis() - inicio;
            int codigo = respuesta.getStatusCode().value();
            log.info("[prestamos-service] <-- RECIBE  {} en {} ms", codigo, ms);
            bitacora.registrar(metodo.name(), url, String.valueOf(codigo), ms, true);
            return respuesta.getBody();
        } catch (HttpStatusCodeException e) {
            long ms = System.currentTimeMillis() - inicio;
            int codigo = e.getStatusCode().value();
            log.warn("[prestamos-service] <-- RECIBE  {} (error) en {} ms", codigo, ms);
            bitacora.registrar(metodo.name(), url, String.valueOf(codigo), ms, false);
            String mensaje = switch (codigo) {
                case 404 -> "Ese libro no existe en libros-service.";
                case 409 -> "No quedan ejemplares de ese libro en el estante.";
                default -> "libros-service respondió con un error " + codigo + ".";
            };
            throw new ServicioLibrosException(mensaje);
        } catch (ResourceAccessException e) {
            long ms = System.currentTimeMillis() - inicio;
            log.error("[prestamos-service] xx  SIN RESPUESTA de {} ({} ms)", url, ms);
            bitacora.registrar(metodo.name(), url, "Sin respuesta", ms, false);
            throw new ServicioLibrosException("libros-service no responde en " + baseUrl
                    + ". Ejecútalo en NetBeans y vuelve a intentar.");
        }
    }
}
