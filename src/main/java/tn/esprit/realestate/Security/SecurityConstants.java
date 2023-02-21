package tn.esprit.realestate.Security;

public class SecurityConstants {
    public static final String SECRET = "mySecretKey";
    public static final long EXPIRATION_TIME = 864_000_000; // 10 jours en millisecondes
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/signup";
    public static final String LOGIN_URL = "/login";
}
