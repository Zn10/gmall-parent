#!/bin/bash


elkIP="192.168.200.128"
esPath="/rsdata/program/es"
esVersion="7.13.4"
logstashPath="/rsdata/program/logstash"
logstashVersion="7.13.4"
kinabaVersion="7.13.4"
esXms="512m"
esXmx="512m"
logstashXms="512m"
logstashXmx="512m"



elk_ENV(){
	mkdir -p $esPath/data
	mkdir -p $esPath/plugins
	mkdir -p $esPath/logs
	mkdir -p $logstashPath/logs
	mkdir -p $logstashPath/pipeline
	mkdir -p $logstashPath/config
	chown 1000:1000 -R $esPath
	chown 1000:1000 -R $logstashPath
	
	firewall-cmd --permanent --zone=public --add-port=5044/tcp
	firewall-cmd --permanent --zone=public --add-port=9600/tcp
	firewall-cmd --permanent --zone=public --add-port=5601/tcp
	firewall-cmd --permanent --zone=public --add-port=9300/tcp
	firewall-cmd --permanent --zone=public --add-port=9200/tcp
	firewall-cmd --reload
	
	if [ `grep "vm.max_map_count" /etc/sysctl.conf | wc -l` -eq 0 ];then
		echo "vm.max_map_count=262144" >> /etc/sysctl.conf
		echo "DefaultLimitNOFILE=65536" >> /etc/systemd/system.conf
		echo "DefaultLimitNPROC=32000" >> /etc/systemd/system.conf
		echo "DefaultLimitMEMLOCK=infinity" >> /etc/systemd/system.conf
		reboot
	fi
}

es_install(){
	if [ `docker ps -f name=elasticsearch | grep elasticsearch | wc -l` -eq 0 ];then
		docker run -d \
		--name elasticsearch \
		--restart=always \
		-p 9200:9200 \
		-p 9300:9300 \
		-e "discovery.type=single-node" \
		-e TZ=Asia/Shanghai \
		-e ES_JAVA_OPTS="-Xms$esXms -Xmx$esXmx" \
		-v $esPath/data:/usr/share/elasticsearch/data \
		-v $esPath/plugins:/usr/share/elasticsearch/plugins \
		-v $esPath/logs:/usr/share/elasticsearch/logs \
		elasticsearch:$esVersion
	fi
}

logstash_config(){
	
	cat > $logstashPath/config/jvm.options<<EOF
## JVM configuration

# Xms represents the initial size of total heap space
# Xmx represents the maximum size of total heap space

-Xms$logstashXms
-Xmx$logstashXmx

################################################################
## Expert settings
################################################################
##
## All settings below this section are considered
## expert settings. Don't tamper with them unless
## you understand what you are doing
##
################################################################

## GC configuration
8-13:-XX:+UseConcMarkSweepGC
8-13:-XX:CMSInitiatingOccupancyFraction=75
8-13:-XX:+UseCMSInitiatingOccupancyOnly

## Locale
# Set the locale language
#-Duser.language=en

# Set the locale country
#-Duser.country=US

# Set the locale variant, if any
#-Duser.variant=

## basic

# set the I/O temp directory
#-Djava.io.tmpdir=$HOME

# set to headless, just in case
-Djava.awt.headless=true

# ensure UTF-8 encoding by default (e.g. filenames)
-Dfile.encoding=UTF-8

# use our provided JNA always versus the system one
#-Djna.nosys=true

# Turn on JRuby invokedynamic
-Djruby.compile.invokedynamic=true
# Force Compilation
-Djruby.jit.threshold=0
# Make sure joni regexp interruptability is enabled
-Djruby.regexp.interruptible=true

## heap dumps

# generate a heap dump when an allocation from the Java heap fails
# heap dumps are created in the working directory of the JVM
-XX:+HeapDumpOnOutOfMemoryError

# specify an alternative path for heap dumps
# ensure the directory exists and has sufficient space
#-XX:HeapDumpPath=${LOGSTASH_HOME}/heapdump.hprof

## GC logging
#-XX:+PrintGCDetails
#-XX:+PrintGCTimeStamps
#-XX:+PrintGCDateStamps
#-XX:+PrintClassHistogram
#-XX:+PrintTenuringDistribution
#-XX:+PrintGCApplicationStoppedTime

# log GC status to a file with time stamps
# ensure the directory exists
#-Xloggc:${LS_GC_LOG_FILE}

# Entropy source for randomness
-Djava.security.egd=file:/dev/urandom

# Copy the logging context from parent threads to children
-Dlog4j2.isThreadContextMapInheritable=true
EOF

	cat > $logstashPath/config/log4j2.properties<<EOF
status = error
name = LogstashPropertiesConfig

appender.console.type = Console
appender.console.name = plain_console
appender.console.layout.type = PatternLayout
appender.console.layout.pattern = [%d{ISO8601}][%-5p][%-25c]%notEmpty{[%X{pipeline.id}]}%notEmpty{[%X{plugin.id}]} %m%n

appender.json_console.type = Console
appender.json_console.name = json_console
appender.json_console.layout.type = JSONLayout
appender.json_console.layout.compact = true
appender.json_console.layout.eventEol = true

rootLogger.level = \${sys:ls.log.level}
rootLogger.appenderRef.console.ref = \${sys:ls.log.format}_console
EOF

	cat > $logstashPath/config/logstash.yml<<EOF
pipeline.workers: 6
#ipipeline.output.workers: 6
#每次发送的事件数
pipeline.batch.size: 5000
#发送延时
pipeline.batch.delay: 100
http.host: "0.0.0.0"
log.level: warn
path.logs: /var/log/logstash
xpack.monitoring.enabled: true
xpack.monitoring.elasticsearch.hosts: [ "http://$elkIP:9200" ]
xpack.monitoring.collection.interval: 10s
slowlog.threshold.warn: 2s
slowlog.threshold.info: 1s
slowlog.threshold.debug: 500ms
slowlog.threshold.trace: 100ms
#xpack.monitoring.elasticsearch.username: "elastic"
#xpack.monitoring.elasticsearch.password: "\$ES_PASSWORD"
EOF

	cat > $logstashPath/config/pipelines.yml<<EOF
- pipeline.id: main
  path.config: "/usr/share/logstash/pipeline"
  #queue.type: persisted
EOF

	cat > $logstashPath/config/startup.options<<EOF
################################################################################
# These settings are ONLY used by $LS_HOME/bin/system-install to create a custom
# startup script for Logstash and is not used by Logstash itself. It should
# automagically use the init system (systemd, upstart, sysv, etc.) that your
# Linux distribution uses.
#
# After changing anything here, you need to re-run $LS_HOME/bin/system-install
# as root to push the changes to the init script.
################################################################################

# Override Java location
#JAVACMD=/usr/bin/java

# Set a home directory
LS_HOME=/usr/share/logstash

# logstash settings directory, the path which contains logstash.yml
LS_SETTINGS_DIR=/etc/logstash

# Arguments to pass to logstash
LS_OPTS="--path.settings \${LS_SETTINGS_DIR}"

# Arguments to pass to java
LS_JAVA_OPTS=""

# pidfiles aren't used the same way for upstart and systemd; this is for sysv users.
LS_PIDFILE=/var/run/logstash.pid

# user and group id to be invoked as
LS_USER=logstash
LS_GROUP=logstash

# Enable GC logging by uncommenting the appropriate lines in the GC logging
# section in jvm.options
LS_GC_LOG_FILE=/var/log/logstash/gc.log

# Open file limit
LS_OPEN_FILES=16384

# Nice level
LS_NICE=19

# Change these to have the init script named and described differently
# This is useful when running multiple instances of Logstash on the same
# physical box or vm
SERVICE_NAME="logstash"
SERVICE_DESCRIPTION="logstash"
EOF

	cat > $logstashPath/pipeline/logstash.conf<<EOF
input {
  tcp {
    port => 5044
    codec => json_lines
  }
}
 
output {
  elasticsearch {
    hosts => ["http://$elkIP:9200"]
    index => "%{[servicename]}-%{+YYYY.MM.dd}"
    #user => "elastic"
    #password => "\$ES_PASSWORD"
  }
  stdout{
    codec => json_lines
  }
}

EOF

}


logstash_install(){
	if [ `docker ps -f name=logstash | grep logstash | wc -l` -eq 0 ];then
		docker run -d \
		--name logstash \
		--restart=always \
		-p 5044:5044 \
		-p 5045:5045 \
		-v $logstashPath/config:/usr/share/logstash/config \
		-v $logstashPath/pipeline:/usr/share/logstash/pipeline \
		-v $logstashPath/logs:/var/log/logstash \
		-e TZ=Asia/Shanghai \
		-m 512m \
		logstash:$logstashVersion
	fi
}

kinaba_install(){
	if [ `docker ps -f name=kibana | grep kibana | wc -l` -eq 0 ];then
		docker run -d \
		--name kibana \
		--restart=always \
		-p 5601:5601 \
		-e SERVER_NAME=kibana \
		-e ELASTICSEARCH_HOSTS="http://$elkIP:9200" \
		-e I18N_LOCALE=zh-CN \
		-e TZ=Asia/Shanghai \
		-m 512m \
		kibana:$kinabaVersion
	fi
}



main(){
	elk_ENV
	es_install
	logstash_config
	logstash_install
	kinaba_install
}


main
