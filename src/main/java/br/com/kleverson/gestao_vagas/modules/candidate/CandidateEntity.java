package br.com.kleverson.gestao_vagas.modules.candidate;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@Entity(name = "candidate")
public class CandidateEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Schema(example = "John Doe")
  private String name;

  @NotBlank(message = "O campo (username) é obrigatório")
  // Tomar cuidado ao utilizar Regex, pois pode causar problemas de segurança, mas
  // vamos ignorar nesse projeto.
  @Pattern(regexp = "^[^\s]+$", message = "O campo (username) não deve conter espaços")
  @Schema(example = "johndoe", requiredMode = RequiredMode.REQUIRED)
  private String username;

  @Email(message = "O campo (email) deve conter um E-mail válido")
  @Schema(example = "johndoe@email.com", requiredMode = RequiredMode.REQUIRED)
  private String email;

  @Length(min = 10, max = 100, message = "O campo (password) deve conter entre 10 e 100 caracteres")
  @Schema(example = "password123", minLength = 10, maxLength = 100, requiredMode = RequiredMode.REQUIRED)
  private String password;

  @Schema(example = "Desenvolvedor Java")
  private String description;

  @Schema(requiredMode = RequiredMode.NOT_REQUIRED)
  private String curriculum;

  @CreationTimestamp
  @Schema(requiredMode = RequiredMode.AUTO)
  private LocalDateTime createdAt;

}
