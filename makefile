CMD    = java
CC     = javac
NAME   = Deadwood
BIN    = ./bin
FILES  = ./src/*.java
CONFIG = deadwood.config

MFLAGS = --module-path ./include/ --add-modules
MODS   = javafx.controls

null:
	echo "read the makefile!!" &>/dev/null

build-gui:
	echo "GUI=1" > $(CONFIG)
	$(CC) $(FILES) -d $(BIN) $(MFLAGS) $(MODS)

run-gui: build-gui
	$(CMD) -classpath $(BIN) $(NAME) $(MFLAGS) $(MODS)


build-tui:
	echo "GUI=0" > $(CONFIG)
	$(CC) $(FILES) -d $(BIN)

run-tui: build-tui
	$(CMD) -classpath $(BIN) $(NAME)

clean:
	rm -f $(BIN)/*.class
