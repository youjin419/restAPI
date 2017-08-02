package module

import com.google.inject.AbstractModule
import org.slf4j.{Logger, LoggerFactory}

/**
  * Created by hana on 2017-07-28.
  */
class MainModule extends AbstractModule{
  private implicit val log: Logger = LoggerFactory.getLogger(getClass)

  override def configure(): Unit = {
    log.debug("Binding MainModule")
    //bind(classOf[CassandraCluster]).asEagerSingleton()
  }
}
