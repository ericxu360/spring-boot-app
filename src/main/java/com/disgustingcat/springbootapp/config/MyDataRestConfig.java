package com.disgustingcat.springbootapp.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer{
    private EntityManager entityManager;

    public MyDataRestConfig(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        /*
        HttpMethod[] unsupportedActions = {HttpMethod.PUT, HttpMethod.POST, HttpMethod.DELETE};

        config.getExposureConfiguration().forDomainType(Cat.class)
            .withItemExposure((metadata, httpMethods) -> httpMethods.disable(unsupportedActions))
            .withCollectionExposure((metadata, httpMethods) -> httpMethods.disable(unsupportedActions));

        config.getExposureConfiguration().forDomainType(Incident.class)
            .withItemExposure((metadata, httpMethods) -> httpMethods.disable(unsupportedActions))
            .withCollectionExposure((metadata, httpMethods) -> httpMethods.disable(unsupportedActions));

        */
        exposeIds(config);
    }

    private void exposeIds(RepositoryRestConfiguration config) {
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();
        List<Class> entityClasses = new ArrayList<>();
        for (EntityType entityType : entities) {
            entityClasses.add(entityType.getJavaType());
        }
        Class[] domainTypes = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);
    }
}
