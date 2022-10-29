FROM adoptopenjdk/openjdk11
WORKDIR /app
COPY ./ app/
ENTRYPOINT ["sh", "app/invoke.sh"]
