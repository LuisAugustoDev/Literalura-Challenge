package com.augusto.literalura.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
    @Table(name = "idioma")
    public class Idioma {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String nome;

        @ManyToMany(mappedBy = "idiomas")
         private Set<Livro> livros = new HashSet<>();

        public Idioma() {}

        public Idioma(String nome) {
            this.nome = nome;
        }


        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getidioma() {
            return nome;
        }

        public void setidioma(String idioma) {
            this.nome = idioma;
        }

    @Override
    public String toString() {
        return "Idioma{" +
                "nome='" + nome + '\'' +
                '}';
    }
}
