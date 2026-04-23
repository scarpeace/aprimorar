package com.aprimorar.api.domain.student.repository;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.aprimorar.api.domain.student.Student;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class StudentSpecificationsTest {

    @Mock
    private Root<Student> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private Path<String> namePath;

    @Mock
    private Path<String> emailPath;

    @Mock
    private Path<String> schoolPath;

    @Mock
    private Path<String> cpfPath;

    @Mock
    private Expression<String> loweredName;

    @Mock
    private Expression<String> loweredEmail;

    @Mock
    private Expression<String> loweredSchool;

    @Mock
    private Expression<String> loweredCpf;

    @Mock
    private Predicate namePredicate;

    @Mock
    private Predicate emailPredicate;

    @Mock
    private Predicate schoolPredicate;

    @Mock
    private Predicate cpfPredicate;

    @Test
    @DisplayName("should include cpf in student search specification")
    void shouldIncludeCpfInStudentSearchSpecification() {
        String searchTerm = "123.456";
        String likeTerm = "%123.456%";
        Specification<Student> specification = StudentSpecifications.searchContainsIgnoreCase(searchTerm);

        when(root.<String>get("name")).thenReturn(namePath);
        when(root.<String>get("email")).thenReturn(emailPath);
        when(root.<String>get("school")).thenReturn(schoolPath);
        when(root.<String>get("cpf")).thenReturn(cpfPath);
        when(criteriaBuilder.lower(namePath)).thenReturn(loweredName);
        when(criteriaBuilder.lower(emailPath)).thenReturn(loweredEmail);
        when(criteriaBuilder.lower(schoolPath)).thenReturn(loweredSchool);
        when(criteriaBuilder.lower(cpfPath)).thenReturn(loweredCpf);
        when(criteriaBuilder.like(loweredName, likeTerm)).thenReturn(namePredicate);
        when(criteriaBuilder.like(loweredEmail, likeTerm)).thenReturn(emailPredicate);
        when(criteriaBuilder.like(loweredSchool, likeTerm)).thenReturn(schoolPredicate);
        when(criteriaBuilder.like(loweredCpf, likeTerm)).thenReturn(cpfPredicate);

        specification.toPredicate(root, query, criteriaBuilder);

        verify(root).get("cpf");
        verify(criteriaBuilder).lower(cpfPath);
        verify(criteriaBuilder).like(loweredCpf, likeTerm);
    }
}
