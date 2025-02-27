package hello.core.singleton;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.junit.jupiter.api.Assertions.*;

class StatefulServiceTest {

    @Test
    void statefulServiceSingleton(){
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefultService1 = applicationContext.getBean(StatefulService.class);
        StatefulService statefultService2 = applicationContext.getBean(StatefulService.class);

        statefultService1.order("userA", 10000);
        statefultService1.order("userB", 20000);

        int price = statefultService1.getPrice();
        System.out.println("price = " + price);

        Assertions.assertThat(statefultService1.getPrice()).isEqualTo(20000);
    }

    static class TestConfig{
        @Bean
        public StatefulService statefulService(){
            return new StatefulService();
        }
    }
}