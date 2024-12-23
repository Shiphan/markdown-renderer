app:
	javac -cp src -d out src/App.java
run: app
	java -cp out App
test: app
	java -cp out App --debug
clean:
	rm -r out
