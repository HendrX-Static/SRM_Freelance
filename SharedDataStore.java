import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;

public class SharedDataStore {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/payment_app";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "Itismyshield@1";
    
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    // Users
    public static void addUser(String username, User user) {
        String sql = "INSERT INTO users (username, password, balance) VALUES (?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, user.getPassword());
            stmt.setDouble(3, user.getBalance());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static User getUser(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getDouble("balance")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static boolean hasUser(String username) {
        String sql = "SELECT username FROM users WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Requests

    public static void sendMessage(int requestId, Message message) {
        String sql = "INSERT INTO messages (request_id, sender, content, timestamp) VALUES (?, ?, ?, NOW())";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            stmt.setString(2, message.getSender());
            stmt.setString(3, message.getContent());
            stmt.executeUpdate();
            System.out.println("Message saved to database: " + message.getContent());
        } catch (SQLException e) {
            System.out.println("Error saving message: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void addRequest(PaymentRequest request) {
        String sql = "INSERT INTO requests (id, requester, title, description, amount, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, request.getId());
            stmt.setString(2, request.getRequester());
            stmt.setString(3, request.getTitle());
            stmt.setString(4, request.getDescription());
            stmt.setDouble(5, request.getAmount());
            stmt.setString(6, request.getStatus().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static List<PaymentRequest> getRequests() {
        List<PaymentRequest> requests = new ArrayList<>();
        String sql = "SELECT * FROM requests";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                PaymentRequest request = new PaymentRequest(
                    rs.getInt("id"),
                    rs.getString("requester"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getDouble("amount")
                );
                request.setStatus(RequestStatus.valueOf(rs.getString("status")));
                request.setAccepter(rs.getString("accepter"));
                // Load chat messages for this request
                request.setChat(getChatMessages(request.getId()));
                requests.add(request);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }
    
    public static PaymentRequest findRequest(int id) {
        String sql = "SELECT * FROM requests WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                PaymentRequest request = new PaymentRequest(
                    rs.getInt("id"),
                    rs.getString("requester"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getDouble("amount")
                );
                request.setStatus(RequestStatus.valueOf(rs.getString("status")));
                request.setAccepter(rs.getString("accepter"));
                request.setChat(getChatMessages(id));
                return request;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static int getNextRequestId() {
        String sql = "SELECT MAX(id) FROM requests";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 1;
    }

    // Chat messages
    public static List<Message> getChatMessages(int requestId) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT sender, content, timestamp FROM messages WHERE request_id = ? ORDER BY timestamp ASC";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, requestId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Message msg = new Message(
                    rs.getString("sender"),
                    rs.getString("content")
                );
                messages.add(msg);
                System.out.println("Retrieved message: " + msg.getContent() + " from " + msg.getSender());
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving messages: " + e.getMessage());
            e.printStackTrace();
        }
        return messages;
    }

    public static void updateRequest(PaymentRequest request) {
        String sql = "UPDATE requests SET status = ?, accepter = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, request.getStatus().toString());
            stmt.setString(2, request.getAccepter());
            stmt.setInt(3, request.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateUserBalance(String username, double balance) {
        String sql = "UPDATE users SET balance = ? WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, balance);
            stmt.setString(2, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}