package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.models.DadosEpisodio;
import br.com.alura.screenmatch.models.DadosSerie;
import br.com.alura.screenmatch.models.DadosTemporada;
import br.com.alura.screenmatch.models.Episodio;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner scanner = new Scanner(System.in);
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=d835230e";
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    public void exibeMenu() {
        System.out.println("Digite o nome da série: ");
        var nomeSerie = scanner.nextLine();
        nomeSerie = nomeSerie.replace(" ", "+");
        var json = this.consumoApi.obterDados(ENDERECO + nomeSerie + API_KEY);
//        https://www.omdbapi.com/?t=gilmore+girls&apikey=d835230e
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);
        List<DadosTemporada> listaDadosTemporadas = new ArrayList<>();
        for (int i = 1; i <= dados.totalTemps(); i++) {
            json = consumoApi.obterDados(ENDERECO + nomeSerie + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            listaDadosTemporadas.add(dadosTemporada);
        }
        listaDadosTemporadas.forEach(System.out::println);

        List<DadosEpisodio> episodiosTemporada = new ArrayList<>();
//        for(int i = 0; i < dados.totalTemps(); i++) {
//            episodiosTemporada = listaTemporadas.get(i).episodios();
//            for (int j = 0; j < episodiosTemporada.size(); j++) {
//                System.out.println(episodiosTemporada.get(j).titulo());
//            }
//        }
        listaDadosTemporadas.forEach(dadosTemporada -> dadosTemporada.dadosEpisodios().forEach(dadosEpisodio ->
                System.out.println(dadosEpisodio.titulo())));

        List<DadosEpisodio> dadosEpisodio = listaDadosTemporadas.stream()
                .flatMap(dadosTemporada -> dadosTemporada.dadosEpisodios().stream())
                .collect(Collectors.toList());

//        dadosEpisodio.stream()
//                .filter(episodio -> !episodio.avaliacao().equalsIgnoreCase("N/A"))
//                .peek(e -> System.out.println("Primeiro filtro(N/A): " + e))
//                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
//                .peek(e -> System.out.println("Ordenação: )" + e))
//                .limit(5)
//                .peek(e -> System.out.println("Limitando lista: " + e))
//                .map(episodio -> episodio.titulo().toUpperCase())
//                .forEach(System.out::println);

        List<Episodio> episodios = listaDadosTemporadas.stream()
                .flatMap(temporada -> temporada.dadosEpisodios().stream()
                        .map(episodio -> new Episodio(temporada.numeroTemporada(), episodio)))
                .toList();
        episodios.forEach(System.out::println);

        System.out.println("Digite o titulo do episodio: ");
        var trechoTitulo = scanner.nextLine();

        Optional<Episodio> result = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
                .findFirst();

        if (result.isPresent()) {
            System.out.println("Temporada: " + result.get().getTemporada());
        } else {
            System.out.println("Episódio não encontrado.");
        }

//        System.out.println("A partir de que ano você deseja ver os episodios?");
//        var ano = scanner.nextInt();
//        scanner.nextLine();
//        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
//
//        episodios.stream()
//                .filter(episodio -> episodio.getDataLancamento() != null)
//                .filter(episodio -> episodio.getDataLancamento().isAfter(dataBusca))
//                .forEach(episodio -> System.out.println(
//                        "Temporada: " + episodio.getTemporada() +
//                            "Episodio: " + episodio.getTitulo() +
//                                "Data Lançamento: " + episodio.getDataLancamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
//                ));

        Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
                .filter(episodio -> episodio.getAvaliacao() != null)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));
        System.out.println(avaliacoesPorTemporada);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(episodio -> episodio.getAvaliacao() != null)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
        System.out.println("Média: " + est.getAverage() + "; Pior Episódio: " + est.getMin() +
                "; Melhor Episódio: " + est.getMax() +
                "Quantidade: " + est.getCount());
    }
}
