package datamodel

object NodeType extends Enumeration {
    type NodeType = Value
    val Master, Data, SearchLoadBalancer = Value
}