import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

public class RequestPaymentApp {
    private User currentUser;
    private StringProperty currentUserProperty;

    public RequestPaymentApp() {
        currentUserProperty = new SimpleStringProperty("");
    }

    public boolean registerUser(String username, String password, double initialBalance) {
        if (SharedDataStore.hasUser(username)) {
            return false;
        }
        SharedDataStore.addUser(username, new User(username, password, initialBalance));
        return true;
    }

    public boolean login(String username, String password) {
        User user = SharedDataStore.getUser(username);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            currentUserProperty.set(username);
            return true;
        }
        return false;
    }

    public void logout() {
        currentUser = null;
        currentUserProperty.set("");
    }

    public void createRequest(String title, String description, double amount) {
        if (currentUser != null) {
            PaymentRequest request = new PaymentRequest(
                SharedDataStore.getNextRequestId(),
                currentUser.getUsername(),
                title,
                description,
                amount
            );
            SharedDataStore.addRequest(request);
            currentUser.setBalance(currentUser.getBalance() - amount);
        }
    }

    public boolean cancelRequest(int requestId) {
        PaymentRequest request = SharedDataStore.findRequest(requestId);
        if (request != null && request.getRequester().equals(currentUser.getUsername()) 
            && request.getStatus() == RequestStatus.OPEN) {
            request.setStatus(RequestStatus.CANCELLED);
            currentUser.setBalance(currentUser.getBalance() + request.getAmount());
            return true;
        }
        return false;
    }

    public void acceptRequest(int requestId) {
        PaymentRequest request = SharedDataStore.findRequest(requestId);
        if (request != null && !request.getRequester().equals(currentUser.getUsername()) 
            && request.getStatus() == RequestStatus.OPEN 
            && currentUser.getBalance() >= request.getAmount()) {
            request.setStatus(RequestStatus.ACCEPTED);
            request.setAccepter(currentUser.getUsername());
            currentUser.setBalance(currentUser.getBalance() - request.getAmount());
        }
    }

    public void completeRequest(int requestId) {
        PaymentRequest request = SharedDataStore.findRequest(requestId);
        if (request != null && request.getStatus() == RequestStatus.ACCEPTED) {
            request.setStatus(RequestStatus.COMPLETED);
            User accepter = SharedDataStore.getUser(request.getAccepter());
            if (accepter != null) {
                accepter.setBalance(accepter.getBalance() + request.getAmount() * 2);
            }
        }
    }

    public String listOpenRequests() {
        return SharedDataStore.getRequests().stream()
            .filter(r -> r.getStatus() == RequestStatus.OPEN)
            .map(r -> String.format("ID: %d | %s | ₹%.2f | %s | By: %s", 
                r.getId(), r.getTitle(), r.getAmount(), r.getDescription(), r.getRequester()))
            .collect(Collectors.joining("\n"));
    }

    public double getUserBalance() {
        return currentUser != null ? currentUser.getBalance() : 0.0;
    }

    public StringProperty currentUserProperty() {
        return currentUserProperty;
    }
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/payment_app",
            "root",
            "Itismyshield@1"
        );
    }
    private PaymentRequest findRequest(int id) {
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
                String accepter = rs.getString("accepter");
                if (accepter != null) {
                    request.setAccepter(accepter);
                }
                return request;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void sendMessage(int requestId, String content) {
        if (currentUser == null) return;
        
        String sql = "INSERT INTO messages (request_id, sender, content, timestamp) VALUES (?, ?, ?, NOW())";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, requestId);
            stmt.setString(2, currentUser.getUsername());
            stmt.setString(3, content);
            stmt.executeUpdate();
            
            System.out.println("Message sent by: " + currentUser.getUsername());
            
        } catch (SQLException e) {
            System.out.println("Error sending message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean canAccessChat(int requestId) {
        String sql = "SELECT requester, accepter FROM requests WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, requestId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String requester = rs.getString("requester");
                String accepter = rs.getString("accepter");
                String currentUsername = currentUser.getUsername();
                
                System.out.println("Checking chat access for request " + requestId);
                System.out.println("Requester: " + requester);
                System.out.println("Accepter: " + accepter);
                System.out.println("Current user: " + currentUsername);
                
                return currentUsername.equals(requester) || 
                       (accepter != null && currentUsername.equals(accepter));
            }
        } catch (SQLException e) {
            System.out.println("Error checking chat access: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
    public List<Message> getChatMessages(int requestId) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM messages WHERE request_id = ? ORDER BY timestamp ASC";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, requestId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                String sender = rs.getString("sender");
                String content = rs.getString("content");
                messages.add(new Message(sender, content));
                System.out.println("Retrieved message from " + sender + ": " + content);
            }
            
        } catch (SQLException e) {
            System.out.println("Error getting messages: " + e.getMessage());
            e.printStackTrace();
        }
        
        return messages;
    }
}