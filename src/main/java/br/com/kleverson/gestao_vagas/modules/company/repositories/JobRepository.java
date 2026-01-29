package br.com.kleverson.gestao_vagas.modules.company.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.kleverson.gestao_vagas.modules.company.entities.JobEntity;

public interface JobRepository extends JpaRepository<JobEntity, UUID> {

  // SELECT * FROM job WHERE description LIKE %filter%
  List<JobEntity> findByDescriptionContainingIgnoreCase(String filter);
}
