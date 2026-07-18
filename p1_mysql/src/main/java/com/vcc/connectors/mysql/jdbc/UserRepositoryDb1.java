package com.vcc.connectors.mysql.jdbc;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserRepositoryDb1 {

    private final JdbcTemplate jdbcTemplate;

    public UserRepositoryDb1(@Qualifier("jdbcTemplateDb1") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public int save(User user) {
        return jdbcTemplate.update("INSERT INTO users (username, email) VALUES (?, ?)", user.getUsername(), user.getEmail());
    }

    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM users ORDER BY id DESC", new UserRowMapper());
    }

    public User findById(Long id) {
        List<User> list = jdbcTemplate.query("SELECT * FROM users WHERE id = ?", new UserRowMapper(), id);
        return list.isEmpty() ? null : list.get(0);
    }

    public int update(User user) {
        return jdbcTemplate.update("UPDATE users SET username = ?, email = ? WHERE id = ?", user.getUsername(), user.getEmail(), user.getId());
    }

    public int delete(Long id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id = ?", id);
    }

    public List<User> search(String keyword) {
        String query = "%" + keyword + "%";
        return jdbcTemplate.query("SELECT * FROM users WHERE username LIKE ? OR email LIKE ? ORDER BY id DESC", new UserRowMapper(), query, query);
    }

    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            return User.builder()
                    .id(rs.getLong("id"))
                    .username(rs.getString("username"))
                    .email(rs.getString("email"))
                    .build();
        }
    }
}
