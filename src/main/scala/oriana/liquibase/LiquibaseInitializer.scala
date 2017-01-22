package oriana.liquibase

import liquibase.Liquibase
import liquibase.logging.LogFactory
import liquibase.database.jvm.JdbcConnection
import liquibase.resource.ClassLoaderResourceAccessor
import org.slf4j.LoggerFactory
import oriana.DatabaseActor.InitComplete
import oriana.{DatabaseCommandExecution, DatabaseContext}

import scala.concurrent.{ExecutionContext, Future}

class LiquibaseInitializer(resourceName: String = "db-changelog.xml", classLoader: ClassLoader = classOf[LiquibaseInitializer].getClassLoader)(implicit ec: ExecutionContext) extends oriana.DBInitializer[DatabaseContext with DatabaseCommandExecution] {
  val logger = LoggerFactory.getLogger(classOf[LiquibaseInitializer])
  override def apply(ctx: DatabaseContext with DatabaseCommandExecution) = {
    LogFactory.setInstance(SLF4JLoggingBridge)

    Future {
      val connection = ctx.database.source.createConnection()
      try {
        val liquibase = new Liquibase(resourceName,
          new ClassLoaderResourceAccessor(classLoader),
          new JdbcConnection(connection))

        liquibase.update("")

        InitComplete
      } finally {
        connection.close()
      }
    }
  }
}
