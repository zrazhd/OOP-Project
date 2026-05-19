package system;

import users.Employee;
import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * Represents the official message in the system.
 */
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

    /**
     * Gets the message id.
     * @return int
     */
    public int getMessageId() { return messageId; }
    /**
     * Gets the subject.
     * @return String
     */
    public String getSubject() { return subject; }
    /**
     * Gets the body.
     * @return String
     */
    public String getBody() { return body; }
    /**
     * Gets the sender.
     * @return Employee
     */
    public Employee getSender() { return sender; }
    /**
     * Gets the recipient.
     * @return Employee
     */
    public Employee getRecipient() { return recipient; }
    /**
     * Gets the recipient department.
     * @return String
     */
    public String getRecipientDepartment() { return recipientDepartment; }
    /**
     * Gets the sent at.
     * @return LocalDateTime
     */
    public LocalDateTime getSentAt() { return sentAt; }
    /**
     * Checks if signed.
     * @return boolean
     */
    public boolean isSigned() { return signed; }
    /**
     * Gets the signatory title.
     * @return String
     */
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