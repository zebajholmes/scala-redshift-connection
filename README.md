# scala-redshift-connection
a simple scala package for creating a jdbc connection to and querying aws redshift

a connection object can be instantiated by passing the DbConnection constructor a RsCreds object like so:

val connection = new DbConnection(RsCreds("host", "user","database", "port", "password"))

you can also get an RsCreds object from an aws secrets manager like so:

val myRsCreds = SecretsUtil.getRsCreds("my_secrets_manager_name", "aws-region") //the aws region defaults to "us-east-1"

a DbConnection object has three methods for querying redshift:

1. runQuery  
runs a query with no return example: 
   connection.runQuery("create table my_schema.my_table (col1 varchar(10));")

2. runQueryList
runs a list of queries with no return example: 
   val queryList = List("create table my_schema.my_table (col1 varchar(10));", "drop table table my_schema.my_table;")
   connection.runQueryList(queryList)
   
3. runQueryGetResults
runs a querey and retuns results as a list example:
   val results = connection.runQueryGetResults("select * from my_schema.my_table;")
