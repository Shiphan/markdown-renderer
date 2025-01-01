app:
	javac -cp src -d out src/App.java
run: app
	java -cp out App
test: app
	java -cp out App --debug
clean:
	rm -r out
tile:
	hyprctl keyword windowrulev2 tile, class:App, title:Markdown Renderer
cleantile:
	hyprctl keyword windowrulev2 unset, class:App, title:Markdown Renderer
