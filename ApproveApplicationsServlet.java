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

public class ApproveApplicationsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ApproveApplicationsServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String jdbcURL = "jdbc:mysql://localhost:3306/project2";
        String jdbcUsername = "root";
        String jdbcPassword = "root";
        PrintWriter out = response.getWriter();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);

            String sql = "SELECT * FROM application";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();

            out.println("<html><body><h1>Application Approval</h1><table border='1'>");
            out.println("<tr><th>UniqueID</th><th>Name</th><th>DOB</th><th>Gender</th><th>Father's Name</th><th>Verification Type</th><th>Verification ID</th><th>Phone</th><th>Email</th><th>Address</th><th>Status</th><th>Action</th></tr>");
            
            while (resultSet.next()) {
                String uniqueID = resultSet.getString("uniqueID");
                String name = resultSet.getString("name");
                String dob = resultSet.getString("dob");
                String gender = resultSet.getString("gender");
                String fname = resultSet.getString("fname");
                String verifytype = resultSet.getString("verifytype");
                String verifyid = resultSet.getString("verifyid");
                String phone = resultSet.getString("phone");
                String email = resultSet.getString("email");
                String address = resultSet.getString("address");
                String status = resultSet.getString("status");

                out.println("<tr>");
                out.println("<td>" + uniqueID + "</td>");
                out.println("<td>" + name + "</td>");
                out.println("<td>" + dob + "</td>");
                out.println("<td>" + gender + "</td>");
                out.println("<td>" + fname + "</td>");
                out.println("<td>" + verifytype + "</td>");
                out.println("<td>" + verifyid + "</td>");
                out.println("<td>" + phone + "</td>");
                out.println("<td>" + email + "</td>");
                out.println("<td>" + address + "</td>");
                out.println("<td>" + status + "</td>");
                out.println("<td><form action='UpdateStatusServlet' method='post'>");
                out.println("<input type='hidden' name='uniqueID' value='" + uniqueID + "'>");
                out.println("<select name='status'><option value='Pending'>Pending</option><option value='Approved'>Approved</option><option value='Rejected'>Rejected</option></select>");
                out.println("<input type='submit' value='Update'></form></td>");
                out.println("</tr>");
            }

            out.println("</table>");
            out.println("<button onclick=\"window.location.href='controller.html'\">Back</button>");
            out.println("<body><html>");
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