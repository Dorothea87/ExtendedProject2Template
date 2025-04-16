package services

import cats.data.EitherT
import connectors.GithubConnector
import models.{APIError, DataModel, GitHubUser}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class GitHubService @Inject()(connector: GithubConnector) {

  def getGithubUserByName(urlOverride: Option[String] = None, username: String)(implicit ec: ExecutionContext): EitherT[Future, APIError, GitHubUser] = {
    connector.get[GitHubUser](urlOverride.getOrElse(s"https://api.github.com/users/$username"))
  }
}