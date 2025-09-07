package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PpiApplication {

    public static void main(String[] args) {

        System.out.println("--- VERIFICANDO FECHA DEL SISTEMA: " + java.time.LocalDate.now() + " ---");
        SpringApplication.run(PpiApplication.class, args);
    }
}