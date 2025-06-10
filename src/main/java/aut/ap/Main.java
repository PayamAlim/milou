package aut.ap;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        Timestamp createAt;
        createAt = Timestamp.valueOf(LocalDateTime.now());
        System.out.println(createAt);
    }
}