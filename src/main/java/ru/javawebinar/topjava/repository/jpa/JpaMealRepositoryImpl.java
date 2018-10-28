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
                User ref = em.getReference(User.class, userId);
                meal.setUser(ref);
            }
            em.persist(meal);
            return meal;
        } else {
            /*Meal existing = em.find(Meal.class, meal.getId());
            if (existing.getUser().getId() != userId) {
                return null;
            }
            meal.setUser(existing.getUser());
            return em.merge(meal);*/
            if (em.createQuery("update Meal m set \n" +
                    "    m.dateTime = :dateTime, \n" +
                    "    m.description = :descr, \n" +
                    "    m.calories = :cal\n" +
                    "where m.id = :id \n" +
                    "      and m.user.id = :userId")
                    .setParameter("dateTime", meal.getDateTime())
                    .setParameter("descr", meal.getDescription())
                    .setParameter("cal", meal.getCalories())
                    .setParameter("id", meal.getId())
                    .setParameter("userId", userId)
                    .executeUpdate() == 1) {
                return meal;
            } else {
                return null;
            }
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