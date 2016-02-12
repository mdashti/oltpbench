/******************************************************************************
 *  Copyright 2015 by OLTPBenchmark Project                                   *
 *                                                                            *
 *  Licensed under the Apache License, Version 2.0 (the "License");           *
 *  you may not use this file except in compliance with the License.          *
 *  You may obtain a copy of the License at                                   *
 *                                                                            *
 *    http://www.apache.org/licenses/LICENSE-2.0                              *
 *                                                                            *
 *  Unless required by applicable law or agreed to in writing, software       *
 *  distributed under the License is distributed on an "AS IS" BASIS,         *
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  *
 *  See the License for the specific language governing permissions and       *
 *  limitations under the License.                                            *
 ******************************************************************************/


package com.oltpbenchmark.benchmarks.tatp.procedures;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.oltpbenchmark.api.Procedure;
import com.oltpbenchmark.api.SQLStmt;
import com.oltpbenchmark.benchmarks.tatp.TATPConstants;
import org.apache.log4j.Logger;

public class GetAccessData extends Procedure {
    private static final Logger LOG = Logger.getLogger(DeleteCallForwarding.class);

     public final SQLStmt getAccessInfo = new SQLStmt(
         "SELECT data1, data2, data3, data4 FROM " + TATPConstants.TABLENAME_ACCESS_INFO + 
         " WHERE s_id = ? AND ai_type = ?"
     );

     public void run(Connection conn, long s_id, byte ai_type) throws SQLException {
        if (LOG.isTraceEnabled()) LOG.trace(String.format("GetAccessData %d %d",s_id,ai_type));
    	 PreparedStatement stmt = this.getPreparedStatement(conn, getAccessInfo);
    	 stmt.setLong(1, s_id);
    	 stmt.setByte(2, ai_type);
    	 ResultSet results = stmt.executeQuery();
    	 assert(results != null);
    	 results.close();
     }
}
