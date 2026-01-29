package br.com.kleverson.gestao_vagas.modules.candidate.useCases;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.kleverson.gestao_vagas.exceptions.JobNotFoundException;
import br.com.kleverson.gestao_vagas.exceptions.UserNotFoundException;
import br.com.kleverson.gestao_vagas.modules.candidate.CandidateEntity;
import br.com.kleverson.gestao_vagas.modules.candidate.CandidateRepository;
import br.com.kleverson.gestao_vagas.modules.candidate.entity.ApplyJobEntity;
import br.com.kleverson.gestao_vagas.modules.candidate.repository.ApplyJobRepository;
import br.com.kleverson.gestao_vagas.modules.company.entities.JobEntity;
import br.com.kleverson.gestao_vagas.modules.company.repositories.JobRepository;

@ExtendWith(MockitoExtension.class)
public class ApplyJobCandidateUseCaseTest {

  @InjectMocks
  private ApplyJobCandidateUseCase applyJobCandidateUseCase;

  @Mock
  private CandidateRepository candidateRepository;

  @Mock
  private JobRepository jobRepository;

  @Mock
  private ApplyJobRepository applyJobRepository;

  @Test
  @DisplayName("Should not be able to apply job with candidate not found")
  public void shouldNotBeAbleToApllyJobWithCandidateNotFound() {

    try {
      applyJobCandidateUseCase.execute(null, null);

    } catch (Exception e) {
      assertThat(e).isInstanceOf(UserNotFoundException.class);
    }
  }

  @Test
  @DisplayName("Should not be able to apply job with job not found")
  public void shouldNotBeAbleToApllyJobWithJobNotFound() {

    UUID idCandidate = UUID.randomUUID();

    CandidateEntity candidate = new CandidateEntity();
    candidate.setId(idCandidate);

    when(candidateRepository.findById(idCandidate)).thenReturn(Optional.of(candidate));

    try {
      applyJobCandidateUseCase.execute(idCandidate, null);

    } catch (Exception e) {
      assertThat(e).isInstanceOf(JobNotFoundException.class);
    }

  }

  @Test
  @DisplayName("Should be able to apply job")
  public void shouldBeAbleToApplyJob() {

    UUID idCandidate = UUID.randomUUID();
    UUID idJob = UUID.randomUUID();

    ApplyJobEntity applyJob = ApplyJobEntity.builder().candidateId(idCandidate)
        .jobId(idJob).build();

    ApplyJobEntity applyJobCreated = ApplyJobEntity.builder().id(UUID.randomUUID()).build();

    when(candidateRepository.findById(idCandidate)).thenReturn(Optional.of(new CandidateEntity()));
    when(jobRepository.findById(idJob)).thenReturn(Optional.of(new JobEntity()));

    when(applyJobRepository.save(applyJob)).thenReturn(applyJobCreated);

    ApplyJobEntity result = applyJobCandidateUseCase.execute(idCandidate, idJob);

    assertThat(result).hasFieldOrProperty("id");
    assertNotNull(result.getId());
  }
}
