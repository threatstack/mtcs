MinTimeCompactionStrategy (MTCS)
================================

**EXPERIMENTAL** compaction strategy that extends DTCS, but switches the
internal filter to use min timestamp instead of max.

Example: a TTL of 30 days and max\_sstable\_age\_days of 1 will result in
individual SSTables only storing roughly 1 day worth of data. Depending on how
everything aligns you may have some slosh over from the previous day. An actual
calendar day is likely to be spread over a few SSTables.

Why?
----

https://issues.apache.org/jira/browse/CASSANDRA-11060

Using it
--------

Building the JAR: `mvn compile && mvn package`

Drop it in your server's CLASSPATH (for DSE, `/usr/share/dse/cassandra/lib`).

Either create a new table or ALTER an existing one to use MTCS. All compaction
configuration settings are the same as DTCS (remember, we just extend DTCS).

```
WITH compaction = {'class': 'com.threatstack.cassandra.db.compaction.MinTimeCompactionStrategy'}
```
