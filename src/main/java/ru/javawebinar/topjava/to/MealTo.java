package ru.javawebinar.topjava.to;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;

public class MealTo extends BaseTo implements Serializable {

    private static final long serialVersionUID = 831037294938916026L;

    @NotNull(message = "ldt null or empty")
    private LocalDateTime dateTime;

    @NotBlank(message = "descr null or small")
    @Size(min = 2, max = 120)
    private String description;

    @Min(value = 10, message = "not < 0")
    @Max(value = 5000, message = "not > 5000")
    @NotNull(message = "calories is null")
    private Integer calories;

    public MealTo() {
    }

    public MealTo(Integer id, LocalDateTime dateTime, String description, int calories) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    @Override
    public String toString() {
        return "MealTo{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}
