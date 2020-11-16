package com.abubusoft.powertrainer.backend.web;

import com.abubusoft.powertrainer.backend.repositories.model.LanguageType;
import com.abubusoft.powertrainer.backend.service.ExerciseService;
import com.abubusoft.powertrainer.backend.service.model.ExerciseDto;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = WebPathConstants.API_ENTRYPOINT + "/exercises")
public class ExerciseController {
  private static final String DEFAULT_LANGUAGE = "it_IT";
  private final ExerciseService exerciseService;

  public ExerciseController(@Autowired ExerciseService exerciseService) {
    this.exerciseService = exerciseService;
  }

  @PageableAsQueryParam
  @GetMapping("/")
  public HttpEntity<Page<ExerciseDto>> getAll(@PageableDefault  Pageable page, @RequestParam(name = "lang", defaultValue = DEFAULT_LANGUAGE) LanguageType language) {
    Page<ExerciseDto> result = exerciseService.findAll(page, language);

    if (result != null) {
      //result.add(linkTo(methodOn(ExerciseController.class).getCustomerById(customerUUID, language)).withSelfRel());

      return new ResponseEntity<>(result, HttpStatus.OK);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/{exerciseUUID}")
  public HttpEntity<ExerciseDto> getExerciseByUUID(@PathVariable String exerciseUUID, @RequestParam(name = "lang", defaultValue = DEFAULT_LANGUAGE) LanguageType language) {
    ExerciseDto result = exerciseService.findByUUID(exerciseUUID, language);

    if (result.getUUID() != null) {
      //result.add(linkTo(methodOn(ExerciseController.class).getCustomerById(customerUUID, language)).withSelfRel());

      return new ResponseEntity<>(result, HttpStatus.OK);
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}