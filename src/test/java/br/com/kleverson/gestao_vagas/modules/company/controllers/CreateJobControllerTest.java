package br.com.kleverson.gestao_vagas.modules.company.controllers;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
//import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import br.com.kleverson.gestao_vagas.modules.company.dto.CreateJobDTO;
import br.com.kleverson.gestao_vagas.modules.company.entities.CompanyEntity;
import br.com.kleverson.gestao_vagas.modules.company.repositories.CompanyRepository;
import br.com.kleverson.gestao_vagas.utils.TestUtils;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CreateJobControllerTest {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private CompanyRepository companyRepository;

  @Test
  public void shouldBeAbleToCreateANewJob() throws Exception {

    var company = CompanyEntity.builder()
        .description("COMPANY_DESCRIPTION")
        .email("email@company.com")
        .password("password123456")
        .username("COMPANY_USERNAME")
        .name("COMPANY_NAME")
        .build();

    company = companyRepository.saveAndFlush(company);

    CreateJobDTO createJobDTO = CreateJobDTO.builder()
        .benefits("BENEFITS_TEST")
        .description("DESCRIPTION_TEST")
        .level("LEVEL_TEST")
        .build();

    var result = mvc.perform(MockMvcRequestBuilders.post("/company/job/")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectToJSON(createJobDTO))
        .header("Authorization",
            TestUtils.generateToken(company.getId(),
                "COMPANY_KLEVERSON_@MACHINE")))
        .andExpect(MockMvcResultMatchers.status().isOk());

    System.out.println(result);
  }

  @Test
  public void shouldNotBeAbleToCreateANewJobIfCompanyNotFound() throws Exception {
    CreateJobDTO createJobDTO = CreateJobDTO.builder()
        .benefits("BENEFITS_TEST")
        .description("DESCRIPTION_TEST")
        .level("LEVEL_TEST")
        .build();

    mvc.perform(MockMvcRequestBuilders.post("/company/job/")
        .contentType(MediaType.APPLICATION_JSON)
        .content(TestUtils.objectToJSON(createJobDTO))
        .header("Authorization",
            TestUtils.generateToken(UUID.randomUUID(),
                "COMPANY_KLEVERSON_@MACHINE")))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());

  }

}
