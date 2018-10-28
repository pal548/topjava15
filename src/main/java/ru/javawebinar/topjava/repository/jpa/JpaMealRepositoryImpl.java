package ru.javawebinar.topjava.repository.jpa;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JpaMealRepositoryImpl implements MealRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            if (meal.getUser() == null) {
                User user = em.find(User.class, userId);
                meal.setUser(user);
            }
            em.persist(meal);
            return meal;
        } else {
            if (meal.getUser().getId() != userId) {
                throw new NotFoundException("The meal is of other user");
            }
            return em.merge(meal);
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return em.createNamedQuery(Meal.DELETE)
                .setParameter("id", id)
                .setParameter("user_id", userId)
                .executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return DataAccessUtils.singleResult(em.createNamedQuery(Meal.BY_ID_USER_ID, Meal.class)
                .setParameter("id", id)
                .setParameter("user_id", userId)
                .getResultList());
    }

    @Override
    public List<Meal> getAll(int userId) {
        return em.createNamedQuery(Meal.ALL_BY_USER_ID, Meal.class)
                .setParameter("user_id", userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return em.createNamedQuery(Meal.ALL_BETWEEN, Meal.class)
                .setParameter("user_id", userId)
                .setParameter("d1", startDate)
                .setParameter("d2", endDate)
                .getResultList();
    }
}