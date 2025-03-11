CMD    = java
CC     = javac
NAME   = Deadwood
BIN    = ./bin
FILES  = ./src/*.java
CONFIG = deadwood.config

build:
	$(CC) -d $(BIN) $(FILES)

run-gui: build
	echo "GUI=1" > $(CONFIG)
	$(CMD) -classpath $(BIN) $(NAME)

run-tui: build
	echo "GUI=0" > $(CONFIG)
	$(CMD) -classpath $(BIN) $(NAME)

clean:
	rm -f $(BIN)/*.class $(CONFIG)
