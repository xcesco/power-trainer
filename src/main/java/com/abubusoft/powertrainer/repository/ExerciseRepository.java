package com.abubusoft.powertrainer.repository;

import com.abubusoft.powertrainer.domain.Exercise;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Exercise entity.
 */
@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long>, JpaSpecificationExecutor<Exercise> {
    @Query(
        value = "select distinct exercise from Exercise exercise left join fetch exercise.muscles",
        countQuery = "select count(distinct exercise) from Exercise exercise"
    )
    Page<Exercise> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct exercise from Exercise exercise left join fetch exercise.muscles")
    List<Exercise> findAllWithEagerRelationships();

    @Query("select exercise from Exercise exercise left join fetch exercise.muscles where exercise.id =:id")
    Optional<Exercise> findOneWithEagerRelationships(@Param("id") Long id);
}
