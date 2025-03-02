CC     = javac
CFLAGS =
FILES  = *.java

build:
	$(CC) $(FILES) $(CFLAGS)

run: build
	java Deadwood

clean:
	rm -rf *.class
