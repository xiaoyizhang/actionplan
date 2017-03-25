package actions

/**
  * Created by xiaoyzhang on 3/21/2017.
  */
class TextInsertAction(actionType: String, target: String, seq: Integer, remoteFile:String, regex:String, newcontent:String)
  extends Action(actionType, target, seq){
  override def getCommand(user: String, host: String): String = {
    val delimiter = {
      if(regex.contains(SLASH) || newcontent.contains(SLASH))
        COMMA
      else if(regex.contains(COMMA) || newcontent.contains(COMMA))
        SLASH
      else if(regex.contains(COMMA) && regex.contains(SLASH)
        || newcontent.contains(COMMA) && newcontent.contains(SLASH)
        || regex.contains(COMMA) && newcontent.contains(SLASH)
        || newcontent.contains(COMMA) && regex.contains(SLASH))
        PIPE
      else
        SLASH
    }
    "sed '" + delimiter + regex + delimiter +"a " + newcontent + "' " + remoteFile
  }

  override def isScp: Boolean = false
}
