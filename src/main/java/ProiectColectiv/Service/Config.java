package ProiectColectiv.Service;

import ProiectColectiv.Repository.DatabaseRepo.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class Config {
    @Bean
    Properties getProps() {
        Properties props = new Properties();
        try {
            props.load(new FileReader("bd.config"));
        }
        catch (IOException e) {
            System.err.println(e.getMessage());
        }
        return props;
    }

    @Bean
    UserRepo userRepo() {
        return new UserRepo(getProps());
    }

    @Bean
    ProductRepo productRepo() {
        return new ProductRepo(getProps());
    }

    @Bean
    OrderRepo orderRepo() {
        return new OrderRepo(getProps());
    }

    @Bean
    CartItemRepo cartItemRepo() {
        return new CartItemRepo(getProps());
    }

    @Bean
    ReviewRepo reviewRepo() {
        return new ReviewRepo(getProps());
    }
}
