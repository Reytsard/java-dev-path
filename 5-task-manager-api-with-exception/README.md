docker run --name task-manager-db ^
  -e POSTGRES_DB=taskmanager ^
  -e POSTGRES_USER=admin ^
  -e POSTGRES_PASSWORD=admin123 ^
  -p 5432:5432 ^
  -d postgres






When using Optional<T> return type, the errors are handled by the controller.
on the other hand, to handle it in service classes. we will use just <T> and not add Optional