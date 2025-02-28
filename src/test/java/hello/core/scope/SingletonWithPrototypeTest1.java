package hello.core.scope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

public class SingletonWithPrototypeTest1 {

    @Test
    void prototypeFind(){
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(PrototypeBean.class);
        PrototypeBean prototypeBean1 = applicationContext.getBean(PrototypeBean.class);
        prototypeBean1.addCount();
        Assertions.assertThat(prototypeBean1.getCount()).isEqualTo(1);

        PrototypeBean prototypeBean2 = applicationContext.getBean(PrototypeBean.class);
        prototypeBean2.addCount();
        Assertions.assertThat(prototypeBean2.getCount()).isEqualTo(1);

    }

    @Test
    void singletonClientUsePrototype(){
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);

        ClientBean clientBean1 = applicationContext.getBean(ClientBean.class);
        int count1 = clientBean1.logic();
        Assertions.assertThat(count1).isEqualTo(1);

        ClientBean clientBean2 = applicationContext.getBean(ClientBean.class);
        int count2 = clientBean2.logic();
        Assertions.assertThat(count2).isEqualTo(2);

    }

    @Test
    void singletonClientUsePrototypeWithProvider(){
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ClientBeanProperty.class, PrototypeBean.class);

        ClientBeanProperty clientBean1 = applicationContext.getBean(ClientBeanProperty.class);
        int count1 = clientBean1.logic();
        Assertions.assertThat(count1).isEqualTo(1);

        ClientBeanProperty clientBean2 = applicationContext.getBean(ClientBeanProperty.class);
        int count2 = clientBean2.logic();
        Assertions.assertThat(count2).isEqualTo(1);

    }

    @Scope("prototype")
    static class PrototypeBean{

        private int count = 0;
        public void addCount(){
            count++;
        }

        private int getCount(){
            return count;
        }

        @PostConstruct
        private void init(){
            System.out.println("PrototypeBean.init " + this);
        }

        @PreDestroy
        private void destroy(){
            System.out.println("PrototypeBean.destroy");
        }

    }

    @Scope("singleton")
    static class ClientBean{
        private final PrototypeBean prototypeBean;

        @Autowired
        public ClientBean(PrototypeBean prototypeBean) {
            this.prototypeBean = prototypeBean;
        }

        public int logic(){
            prototypeBean.addCount();
            return prototypeBean.getCount();
        }
    }

    @Scope("singleton")
    static class ClientBeanProperty{
        @Autowired
        private ObjectProvider<PrototypeBean> prototypeBeanObjectProvider;

        public int logic(){
            PrototypeBean prototypeBean = prototypeBeanObjectProvider.getObject();
            prototypeBean.addCount();
            return prototypeBean.getCount();
        }
    }
}

