package com.devfernandes.demoajax;

import com.devfernandes.demoajax.service.SocialMetaTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoAjaxApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(DemoAjaxApplication.class, args);
	}

	@Autowired
	SocialMetaTagService service;

	@Override
	public void run(String... args) throws Exception {
		//System.out.println(service.getSocialMetaTagByUrl("https://www.udemy.com/course/spring-boot-mvc-com-ajax/").toString());
		//System.out.println(service.getSocialMetaTagByUrl("https://www.livrariasfamiliacrista.com.br/box-11-livros-machado-de-assis-complemento-de-leitura.html").toString());
	}
}
;