package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.sql.DataSource;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final ResultSetExtractor<List<User>> EXTRACTOR = (resultSet) -> {
        final Map<Integer, User> users = new LinkedHashMap<>();
        while (resultSet.next()) {
            Role role = Role.valueOf(resultSet.getString("role"));
            int id = resultSet.getInt("id");

            if (!users.containsKey(id)) {
                users.put(id, new User(resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getInt("calories_per_day"),
                        resultSet.getBoolean("enabled"),
                        resultSet.getDate("registered"),
                        EnumSet.of(role)));
                continue;
            }
            users.get(id).getRoles().add(role);
        }
        return new ArrayList<>(users.values());
    };

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepositoryImpl(DataSource dataSource, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(dataSource)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

//    @Override
//    @Transactional
//    public User save(User user) {
//        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
//        List<Role> roles = new ArrayList<>(user.getRoles());
//
//        if (user.isNew()) {
//            Number newKey = insertUser.executeAndReturnKey(parameterSource);
//            user.setId(newKey.intValue());
//            jdbcTemplate.batchUpdate("INSERT INTO user_roles(user_id, role) VALUES (?, ?)", new BatchPreparedStatementSetter() {
//                @Override
//                public void setValues(PreparedStatement ps, int i) throws SQLException {
//                    ps.setInt(1, user.getId());
//                    ps.setString(2, roles.get(i).toString());
//                }
//                @Override
//                public int getBatchSize() {
//                    return user.getRoles().size();
//                }
//            });
//        } else {
//            if (namedParameterJdbcTemplate.update(
//                    "UPDATE users SET name=:name, email=:email, password=:password, " +
//                            "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
//                return null;
//            }
//            jdbcTemplate.batchUpdate("DELETE FROM user_roles WHERE user_id=?", Collections.singletonList(new Integer[]{user.getId()}));
//            jdbcTemplate.batchUpdate("INSERT INTO user_roles(user_id, role) VALUES (?, ?)", new BatchPreparedStatementSetter() {
//                @Override
//                public void setValues(PreparedStatement ps, int i) throws SQLException {
//                    ps.setInt(1, user.getId());
//                    ps.setString(2, roles.get(i).toString());
//                }
//                @Override
//                public int getBatchSize() {
//                    return user.getRoles().size();
//                }
//            });
//        }
//        return user;
//    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());

            jdbcTemplate.batchUpdate("INSERT INTO user_roles(user_id, role) VALUES (?, ?)", args(user, user.getId()));
        } else {
            if (namedParameterJdbcTemplate.update(
                    "UPDATE users SET name=:name, email=:email, password=:password, " +
                            "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
                return null;
            }
            jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());
            jdbcTemplate.batchUpdate("INSERT INTO user_roles(user_id, role) VALUES (?, ?)", args(user, user.getId()));
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users LEFT OUTER JOIN user_roles ON id=user_id WHERE id=?", EXTRACTOR, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users LEFT OUTER JOIN user_roles ON id=user_roles.user_id WHERE email=?", EXTRACTOR, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users LEFT OUTER JOIN user_roles ON id=user_id ORDER BY name, email DESC", EXTRACTOR);
    }

    private List<Object[]> args(User user, Integer id) {
        return user.getRoles().stream().map(role -> new Object[]{id, role.toString()}).collect(Collectors.toList());
    }
}
