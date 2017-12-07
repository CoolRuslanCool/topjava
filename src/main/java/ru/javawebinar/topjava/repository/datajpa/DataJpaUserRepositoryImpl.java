package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class DataJpaUserRepositoryImpl implements UserRepository {
    private static final Sort SORT_NAME_EMAIL = new Sort(Sort.Direction.ASC, "name", "email");

    @Autowired
    private CrudUserRepository crudRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User save(User user) {
        return crudRepository.save(user);
    }

    @Override
    public boolean delete(int id) {
        return crudRepository.delete(id) != 0;
    }

    @Override
    public User get(int id) {
        return crudRepository.findById(id).orElse(null);
    }

    @Override
    public User getByEmail(String email) {
        return crudRepository.getByEmail(email);
    }

    @Override
    public List<User> getAll() {
        return crudRepository.findAll(SORT_NAME_EMAIL);
    }

    @Override
//    @Transactional
    public User getWithMeals(int id) {
        return crudRepository.findFirstWithMealsById(id);
//        Map<String, Object> hints = new HashMap<>(1);
//        hints.put("javax.persistence.loadgraph", entityManager.getEntityGraph("mealsGraph"));
//        hints.put("javax.persistence.loadgraph", ("mealsGraph"));
//        hints.put("javax.persistence.fetchgraph", entityManager.getEntityGraph("mealsGraph"));
//        return entityManager.find(User.class, id, hints);
        // fetch only meals
//        User user = crudRepository.getOne(id);
//        Hibernate.initialize(user.getMeals());
//        return user;
    }
}
