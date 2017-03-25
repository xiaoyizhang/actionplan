package actions

/**
  * Created by xiaoyzhang on 3/21/2017.
  */
class ScpAction(actionType: String, target: String, seq: Integer, fromLocal:String, toRemote:String)
  extends Action(actionType,target, seq){

  override def getCommand(user:String, host:String): String = "scp " + fromLocal + " " + user + "@" + host + ":" + toRemote

  override def isScp: Boolean = true

  def getFromLocal: String = fromLocal

  def getToRemote: String = toRemote
}
