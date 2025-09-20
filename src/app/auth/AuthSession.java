package app.auth;

public final class AuthSession {
    public enum Role { ADMIN, USER, GUEST }

    private static final AuthSession INSTANCE = new AuthSession();
    private String username;            // null when logged out
    private Role role = Role.GUEST;

    private AuthSession() {}

    public static AuthSession get() { return INSTANCE; }

    public boolean isLoggedIn() { return username != null; }
    public String getUsername() { return username; }
    public Role getRole() { return role; }

    // login or change role
    public void login(String username, Role role) {
        this.username = username;
        this.role = role;
    }

    public void logout() {
        this.username = null;
        this.role = Role.GUEST;
    }
}
