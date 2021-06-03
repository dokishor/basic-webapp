import javax.imageio.ImageIO;
import javax.servlet.http.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class Captcha extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        String captcha = captchaGenerator(4);
        int width = 140;
        int height = 40;
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.OPAQUE);
        Graphics graphics = bufferedImage.createGraphics();
        graphics.setFont(new Font("Arial", Font.ITALIC, 16));
        graphics.setColor(new Color(50, 150, 150));
        graphics.fillOval(0, 0, width, height);
        graphics.setColor(new Color(0, 0, 100));
        graphics.drawString(captcha, width / 3, 25);

        HttpSession session = request.getSession(true);
        session.setAttribute("serverCaptcha", captcha);

        Integer tryingCounter = (Integer) session.getAttribute("tryingCounter");
        Cookie[] cookies = request.getCookies();
        if (cookies != null && tryingCounter == null)
        {
            for (Cookie cookie : cookies)
            {
                if (cookie.getName().equals("cookieCounter"))
                {
                    tryingCounter = Integer.parseInt(cookie.getValue()) + 1;
                    session.setAttribute("tryingCounter", tryingCounter);
                }
            }
        }
        else if (tryingCounter != null)
        {
            session.setAttribute("tryingCounter", tryingCounter + 1);
        }
        if (tryingCounter == null)
        {
            tryingCounter = 1;
            session.setAttribute("tryingCounter", tryingCounter);
        }
        Cookie cookieCounter = new Cookie("cookieCounter", tryingCounter.toString());
        cookieCounter.setMaxAge(24 * 60 * 60);
        response.addCookie(cookieCounter);

        OutputStream outputStream = response.getOutputStream();
        ImageIO.write(bufferedImage, "jpeg", outputStream);
        outputStream.close();
    }

    private String captchaGenerator(int captchaLength)
    {
        String captcha = "!@#$%^&*()=ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        StringBuilder captchaBuffer = new StringBuilder();
        Random random = new Random();
        int index;
        while (captchaBuffer.length() < captchaLength)
        {
            index = (int) (random.nextFloat() * captcha.length());
            captchaBuffer.append(captcha.charAt(index));
        }
        return captchaBuffer.toString();
    }
}
