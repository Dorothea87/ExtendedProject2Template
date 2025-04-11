package model

import play.api.libs.json.{Json, OFormat}

case class DataModel(_id: String, username: String, created_at: String, location: String, followers: Int, following: Int)


object DataModel {
  implicit val formats: OFormat[DataModel] = Json.format[DataModel]
}