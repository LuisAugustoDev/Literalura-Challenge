package com.augusto.literalura;

import com.augusto.literalura.main.Main;
import com.augusto.literalura.model.Autor;
import com.augusto.literalura.repository.AutorRepository;
import com.augusto.literalura.repository.IdiomaRepository;
import com.augusto.literalura.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	@Autowired
	private LivroRepository livroRepository;
	@Autowired
	private AutorRepository autorRepository;
	@Autowired
	private IdiomaRepository idiomaRepository;

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Main main = new Main(livroRepository, autorRepository, idiomaRepository);
		main.exibeMenu();
	}
}
