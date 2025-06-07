package com.augusto.literalura.main;

import com.augusto.literalura.model.*;
import com.augusto.literalura.repository.AutorRepository;
import com.augusto.literalura.repository.IdiomaRepository;
import com.augusto.literalura.repository.LivroRepository;
import com.augusto.literalura.service.ConsumoApi;
import com.augusto.literalura.service.ConverterDados;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private final String ENDERECO = "https://gutendex.com/books?search=";
    private Scanner leia = new Scanner(System.in);
    private LivroRepository livroRepository;
    private AutorRepository autorRepository;
    private IdiomaRepository idiomaRepository;

    public Main(LivroRepository livroRepository, AutorRepository autorRepository, IdiomaRepository idiomaRepository){
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
        this.idiomaRepository = idiomaRepository;
    }

    public void exibeMenu(){

        var opcao = -1;
        while (opcao != 0) {
            System.out.println("""
                    ----- Literalura -----
                    
                    1) Buscar livro por título
                    2) Listar livros registrados
                    3) Listar autores registrados
                    4) Listar autores vivos em um determinado ano
                    5) Listar livros de uma determinada idioma
                    6) Top 10 livros mais baixados
                    7) Buscar autor por nome
                    
                    0) Sair
                    
                    ----------------------
                    """);

            opcao = leia.nextInt();
            leia.nextLine();

            switch (opcao){
                case 1 -> buscarLivroTitulo();
                case 2 -> listarLivrosRegistrados();
                case 3 -> listarAutoresRegistrados();
                case 4 -> listarAutoresVivosAno();
                case 5 -> listarLivrosIdioma();
                case 6 -> top10LivrosBaixados();
                case 7 -> buscarAutorNome();
                case 0 -> System.out.println("Saindo...");
                default -> System.out.println("Opção invalida");
            }
        }
    }

    private void buscarLivroTitulo() {
        System.out.println("Digite o nome do livro para busca:");
        var nomeLivro = leia.nextLine();

        ConsumoApi consumo = new ConsumoApi();
        var json = consumo.obterDados(ENDERECO + nomeLivro.replace(" ", "+"));
        System.out.println(json);

        ConverterDados conversor = new ConverterDados();
        ResultadoBusca resultado = conversor.converterResultadoBusca(json, ResultadoBusca.class);
        System.out.println(resultado);

        if (resultado.results() == null || resultado.results().isEmpty()) {
            System.out.println("Nenhum livro encontrado para o título: " + nomeLivro);
            return;
        }

        Optional<DadosLivro> dadosOptional = resultado.results().stream().findFirst();

        if (dadosOptional.isPresent()) {
            DadosLivro dados = dadosOptional.get();

            Livro livro = new Livro(dados);

            Set<Idioma> idiomas = dados.idiomas().stream()
                    .map(nome -> idiomaRepository.findByNome(nome)
                            .orElseGet(() -> idiomaRepository.save(new Idioma(nome))))
                    .collect(Collectors.toSet());

            List<Autor> autores = dados.autores().stream()
                    .map(Autor::new)
                    .toList();

            livro.setAutores(autores);
            livro.setIdioma(idiomas);

            livroRepository.save(livro);

            System.out.println("Livro salvo com sucesso: ");
            System.out.println(livro);
        } else {
            System.out.println("Dados do livro retornados estão vazios.");
        }
    }



    private void listarLivrosRegistrados() {
        if(livroRepository.findAll().isEmpty()){
            System.out.println("Nenhum livro foi cadastrado!");
        }else {
            System.out.println(livroRepository.findAll());
        }
    }

    private void listarAutoresRegistrados() {
        if(autorRepository.findAll().isEmpty()){
            System.out.println("Nenhum autor foi cadastrado!");
        }else {
            System.out.println(autorRepository.findAll());
        }
    }

    private void listarAutoresVivosAno() {
        System.out.println("Digite o ano que deseja buscar: ");
        var ano = leia.nextInt();
        if(autorRepository.buscarAutoresVivosNoAno(ano).isEmpty()){
            System.out.println("Nenhum autor encontrado no ano de: " + ano);
        }else {
            System.out.println(autorRepository.buscarAutoresVivosNoAno(ano));
        }

    }

    private void listarLivrosIdioma() {
        System.out.println("Escolha o idioma de busca: ");
        String idioma = leia.nextLine().trim();

        List<Livro> livros = livroRepository.findAll().stream()
                .filter(l -> l.getIdioma().stream()
                        .anyMatch(i -> i.getidioma().equalsIgnoreCase(idioma)))
                .toList();

        if (livros.isEmpty()) {
            System.out.println("Nenhum livro foi encontrado com esse idioma!");
        } else {
            livros.forEach(System.out::println);
        }
    }


    private void top10LivrosBaixados() {
        List<Livro> livros = livroRepository.findTop10ByOrderByDownloadsDesc();
        if (livros.isEmpty()){
            System.out.println("Nenhum livro foi cadastrado!");
        }else {
            System.out.println(livros);
        }

    }

    private void buscarAutorNome() {
        System.out.println("Digite o nome do autor para buscar: ");
        var nome = leia.nextLine();
        List<Autor> autores = autorRepository.findByNomeContainingIgnoreCase(nome);
        if(autores.isEmpty()){
            System.out.println("Nenhum autor encontrado com esse nome");
        }else {
            System.out.println(autores);
        }
    }
}
