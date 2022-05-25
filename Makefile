all: check_requirements compile_samples
	echo 1

check_requirements: check_java check_mvn

check_java:
	[ $$(which java) ] || ( echo "java not found" ; open "https://google.com/search?q=jdk+11+download"; exit 1 )

check_mvn:
	[ $$(which mvn) ] || ( echo "maven (mvn) not found" ; open "https://maven.apache.org/download.cgi"; exit 1 )

compile_samples:
	mvn package

