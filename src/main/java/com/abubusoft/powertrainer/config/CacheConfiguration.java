package com.abubusoft.powertrainer.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.abubusoft.powertrainer.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.abubusoft.powertrainer.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.abubusoft.powertrainer.domain.User.class.getName());
            createCache(cm, com.abubusoft.powertrainer.domain.Authority.class.getName());
            createCache(cm, com.abubusoft.powertrainer.domain.User.class.getName() + ".authorities");
            createCache(cm, com.abubusoft.powertrainer.domain.Language.class.getName());
            createCache(cm, com.abubusoft.powertrainer.domain.Language.class.getName() + ".translations");
            createCache(cm, com.abubusoft.powertrainer.domain.Translation.class.getName());
            createCache(cm, com.abubusoft.powertrainer.domain.Calendar.class.getName());
            createCache(cm, com.abubusoft.powertrainer.domain.Calendar.class.getName() + ".exerciseValues");
            createCache(cm, com.abubusoft.powertrainer.domain.Calendar.class.getName() + ".misurations");
            createCache(cm, com.abubusoft.powertrainer.domain.Calendar.class.getName() + ".workouts");
            createCache(cm, com.abubusoft.powertrainer.domain.Exercise.class.getName());
            createCache(cm, com.abubusoft.powertrainer.domain.Exercise.class.getName() + ".notes");
            createCache(cm, com.abubusoft.powertrainer.domain.Exercise.class.getName() + ".muscles");
            createCache(cm, com.abubusoft.powertrainer.domain.Exercise.class.getName() + ".exerciseTools");
            createCache(cm, com.abubusoft.powertrainer.domain.ExerciseValue.class.getName());
            createCache(cm, com.abubusoft.powertrainer.domain.ExerciseTool.class.getName());
            createCache(cm, com.abubusoft.powertrainer.domain.ExerciseTool.class.getName() + ".exercises");
            createCache(cm, com.abubusoft.powertrainer.domain.Misuration.class.getName());
            createCache(cm, com.abubusoft.powertrainer.domain.MisurationType.class.getName());
            createCache(cm, com.abubusoft.powertrainer.domain.MisurationType.class.getName() + ".misurations");
            createCache(cm, com.abubusoft.powertrainer.domain.WorkoutSheet.class.getName());
            createCache(cm, com.abubusoft.powertrainer.domain.WorkoutSheet.class.getName() + ".workoutSheetExercises");
            createCache(cm, com.abubusoft.powertrainer.domain.WorkoutSheetExercise.class.getName());
            createCache(cm, com.abubusoft.powertrainer.domain.Workout.class.getName());
            createCache(cm, com.abubusoft.powertrainer.domain.Workout.class.getName() + ".workoutSteps");
            createCache(cm, com.abubusoft.powertrainer.domain.WorkoutStep.class.getName());
            createCache(cm, com.abubusoft.powertrainer.domain.Device.class.getName());
            createCache(cm, com.abubusoft.powertrainer.domain.Muscle.class.getName());
            createCache(cm, com.abubusoft.powertrainer.domain.Muscle.class.getName() + ".exercises");
            createCache(cm, com.abubusoft.powertrainer.domain.Note.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
