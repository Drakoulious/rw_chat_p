package ru.ilonich.roswarcp.repo;

import java.util.List;

public interface RawSqlExecutor {
    String executeDirectQuery(String sql);
    List<List<String>> executeQuery(String sql);
}
