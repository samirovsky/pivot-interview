# Use the official Maven image to build your application
FROM gcr.io/distroless/java21-debian12

ARG JAR_FILE=build/libs/*SNAPSHOT.jar

# Copy the packaged application from the build stage
COPY --from=build ${JAR_FILE} /usr/local/lib/purchasing-app.jar

# Expose the port your app runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/usr/local/lib/purchasing-app.jar"]
