package com.augusto.literalura.repository;

import com.augusto.literalura.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LivroRepository extends JpaRepository<Livro, Long> {
    List<Livro> findTop10ByOrderByDownloadsDesc();
}
