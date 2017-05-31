package ru.ilonich.roswarcp.repo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component("rawSqlExecutor")
public class RawSqlExecutorImpl implements RawSqlExecutor {
    @Autowired
    DataSource dataSource;

    public String executeDirectQuery(String sql){
        Connection conn = null;
        Statement stmt = null;
        String result = null;
        try{
            conn = dataSource.getConnection();
            conn.setAutoCommit(true);
            //conn.setReadOnly(true); чё париться то лол (на самом деле хз)
            stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery(sql);
            StringBuilder builder = new StringBuilder();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++){
                builder.append(metaData.getColumnName(i)).append(" | ");
            }
            builder.append("\r\n");

            while (rs.next()) {
                for (int i = 0; i < columnCount;) {
                    builder.append(rs.getString(i + 1));
                    if (++i < columnCount) builder.append(" | ");
                }
                builder.append("\r\n");
            }
            result = builder.toString();
            rs.close();
            stmt.close();
            conn.close();
        } catch(Exception e){
            result = e.getMessage();
        }finally {
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
                result = se2.getMessage();
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                result = se.getMessage();
            }
        }
        return result;
    }

    public List<List<String>> executeQuery(String sql){
        Connection conn = null;
        Statement stmt = null;
        List<List<String>> result = new ArrayList<>();
        try{
            conn = dataSource.getConnection();
            conn.setAutoCommit(true);
            //conn.setReadOnly(true);
            stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            List<String> head = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++){
                head.add(metaData.getColumnName(i));
            }
            result.add(head);

            while (rs.next()) {
                List<String> row = new ArrayList<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getString(i));
                }
                result.add(row);
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch(Exception e){
            result.add(Collections.singletonList(e.getMessage()));
        }finally {
            try{
                if(stmt!=null)
                    stmt.close();
            }catch(SQLException se2){
                result.add(Collections.singletonList(se2.getMessage()));
            }
            try{
                if(conn!=null)
                    conn.close();
            }catch(SQLException se){
                result.add(Collections.singletonList(se.getMessage()));
            }
        }
        return result;
    }

}
