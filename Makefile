# decompile
JAVAP=javap -v -constants -c -s -p

# decompile result
SAMPLE1_CLS=intro/target/test-classes/sample/Sample1.class
SAMPLE1_TXT=doc/sample-1/decompile.txt

all: check_requirements compile_samples
	echo 1

check_requirements: check_java check_mvn

check_java:
	[ $$(which java) ] || ( echo "java not found" ; open "https://google.com/search?q=jdk+11+download"; exit 1 )
	[ $$(which javap) ] || ( echo "javap not found" ; open "https://google.com/search?q=jdk+11+download"; exit 1 )

check_mvn:
	[ $$(which mvn) ] || ( echo "maven (mvn) not found" ; open "https://maven.apache.org/download.cgi"; exit 1 )

compile_samples: check_java check_mvn
	mvn package

$(SAMPLE1_CLS): compile_samples

$(SAMPLE1_TXT): check_java
	$(JAVAP) $(SAMPLE1_CLS) > $(SAMPLE1_TXT)

decompile: $(SAMPLE1_TXT)