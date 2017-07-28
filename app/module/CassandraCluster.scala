package module

import javax.inject.Inject

import com.outworkers.phantom.connectors.{CassandraConnection, ContactPoints}
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory
import play.api.inject.ApplicationLifecycle

import scala.collection.JavaConversions._
/**
  * Created by hana on 2017-07-28.
  */
class CassandraCluster {
  val log = LoggerFactory.getLogger(getClass)
  log.debug("Binding CassandraCluster")
  private val config = ConfigFactory.load()

  private val hosts = config.getStringList("cassandra.host")
  private val keyspace = config.getString("cassandra.keyspace")
  private val username = config.getString("cassandra.username")
  private val password = config.getString("cassandra.password")

  /**
    * Create a connector with the ability to connects to
    * multiple hosts in a secured cluster
    */

  lazy val connector: CassandraConnection = ContactPoints(hosts).withClusterBuilder(_.withCredentials(username, password)).keySpace(keyspace)
}
