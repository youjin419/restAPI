package control

import play.api.libs.json.{Json, Reads, Writes}
import play.api.mvc.{Action, Controller}
import play.api.libs.functional.syntax._
import play.api.libs.json._

class HomeController extends Controller {

  // user 기본 데이터 생성
  case class User(Id : String, password : String, email : Option[String] = None, age : Option[Int]=None)

  class UserInfo(pId : String, pPass : String) {
    val Id =  pId
    val pass = pPass
  }
  def index = Action {
    Ok("test")
  }

  // create user
  def createUser = Action(parse.json) { request =>
    val userInfoJson = request.body
    val userInfo = userInfoJson.as[User]

    print(userInfo)
    Ok(Json.obj("Id" -> "admin"))
  }

 // read user
  def readUser(Id : String) = Action {
    val userMem: User = User("you","aaa")
    val userMem1 = new  UserInfo("you", "aaa")

    Ok(Json.toJson(userMem))
    NoContent
  }

  // update user
  def updateUser(Id : String) = Action { request =>
    val userInfoJson = request.body

    // update : DB에서 아이디 값 받은 후 업데이트 처리
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
