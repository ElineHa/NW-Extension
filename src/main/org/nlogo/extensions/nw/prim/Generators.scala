// (C) Uri Wilensky. https://github.com/NetLogo/NW-Extension

package org.nlogo.extensions.nw.prim

import org.nlogo.api
import org.nlogo.api.ExtensionException
import org.nlogo.agent.World
import org.nlogo.core.Syntax._
import org.nlogo.extensions.nw.NetworkExtensionUtil.AgentSetToRichAgentSet
import org.nlogo.extensions.nw.NetworkExtensionUtil.TurtleCreatingCommand
import org.nlogo.extensions.nw.algorithms

class ErdosRenyiGenerator extends TurtleCreatingCommand {
  override def getSyntax = commandSyntax(
    List(TurtlesetType, LinksetType, NumberType, NumberType, CommandBlockType | OptionalType), blockAgentClassString = Some("-T--"))
  def createTurtles(args: Array[api.Argument], context: api.Context) = {
    implicit val world = context.world.asInstanceOf[World]
    val turtleBreed = args(0).getAgentSet.requireTurtleBreed
    val linkBreed = args(1).getAgentSet.requireLinkBreed
    val nbTurtles = getIntValueWithMinimum(args(2), 1)
    val connexionProbability = args(3).getDoubleValue
    if (!(connexionProbability >= 0 && connexionProbability <= 1.0))
      throw new ExtensionException("The connexion probability must be between 0 and 1.")
    algorithms.ErdosRenyiGenerator.generate(world, turtleBreed, linkBreed, nbTurtles, connexionProbability, context.getRNG)
  }
}

class WattsStrogatzGenerator extends TurtleCreatingCommand {
  override def getSyntax = commandSyntax(
    List(TurtlesetType, LinksetType, NumberType, NumberType, NumberType, CommandBlockType | OptionalType))
  def createTurtles(args: Array[api.Argument], context: api.Context) = {
    implicit val world = context.world.asInstanceOf[World]
    val turtleBreed = args(0).getAgentSet.requireTurtleBreed
    val linkBreed = args(1).getAgentSet.requireLinkBreed
    val nbTurtles = getIntValueWithMinimum(args(2), 1)
    val neighborhoodSize = args(3).getIntValue
    if (neighborhoodSize < 0 || neighborhoodSize > (nbTurtles / 2.0 - 1.0).ceil)
      throw new ExtensionException("Neighborhood size must be less than half the number of turtles.")
    val rewireProbability = args(4).getDoubleValue
    if (!(rewireProbability >= 0 && rewireProbability <= 1.0))
      throw new ExtensionException("The rewire probability must be between 0 and 1.")
    algorithms.WattsStrogatzGenerator.generate(world, turtleBreed, linkBreed, nbTurtles, neighborhoodSize, rewireProbability, context.getRNG)
  }
}
