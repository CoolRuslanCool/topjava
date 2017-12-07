package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class DataJpaMealRepositoryImpl implements MealRepository {

    @Autowired
    private CrudMealRepository crudRepository;

    @Autowired
    private EntityManager entityManager;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setUser(entityManager.getReference(User.class, userId));
            entityManager.persist(meal);
        } else {
            return crudRepository.update(meal.getDescription(),
                    meal.getCalories(),
                    meal.getDateTime(),
                    meal.getId(),
                    userId) != 0 ? meal : null;
        }
        return meal;
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return crudRepository.deleteMealByIdAndUserId(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return crudRepository.findByIdAndUserId(id, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudRepository.findAllByUserIdOrderByDateTimeDesc(userId);
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return crudRepository.findAllByUserIdAndDateTimeBetweenOrderByDateTimeDesc(userId, startDate, endDate);
    }

    @Override
//    @Transactional
    public Meal getWithUser(int id) {
        return crudRepository.findFirstWithUserById(id);
//     Map<String, Object> hints = new HashMap<>(1);
//     hints.put("javax.persistence.loadgraph", entityManager.getEntityGraph("user"));
//     return entityManager.find(Meal.class, id, hints);
//        Meal meal = crudRepository.getOne(id);
//        Hibernate.initialize(meal.getUser());
//        return meal;
    }
}
