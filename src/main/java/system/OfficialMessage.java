package system;

import users.Employee;
import java.io.Serializable;
import java.time.LocalDateTime;


public class OfficialMessage implements Serializable {

    private static int counter = 1;

    private int messageId;
    private String subject;
    private String body;
    private Employee sender;
    private Employee recipient;
    private String recipientDepartment; // can be addressed to a whole department
    private LocalDateTime sentAt;
    private boolean signed;
    private String signatoryTitle; // e.g. "Dean", "Rector"

    
    public OfficialMessage(String subject, String body, Employee sender, Employee recipient,
                           boolean signed, String signatoryTitle) {
        this.messageId = counter++;
        this.subject = subject;
        this.body = body;
        this.sender = sender;
        this.recipient = recipient;
        this.sentAt = LocalDateTime.now();
        this.signed = signed;
        this.signatoryTitle = signatoryTitle;
    }

    
    public OfficialMessage(String subject, String body, Employee sender,
                           String recipientDepartment, boolean signed, String signatoryTitle) {
        this(subject, body, sender, (Employee) null, signed, signatoryTitle);
        this.recipientDepartment = recipientDepartment;
    }

    public int getMessageId() { return messageId; }
    public String getSubject() { return subject; }
    public String getBody() { return body; }
    public Employee getSender() { return sender; }
    public Employee getRecipient() { return recipient; }
    public String getRecipientDepartment() { return recipientDepartment; }
    public LocalDateTime getSentAt() { return sentAt; }
    public boolean isSigned() { return signed; }
    public String getSignatoryTitle() { return signatoryTitle; }

    @Override
    public String toString() {
        String to = (recipient != null) ? recipient.getFullName()
                : ("Dept: " + recipientDepartment);
        return "[OFFICIAL #" + messageId + "] [" + sentAt.toLocalDate() + "]"
                + (signed ? " ✓" + signatoryTitle : " (unsigned)")
                + "\n  From: " + sender.getFullName()
                + "\n  To: " + to
                + "\n  Subject: " + subject
                + "\n  " + body;
    }
}