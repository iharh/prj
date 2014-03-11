// http://www.scala-sbt.org/release/docs/Extending/Build-State.html
// http://www.scala-sbt.org/release/docs/Detailed-Topics/Tasks
// http://stackoverflow.com/questions/11665597/where-are-all-settingt-stored-in-sbt-0-11

// http://jackviers.blogspot.com/2012/01/sbt-scalas-simple-build-tool-series_16.html

//sealed trait Settings[Scope]
//{
//	def data: Map[Scope, AttributeMap]
//	def keys(scope: Scope): Set[AttributeKey[_]]
//	def scopes: Set[Scope]
//	def definingScope(scope: Scope, key: AttributeKey[_]): Option[Scope]
//	def allKeys[T](f: (Scope, AttributeKey[_]) => T): Seq[T]
//	def get[T](scope: Scope, key: AttributeKey[T]): Option[T]
//	def getDirect[T](scope: Scope, key: AttributeKey[T]): Option[T]
//	def set[T](scope: Scope, key: AttributeKey[T], value: T): Settings[Scope]
//}

    // structure.data - BuildStructure
    // val data: Settings[Scope]
    //println(extracted.getOpt(dstPath))
    //val extracted: Extracted = Project.extract(state.value)
    //println(extracted.structure.data.get(dstPath.scope.copy(project = Select(extracted.currentRef)), dstPath.key))

