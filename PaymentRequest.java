import java.util.ArrayList;
import java.util.List;

public class PaymentRequest {
    private int id;
    private String requester;
    private String accepter;
    private String title;
    private String description;
    private double amount;
    private RequestStatus status;
    private List<Message> chat;

    public PaymentRequest(int id, String requester, String title, String description, double amount) {
        this.id = id;
        this.requester = requester;
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.status = RequestStatus.OPEN;
        this.chat = new ArrayList<>();
    }

    // Existing getters
    public int getId() { return id; }
    public String getRequester() { return requester; }
    public String getAccepter() { return accepter; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public double getAmount() { return amount; }
    public RequestStatus getStatus() { return status; }
    public List<Message> getChat() { return chat; }

    // Existing setters
    public void setAccepter(String accepter) { this.accepter = accepter; }
    public void setStatus(RequestStatus status) { this.status = status; }
    
    // New setter for chat
    public void setChat(List<Message> chat) { 
        this.chat = new ArrayList<>(chat); 
    }

    // Existing method for adding messages
    public void addMessage(Message message) {
        if (this.chat == null) {
            this.chat = new ArrayList<>();
        }
        this.chat.add(message);
    }
}