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

public class CheckStatusServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public CheckStatusServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String jdbcURL = "jdbc:mysql://localhost:3306/project2";
        String jdbcUsername = "root";
        String jdbcPassword = "root";
        PrintWriter out = response.getWriter();
        String uniqueID = request.getParameter("uniqueID");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);

            String sql = "SELECT status FROM application WHERE uniqueID = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uniqueID);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String status = resultSet.getString("status");
                out.println("<html><body><h1>Application Status</h1>");
                out.println("<p>Your application status is: <strong>" + status + "</strong></p>");
                out.println("<button onclick=\"window.location.href='checkstatus.html'\">Back</button></body></html>");
            } else {
                out.println("<html><body><h1>Application Status</h1>");
                out.println("<p>No application found with the provided unique ID.</p>");
                out.println("<button onclick=\"window.location.href='checkstatus.html'\">Back</button></body></html>");
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("Error occurred: " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}