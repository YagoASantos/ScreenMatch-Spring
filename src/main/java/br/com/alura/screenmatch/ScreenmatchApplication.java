package br.com.alura.screenmatch;

import br.com.alura.screenmatch.models.Serie;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ConsumoApi api = new ConsumoApi();
		var json = api.obterDados("https://www.omdbapi.com/?t=gilmore+girls&apikey=d835230e");
		System.out.println(json);
		ConverteDados converteDados = new ConverteDados();
		Serie dados = converteDados.obterDados(json, Serie.class);
		System.out.println(dados);
	}
}
