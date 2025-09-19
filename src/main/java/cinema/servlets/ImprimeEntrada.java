package cinema.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletContext;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

@WebServlet("/procesar-compra")
public class ImprimeEntrada extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombres = request.getParameter("nombres");
        String apellido = request.getParameter("apellido");
        String dni = request.getParameter("dni");
        String peliculaCodigo = request.getParameter("pelicula");

        String pelicula = switch (peliculaCodigo) {
            case "LILO_STICH" -> "LILO & STICH";
            case "LA_MUJER_DE_LA_FILA" -> "LA MUJER DE LA FILA";
            case "DEMON_SLAYER" -> "DEMON SLAYER";
            case "LA_LUZ_QUE_IMAGINAMOS" -> "LA LUZ QUE IMAGINAMOS";
            default -> "Película no especificada";
        };

        boolean ganoLata = verificarGanador();
        if (ganoLata) actualizarLatasDisponibles();

        // Todo el texto descriptivo va dentro del QR
        String textoQR = ganoLata
                ? "Entrada para la pelicula " + pelicula + ".\n¡¡Felicitaciones!!\n" +
                nombres + " " + apellido + ", DNI: " + dni +
                "\nTe ganaste una LATA DE POCHOCLOS. Podes retirarla con esta entrada"
                : "Entrada para la película " + pelicula + ".\n" +
                nombres + " " + apellido + ", DNI: " + dni +
                "\n¡Segui viniendo al CINE!";

        InputStream is = getServletContext().getResourceAsStream("/images/cupon.jpeg");
        if (is == null) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "No se encontró la imagen cupon.jpeg");
            return;
        }

        BufferedImage base = ImageIO.read(is);
        Graphics2D g = base.createGraphics();

        // Configurar color y fuente grande
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 36));

        int startY = 220; // antes estaba 150, ahora bajamos ~50px
        int lineSpacing = 50; // separación entre líneas

        int centerX = base.getWidth() / 2;

        java.awt.FontMetrics fm = g.getFontMetrics();
        g.drawString("Apellido: " + apellido, centerX - fm.stringWidth("Apellido: " + apellido) / 2, startY);
        g.drawString("Nombres: " + nombres, centerX - fm.stringWidth("Nombres: " + nombres) / 2, startY + lineSpacing);
        g.drawString("DNI: " + dni, centerX - fm.stringWidth("DNI: " + dni) / 2, startY + 2 * lineSpacing);

        try {
            BufferedImage qr = generarQR(textoQR, 200, 200);
            g.drawImage(qr, (base.getWidth() - qr.getWidth()) / 2, startY + 2 * lineSpacing + 50, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        g.dispose();

        // Enviar la imagen al navegador
        response.setContentType("image/jpeg");
        ImageIO.write(base, "jpeg", response.getOutputStream());
    }

    private boolean verificarGanador() {
        return new Random().nextInt(10) < 3;
    }

    private void actualizarLatasDisponibles() {
        ServletContext ctx = getServletContext();
        Integer cant = (Integer) ctx.getAttribute("cantidadLatas");
        if (cant != null && cant > 0) ctx.setAttribute("cantidadLatas", cant - 1);
    }

    private BufferedImage generarQR(String text, int w, int h) throws WriterException {
        BitMatrix bm = new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, w, h);
        return MatrixToImageWriter.toBufferedImage(bm);
    }
}
