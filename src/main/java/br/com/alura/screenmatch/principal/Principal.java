package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.models.*;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=d835230e";
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private List<DadosSerie> dadosSeries = new ArrayList<>();
    private SerieRepository repository;
    private List<Serie> series = new ArrayList<>();
    private Optional<Serie> serieBusca;

    public Principal(SerieRepository repository) {
        this.repository = repository;
    }

    public void exibeMenu() {
        series = repository.findAll();
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar as séries buscadas
                    4 - Buscar série por título
                    5 - Buscar séries por ator
                    6 - Buscar as top 5 séries
                    7 - Buscar séries por categoria
                    8 - Buscar séries pela quantidade de temporadas
                    9 - Buscar episódio por trecho
                    10 - Buscar os 5 melhores episodios de uma serie   
                    11 - Buscar episódios a partir de uma data                 
                    
                    0 - Sair                                 
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();


            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriesPorAtor();
                    break;
                case 6:
                    buscarTop5Series();
                    break;
                case 7:
                    buscarSeriesPorCategoria();
                    break;
                case 8:
                    buscarSeriesPorQuantTemporadas();
                    break;
                case 9:
                    buscaEpisodioPorTrecho();
                case 10:
                    top5EpisodiosPorSerie();
                    break;
                case 11:
                    buscarEpisodiosPorSerieEAno();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarEpisodiosPorSerieEAno() {
        buscarSeriePorTitulo();
        if(serieBusca.isPresent()) {
            System.out.println("Digite o ano de início para a busca: ");
            var anoEpisodios = leitura.nextInt();
            List<Episodio> episodios = repository.buscarEpisodiosPorSerieEAno(serieBusca.get(), anoEpisodios);
            episodios.forEach(System.out::println);
        }
    }

    private void top5EpisodiosPorSerie() {
        buscarSeriePorTitulo();
        if(serieBusca.isPresent()) {
            Serie serie = serieBusca.get();
            System.out.println(repository.topEpisodiosPorSerie(serie));
            List<Episodio> episodios = repository.topEpisodiosPorSerie(serie);
            episodios.forEach(episodio -> System.out.printf("Serie: %s Temporada: %s Episodio: %s - %s Avaliação: %s \n",
                    episodio.getSerie().getTitulo(), episodio.getTemporada(),
                    episodio.getNumeroEpisodios(), episodio.getTitulo(), episodio.getAvaliacao()));
        }
    }

    private void buscaEpisodioPorTrecho() {
        System.out.println("Qual o nome do episódio para busca: ");
        var trechoEpisodio = leitura.nextLine();
        List<Episodio> episodiosEncontrados = repository.episodiosPorTrecho(trechoEpisodio);
        episodiosEncontrados.forEach(episodio -> System.out.printf("Serie: %s Temporada: %s Episodio: %s - %s \n",
                episodio.getSerie().getTitulo(), episodio.getTemporada(),
                episodio.getNumeroEpisodios(), episodio.getTitulo()));
    }

    private void buscarSeriesPorQuantTemporadas() {
        System.out.println("Digite a quantidade máxima de temporadas: ");
        var numeroTemporadas = leitura.nextInt();
        System.out.println("Digite o valor mínimo da avaliação: ");
        var avaliacao = leitura.nextDouble();
        List<Serie> series = repository
                .seriesPorTemporadaEAvaliacao(numeroTemporadas, avaliacao);
        series.forEach(serie -> System.out.println("Nome da série: " + serie.getTitulo() +
                ", avaliação: " + serie.getAvaliacao()));
    }

    private void buscarSeriesPorCategoria() {
        System.out.println("Deseja buscar séries de qual categoria? ");
        var nomeGenero = leitura.nextLine();
        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        List<Serie> seriesPorCategoria = repository.findByGenero(categoria);
        System.out.println("Séries da categoria: " + nomeGenero);
        seriesPorCategoria.forEach(serie -> System.out.println("Nome da série: " + serie.getTitulo() +
                ", avaliação: " + serie.getAvaliacao()));
    }

    private void buscarTop5Series() {
        List<Serie> topSeries = repository.findTop5ByOrderByAvaliacaoDesc();
        topSeries.forEach(serie -> System.out.println("Nome da série: " + serie.getTitulo() +
                ", avaliação: " + serie.getAvaliacao()));
    }

    private void buscarSeriesPorAtor() {
        System.out.println("Digite o nome do ator: ");
        var nomeAtor = leitura.nextLine();
        System.out.println("Séries com avaliacoes a partir de: ");
        var avaliacao = leitura.nextDouble();
        List<Serie> series = repository.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);
        series.forEach(serie -> System.out.println("Nome da série: " + serie.getTitulo() +
                ", avaliação: " + serie.getAvaliacao()));
    }

    private void buscarSeriePorTitulo() {
        System.out.println("Escolha uma série pelo nome: ");
        var nomeSerie = leitura.nextLine();
        Optional<Serie> serieBuscada = repository.findByTituloContainingIgnoreCase(nomeSerie);

        if(serieBuscada.isPresent()) {
            serieBusca = serieBuscada;
            System.out.println("Dados da série: " + serieBuscada.get());
        } else {
            System.out.println("Série não encontrada.");
        }

    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        try {
            repository.save(serie);
        } catch (DataIntegrityViolationException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie(){

        series.forEach(System.out::println);
        System.out.println("Escolha uma série salva no banco de dados: ");
        var nomeSerie = leitura.nextLine();

        Optional<Serie> serie = repository.findByTituloContainingIgnoreCase(nomeSerie);

        if (serie.isPresent()) {
            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo()
                        .replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);
            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.dadosEpisodios().stream()
                            .map(e -> new Episodio(d.numeroTemporada(), e)))
                    .toList();
            serieEncontrada.setEpisodios(episodios);
            repository.save(serieEncontrada);
        } else {
            System.out.println("Série não encontrada.");
        }
    }

    private void listarSeriesBuscadas() {
        series = repository.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }
}
