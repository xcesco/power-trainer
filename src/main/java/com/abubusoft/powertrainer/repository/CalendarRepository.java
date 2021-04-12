package com.abubusoft.powertrainer.repository;

import com.abubusoft.powertrainer.domain.Calendar;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Calendar entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Long>, JpaSpecificationExecutor<Calendar> {}
