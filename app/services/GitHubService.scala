package services

import connectors.GithubConnector
import models.{DataModel, GitHubUser}

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class GitHubService @Inject()(connector: GithubConnector) {

  def getGithubUserByName(urlOverride: Option[String] = None, username: String) (implicit ec: ExecutionContext): Future[GitHubUser] =
    connector.get[DataModel](urlOverride.getOrElse(s"https://api.github.com/users/$username"))
}
