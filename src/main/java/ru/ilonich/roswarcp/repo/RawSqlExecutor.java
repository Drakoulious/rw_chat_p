package ru.ilonich.roswarcp.repo;

/**
 * Created by Никола on 10.05.2017.
 */
public interface RawSqlExecutor {
    String executeDirectQuery(String sql);
}
