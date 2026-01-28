package com.aprimorar.api.config;


import com.aprimorar.api.entity.Address;
import com.aprimorar.api.entity.Parent;
import com.aprimorar.api.entity.Student;
import com.aprimorar.api.enums.Activity;
import com.aprimorar.api.repository.AddressRepository;
import com.aprimorar.api.repository.ParentRepository;
import com.aprimorar.api.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.text.SimpleDateFormat;
import java.util.logging.Logger;

@Configuration
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class DatabaseSeeder {

    private final StudentRepository studentRepo;
    private final AddressRepository addressRepo;
    private final ParentRepository parentRepo;


    @Bean
    CommandLineRunner initDatabase(){
        return args -> {
            if (studentRepo.count() == 0 && addressRepo.count() == 0 && parentRepo.count() == 0) {
                log.info("Iniciando SEED no banco de dados");
                seedStudents();
                log.info("SEED concluído com sucesso");
            } else {
                log.info("Banco de dados já contém dados. SEED não executado.");
            }
        };
    }

    private void seedStudents(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {

            Student student1 = new Student();

            student1.setName("João Silva");
            student1.setBirthdate(sdf.parse("22/04/2001"));
            student1.setCpf("123.456.789-00");
            student1.setSchool("Escola Estadual São Paulo");
            student1.setActivity(Activity.ENEM);
            student1.setActive(true);

            Student student2 = new Student();

            student2.setName("Marcelo Carvalho");
            student2.setBirthdate(sdf.parse("21/03/2000"));
            student2.setCpf("123.443.789-00");
            student2.setSchool("Leonardo da Vinci");
            student2.setActivity(Activity.MENTORIA);
            student2.setActive(true);

            Address address1 = new Address();
            address1.setStreet("Rua das Flores, 123");
            address1.setDistrict("Centro");
            address1.setCity("São Paulo");
            address1.setState("SP");
            address1.setZip_code("01234-567");
            address1.setStudent(student1);

            Address address2 = new Address();
            address2.setStreet("Servidão Bertolina, 230");
            address2.setDistrict("Barra da Lagoa");
            address2.setCity("Florianópolis");
            address2.setState("SC");
            address2.setZip_code("01234-245");
            address2.setStudent(student2);

            student1.setAddress(address1);
            student2.setAddress(address2);

            Parent parent1 = new Parent();
            parent1.setName("João Silva Sauro");
            parent1.setEmail("joao_silva@email.com");
            parent1.setStudent(student1);
            student1.setParent(parent1);

            Parent parent2 = new Parent();
            parent2.setName("Maurício Pereira Souza");
            parent2.setEmail("mauricio_pereira@email.com");
            parent2.setStudent(student2);
            student2.setParent(parent2);

            studentRepo.save(student1);
            studentRepo.save(student2);

        }catch (Exception e){
            log.error("ERRO ao popular banco de dados: {}", e.getMessage());
        }
    }


}
