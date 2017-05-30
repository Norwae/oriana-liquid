package oriana.liquibase

import liquibase.Liquibase
import liquibase.database.jvm.JdbcConnection
import liquibase.logging.LogFactory
import liquibase.resource.{ClassLoaderResourceAccessor, ResourceAccessor}
import org.slf4j.LoggerFactory
import oriana.DatabaseActor.InitComplete
import oriana.{DatabaseCommandExecution, DatabaseContext, ExecutableDatabaseContext}

import scala.concurrent.{ExecutionContext, Future}

class LiquibaseInitializer(resourceName: String = "db-changelog.xml")(implicit ec: ExecutionContext) extends oriana.DBInitializer[ExecutableDatabaseContext] {
  override def apply(ctx: DatabaseContext with DatabaseCommandExecution): Future[InitComplete.type] = {
    LogFactory.setInstance(SLF4JLoggingBridge)

    Future {
      val connection = ctx.database.source.createConnection()
      try {
        val liquibase = new Liquibase(resourceName,
          resourceAccessor(ctx),
          new JdbcConnection(connection))

        liquibase.update("")

        InitComplete
      } finally {
        connection.close()
      }
    }
  }

  protected def resourceAccessor(originContext: ExecutableDatabaseContext): ResourceAccessor =
    new ClassLoaderResourceAccessor(getClass.getClassLoader) with SlickContextResourceAccessor{
      override def context: ExecutableDatabaseContext = originContext
    }

}
