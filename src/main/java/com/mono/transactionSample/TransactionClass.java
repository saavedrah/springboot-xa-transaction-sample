package com.mono.transactionSample;

import com.arjuna.ats.arjuna.common.CoreEnvironmentBeanException;
import com.arjuna.ats.arjuna.common.arjPropertyManager;
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
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

public class TransactionClass {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionClass.class);

    String sqlTemplate = "SELECT COMMODITY_ID FROM COMMODITY_CATEGORY";
    String insertTemplate = "INSERT INTO COMMODITY_CATEGORY (COMMODITY_ID, CATEGORY_ID, PROGRAM_ID) VALUES ('%s', '%s', 'TEST_NAME')";

    static final String dataSourceName = AppConfiguration.dataSourceName;
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
                String sql = sqlTemplate;
                stmt = conn.getConnection().prepareStatement(sql);
                stmt.executeQuery();
                LOGGER.info("SELECT...cycle: " + loop);
            } else {
                String sql = String.format(insertTemplate, id, id);
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

}
