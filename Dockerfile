# The deployment Image
FROM gcr.io/distroless/base

# Pass in the native executable
ARG APP_FILE

EXPOSE 8080

# Copy the native executable into the root directory and call it "app"
COPY ${APP_FILE} app

ENTRYPOINT ["/app"]
