MinTimeCompactionStrategy (MTCS)
================================

**EXPERIMENTAL** compaction strategy that extends DTCS, but switches the
internal filter to use min timestamp instead of max.

Building the JAR: `mvn compile && mvn package`

Drop it in your server's CLASSPATH (for DSE, `/usr/share/dse/cassandra/lib`).
