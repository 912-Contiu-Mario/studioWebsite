package com.example.Repository;

import com.example.Model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class userRepo {
    private final JdbcTemplate query;

    public userRepo(JdbcTemplate query) {
        this.query = query;
    }


    public List<User> getUsers(){
        String sql = "SELECT * FROM Users";
        return query.query(sql, (rs, rownumber) -> new User(rs.getString(1), rs.getString(2)));
    }
}
