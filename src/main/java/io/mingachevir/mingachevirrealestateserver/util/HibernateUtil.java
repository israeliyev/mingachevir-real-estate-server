package io.mingachevir.mingachevirrealestateserver.util;

import jakarta.persistence.PersistenceException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public final class HibernateUtil {
    private HibernateUtil() {
    }

    public static Predicate[] predicateListToArray(List<Predicate> predicates) {
        if (predicates.isEmpty()) {
            return new Predicate[]{};
        }
        Predicate[] predicatesArray = new Predicate[predicates.size()];
        return predicates.toArray(predicatesArray);
    }

    public static <T> Optional<T> toOptional(Query<T> query) {
        try {
            return Optional.of(query.getSingleResult());
        } catch (PersistenceException pe) {
            return Optional.empty();
        }
    }

    private static <T> long rowCount(Session session, Root<?> root, CriteriaQuery<T> criteria) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);

        query.select(criteriaBuilder.countDistinct(root));
        query.distinct(criteria.isDistinct());
        query.getRoots().addAll(criteria.getRoots());

        if (Objects.nonNull(criteria.getRestriction())) {
            query.where(criteria.getRestriction());
        }
        if (!CollectionUtils.isEmpty(criteria.getGroupList())) {
            query.groupBy(criteria.getGroupList());
            if (Objects.nonNull(criteria.getGroupRestriction())) {
                query.having(criteria.getGroupRestriction());
            }
            return session.createQuery(query).getResultList().size();
        }
        return session.createQuery(query).getSingleResult();
    }

    public static <T> PageableData<List<T>> getPageableData(Integer pageNumber,
                                                            Integer pageSize,
                                                            Session session,
                                                            Root<?> root,
                                                            CriteriaQuery<T> query) {
        long totalRowCount = rowCount(session, root, query);
        List<T> result = session.createQuery(query)
                .setFirstResult(calculatePage(pageNumber, pageSize))
                .setMaxResults(pageSize).getResultList();
        return new PageableData<>(pageNumber, pageSize, totalRowCount, result);
    }

    private static int calculatePage(Integer pageNumber, Integer pageSize) {
        return (pageNumber * pageSize);
    }


}
