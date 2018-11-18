package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

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
            insertUserRoles(user);
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) != 0) {
            updateUserRoles(user);
        } else {
            return null;
        }
        return user;
    }

    private void updateUserRoles(User user) {
        jdbcTemplate.update("DELETE FROM user_roles ur WHERE ur.user_id = ?", user.getId());
        insertUserRoles(user);
    }

    private void insertUserRoles(User user) {
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
        var users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        var user = DataAccessUtils.singleResult(users);
        if (user != null) {
            getUserRoles(user);
        }
        return user;
    }

    @Override
    public User getByEmail(String email) {
        var users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        var user = DataAccessUtils.singleResult(users);
        if (user != null) {
            getUserRoles(user);
        }
        return user;
    }

    private void getUserRoles(User user) {
        var roles = jdbcTemplate.query("SELECT * FROM user_roles WHERE user_id = ? ",
                (rs, i) -> Role.valueOf(rs.getString("role")), user.getId());
        user.setRoles(roles);
    }

    @Override
    public List<User> getAll() {
        var users = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
        Map<Integer, Set<Role>> map = new HashMap<>();
        jdbcTemplate.query("SELECT * FROM user_roles ",
                rs -> {
                    var role = Role.valueOf(rs.getString("role"));
                    map.computeIfAbsent(rs.getInt("user_id"), k -> new HashSet<>()).add(role);
                });
        users.forEach(user -> user.setRoles(map.get(user.getId())));
        return users;
    }

}
