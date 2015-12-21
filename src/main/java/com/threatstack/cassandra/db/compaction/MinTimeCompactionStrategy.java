/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.threatstack.cassandra.db.compaction;

import java.util.*;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Predicate;
import com.google.common.collect.*;

import org.apache.cassandra.db.ColumnFamilyStore;
import org.apache.cassandra.db.compaction.*;
import org.apache.cassandra.exceptions.ConfigurationException;
import org.apache.cassandra.io.sstable.SSTableReader;
import org.apache.cassandra.utils.Pair;

public class MinTimeCompactionStrategy extends org.apache.cassandra.db.compaction.DateTieredCompactionStrategy
{
    public MinTimeCompactionStrategy(ColumnFamilyStore cfs, Map<String, String> options)
    {
        super(cfs, options);
    }

   /**
    * Removes all sstables with min timestamp older than maxSSTableAge.
    * @param sstables all sstables to consider
    * @param maxSSTableAge the age in milliseconds when an SSTable stops participating in compactions
    * @param now current time. SSTables with min timestamp less now (now - maxSSTableAge) are filtered.
    * @return a list of sstables with the oldest sstables excluded
    */
    @VisibleForTesting
    static Iterable<SSTableReader> filterOldSSTables(List<SSTableReader> sstables, long maxSSTableAge, long now)
    {
        if (maxSSTableAge == 0)
            return sstables;
        final long cutoff = now - maxSSTableAge;
        return Iterables.filter(sstables, new Predicate<SSTableReader>()
        {
            @Override
            public boolean apply(SSTableReader sstable)
            {
                return sstable.getMinTimestamp() >= cutoff;
            }
        });
    }

    public String toString()
    {
        return String.format("MinTimeCompactionStrategy[%s/%s]",
                cfs.getMinimumCompactionThreshold(),
                cfs.getMaximumCompactionThreshold());
    }
}
