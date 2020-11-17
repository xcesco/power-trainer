package com.abubusoft.powertrainer.backend.web;

import com.abubusoft.powertrainer.backend.model.ExerciseDto;
import com.abubusoft.powertrainer.backend.repositories.model.LanguageType;
import com.abubusoft.powertrainer.backend.service.ExerciseService;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;

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
  public ResponseEntity<Page<ExerciseDto>> getAll(@PageableDefault Pageable page, @RequestParam(name = "lang", defaultValue = DEFAULT_LANGUAGE) LanguageType language) {
    Page<ExerciseDto> result = exerciseService.findAll(language, page);

    if (result != null) {
      return ResponseEntity.ok(result);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @PageableAsQueryParam
  @GetMapping("/search")
  public ResponseEntity<Page<ExerciseDto>> getByName(@PageableDefault Pageable page, String name, @RequestParam(name = "lang", defaultValue = DEFAULT_LANGUAGE) LanguageType language) {
    Page<ExerciseDto> result = exerciseService.findByName(name, language, page);

    if (result != null) {
      return ResponseEntity.ok(result);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @PageableAsQueryParam
  @GetMapping("/searchByMuscle")
  public ResponseEntity<Page<ExerciseDto>> getByMuscle(@PageableDefault Pageable page, String muscle, @RequestParam(name = "lang", defaultValue = DEFAULT_LANGUAGE) LanguageType language) {
    Page<ExerciseDto> result = exerciseService.findByMuscle(muscle, language, page);

    if (result != null) {
      return ResponseEntity.ok(result);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/{exerciseUUID}")
  public ResponseEntity<ExerciseDto> getExerciseByUUID(@PathVariable String exerciseUUID, @RequestParam(name = "lang", defaultValue = DEFAULT_LANGUAGE) LanguageType language) {
    ExerciseDto result = exerciseService.findByUUID(exerciseUUID, language);

    if (result.getUUID() != null) {
      return ResponseEntity.ok(result);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping(value = "/{exerciseUUID}/image", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
  @ResponseBody
  public ResponseEntity<Resource> getExerciseImage(@PathVariable String exerciseUUID) {
    Resource resource = exerciseService.findImageByUUID(exerciseUUID);
    try {
      String mimeType = Files.probeContentType(resource.getFile().toPath());
      String fileName = resource.getFilename();
      String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
      return ResponseEntity
              .ok()
              .contentType(MediaType.parseMediaType(mimeType))
              .contentLength(resource.contentLength())
              .header("Content-Disposition", "inline; filename=\"" + exerciseUUID + "." + fileExtension + "\"")
              .body(resource);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}