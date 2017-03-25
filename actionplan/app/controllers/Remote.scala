package controllers

import java.io.File
import play.api.Logger
import datamodel.UserInfo

class Remote(operationHost: String = null) {

  
  def localHost = operationHost
  
  def ssh(user: UserInfo, host : String, command: String) : String = {
    try{
      jassh.SSH.once(host, user.username, user.password) { ssh =>
        ssh.execute({
          var cmdBuilder = new StringBuilder()          
          .append("echo \'")
          .append(user.password)
          .append("\' | sudo -S ")
          .append(command).toString()
          
          Logger.info("ssh command :"+ cmdBuilder)
          cmdBuilder
        })
      }
    }catch{
      case e: Exception => {
        var errorMsg = new StringBuilder("Failed to ssh to host: ").append(host).append(" with username").append(user.username).toString()
        Logger.error(errorMsg)
        errorMsg
      }
    }
  }

  def sshAsUser(user: UserInfo, host : String, command: String) : String = {
    try{
      jassh.SSH.once(host, user.username, user.password) { ssh =>
        ssh.execute({
          Logger.info("ssh command :"+ command)
          command
        })
      }
    }catch{
      case e: Exception => {
        var errorMsg = new StringBuilder("Failed to ssh to host: ").append(host).append(" with username").append(user.username).toString()
        Logger.error(errorMsg)
        errorMsg
      }
    }
  }
  
  def sshLocal(user: UserInfo, command: String) : String = {
    ssh(user, localHost, command)
  }
  
  def downloadLocal(user: UserInfo, localLocation: String, remoteURL: String) : String = {
    sshLocal(user: UserInfo, {
      // Generate command line: wget -P /usr/local/ http://endpoint/elasticsearch-1.4.2.tar.gz & tar -zxvf /usr/local/elasticsearch-1.4.2.tar.gz
      var cmdBuffer = new StringBuilder
      cmdBuffer
      .append("wget -P ")
      .append(localLocation)
      .append(" ")
      .append(remoteURL)
      .toString()
    })
  }
  
  def scpLocalFile(user: UserInfo, localFile: File, remoteDestination: String, targetHost : String): String = {
    try{
      jassh.SSH.once(targetHost, user.username, user.password) { scp =>
        scp.send(localFile, remoteDestination)
      }
      "scp " + localFile + " " + user.username + "@" + targetHost + remoteDestination
    }catch{
      case e: Exception => {
        var errorMsg = new StringBuilder("Failed to scp file: ")
        .append(localFile.getName)
        .append(" to host: ")
        .append(targetHost)
        .append(" with username: ")
        .append(user.username)
        .append(" ")
        .append(e)
        .toString()
        Logger.error(errorMsg)
        errorMsg
      }
    }
  }
  
  def scpString(user: UserInfo, content: String, remoteDestination: String, targetHost : String) = {
    try{
      jassh.SSH.once(targetHost, user.username, user.password) { scp =>
        scp.put(content, remoteDestination)
      }
    }catch{
      case e: Exception => {
        var errorMsg = new StringBuilder("Failed to scp string to host ")
        .append(targetHost)
        .append(" with username: ")
        .append(user.username)
        .toString()
        Logger.error(errorMsg)
        errorMsg
      }
    }
  }
}