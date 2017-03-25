package controllers

import java.io._

import actions.{ScpAction, _}
import commons._
import datamodel.NodeType._
import datamodel.UserInfo
import play.api._
import play.api.libs.json.{JsArray, JsValue, Json}
import play.api.mvc._

import scala.collection.mutable.ListBuffer
import scala.io.Source
import scala.util.{Properties, Random}

object Execution extends Controller {
  
  def index = Action {
    Ok("ok")
  }
  
  def executeAll = Action(parse.urlFormEncoded) { request =>

    val plan = request.body.get(Conf.FIELD_PLAN).head.head
    val user = UserInfo(request.body.get(Conf.USER_NAME).head.head, request.body.get(Conf.USER_PASSWORD).head.head)

    val json: JsValue = Json.parse(scala.io.Source.fromFile(
      Play.current.getFile(Conf.PLAN_PATH + plan)
    ).mkString)

    val targets = (json \ "targets").as[Map[String, Seq[String]]]
    val operationHost = (json \ "operationHost").as[String]
    val remote = new Remote(operationHost)

    val buf = new ListBuffer[String]

    (json \ "actions")
      .as[Map[String, String]]
      .map { case (key, value) => (key, getPlainAction(key, value)) }
      .values
      .toSeq
      .sortBy(_.getSeq)
      .foreach(action => {
        targets(action.getTarget)
          .par
          .foreach(
            host => {
              if(action.isScp) {
                val scpAction = action.asInstanceOf[ScpAction]
                buf += remote.scpLocalFile(user, new File(scpAction.getFromLocal), scpAction.getToRemote, host)
              } else {
                buf += remote.ssh(user, host, action.getCommand(user.username, host))
              }
            }
          )
      })

    Ok(Json.obj("results" -> buf.toSeq).toString)
  }

  def execute = Action(parse.urlFormEncoded) { request =>

    val plan = request.body.get(Conf.FIELD_PLAN).head.head
    val user = UserInfo(request.body.get(Conf.USER_NAME).head.head, request.body.get(Conf.USER_PASSWORD).head.head)
    val seq = request.body.get(Conf.FIELD_SEQ).head.head

    val json: JsValue = Json.parse(scala.io.Source.fromFile(
      Play.current.getFile(Conf.PLAN_PATH + plan)
    ).mkString)

    val targets = (json \ "targets").as[Map[String, Seq[String]]]
    val operationHost = (json \ "operationHost").as[String]
    val remote = new Remote(operationHost)

    val buf = new ListBuffer[String]

    val action = getPlainAction(seq, (json \ "actions" \ seq).as[String])
    targets(action.getTarget)
      .par
      .foreach(
        host => {
          if(action.isScp) {
            val scpAction = action.asInstanceOf[ScpAction]
            buf += remote.scpLocalFile(user, new File(scpAction.getFromLocal), scpAction.getToRemote, host)
          } else {
            buf += remote.ssh(user, host, action.getCommand(user.username, host))
          }
        }
      )

    Ok(Json.obj("results" -> buf.toSeq).toString)
  }

  def getPlainAction(seq: String, action: String): IAction = {
    val json = Json.parse(action)
    val actionType = (json \ "actionType").as[String]
    val target = (json \ "target").as[String]
    actionType match {
      case "script" => new ScriptAction(actionType,
        target,
        seq.toInt,
        (json \ "command").as[String])
      case "scp" => new ScpAction(actionType,
        target,
        seq.toInt,
        (json \ "fromLocal").as[String],
        (json \ "toRemote").as[String])
      case "text.replace" => new TextReplaceAction(actionType,
        target,
        seq.toInt,
        (json \ "remoteFile").as[String],
        (json \ "find").as[String],
        (json \ "newcontent").as[String])
      case "text.insert" => new TextInsertAction(actionType,
        target,
        seq.toInt,
        (json \ "remoteFile").as[String],
        (json \ "regex").as[String],
        (json \ "newcontent").as[String])
      case "text.append" => new TextAppendAction(actionType,
        target,
        seq.toInt,
        (json \ "remoteFile").as[String],
        (json \ "newcontent").as[String])
    }
  }

}