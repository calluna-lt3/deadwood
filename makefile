CC     = javac
CFLAGS =
FILES  = Main.java

build:
	$(CC) $(FILES) $(CFLAGS)

run: build
	java Main

clean:
	rm -rf *.class
