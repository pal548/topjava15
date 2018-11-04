package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepositoryImpl implements MealRepository {

    private final CrudUserRepository userRepository;

    private final CrudMealRepository crudRepository;

    @Autowired
    public DataJpaMealRepositoryImpl(CrudUserRepository userRepository, CrudMealRepository crudRepository) {
        this.userRepository = userRepository;
        this.crudRepository = crudRepository;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (!meal.isNew() && get(meal.getId(), userId) == null) {
            return null;
        }
        meal.setUser(userRepository.getOne(userId));
        return crudRepository.save(meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        if (get(id, userId) == null) {
            return false;
        }
        crudRepository.deleteById(id);
        return true;
    }

    @Override
    public Meal get(int id, int userId) {
        var meal = crudRepository.findById(id).orElse(null);
        if (meal == null || meal.getUser().getId() == null || meal.getUser().getId() != userId) {
            return null;
        } else {
            return meal;
        }
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudRepository.findAllByUserIdOrderByDateTimeDesc(userId);
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return crudRepository.findAllByUserIdAndDateTimeIsBetweenOrderByDateTimeDesc(userId, startDate, endDate);
    }
}
