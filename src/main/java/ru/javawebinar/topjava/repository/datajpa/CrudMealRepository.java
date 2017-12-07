package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    Meal findByIdAndUserId(Integer id, Integer userId);

    @Modifying
    @Query("update Meal m set m.description=:descr, m.calories=:calories, m.dateTime=:dateTime where m.id=:id and m.user.id=:userId")
    int update(@Param("descr")String descr,
               @Param("calories")int calories,
               @Param("dateTime")LocalDateTime dateTime,
               @Param("id") int id,
               @Param("userId") int userId);

    @Modifying
    @Query("delete from Meal m where m.id=:id and m.user.id=:userId")
    int deleteMealByIdAndUserId(@Param("id") int id, @Param("userId") int userId);
//    int deleteMealById(int id, int userId);

    List<Meal> findAllByUserIdOrderByDateTimeDesc(Integer userId);

    List<Meal> findAllByUserIdAndDateTimeBetweenOrderByDateTimeDesc(Integer userId, LocalDateTime start, LocalDateTime end);

    @EntityGraph(value = "user", type = EntityGraph.EntityGraphType.LOAD)
    Meal findFirstWithUserById(int id);
}
