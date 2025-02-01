import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class loginservlet extends HttpServlet {
   public loginservlet() {
   }

   protected void doGet(HttpServletRequest var1, HttpServletResponse var2) throws ServletException, IOException {
      var2.setContentType("text/html");
      PrintWriter var3 = var2.getWriter();
      String var4 = var1.getParameter("user");
      String var5 = var1.getParameter("pass");
      String var6 = "jdbc:mysql://localhost:3306/project2";
      String var7 = "root";
      String var8 = "root";

      try {
         Class.forName("com.mysql.jdbc.Driver");
         Connection var9 = DriverManager.getConnection(var6, var7, var8);
         String var10 = "SELECT * FROM controller WHERE user=? AND pass=?";
         PreparedStatement var11 = var9.prepareStatement(var10);
         var11.setString(1, var4);
         var11.setString(2, var5);
         ResultSet var12 = var11.executeQuery();
         if (var12.next()) {
            var2.sendRedirect("controller.html");
         } else {
            var3.println("Invalid username or password. Please try again.");
         }

         var9.close();
      } catch (Exception var13) {
         var3.println("Error: " + var13.getMessage());
      }

   }
}
