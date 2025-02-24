package santiago.barr.befit;

public class Message {
    private String messageId;
    private String text;
    private long timestamp;
    private String senderId;
    private String senderName;
    private String senderProfileImage;

    public Message() {
        // Constructor vacío necesario para Firebase
    }

    public Message(String messageId, String text, long timestamp, String senderId, String senderName, String senderProfileImage) {
        this.messageId = messageId;
        this.text = text;
        this.timestamp = timestamp;
        this.senderId = senderId;
        this.senderName = senderName;
        this.senderProfileImage = senderProfileImage;
    }

    // Getters y setters
    public String getMessageId() { return messageId; }
    public String getText() { return text; }
    public long getTimestamp() { return timestamp; }
    public String getSenderId() { return senderId; }
    public String getSenderName() { return senderName; }
    public String getSenderProfileImage() { return senderProfileImage; }
}
