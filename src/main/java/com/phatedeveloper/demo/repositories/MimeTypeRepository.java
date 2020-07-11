package com.phatedeveloper.demo.repositories;

import com.phatedeveloper.demo.models.MimeType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MimeTypeRepository extends JpaRepository<MimeType, Long> {}
