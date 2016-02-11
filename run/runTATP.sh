#This script loads and runs TATP benchmark

#change dir to directory of script
SCRIPT_DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )

#script params
SCALE=`python -c "from lxml.etree import parse; from sys import stdin; print parse(stdin).xpath('//scalefactor')[0].text" < $SCRIPT_DIR/../config/sample_tatp_config.xml`
DBUSER=`python -c "from lxml.etree import parse; from sys import stdin; print parse(stdin).xpath('//username')[0].text" < $SCRIPT_DIR/../config/sample_tatp_config.xml`
PASS=`python -c "from lxml.etree import parse; from sys import stdin; print parse(stdin).xpath('//password')[0].text" < $SCRIPT_DIR/../config/sample_tatp_config.xml`
DB=tatp

eval "mkdir -p $SCRIPT_DIR/mysql"
eval "cd $SCRIPT_DIR/mysql"

#drop and recreate the database
mysql -u$DBUSER -p$PASS -e "drop database if exists $DB;"
mysql -u$DBUSER -p$PASS -e "create database $DB;"

if [ ! -f "$DB-s$SCALE-mysql.sql.bz2" ]; then 
	echo "Creating the database for ScaleFactor=$SCALE using oltpbench script"

    eval "cd $SCRIPT_DIR/.."

    java  -Dlog4j.configuration=./log4j.properties -Xmx1024m -cp `run/classpath.sh` com.oltpbenchmark.DBWorkload -b tatp -c config/sample_tatp_config.xml --create true --load true
    
    eval "cd $SCRIPT_DIR/mysql"
    
    mysqldump -u$DBUSER -p$PASS $DB | bzip2 > $DB-s$SCALE-mysql.sql.bz2

    #creating text backup from input
    resdir="$SCRIPT_DIR/mysql/$DB-s$SCALE-mysql"
	eval "rm -rf $resdir"
	eval "mkdir -p $resdir"
	eval "chmod -R 777 $resdir"
	mysqldump --fields-optionally-enclosed-by='"' --fields-terminated-by=',' --tab $resdir -u$DBUSER -p$PASS $DB
else
	echo "Reusing the existing database for ScaleFactor=$SCALE"

	bzcat $DB-s$SCALE-mysql.sql.bz2 | mysql -u$DBUSER -p$PASS $DB;
fi
echo "Finished loading the database"

eval "cd $SCRIPT_DIR/.."

java  -Dlog4j.configuration=./log4j.properties -Xmx1024m -cp `run/classpath.sh` com.oltpbenchmark.DBWorkload -b tatp -c config/sample_tatp_config.xml --execute true

#creating text backup from output
resdir="$SCRIPT_DIR/mysql/results-$DB-s$SCALE-mysql"
eval "rm -rf $resdir"
eval "mkdir -p $resdir"
eval "chmod -R 777 $resdir"
echo "Saving the state of the result database (into $resdir)..."
mysqldump --fields-optionally-enclosed-by='"' --fields-terminated-by=',' --tab $resdir -u$DBUSER -p$PASS $DB