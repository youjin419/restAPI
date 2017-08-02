package database

import com.outworkers.phantom.dsl._
import module.CassandraCluster._
import services.UserService

class YouDatabase (override val connector: KeySpaceDef) extends Database[YouDatabase](connector) {
  // 데이터베이스 생성 : 추상클래스 extends
  object testModel extends UserService with connector.Connector
}

object YJDatabase extends YouDatabase(connector)

// 상속받을 데이터베이스.
trait Jindatabase {
  def database: YouDatabase
}

// 최종 데이터베이스
trait YouJindatabase extends Jindatabase {
  override val database = YJDatabase
}