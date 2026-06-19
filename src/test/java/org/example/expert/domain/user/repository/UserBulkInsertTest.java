package org.example.expert.domain.user.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserBulkInsertTest {

    private static final int TOTAL_COUNT = 1_000_000;
    private static final int BATCH_SIZE = 1000;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 유저_100만건을_JDBC_bulk_insert로_생성한다() {
        String sql = "INSERT INTO users (nickname, email, password, user_role, created_at, modified_at) VALUES (?, ?, ?, ?, ?, ?)";
        LocalDateTime now = LocalDateTime.now();

        long start = System.currentTimeMillis();

        for (int batchStart = 1; batchStart <= TOTAL_COUNT; batchStart += BATCH_SIZE) {
            int batchEnd = Math.min(batchStart + BATCH_SIZE - 1, TOTAL_COUNT);
            int batchFrom = batchStart;

            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int index) throws SQLException {
                    int i = batchFrom + index;
                    ps.setString(1, "user_" + i);
                    ps.setString(2, "user" + i + "@test.com");
                    ps.setString(3, "password");
                    ps.setString(4, "NORMAL");
                    ps.setObject(5, now);
                    ps.setObject(6, now);
                }

                @Override
                public int getBatchSize() {
                    return batchEnd - batchFrom + 1;
                }
            });
        }

        long elapsed = System.currentTimeMillis() - start;
        System.out.println("100만건 insert 소요 시간: " + elapsed + "ms");

        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Integer.class);
        assertThat(count).isGreaterThanOrEqualTo(TOTAL_COUNT);
    }
}