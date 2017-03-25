package actions

/**
  * Created by xiaoyzhang on 3/21/2017.
  */
class ScriptAction(actionType: String,target: String, seq: Integer, command:String)
  extends Action(actionType, target, seq){

  override def getCommand(user: String, host: String): String = this.command

  override def isScp: Boolean = false
}