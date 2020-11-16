package com.abubusoft.powertrainer.backend.repositories;

import com.abubusoft.powertrainer.backend.repositories.model.Exercise;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//@RepositoryRestResource(collectionResourceRel = "exercises", path = "exercises")
@Repository
public interface ExercisesRepository extends PagingAndSortingRepository<Exercise, Long> {
  @Query("SELECT e FROM Exercise e WHERE UPPER(e.name) like UPPER(concat('%',:name,'%')) ")
  Page<Exercise> findByName(@Param("name") String name, Pageable pageable);

  Optional<Exercise> findByUUID(String UUID);
}
