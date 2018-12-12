all: build

build: build-jar build-docker

build-jar:
	mvn package

build-docker:
	docker build -t csms/coffee-society-product:latest .

clean:
	mvn clean

run-jar:
	java -jar target/coffee-society-product*.jar

run-docker:
	docker run -d --name coffee-society-product csms/coffee-society-product

tag:
	docker tag csms/coffee-society-product csms/coffee-society-product:${TAG}

push-latest:
	docker push csms/coffee-society-product:latest

push-tag:
	docker push csms/coffee-society-product:${TAG}

docker-login:
	@docker login -u "${DOCKER_ID}" -p "${DOCKER_PASS}"
	
docker-run: run-docker

docker-remove:
	docker rm -f coffee-society-product

docker-logs:
	docker logs coffee-society-product
