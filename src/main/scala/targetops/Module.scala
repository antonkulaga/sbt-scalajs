package com.inthenow.sbt.scalajs


import sbt._
import sbt.Keys._

class ModuleOps(target:Target, projectOps: ProjectOps) extends TargetOps(target, projectOps) {

  override def mkProject(b:BuildOps, params:ProjectParams, options: ProjectOptions): Project = {
    val modName: Seq[Setting[_]] = b match {
      case s:RootBuild => Seq(sbt.Keys.name := { s"${b.moduleOps.getModuleName()}_root" })
      case _           =>  projectOps.moduleNameSettings
    }
    val p= projectOps.crossModuleParams(this,SbtScalajs.noRootSettings ++ modName, params.projects)
    val o = projectOps.crossModuleOptions.copy(addProjects = true)
    super.mkProject(b,p,o)

  }
}

case object Module extends TargetType { //with JsTargetOps {

  def apply(id: String = "Module", name: String = "module", settings: Seq[Def.Setting[_]] = Seq()): Target =
    Target(Module, id, name, settings)

  def targetOps(target:Target,projectOps: ProjectOps): TargetOps = {
    new ModuleOps(target,  projectOps)
  }
}

