package ru.javawebinar.topjava.model;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@NamedQueries({
        @NamedQuery(name = Meal.BY_ID_USER_ID, query = "select m from Meal m WHERE m.id=:id and m.user.id = :user_id"),
        @NamedQuery(name = Meal.ALL_BY_USER_ID, query = "select m from Meal m WHERE m.user.id = :user_id order by m.dateTime desc"),
        @NamedQuery(name = Meal.ALL_BETWEEN, query = "select m from Meal m \n" +
                "WHERE m.user.id = :user_id \n" +
                "      and m.dateTime >= :d1 \n" +
                "      and m.dateTime <= :d2 \n" +
                "order by m.dateTime desc"),
        @NamedQuery(name = Meal.DELETE, query = "delete from Meal m where m.id = :id and m.user.id = :user_id"),
})
@Entity
@Table(name = "meals", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "date_time"}, name = "meals.meals_user_id_date_time_uindex")})
public class Meal extends AbstractBaseEntity {

    public static final String BY_ID_USER_ID = "Meal.getByIdAnsUserId";
    public static final String ALL_BY_USER_ID = "Meal.getAllByUserId";
    public static final String ALL_BETWEEN = "Meal.getAllBetween";
    public static final String DELETE = "Meal.delete";

    @Column(name = "date_time", nullable = false)
    @NotNull
    private LocalDateTime dateTime;

    @Column(name = "description", nullable = false)
    @NotBlank
    private String description;

    @Column(name = "calories", nullable = false)
    @NotNull
    @Range(min = 10, max = 10000)
    private int calories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private User user;

    public Meal() {
    }

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this(null, dateTime, description, calories);
    }

    public Meal(Integer id, LocalDateTime dateTime, String description, int calories) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public Meal(User user, Integer id, LocalDateTime dateTime, String description, int calories) {
        this(id, dateTime, description, calories);
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}
