/**
 * Copyright (c) 2002-2014 "Neo Technology,"
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
package org.neo4j.kernel.logging;

/**
 * List of standard Logger names
 */
public interface Loggers
{
    public static final String NEO4J = "neo4j";
    public static final String CONFIG = "neo4j.config";
    public static final String DATASOURCE = "neo4j.datasource";
    public static final String DIAGNOSTICS = "neo4j.diagnostics";
    public static final String TXMANAGER = "neo4j.txmanager";
    public static final String XAFACTORY = "neo4j.xafactory";
    public static final String NEOSTORE = "neo4j.neostore";
    public static final String EXTENSION = "neo4j.extension";
    public static final String INDEX = "neo4j.index";
    public static final String CYPHER = "neo4j.cypher";
}