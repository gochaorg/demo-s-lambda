# decompile
JAVAP=javap -v -constants -c -s -p

# decompile result
SAMPLE1_CLS=intro/target/test-classes/sample/Sample1.class
SAMPLE1_TXT=doc/sample-1/decompile.txt

COMPILE_FIES_DIR=doc/compiled_files
COMPILE_FIES=$(COMPILE_FIES_DIR)/list.txt

SAMPLE5_CLS=intro/target/test-classes/sample/Sample5.class
SAMPLE5_TXT=doc/sample-5/decompile.txt

SAMPLE6_CLS=intro/target/test-classes/sample/Sample6.class
SAMPLE6_TXT=doc/sample-6/decompile.txt

SAMPLE6_L1_TXT=doc/sample-6/decompile-L1.txt
SAMPLE6_L1_CLS='intro/target/generated-proxy/sample/Sample6$$$$Lambda$$1.class'
SAMPLE6_L2_TXT=doc/sample-6/decompile-L2.txt
SAMPLE6_L2_CLS='intro/target/generated-proxy/sample/Sample6$$$$Lambda$$2.class'

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

$(SAMPLE5_CLS): compile_samples

$(SAMPLE5_TXT): $(SAMPLE5_CLS)
	mkdir -p doc/sample-5
	$(JAVAP) $(SAMPLE5_CLS) > $(SAMPLE5_TXT)

$(SAMPLE6_CLS): compile_samples

$(SAMPLE6_TXT): $(SAMPLE6_CLS)
	mkdir -p doc/sample-6
	$(JAVAP) $(SAMPLE6_CLS) > $(SAMPLE6_TXT)

decompile: $(SAMPLE1_TXT) $(SAMPLE5_TXT) $(SAMPLE6_TXT) $(SAMPLE6_L1_TXT) $(SAMPLE6_L2_TXT)

$(COMPILE_FIES): compile_samples
	mkdir -p $(COMPILE_FIES_DIR)
	find intro/target/test-classes/sample > $(COMPILE_FIES)

doc_samples: $(COMPILE_FIES) decompile $(SAMPLE1_TXT)

gen_proxy: compile_samples
	mkdir -p intro/target/generated-proxy
	java -Djdk.internal.lambda.dumpProxyClasses=intro/target/generated-proxy -cp "intro/target/test-classes" sample.Sample6

$(SAMPLE6_L1_CLS): gen_proxy

$(SAMPLE6_L2_CLS): gen_proxy

$(SAMPLE6_L1_TXT): $(SAMPLE6_L1_CLS)
	$(JAVAP) $(SAMPLE6_L1_CLS) > $(SAMPLE6_L1_TXT)

$(SAMPLE6_L2_TXT): $(SAMPLE6_L2_CLS)
	$(JAVAP) $(SAMPLE6_L2_CLS) > $(SAMPLE6_L2_TXT)

