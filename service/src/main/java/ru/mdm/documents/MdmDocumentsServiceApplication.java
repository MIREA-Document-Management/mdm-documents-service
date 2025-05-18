package ru.mdm.documents;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MdmDocumentsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MdmDocumentsServiceApplication.class, args);
	}

}
