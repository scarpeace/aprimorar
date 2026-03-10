package com.aprimorar.api.domain.parent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aprimorar.api.domain.parent.dto.ParentResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1/parents")
@Tag(name = "Parents", description = "Parent management APIs")
public class ParentController {

    private final ParentService parentService;

    public ParentController(ParentService parentService) {
        this.parentService = parentService;
    }

    @Operation(summary = "List all parents", description = "Retrieves parents from database with pagination and optional archived records")
    @GetMapping
    public ResponseEntity<Page<ParentResponseDTO>> listParents(
             @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    )
    {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ParentResponseDTO> parents = parentService.getParents(pageRequest);

        log.info("ParentController:listParents execution completed. Page: {}, Size: {}, Total Elements: {}", page, size, parents.getTotalElements());
        return ResponseEntity.ok(parents);
    }

}
