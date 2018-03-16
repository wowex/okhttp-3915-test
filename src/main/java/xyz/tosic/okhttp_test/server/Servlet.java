package xyz.tosic.okhttp_test.server;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Servlet extends HttpServlet {

  private static String body; // 60KB

  static {
   final String BASE = "0123456789";
    StringBuilder sb = new StringBuilder(BASE);
    for (int i = 0; i < 6000; i++) {
      sb.append(BASE);
    }
    body = sb.toString();
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    resp.setContentType("text/plain");
    resp.getOutputStream().println(body);
  }
}
