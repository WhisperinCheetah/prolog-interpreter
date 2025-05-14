FROM tolauwae/lp-project-2024-2025:latest

# Install curl if not present
RUN apt-get update && apt-get install -y curl tar

# Download Azul JDK 21
RUN mkdir -p /opt/java && \
    curl -fsSL https://cdn.azul.com/zulu/bin/zulu21.32.17-ca-jdk21.0.2-linux_x64.tar.gz \
    | tar -xz -C /opt/java

# Set environment variables
ENV JAVA_HOME=/opt/java/zulu21.32.17-ca-jdk21.0.2-linux_x64
ENV PATH="${JAVA_HOME}/bin:${PATH}"

CMD ["bash"]

