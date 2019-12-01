package com.mono.transactionSample;

import com.arjuna.ats.arjuna.common.CoreEnvironmentBeanException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.naming.NamingException;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import java.sql.SQLException;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TransactionSampleApplication.class, loader = AnnotationConfigContextLoader.class)
public class XATransactionSampleApplicationTests {
	private static final Logger LOGGER = LoggerFactory.getLogger(XATransactionSampleApplicationTests.class);


	@Test
	public void executeTransaction() throws SQLException, NotSupportedException, NamingException, SystemException, CoreEnvironmentBeanException {
		XATransactionClass transactionClass = new XATransactionClass();
		transactionClass.runStatement();
	}

	@Test
	public void executeInsertDelete() throws Exception {
		XATransactionClass transactionClass = new XATransactionClass();
		transactionClass.runInsertDelete();
	}

	@Test
	public void deleteInsertWithJdbcDriverPropertiesTest() throws Exception {
		XATransactionClass transactionClass = new XATransactionClass();
		transactionClass.deleteInsertWithJdbcDriverProperties();
	}
}
