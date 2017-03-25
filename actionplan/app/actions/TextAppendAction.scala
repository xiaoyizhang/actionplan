package actions

/**
  * Created by xiaoyzhang on 3/21/2017.
  */
class TextAppendAction(actionType: String, target: String, seq: Integer, remoteFile:String, newcontent:String)
  extends Action(actionType, target, seq){

  override def getCommand(user: String, host: String): String = "sed -ie '$a" + newcontent + "' " + remoteFile

  override def isScp: Boolean = false
}
