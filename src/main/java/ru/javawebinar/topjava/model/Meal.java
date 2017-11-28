package ru.javawebinar.topjava.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@NamedNativeQueries({
        @NamedNativeQuery(name = Meal.INSERT, query = "INSERT INTO meals (date_time, description" +
                ", calories, user_id) VALUES (:date_time, :description, :calories, :user_id)"),
//        @NamedNativeQuery(name = Meal.UPDATE, query = "UPDATE meals SET date_time=:date_time, description=:description, calories=:calories WHERE id=:id AND user_id=:user_id")
})
@NamedQueries({
        @NamedQuery(name = Meal.DELETE, query = "DELETE FROM Meal m WHERE m.id=:id and m.user.id=:user_id"),
        @NamedQuery(name = Meal.BY_ID, query = "SELECT m FROM Meal m WHERE m.id=:id and m.user.id=:user_id"),
        @NamedQuery(name = Meal.ALL_SORTED, query = "SELECT m FROM Meal m where m.user.id=:user_id ORDER BY m.dateTime"),
        @NamedQuery(name = Meal.UPDATE, query = "update Meal m set m.dateTime=:date_time, m.description=:description, m.calories=:calories where m.id=:id and m.user.id=:user_id")
})
@Entity
@Table(name = "meals", uniqueConstraints = @UniqueConstraint(name = "meal_user_datetime_unique", columnNames = {"user_id", "date_time"}))
public class Meal extends AbstractBaseEntity {

    public static final String DELETE = "Meal.delete";
    public static final String BY_ID = "Meal.byId";
    public static final String ALL_SORTED = "Meal.allSorted";
    public static final String INSERT = "Meal.insert";
    public static final String UPDATE = "Meal.update";

    @Column(name = "date_time", nullable = false, columnDefinition = "timestamp default now()")
    @NotNull
    private LocalDateTime dateTime;

    @Column(name = "description", nullable = false, length = 100)
    @NotBlank
    @Size(max = 100)
    private String description;

    @Column(name = "calories", nullable = false, columnDefinition = "int default 0")
    @NotNull
    private int calories;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id", nullable = false)
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