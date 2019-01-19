package hello;

import java.awt.EventQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringBootApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan
@EnableScheduling
public class Application {
	private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
    	ConfigurableApplicationContext context = new  SpringApplicationBuilder(Application.class).headless(false).run(args);
        EventQueue.invokeLater(() -> {
            // this is your JFrame
        	ScheduledTasks appFrame = context.getBean(ScheduledTasks.class);
            //appFrame.setVisible(true);
        });
    }
    
    
}
