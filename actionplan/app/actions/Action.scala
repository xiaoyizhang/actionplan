package actions

/**
  * Created by xiaoyzhang on 3/17/2017.
  */
abstract class Action(actionType: String,target: String,seq: Integer) extends IAction {


  val COMMA = ","
  val SLASH = "/"
  val PIPE = "|"

  def getCommand(user:String, host:String): String

  def getTarget: String = target

  def getSeq: Integer = seq

  def isScp: Boolean

}
/*
case class ScriptAction(actionType: String, target: String, command:String) extends Action

case class ScpAction(actionType: String, target: String, from:String, to:String) extends Action

case class TextReplaceAction(actionType: String, target: String, remoteFile:String, find:String, newcontent:String) extends Action

case class TextInsertAction(actionType: String, target: String, remoteFile:String, targetLine:String, newcontent:String) extends Action

case class TextAppendAction(actionType: String, target: String, remoteFile:String, newcontent:String) extends Action
*/