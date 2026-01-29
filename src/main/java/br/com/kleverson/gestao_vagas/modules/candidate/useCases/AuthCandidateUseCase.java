package br.com.kleverson.gestao_vagas.modules.candidate.useCases;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import br.com.kleverson.gestao_vagas.modules.candidate.CandidateRepository;
import br.com.kleverson.gestao_vagas.modules.candidate.dto.AuthCandidateRequestDTO;
import br.com.kleverson.gestao_vagas.modules.candidate.dto.AuthCandidateResponseDTO;

@Service
public class AuthCandidateUseCase {

  @Value("${security.token.secret.candidate}")
  private String secretKey;

  @Autowired
  private CandidateRepository candidateRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public AuthCandidateResponseDTO execute(AuthCandidateRequestDTO requestDTO) throws AuthenticationException {
    var candidate = this.candidateRepository.findByUsername(requestDTO.username()).orElseThrow(() -> {
      throw new UsernameNotFoundException("Username/Password incorrect");
    });

    var passwordMatches = this.passwordEncoder.matches(requestDTO.password(), candidate.getPassword());

    if (!passwordMatches) {
      throw new AuthenticationException("Username/Password incorrect");
    }

    Algorithm algorithm = Algorithm.HMAC256(secretKey);
    var expiresIn = Instant.now().plus(Duration.ofHours(2));
    var token = JWT.create().withIssuer("KleversonCompany").withSubject(candidate.getId().toString())
        .withClaim("roles", Arrays.asList("CANDIDATE")).withExpiresAt(expiresIn)
        .sign(algorithm);

    var authCandidateResponse = AuthCandidateResponseDTO.builder().access_token(token)
        .expires_in(expiresIn.toEpochMilli()).build();

    return authCandidateResponse;

  }

}
