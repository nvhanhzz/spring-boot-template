package sti.project.template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import sti.project.template.base.file.FileStorageProperties;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(FileStorageProperties.class)
public class TemplateApplication {

	public static void main(String[] args) {
		SpringApplication.run(TemplateApplication.class, args);
	}

}