# Test XA DataSource

Uses PGXADataSource and JBoss Transaction manager.
For now, Only one data source is tested.
Sends selects and updates in a loop

## Settings

Create the test table located in ddl.
Run the test case TransactionSimpleApplicationTest

## Problem
Fails getting the connection the second time
How to assign a connection pool to the XA data source