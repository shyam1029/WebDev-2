import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class applservlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public applservlet() {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String jdbcURL = "jdbc:mysql://localhost:3306/project2";
        String jdbcUsername = "root";
        String jdbcPassword = "root";
        PrintWriter out = response.getWriter();
        String name = request.getParameter("name");
        String dob = request.getParameter("dob");
        String gender = request.getParameter("gender");
        String fname = request.getParameter("fname");
        String verifytype = request.getParameter("verifytype");
        String verifyid = request.getParameter("verifyid");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String address = request.getParameter("address");
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);

            // Check if user already exists
            if (userExists(connection, name, verifyid)) {
                out.println("User with this name or verification ID already exists.");
            } else {
                // Generate unique ID
                String uniqueID = generateUniqueID(connection);

                String sql = "INSERT INTO application(uniqueID, name, dob, gender, fname, verifytype, verifyid, phone, email, address) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, uniqueID);
                preparedStatement.setString(2, name);
                preparedStatement.setString(3, dob);
                preparedStatement.setString(4, gender);
                preparedStatement.setString(5, fname);
                preparedStatement.setString(6, verifytype);
                preparedStatement.setString(7, verifyid);
                preparedStatement.setString(8, phone);
                preparedStatement.setString(9, email);
                preparedStatement.setString(10, address);
                
                int row = preparedStatement.executeUpdate();

                if (row > 0) {
                    request.setAttribute("uniqueID", uniqueID);
                    request.getRequestDispatcher("/success.jsp").forward(request, response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            out.println("Error occurred: " + e.getMessage());
        } finally {
            try {
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

    private boolean userExists(Connection connection, String name, String verifyid) throws Exception {
        String sql = "SELECT COUNT(*) FROM application WHERE name = ? OR verifyid = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, verifyid);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);
        resultSet.close();
        preparedStatement.close();
        return count > 0;
    }

    private String generateUniqueID(Connection connection) throws Exception {
        Random random = new Random();
        String uniqueID;
        while (true) {
            uniqueID = "CI" + String.format("%08d", random.nextInt(100000000));
            String sql = "SELECT COUNT(*) FROM application WHERE uniqueID = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, uniqueID);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            resultSet.close();
            preparedStatement.close();
            if (count == 0) {
                break;
            }
        }
        return uniqueID;
    }
}
