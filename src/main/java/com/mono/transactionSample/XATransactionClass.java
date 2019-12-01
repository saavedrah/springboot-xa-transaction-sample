package com.mono.transactionSample;

import com.arjuna.ats.arjuna.common.CoreEnvironmentBeanException;
import com.arjuna.ats.arjuna.common.arjPropertyManager;
import com.arjuna.ats.jdbc.TransactionalDriver;
import org.apache.commons.dbcp.BasicDataSource;
import org.postgresql.xa.PGXADataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.XAConnection;
import javax.sql.XADataSource;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class XATransactionClass {
    private static final Logger LOGGER = LoggerFactory.getLogger(XATransactionClass.class);

    final static String SELECT_TEMPLATE = "SELECT COMMODITY_ID FROM COMMODITY_CATEGORY WHERE COMMODITY_ID = '%s'";
    final static String INSERT_TEMPLATE = "INSERT INTO COMMODITY_CATEGORY (COMMODITY_ID, CATEGORY_ID, PROGRAM_ID) VALUES ('%s', '%s', 'TEST_NAME')";
    final static String DELETE_TEMPLATE = "DELETE FROM COMMODITY_CATEGORY WHERE COMMODITY_ID = '%s'";

    final static String INSERT_TEMPLATE_PS = "INSERT INTO COMMODITY_CATEGORY (COMMODITY_ID, CATEGORY_ID, PROGRAM_ID) VALUES (?, ?,  ?)";
    final static String DELETE_TEMPLATE_PS = "DELETE FROM COMMODITY_CATEGORY WHERE COMMODITY_ID = ?";

    static final String dataSourceName = "application";

    private XAConnection connection;

    private static boolean initialize = true;

    public void runStatement() throws SystemException, NotSupportedException, SQLException, NamingException, CoreEnvironmentBeanException {

        if (initialize) {
            initialize = false;

            _initializeContext();
        }

        javax.transaction.TransactionManager transactionManager = com.arjuna.ats.jta.TransactionManager.transactionManager();

        transactionManager.begin();

        for (int i = 0; i<10; i++) {
            LOGGER.info("Transaction....: " + i);

            this.runStatement(i);
        }

        transactionManager.rollback();

        LOGGER.info("END");
    }

    public void runInsertDelete() throws Exception {
        if (initialize) {
            initialize = false;

            _initializeContext();
        }
        String commodityId = "MY_TEST_ID";
        javax.transaction.TransactionManager transactionManager = com.arjuna.ats.jta.TransactionManager.transactionManager();

        transactionManager.begin();

        {
            XADataSource ds = InitialContext.doLookup("java:/comp/env/jdbc/" + dataSourceName);
            connection = ds.getXAConnection();
//            connection.getConnection().setAutoCommit(false);
            transactionManager.getTransaction().enlistResource(connection.getXAResource());

            LOGGER.info("CONNECTION" + connection.getConnection());
            LOGGER.info("CONNECTION AUTOCOMMIT" + connection.getConnection().getAutoCommit());
        }

        {
            LOGGER.info("SELECT - TransactionId: " + transactionManager);
            String selectStatement = String.format(SELECT_TEMPLATE, commodityId);
            List<String> result = this.getCommodity(selectStatement);
            if (result.size() > 0) {
                LOGGER.info("DELETE - TransactionId: " + transactionManager);
                String deleteStatement = String.format(DELETE_TEMPLATE, commodityId);
                this.sqlUpdatePrepareStatement(deleteStatement);

                LOGGER.info("INSERT - TransactionId: " + transactionManager);
                String insertStatement = String.format(INSERT_TEMPLATE, commodityId, commodityId);
                this.sqlUpdatePrepareStatement(insertStatement);

                LOGGER.info("SELECT - TransactionId: " + transactionManager);
                List<String> inResult2 = this.getCommodity(selectStatement);
                if (inResult2.size() != 1) {
                    throw new Exception ("Not inserted");
                }
            } else {
                LOGGER.info("SINGLE INSERT - TransactionId: " + transactionManager);
                String insertStatement = String.format(INSERT_TEMPLATE, commodityId, commodityId);
                this.sqlUpdatePrepareStatement(insertStatement);
            }
        }

        transactionManager.commit();

        connection.close();
    }

    private void sqlUpdatePrepareStatement(String statementSql) throws SQLException {
        connection.getConnection().createStatement().execute(statementSql);
    }

    public void close() throws SQLException {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }

    private List<String> getCommodity(String sql) throws SQLException {
        List<String> strings = new LinkedList<>();
        ResultSet resultSet = connection.getConnection().createStatement().executeQuery(sql);
        while (resultSet.next()) {
            strings.add(resultSet.getString("commodity_id"));
        }

        return strings;
    }

    private void runStatement (int loop) throws NamingException, SQLException {

        XADataSource ds = InitialContext.doLookup("java:/comp/env/jdbc/" + dataSourceName);

        Assert.notNull(ds, "DataSource is null");
        XAConnection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ds.getXAConnection();

            LOGGER.info("******************** SELECT XA-CONNECTION: " + conn.toString());
            LOGGER.info("******************** SELECT CONNECTION: " + conn.getConnection());
            LOGGER.info("Connection - CONNECTION CLASS: " + conn.getClass().getName());
            LOGGER.info("connection - isClosed:" + conn.getConnection().isClosed());
            LOGGER.info("connection - getAutoCommit:" + conn.getConnection().getAutoCommit());

            String id = UUID.randomUUID().toString();

            if ( (loop % 2) != 0) {
                stmt = conn.getConnection().prepareStatement(SELECT_TEMPLATE);
                stmt.executeQuery();
                LOGGER.info("SELECT...cycle: " + loop);
            } else {
                String sql = String.format(INSERT_TEMPLATE, id, id);
                stmt = conn.getConnection().prepareStatement(sql);
                int effective_row =  stmt.executeUpdate();
                LOGGER.info("UPDATE ..." + effective_row);
            }

        } catch (SQLException e) {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
            conn = null;

            throw e;
        }
    }

    private void _initializeContext() throws NamingException, SQLException, CoreEnvironmentBeanException {
        arjPropertyManager.getCoreEnvironmentBean().setNodeIdentifier("1");
        System.setProperty("java.naming.factory.initial", "org.apache.naming.java.javaURLContextFactory");
        System.setProperty("java.naming.factory.url.pkgs", "org.apache.naming");
        Map<String, BasicDataSource> dataSourceMap = ApplicationContextHolder.getContext().getBeansOfType(org.apache.commons.dbcp.BasicDataSource.class);
        if (dataSourceMap != null && !dataSourceMap.isEmpty()) {
            for (Map.Entry<String,org.apache.commons.dbcp.BasicDataSource> dataSource : dataSourceMap.entrySet()) {
                System.out.println("DRIVER MANAGER DATASOURCE: " + dataSource.getKey());
                System.out.println("DRIVER MANAGER DATASOURCE URL: " + dataSource.getValue().getUrl());

                // What to do with it
            }
        }

        PGXADataSource xaDataSource = new org.postgresql.xa.PGXADataSource();
        xaDataSource.setUser("scuserapp");
        xaDataSource.setPassword("scuserapppw");
        // how to set max-pool-size ??

        InitialContext initialContext = prepareInitialContext();
        final String name = "java:/comp/env/jdbc/" + dataSourceName;
        initialContext.bind(name, xaDataSource);

        DriverManager.registerDriver(new com.arjuna.ats.jdbc.TransactionalDriver());
    }

    private static InitialContext prepareInitialContext()
            throws NamingException {
        final InitialContext initialContext = new InitialContext();

        try {
            initialContext.lookup("java:/comp/env/jdbc");
        } catch (NamingException ne) {
            initialContext.createSubcontext("java:");
            initialContext.createSubcontext("java:/comp");
            initialContext.createSubcontext("java:/comp/env");
            initialContext.createSubcontext("java:/comp/env/jdbc");
        }

        return initialContext;
    }


    public void deleteInsertWithJdbcDriverProperties() throws Exception {
        arjPropertyManager.getCoreEnvironmentBean().setNodeIdentifier("1");

        // starting transaction
        javax.transaction.TransactionManager tm = com.arjuna.ats.jta.TransactionManager.transactionManager();
        tm.begin();

        // XADataSource initialized to be passed to transactional driver
        XADataSource xaDataSource = new org.postgresql.xa.PGXADataSource();
        ((org.postgresql.xa.PGXADataSource)xaDataSource).setUser("scuserapp");
        ((org.postgresql.xa.PGXADataSource)xaDataSource).setPassword("scuserapppw");

    // the datasource is put as property with the special name
        Properties connProperties = new Properties();
        connProperties.put(TransactionalDriver.XADataSource, xaDataSource);

// getting connection when the 'url' is 'jdbc:arjuna' prefix which determines
// the Naryana drive to be used
        Connection conn = DriverManager.getConnection(TransactionalDriver.arjunaDriver, connProperties);



        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/ddHH:mm:ss");





// execution, committing/rolling-back
        try {
            LOGGER.info("DELETE");
            PreparedStatement deletePS = conn.prepareStatement(DELETE_TEMPLATE_PS);
            deletePS.setString(1, "MY_TEST");
            deletePS.executeUpdate();

            LOGGER.info("INSERT");
            PreparedStatement insertPS = conn.prepareStatement(INSERT_TEMPLATE_PS);
            insertPS.setString(1, "MY_TEST");
            insertPS.setString(2, "MY_TEST");
            insertPS.setString(3, dateFormat.format(new Date()));
            insertPS.executeUpdate();

            LOGGER.info("COMMIT");
            tm.commit();
        } catch (Exception e) {
            tm.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }
}
