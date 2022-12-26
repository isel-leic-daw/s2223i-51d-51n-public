# Tic Tac Toe Service

## Development time commands

* Start docker image with development time services
```
docker compose up --build --force-recreate 
```

* Start shell on postgres container

```
docker exec -ti db-tests bash
```

* Start `psql` inside postgres container
```
psql -U dbuser -d db
```
* `psql` commands
  * `\h` - show help. 
  * `\d <table>` - show table.
  * `select ... ;` - execute query.
  * `\q` - quit `psql`.

* Start the full composition
```
./gradlew composeUp
```

* Loop curl
```
while true; do curl -w "\n" http://localhost:8080/status/hostname; done
```
