package io.mingachevir.mingachevirrealestateserver.util;

import io.mingachevir.mingachevirrealestateserver.model.entity.base.BaseEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;

@Transactional(readOnly = true)
public abstract class AbstractBaseRepository<T extends BaseEntity, ID>{

    @PersistenceContext
    private EntityManager entityManager;
    private Class<T> entity;

    @SuppressWarnings("unchecked")
    public AbstractBaseRepository() {
        this.entity = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    }

    protected Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    protected CriteriaBuilder getCriteriaBuilder() {
        return getCurrentSession().getCriteriaBuilder();
    }

}
