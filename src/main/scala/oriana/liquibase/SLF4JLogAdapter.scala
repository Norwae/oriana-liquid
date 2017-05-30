package oriana.liquibase

import liquibase.changelog.{ChangeSet, DatabaseChangeLog}
import liquibase.logging.{LogLevel, Logger}
import liquibase.servicelocator.PrioritizedService
import org.slf4j.LoggerFactory

class SLF4JLogAdapter(name: String) extends Logger {
  private val backing = LoggerFactory.getLogger(name)

  override def severe(message: String) = backing.error(message)

  override def severe(message: String, e: Throwable) = backing.error(message, e)

  override def warning(message: String) = backing.warn(message)

  override def warning(message: String, e: Throwable) = backing.warn(message, e)

  override def debug(message: String) = backing.debug(message)

  override def debug(message: String, e: Throwable) = backing.debug(message, e)

  override def getLogLevel = LogLevel.DEBUG

  override def setChangeSet(changeSet: ChangeSet) = if (changeSet != null) backing.info(s"Now processing $changeSet")

  override def closeLogFile() = ()

  override def setName(name: String) = ()

  override def setChangeLog(databaseChangeLog: DatabaseChangeLog) = if (databaseChangeLog != null) backing.info(s"Now procesing $databaseChangeLog")

  override def info(message: String) = backing.info(message)

  override def info(message: String, e: Throwable) = backing.info(message, e)

  override def setLogLevel(level: String) = backing.info(s"Tried to change log level to $level")

  override def setLogLevel(level: LogLevel) = backing.info(s"Tried to change log level to $level")

  override def setLogLevel(logLevel: String, logFile: String) = backing.info(s"Tried to change log level to $logLevel, with file $logFile")

  override def getPriority = PrioritizedService.PRIORITY_DEFAULT
}
