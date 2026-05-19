package users;

import enums.Language;
import java.io.Serializable;
import java.util.Objects;

/**
 * Represents the user in the system.
 */
public abstract class User implements Serializable {

    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Language language;
    private boolean isLoggedIn;

    /**
     * Constructor for User.
     * @param userId parameter value.
     * @param firstName parameter value.
     * @param lastName parameter value.
     * @param email parameter value.
     * @param password parameter value.
     */
    public User(String userId, String firstName, String lastName, String email, String password) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.language = Language.EN;
        this.isLoggedIn = false;
    }

    /**
     * login.
     * @param email parameter value.
     * @param password parameter value.
     * @return boolean
     */
    public boolean login(String email, String password) {
        if (this.email.equals(email) && this.password.equals(password)) {
            this.isLoggedIn = true;
            return true;
        }
        return false;
    }

    /**
     * logout.
     */
    public void logout() {
        this.isLoggedIn = false;
    }

    /**
     * switchLanguage.
     * @param language parameter value.
     */
    public void switchLanguage(Language language) {
        this.language = language;
    }

    // Getters and Setters
    /**
     * Gets the user id.
     * @return String
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the user id.
     * @param userId parameter value.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Gets the first name.
     * @return String
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name.
     * @param firstName parameter value.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gets the last name.
     * @return String
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name.
     * @param lastName parameter value.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gets the full name.
     * @return String
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Gets the email.
     * @return String
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email.
     * @param email parameter value.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the password.
     * @return String
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     * @param password parameter value.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the language.
     * @return Language
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * Checks if logged in.
     * @return boolean
     */
    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }

    @Override
    public String toString() {
        return getFullName() + " (" + userId + ")";
    }
}
