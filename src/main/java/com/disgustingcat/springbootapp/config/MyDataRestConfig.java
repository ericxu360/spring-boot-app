package com.disgustingcat.springbootapp.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.disgustingcat.springbootapp.entity.Cat;
import com.disgustingcat.springbootapp.entity.Incident;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;

public class MyDataRestConfig implements RepositoryRestConfigurer{
    private EntityManager entityManager;

    @Autowired
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
        config.exposeIdsFor(Cat.class, Incident.class);
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
