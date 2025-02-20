package santiago.barr.befit;

public class User {
    public String userId;
    public String name;
    public String email;
    public String phone;
    public String profileImageUrl;

    // Constructor vacío requerido por Firebase
    public User() {}

    public User(String userId, String name, String email, String phone, String profileImageUrl) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.profileImageUrl = profileImageUrl;
    }
}
