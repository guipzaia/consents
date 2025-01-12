install:
	mvn install

clean:
	mvn clean

test:
	mvn test

run:
	set -a; . ./local.env; set +a; mvn spring-boot:run
