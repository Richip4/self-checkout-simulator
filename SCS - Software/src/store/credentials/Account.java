package store.credentials;

/**
 * Class for creating an account in the credentials system
 * 
 * @author Tyler Chen
 *
 */
public class Account {
    private final String username;
    private final String password;

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername(){
        return username;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;

        final Account other = (Account) obj;
        if (!this.username.equals(other.username))
            return false;
        
        if (!this.password.equals(other.password))
            return false;

        return true;
    }

}
