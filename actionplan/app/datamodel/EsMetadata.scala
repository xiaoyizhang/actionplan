package datamodel

case class EsMetadata (currentNode: String,
                       esVersion : String,
                       tarFileName: String,
                       plugins : Seq[String],
                       masterNodes: Set[String],
                       allNodes: Set[String],
                       defaultYaml: String
                      )