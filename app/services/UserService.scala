package services


import com.outworkers.phantom.dsl.{CassandraTable, ConsistencyLevel, PartitionKey, ResultSet, RootConnector, StringColumn, _}
import database.YouJindatabase
import models.User

import scala.concurrent.Future

class testYoumodel extends CassandraTable[UserService, User] {
  // 테이블명 명시.
  override def tableName: String = "user"

  // 테이블 데이터 타입 명시
  object id extends StringColumn(this) with PartitionKey {
    override lazy val name = "id"
  }

  // 컬럼 데이터 타입 결정
  object password extends StringColumn(this)

  // 컬럼 데이터 null 허용
  object email extends OptionalStringColumn(this)

  // 컬럼 데이터 null 허용
  object age extends OptionalIntColumn(this)

  override def fromRow(r: Row): User = User(id(r), password(r), email(r), age(r))

}

// 쿼리 작성
abstract class UserService extends testYoumodel with RootConnector {
  // create
  // ResultSet: db에서 명령값
  def insertUser(user: User): Future[ResultSet] = {
    insert
      .value(_.id, user.id)
      .value(_.password, user.password)
      .value(_.email, user.email)
      .value(_.age, user.age)
      .consistencyLevel_=(ConsistencyLevel.ONE)
      .future()
  }

  //read, future가 언젠간 option[user]로 값을 가지게 됨.
  def readUser(id: String): Future[Option[User]] = {
    select
      .where(_.id eqs id)
      .consistencyLevel_=(ConsistencyLevel.ONE).one()
  }

  // update
  def updateUser(user: User): Future[ResultSet] = {
    update
      .where(_.id eqs user.id)
      .modify(_.email setTo user.email)
      .and(_.age setTo user.age)
      .and(_.password setTo user.password)
      .consistencyLevel_=(ConsistencyLevel.ONE)
      .future()
  }

  //delete
  def deleteUser(id: String): Future[ResultSet] = {
    delete
      .where(_.id eqs id)
      .consistencyLevel_=(ConsistencyLevel.ONE)
      .future()
  }

}

// 실제 컨트롤러에서 작동하는 동작
trait UserFinalService extends YouJindatabase {
  //insert
  def userInsert(user: User): Future[User] = {
    database.testModel.insertUser(user).map(_ => user)
  }

  //read
  def userRead(id: String): Future[Option[User]] = {
    database.testModel.readUser(id)
  }

  // update (데이터는 전부 받아옴. 아이디 값에 따라 비교)
  def userUpdate(user: User): Future[User] = {
    for {
      byUser <- database.testModel.updateUser(user).map(_ => user)
    } yield byUser

  }

  // delete
  def userDelete(id: String): Future[ResultSet] = {
    for {
      byUser <- database.testModel.deleteUser(id)
    } yield byUser

  }
}

// object
object UserFinalService extends UserFinalService with YouJindatabase