
package views.html

import _root_.play.twirl.api.TwirlFeatureImports._
import _root_.play.twirl.api.TwirlHelperImports._
import _root_.play.twirl.api.Html
import _root_.play.twirl.api.JavaScript
import _root_.play.twirl.api.Txt
import _root_.play.twirl.api.Xml
import models._
import controllers._
import play.api.i18n._
import views.html._
import play.api.templates.PlayMagic._
import play.api.mvc._
import play.api.data._

object index extends _root_.play.twirl.api.BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,_root_.play.twirl.api.Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with _root_.play.twirl.api.Template0[play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/():play.twirl.api.HtmlFormat.Appendable = {
    _display_ {
      {


Seq[Any](format.raw/*2.1*/("""
"""),_display_(/*3.2*/main("GitHub Example")/*3.24*/ {_display_(Seq[Any](format.raw/*3.26*/("""
  """),format.raw/*4.3*/("""<h1>Welcome to your version of GitHub</h1>
  <h2>Let's get started we will be connecting to the GitHub API</h2>
  <p>Have a look at the documentation for the API <a href="https://docs.github.com/en/rest" target="_blank">here</a></p>
""")))}),format.raw/*7.2*/("""
"""))
      }
    }
  }

  def render(): play.twirl.api.HtmlFormat.Appendable = apply()

  def f:(() => play.twirl.api.HtmlFormat.Appendable) = () => apply()

  def ref: this.type = this

}


              /*
                  -- GENERATED --
                  SOURCE: app/views/index.scala.html
                  HASH: a33ed5f339621b8fa7ac9230b619eae94f1e53ba
                  MATRIX: 722->1|818->4|845->6|875->28|914->30|943->33|1206->267
                  LINES: 21->1|26->2|27->3|27->3|27->3|28->4|31->7
                  -- GENERATED --
              */
          