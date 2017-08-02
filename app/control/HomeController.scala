package control

import javax.inject.Inject

import database.YouJindatabase
import models.User
import module.CassandraCluster
import play.api.libs.functional.syntax._
import play.api.libs.json.{Json, Reads, Writes, _}
import play.api.mvc.{Action, Controller}
import services.UserFinalService

import scala.concurrent.ExecutionContext.Implicits.global


class HomeController @Inject()() extends Controller with YouJindatabase with CassandraCluster.connector.Connector {
  // user 기본 데이터 생성
  // case class와 class의
  // 상속받아온 service가 있으므로 this 입력 후 해당 메소드 출력 가능

  class UserInfo(pId : String, pPass : String) {
    val Id =  pId
    val pass = pPass
  }

  // 실행 action
  def index = Action {
    Ok("test")
  }

  // create user,post
  def createUser = Action.async(parse.json) { request =>
    val userInfoJson = request.body
    val userInfo: User = userInfoJson.as[User]

    // id값을 보낸다. 이미 create에는 데이터가 전부 들어가 있기 때문에 id로 구분.
    // string -> 데이터타입 형식
    UserFinalService.userInsert(userInfo).map  { i =>
      Ok(Json.toJson(i))
    }

  }

  // read user,get
  def readUser(id: String, limit : String) = Action.async {
    //val result = UserFinalService.userRead(id)
    UserFinalService.userRead(id).map { m =>
      // 데이터 전체 값을 받아 read
      // map으로 받은 후 최종적으로 map으로 변환된다.s
      Ok(limit)
    }
  }

  // update user
  def updateUser(id : String) = Action.async(parse.json) { request =>
    val userInfoJson = request.body
    val userInfo = userInfoJson.as[User]
    // 수정값 body
    // 아이디값

    UserFinalService.userUpdate(userInfo.copy(id=id)).map { test =>
      Created(Json.toJson(test))
    }
  }

  // delete user
  def deleteUser(id : String) = Action.async {
    UserFinalService.userDelete(id).map { d =>
      NoContent
    }
  }

  implicit def userInfoReads: Reads[User] = (
    (__ \ "id").readNullable[String].map{case Some(id) => id case None => null} and
      (__ \ "password").read[String] and
      (__ \ "email").readNullable[String] and
      (__ \ "age").readNullable[Int]
    ) (User.apply _)

  implicit def userInfoWrites: Writes[User] = (
    (__ \ "id").write[String] and
      (__ \ "password").write[String] and
      (__ \ "email").writeNullable[String] and
      (__ \ "age").writeNullable[Int]
    ) (unlift(User.unapply))

}