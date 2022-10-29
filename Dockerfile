FROM adoptopenjdk/openjdk11
WORKDIR /app
COPY ./ app/
EXPOSE 8080
ENTRYPOINT ["sh", "app/invoke.sh"]
