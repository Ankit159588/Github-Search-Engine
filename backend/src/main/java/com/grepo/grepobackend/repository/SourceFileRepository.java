package com.grepo.grepobackend.repository;

import com.grepo.grepobackend.model.SourceFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SourceFileRepository extends JpaRepository<SourceFile, Long> {
}
