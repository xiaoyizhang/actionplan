package actions

/**
  * Created by xiaoyzhang on 3/21/2017.
  */
class TextReplaceAction(actionType: String, target: String, seq: Integer, remoteFile:String, find:String, newcontent:String)
  extends Action(actionType, target, seq){

  override def getCommand(user: String, host: String): String = {
    val delimiter = {
      if(find.contains(SLASH) || newcontent.contains(SLASH))
        COMMA
      else if(find.contains(COMMA) || newcontent.contains(COMMA))
        SLASH
      else if(find.contains(COMMA) && find.contains(SLASH)
        || newcontent.contains(COMMA) && newcontent.contains(SLASH)
        || find.contains(COMMA) && newcontent.contains(SLASH)
        || newcontent.contains(COMMA) && find.contains(SLASH))
        PIPE
      else
        SLASH
    }
    "sed -ie 's" + delimiter + find + delimiter + newcontent + SLASH + "g' " + remoteFile
  }

  override def isScp: Boolean = false
}
