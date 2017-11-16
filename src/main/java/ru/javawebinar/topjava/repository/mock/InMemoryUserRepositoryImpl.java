package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepositoryImpl.class);
    private final Map<Integer, User> USERS = new ConcurrentHashMap();
    private final AtomicInteger ID_COUNTER = new AtomicInteger(0);

    {
        USERS.put(ID_COUNTER.incrementAndGet(), new User(ID_COUNTER.get(), "User", "cool_user@mail.ru", "user", Role.ROLE_USER));
        USERS.put(ID_COUNTER.incrementAndGet(), new User(ID_COUNTER.get(), "Admin", "cool_admin@mail.ru", "admin", Role.ROLE_ADMIN, Role.ROLE_USER));
    }

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return USERS.remove(id) != null;
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);
        if (user.isNew()) {
            if (USERS.values().stream().anyMatch(user1 -> user1.getName().equalsIgnoreCase(user.getName()))) {
                throw new IllegalArgumentException(String.format("User with name '%s' exist.", user.getName()));
            }
            user.setId(ID_COUNTER.incrementAndGet());
        }
        USERS.put(user.getId(), user);
        return user;
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return USERS.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        return USERS.values().stream()
                .sorted((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        return USERS.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }
}
