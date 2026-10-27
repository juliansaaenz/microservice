package co.sena.biblioteca.libros.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Imprime en la consola de NetBeans cada petición que LLEGA a /api.
 * Así, en la demo, se ve la petición "aterrizar" en este servicio.
 */
@Component
public class RegistroPeticiones implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(RegistroPeticiones.class);
    private final BitacoraEntrada bitacora;

    public RegistroPeticiones(BitacoraEntrada bitacora) {
        this.bitacora = bitacora;
    }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) {
        log.info("[libros-service] <-- LLEGA    {} {}", req.getMethod(), req.getRequestURI());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object handler, Exception ex) {
        log.info("[libros-service] --> RESPONDE {} a {} {}", res.getStatus(), req.getMethod(), req.getRequestURI());
        bitacora.registrar(req.getMethod(), req.getRequestURI(), res.getStatus());
    }
}
