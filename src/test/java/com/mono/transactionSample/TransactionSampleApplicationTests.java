package com.mono.transactionSample;

import com.arjuna.ats.arjuna.common.CoreEnvironmentBeanException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.naming.NamingException;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import java.sql.SQLException;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TransactionSampleApplication.class, loader = AnnotationConfigContextLoader.class)
public class TransactionSampleApplicationTests {
	private static final Logger LOGGER = LoggerFactory.getLogger(TransactionSampleApplicationTests.class);


	@Test
	public void executeTransaction() throws SQLException, NotSupportedException, NamingException, SystemException, CoreEnvironmentBeanException {
		TransactionClass transactionClass = new TransactionClass();
		transactionClass.runStatement();
	}
}
