package models

import play.api.libs.json.{Json, OFormat}

case class GitHubUser(_id: String,
                      location: Option[String],
                      followers: Int,
                      following: Int,
                      created_at: String
                     )


object GitHubUser {
  implicit val formats: OFormat[GitHubUser] = Json.format[GitHubUser]
}