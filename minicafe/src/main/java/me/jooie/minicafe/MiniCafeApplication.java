package me.jooie.minicafe;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"me.jooie.minicafe", "me.jooie.minicafe.control"})
public class MiniCafeApplication {
    public static void main(String[] args){
        SpringApplication.run(MiniCafeApplication.class,args);
    }
}
