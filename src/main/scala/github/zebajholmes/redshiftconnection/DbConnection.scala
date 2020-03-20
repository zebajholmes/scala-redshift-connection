package github.zebajholmes.redshiftconnection

import java.sql.{DriverManager, ResultSet}
import java.util.Properties
import github.zebajholmes.redshiftconnection.SecretsUtil.RsCreds


class DbConnection(creds: RsCreds) {

  Class.forName("com.amazon.redshift.jdbc.Driver")

  val jdbcUrl = s"jdbc:redshift://${creds.host}:${creds.port}/${creds.database}"
  val connectionProperties = new Properties()

  connectionProperties.put("user", creds.user)
  connectionProperties.put("password", creds.password)


  def runQuery(sql: String): Unit = {

    val conn = DriverManager.getConnection(
      jdbcUrl, connectionProperties)
    val stmt = conn.createStatement

    try {
      stmt.execute(sql)
    } finally {
      stmt.close()
      conn.close()
    }

  }


  def runQueryList(sql: List[String]): Unit = {

    val conn = DriverManager.getConnection(jdbcUrl, connectionProperties)
    val stmt = conn.createStatement

    try {
      sql.foreach{i => stmt.execute(i)}
    } finally {
      stmt.close()
      conn.close()
    }

  }


  def runQueryGetResults(sql: String): List[Any] = {

    val conn = DriverManager.getConnection(jdbcUrl, connectionProperties)
    val stmt = conn.createStatement
    val rs: ResultSet = stmt.executeQuery(sql)

    val it = new Iterator[String] {
      def hasNext = rs.next()
      def next() = rs.getString(1)
    }.toList

    stmt.close()
    conn.close()

    it

  }

}
