FROM quay.io/keycloak/keycloak:18.0.0 as builder

ENV SPI_VERSION 0.1.0

# Install custom providers 
COPY ../build/libs/iota-identity-provider-${SPI_VERSION}.jar /opt/keycloak/providers/

# init realm: see https://www.keycloak.org/server/importExport
COPY ../realm-iota.json /opt/keycloak/data/import/

# COPY ./src/main/theme/material /opt/jboss/keycloak/themes/material
# TODO: release: do not package keywind into spi
COPY ../theme/keywind /opt/keycloak/themes/keywind

# To start SSL you need a certificate and a private key
# either a) you create them e.g. in folder certs/ - then you have to copy them:
COPY ../certs/server.crt.pem /opt/keycloak/conf/server.crt.pem
COPY ../certs/server.key.pem /opt/keycloak/conf/server.key.pem
# or b) for demonstration purposes only, do not use in production:
#RUN keytool -genkeypair -storepass password -storetype PKCS12 -keyalg RSA -keysize 2048 -dname "CN=server" -alias server -ext "SAN:c=DNS:localhost,IP:127.0.0.1" -keystore conf/server.keystore


# The build options to configure existing and custom providers
RUN /opt/keycloak/bin/kc.sh build --spi-authenticator-provider=iota-did-authenticator --spi-authenticator-iota-did-authenticator-enabled=true

WORKDIR /opt/keycloak

# To start in Production Mode you need a hostname
ENV KC_HOSTNAME=localhost

# Start in Production Mode 
ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start --import-realm"]