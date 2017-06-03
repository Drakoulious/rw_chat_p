package ru.ilonich.roswarcp.model;

/**
 * Created by Никола on 03.06.2017.
 */
public class SqlCommand {
    private int id;
    private String title;
    private String sql;

    public SqlCommand() {
    }

    public SqlCommand(int id, String title, String sql) {
        this.id = id;
        this.title = title;
        this.sql = sql;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
