package cinema.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter("/procesar-compra")
public class FiltroVentaPeliculas implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        // Inicializar contadores por película
        ServletContext ctx = filterConfig.getServletContext();
        ctx.setAttribute("LILO_STICH", 0);
        ctx.setAttribute("LA_MUJER_DE_LA_FILA", 0);
        ctx.setAttribute("DEMON_SLAYER", 0);
        ctx.setAttribute("LA_LUZ_QUE_IMAGINAMOS", 0);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        chain.doFilter(request, response); // continuar con el servlet primero

        String pelicula = request.getParameter("pelicula");
        String cantidadStr = request.getParameter("cantidadEntradas");

        if (pelicula != null && cantidadStr != null) {
            try {
                int cantidad = Integer.parseInt(cantidadStr); // cantidad pedida
                ServletContext ctx = request.getServletContext();
                Integer actual = (Integer) ctx.getAttribute(pelicula);
                if (actual == null) actual = 0;
                ctx.setAttribute(pelicula, actual + cantidad);
            } catch (NumberFormatException e) {
            }
        }
    }

    @Override
    public void destroy() { }
}
