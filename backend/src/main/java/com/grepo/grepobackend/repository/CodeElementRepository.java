package com.grepo.grepobackend.repository;

import com.grepo.grepobackend.model.CodeElement;
import com.grepo.grepobackend.model.ElementType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CodeElementRepository extends JpaRepository<CodeElement, Long> {

    List<CodeElement> findByNameContainingIgnoreCase(String name);

    List<CodeElement> findByType(ElementType type);

    @Query("SELECT ce FROM CodeElement ce " +
            "JOIN ce.sourceFile sf " +
            "JOIN sf.repository gr " +
            "WHERE (:query IS NULL OR LOWER(ce.name) LIKE LOWER(CONCAT('%', CAST(:query AS string), '%')) " +          // CHANGED
            "       OR LOWER(ce.signature) LIKE LOWER(CONCAT('%', CAST(:query AS string), '%'))) " +                   // CHANGED
            "AND (:type IS NULL OR ce.type = :type) " +
            "AND (:repoName IS NULL OR gr.repoName = :repoName)")
    List<CodeElement> search(@Param("query") String query,
                             @Param("type") ElementType type,
                             @Param("repoName") String repoName);
}