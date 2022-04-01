
# 1st stage, build the app
FROM maven:3.6-jdk-11 as build

WORKDIR /parkguidance
#Install git

# Create a first layer to cache the "Maven World" in the local repository.
# Incremental docker builds will always resume after that, unless you update
# the pom
#ADD parkguidance-components/pom.xml parkguidance-components/
ADD parkguidance-microservices parkguidance-microservices
ADD parkguidance-components parkguidance-components
RUN ["/usr/local/bin/mvn-entrypoint.sh", "mvn", "-fparkguidance-microservices/pom.xml", "package", "-DskipTests"]

# Do the Maven build!
# Incremental docker builds will resume here when you change sources
RUN echo "done!"

# 2nd stage, build the runtime image
FROM openjdk:11-jre-slim
WORKDIR /parkguidance

# Copy the binary built in the 1st stage
COPY --from=build /parkguidance/parkguidance-microservices/parkguidance-ms-user/target/parkguidance-ms-user.jar ./
COPY --from=build /parkguidance/parkguidance-microservices/parkguidance-ms-user/target/libs ./libs

CMD ["java", "-jar", "parkguidance-ms-user.jar"]

EXPOSE 8080
