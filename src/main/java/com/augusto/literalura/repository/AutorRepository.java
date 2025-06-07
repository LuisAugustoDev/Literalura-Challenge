package com.augusto.literalura.repository;

import com.augusto.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {
        @Query("SELECT a FROM Autor a WHERE a.anoNascimento <= :ano AND (a.anoFalecimento IS NULL OR a.anoFalecimento >= :ano)")
        List<Autor> buscarAutoresVivosNoAno(int ano);

        List<Autor> findByNomeContainingIgnoreCase(String nome);
}
