package models

case class User(id : String, password : String, email : Option[String] = None, age : Option[Int]=None)
