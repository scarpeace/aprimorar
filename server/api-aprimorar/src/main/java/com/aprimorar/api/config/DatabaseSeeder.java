package com.aprimorar.api.config;


import com.aprimorar.api.entity.Address;
import com.aprimorar.api.entity.Parent;
import com.aprimorar.api.entity.Student;
import com.aprimorar.api.enums.Activity;

import com.aprimorar.api.repository.ParentRepository;
import com.aprimorar.api.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
@Profile("dev")
public class DatabaseSeeder {

    private static final Logger log = LoggerFactory.getLogger(DatabaseSeeder.class);

    private final StudentRepository studentRepo;
    private final ParentRepository parentRepo;

    public DatabaseSeeder(StudentRepository studentRepo, ParentRepository parentRepo) {
        this.studentRepo = studentRepo;
        this.parentRepo = parentRepo;
    }

    @Bean
    CommandLineRunner initDatabase(){
        return args -> {
            if (studentRepo.count() == 0 && parentRepo.count() == 0) {
                log.info("Iniciando SEED no Banco de dados");
                System.out.println();
                seedStudents();
                log.info("SEED do banco de dados finalizado");
            } else {
                log.info("O banco já possui registros, SEED não iniciado");
            }
        };
    }

    private void seedStudents(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {

            Student student1 = new Student();

            student1.setName("João Silva");
            student1.setBirthdate(LocalDate.of(2001,3,22));
            student1.setCpf("123.456.789-00");
            student1.setContact("(61) 99456-2345");
            student1.setEmail("joao.silva@email.com");
            student1.setSchool("Escola Estadual São Paulo");
            student1.setActivity(Activity.ENEM);
            student1.setActive(true);

            Student student2 = new Student();

            student2.setName("Marcelo Carvalho");
            student2.setBirthdate(LocalDate.of(1999, 5, 18));
            student2.setCpf("123.443.789-00");
            student2.setContact("(61) 99435-4221");
            student2.setEmail("marcelo.carvalho@email.com");
            student2.setSchool("Leonardo da Vinci");
            student2.setActivity(Activity.MENTORIA);
            student2.setActive(true);

            Address address1 = new Address();
            address1.setStreet("Rua das Flores");
            address1.setNumber("123");
            address1.setComplement("Apto 45");
            address1.setDistrict("Centro");
            address1.setCity("São Paulo");
            address1.setState("SP");
            address1.setZipCode("01234-567");

            student1.setAddress(address1);

            Address address2 = new Address();
            address2.setStreet("Servidão Bertolina");
            address2.setNumber("230");
            address2.setComplement("Casa");
            address2.setDistrict("Barra da Lagoa");
            address2.setCity("Florianópolis");
            address2.setState("SC");
            address2.setZipCode("01234-245");

            student2.setAddress(address2);

            Parent parent1 = new Parent();
            parent1.setName("João Silva Sauro");
            parent1.setEmail("joao_sauro@email.com");
            parent1.setContact("(61) 98888-1111");
            parent1.setCpf("111.222.333-44");

            student1.setParent(parent1);

            Parent parent2 = new Parent();
            parent2.setName("Maurício Pereira Souza");
            parent2.setEmail("mauricio_souza@email.com");
            parent2.setContact("(11) 97777-2222");
            parent2.setCpf("555.666.777-88");

            student2.setParent(parent2);

            studentRepo.save(student1);
            studentRepo.save(student2);

        }catch (Exception e){
            log.error("ERRO ao popular banco de dados: {}", e.getMessage());
        }
    }

}
