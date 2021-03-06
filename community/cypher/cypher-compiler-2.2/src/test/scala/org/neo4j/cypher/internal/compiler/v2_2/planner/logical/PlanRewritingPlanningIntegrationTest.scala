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

import org.neo4j.cypher.internal.commons.CypherFunSuite
import org.neo4j.cypher.internal.compiler.v2_2.ast.GetDegree
import org.neo4j.cypher.internal.compiler.v2_2.planner.logical.plans.{AllNodesScan, Projection}
import org.neo4j.cypher.internal.compiler.v2_2.planner.{LogicalPlanningTestSupport2, PlannerQuery}
import org.neo4j.graphdb.Direction.OUTGOING

class PlanRewritingPlanningIntegrationTest  extends CypherFunSuite with LogicalPlanningTestSupport2 {

  test("should use GetDegree to compute the degree of a node") {
    val result = (new given {
      cardinality = mapCardinality {
        case _: AllNodesScan => 1
        case _               => Double.MaxValue
      }
    } planFor "MATCH (n) RETURN length((n)-->()) AS deg").plan

    result should equal(
      Projection(
        AllNodesScan("n", Set.empty)(PlannerQuery.empty),
        Map("deg" -> GetDegree(ident("n"), None, OUTGOING)_)
      )(result.solved)
    )
  }
}
