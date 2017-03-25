package actions

import java.io.File

/**
  * Created by xiaoyzhang on 3/21/2017.
  */
trait IAction {

  def getCommand(user:String, host:String): String

  def getTarget: String

  def getSeq: Integer

  def isScp: Boolean
}
