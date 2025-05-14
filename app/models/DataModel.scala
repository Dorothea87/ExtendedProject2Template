package models

import play.api.libs.json.{Json, OFormat}

case class DataModel(_id: String,
                     username: String,
                     location: String,
                     followers: Int,
                     following: Int,
                     created_at: String)


object DataModel {
  implicit val formats: OFormat[DataModel] = Json.format[DataModel]
}