java -Dlog4j.configuration=./log4j.properties -Xmx1024m -cp `run/classpath.sh` com.oltpbenchmark.DBWorkload -b tpcc -c config/sample_tpcc_config.xml  --execute true

