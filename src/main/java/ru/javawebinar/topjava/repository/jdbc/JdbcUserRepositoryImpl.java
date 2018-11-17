package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final String SQL_GET_USERS =
            "SELECT u.*,\n" +
                    "       (SELECT string_agg(r.role, ',')\n" +
                    "        FROM user_roles r \n" +
                    "        WHERE r.user_id = u.id) AS roles\n" +
                    "FROM users u \n" +
                    "WHERE (u.id=:id OR :id = -1)\n" +
                    "      AND (u.email = :email or :email = '')\n" +
                    "ORDER BY u.name, u.email";

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
            return null;
        }
        saveUserRoles(user);
        return user;
    }

    private void saveUserRoles(User user) {
        jdbcTemplate.update("DELETE FROM user_roles ur WHERE ur.user_id = ?", user.getId());

        var params = user.getRoles().stream()
                .map(role -> new Object[]{user.getId(), role.toString()})
                .collect(Collectors.toList());
        jdbcTemplate.batchUpdate(
                "INSERT INTO user_roles (user_id, role) VALUES (?, ?)",
                params);
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        return DataAccessUtils.singleResult(getUsers(id, ""));
    }

    @Override
    public User getByEmail(String email) {
        return DataAccessUtils.singleResult(getUsers(-1, email));
    }

    @Override
    public List<User> getAll() {
        return getUsers(-1, "");
    }

    private List<User> getUsers(int id, String email) {
        return namedParameterJdbcTemplate.query(SQL_GET_USERS,
                new MapSqlParameterSource()
                        .addValue("id", id)
                        .addValue("email", email),
                (rs, i) -> {
                    User user = new User(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getInt("calories_per_day"),
                            rs.getBoolean("enabled"),
                            rs.getDate("registered"),
                            null);
                    var roles = Arrays.stream(rs.getString("roles").split(","))
                            .map(Role::valueOf)
                            .collect(Collectors.toSet());
                    user.setRoles(roles);
                    return user;
                });
    }
}
