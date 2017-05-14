package ru.ilonich.roswarcp.repo;

public interface RawSqlExecutor {
    String executeDirectQuery(String sql);
}
