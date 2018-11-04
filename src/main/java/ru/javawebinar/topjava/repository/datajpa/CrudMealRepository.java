package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    public List<Meal> findAllByUserIdOrderByDateTimeDesc(int userId);
    public List<Meal> findAllByUserIdAndDateTimeIsBetweenOrderByDateTimeDesc(int userId, LocalDateTime d1, LocalDateTime d2);
}