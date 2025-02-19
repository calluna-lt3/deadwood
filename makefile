CC     = javac
CFLAGS =
FILES  = *.java

build:
	$(CC) $(FILES) $(CFLAGS)

run: build
	java Main

clean:
	rm -rf *.class
