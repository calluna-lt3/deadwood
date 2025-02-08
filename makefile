CC     = javac
CFLAGS =
FILES  = Main.java Board.java InertRoom.java Moderator.java Player.java Role.java Room.java SceneCard.java SoundStage.java UI.java

build:
	$(CC) $(FILES) $(CFLAGS)

run: build
	java Main

clean:
	rm -rf *.class
