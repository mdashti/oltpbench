java  -Dlog4j.configuration=./log4j.properties -Xmx1024m -cp `run/classpath.sh` com.oltpbenchmark.DBWorkload -b tatp -c config/sample_tatp_config.xml --execute true --clear true --load true
