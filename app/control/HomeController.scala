package control

import javax.inject.Inject

import module.CassandraCluster
import play.api.libs.json.{Json, Reads, Writes}
import play.api.mvc.{Action, Controller}
import play.api.libs.functional.syntax._
import play.api.libs.json._

class HomeController @Inject()(cassandraCluster : CassandraCluster) extends Controller {

  // user 기본 데이터 생성
  // case class와 class의 차이
  case class User(Id : String, password : String, email : Option[String] = None, age : Option[Int]=None)

  class UserInfo(pId : String, pPass : String) {
    val Id =  pId
    val pass = pPass
  }

  // 실행 action
  def index = Action {
    println(cassandraCluster.connector)
    Ok("test")
  }

  // create user
  def createUser = Action(parse.json) { request =>
    val userInfoJson = request.body
    val userInfo = userInfoJson.as[User]

    print(userInfo)
    // id값을 보낸다. 이미 create에는 데이터가 전부 들어가 있기 때문에 id로 구분.
    // string -> 데이터타입 형식
    Ok(Json.obj("Id" -> "admin"))
  }

 // read user
  def readUser(Id : String) = Action {
    val userMem: User = User("you","aaa")
    val userMem1 = new  UserInfo("you", "aaa")

    // 데이터 전체 값을 받아 read
    Ok(Json.toJson(userMem))
    NoContent
  }

  // update user
  def updateUser(Id : String) = Action { request =>
    val userInfoJson = request.body

    // update query
    NoContent
  }

  // delete user
  def deleteUser(Id : String) = Action {

    // delete : DB에서 아이디 값 받은 후 삭제 처리
    NoContent
  }

  implicit def userInfoReads: Reads[User] = (
    (__ \ "Id").read[String] and
      (__ \ "password").read[String] and
      (__ \ "email").readNullable[String] and
      (__ \ "age").readNullable[Int]
    ) (User.apply _)

  implicit def userInfoWrites: Writes[User] = (
    (__ \ "Id").write[String] and
      (__ \ "password").write[String] and
      (__ \ "email").writeNullable[String] and
      (__ \ "age").writeNullable[Int]
    ) (unlift(User.unapply))

}
