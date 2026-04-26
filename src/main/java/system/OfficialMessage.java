package system;

import users.Employee;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * OfficialMessage represents a signed, official internal communication
 * between employees — for example, booking a room for an exam,
 * or notifying about a scheduled event.
 *
 * Unlike a regular Message (which is informal), OfficialMessage
 * includes a subject, requires a signature, and is addressed to
 * a department or specific recipient.
 */
public class OfficialMessage implements Serializable {
    private static final long serialVersionUID = 1L;

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

    /**
     * Constructor for person-to-person official message.
     */
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

    /**
     * Constructor for department-wide official message.
     */
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