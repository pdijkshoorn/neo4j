/**
 * Copyright (c) 2002-2015 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.cypher.internal.compiler.v2_2.planner.logical

import org.neo4j.cypher.internal.compiler.v2_2.InternalException
import org.neo4j.cypher.internal.compiler.v2_2.ast.{RelationshipChain, NodePattern, PatternExpression}
import org.neo4j.cypher.internal.compiler.v2_2.planner.logical.greedy.GreedyPlanTable
import org.neo4j.cypher.internal.compiler.v2_2.planner.logical.plans.{IdName, LogicalPlan}
import org.neo4j.cypher.internal.compiler.v2_2.planner.logical.steps.LogicalPlanProducer._
import org.neo4j.cypher.internal.compiler.v2_2.planner.{QueryGraph, UnionQuery}

trait QueryGraphSolver {
  def plan(queryGraph: QueryGraph)(implicit context: LogicalPlanningContext, leafPlan: Option[LogicalPlan] = None): LogicalPlan
  def planPatternExpression(planArguments: Set[IdName], expr: PatternExpression)(implicit context: LogicalPlanningContext): (LogicalPlan, PatternExpression)
}

trait PatternExpressionSolving {

  self: QueryGraphSolver =>

  import org.neo4j.cypher.internal.compiler.v2_2.ast.convert.plannerQuery.ExpressionConverters._

  def planPatternExpression(planArguments: Set[IdName], expr: PatternExpression)(implicit context: LogicalPlanningContext): (LogicalPlan, PatternExpression) = {
    val dependencies = expr.dependencies.map(IdName.fromIdentifier)
    val qgArguments = planArguments intersect dependencies
    val (namedExpr, namedMap) = PatternExpressionPatternElementNamer(expr)
    val qg = namedExpr.asQueryGraph.withArgumentIds(qgArguments)

    val argLeafPlan = Some(planQueryArgumentRow(qg))
    val namedNodes = namedMap.collect { case (elem: NodePattern, identifier) => identifier}
    val namedRels = namedMap.collect { case (elem: RelationshipChain, identifier) => identifier}
    val patternPlanningContext = context.forExpressionPlanning(namedNodes, namedRels)
    val plan = self.plan(qg)(patternPlanningContext, argLeafPlan)
    (plan, namedExpr)
  }
}

trait TentativeQueryGraphSolver extends QueryGraphSolver with PatternExpressionSolving {
  def config: QueryPlannerConfiguration
  def tryPlan(queryGraph: QueryGraph)(implicit context: LogicalPlanningContext, leafPlan: Option[LogicalPlan] = None): Option[LogicalPlan]
  def plan(queryGraph: QueryGraph)(implicit context: LogicalPlanningContext, leafPlan: Option[LogicalPlan] = None): LogicalPlan =
    tryPlan(queryGraph).getOrElse(throw new InternalException("Failed to create a plan for the given QueryGraph " + queryGraph))
}
