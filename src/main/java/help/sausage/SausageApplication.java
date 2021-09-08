package help.sausage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@Component
@Slf4j
public class SausageApplication implements ApplicationListener<ApplicationReadyEvent> {


	public static void main(String[] args) {
		SpringApplication.run(SausageApplication.class, args);
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
	}
}
