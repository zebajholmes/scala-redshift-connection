package github.zebajholmes.redshiftconnection

import org.scalatest.funspec.AnyFunSpec
import java.sql.DriverManager

//tests are set to ignore as you will need a secrets manager for redshift set up to run them
//just replace "my_secrets_manager" with your secrets manager name then set test to describe if you want to run

class Test extends AnyFunSpec {

  ignore ("test SecretsUtil") {

    it("should get the creds") {

      val tester = SecretsUtil.getSecretString("my_secrets_manager", "region")

    }

  }

  ignore ("test DbConnection") {

    val tester = new DbConnection(SecretsUtil.getRsCreds("my_secrets_manager"))

    it("should make a connection to the db") {

      val conn = DriverManager.getConnection(tester.jdbcUrl, tester.connectionProperties)

      assert(!conn.isClosed)
      conn.close()
      assert(conn.isClosed)

    }

    it("should execute a query") {

      tester.runQuery("create table if not exists public.scala_conn_test(id integer);")
      tester.runQuery("drop table public.scala_conn_test")

    }

    it("should execute a list of queries") {

      tester.runQueryList(
        List("create table if not exists public.scala_conn_test2(id integer);", "drop table public.scala_conn_test2"))

    }

    it("should query and get results") {

      val res = tester.runQueryGetResults("select 'hello';")

      assert(res.head == "hello")

    }

  }

}
