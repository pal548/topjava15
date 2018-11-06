package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    List<Meal> findAllByUserIdOrderByDateTimeDesc(int userId);

    List<Meal> findAllByUserIdAndDateTimeIsBetweenOrderByDateTimeDesc(int userId, LocalDateTime d1, LocalDateTime d2);

    int deleteByIdAndUserId(int id, int userId);

    @Query("SELECT m " +
            "FROM Meal m " +
            "JOIN FETCH m.user " +
            "JOIN FETCH m.user.roles " +
            "WHERE m.id=:id AND m.user.id = :userId")
    Meal getWithUser(@Param("id") int id, @Param("userId") int userId);
}