package ua.cafe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.SpringVersion;

@SpringBootApplication
public class CafeApplication {
    public static void main(String[] args) {
        System.out.println("Spring version: " + SpringVersion.getVersion());
        System.out.println("Java version: " + System.getProperty("java.version"));
        SpringApplication.run(CafeApplication.class, args);
    }
}
