[![CI](https://github.com/cambriota/iota-identity-provider/workflows/CI/badge.svg)](https://github.com/cambriota/iota-identity-provider/actions?query=workflow%3ACI)
[![GitHub release (latest SemVer)](https://img.shields.io/github/v/release/cambriota/iota-identity-provider?logo=github&sort=semver)](https://github.com/cambriota/iota-identity-provider/releases/latest)
[![codecov](https://codecov.io/gh/cambriota/iota-identity-provider/branch/develop/graph/badge.svg)](https://codecov.io/gh/cambriota/iota-identity-provider)
[![GitHub license](https://img.shields.io/github/license/cambriota/iota-identity-provider)](https://github.com/cambriota/iota-identity-provider/blob/main/LICENSE)

# IOTA Identity Provider

Bridging IOTA's self-sovereign identities to existing "Web 2.0" OAuth solutions. This fork supports the Keycloak IAM versions 17+ (Quarkus based).

<img src="docs/login-with-iota-button.png" alt="Login With IOTA" style="height: 64px;"/>

> NOTE: This plugin has NOT been audited or tested in production environments and should only used in non-critical environments until further notice!

The IOTA Identity Provider is based on the work of [Daniel Mader](https://github.com/daniel-mader). There is also a video (Jan 2022) where he explains the context and the Proof of Concept in detail: [2022-01-31 IOTA Experience Identity Meeting – Login with DID](https://www.youtube.com/watch?v=Vu-LuDZTxhg&t=89s)

## TL;DR
* This repo contains a plugin for the battle-tested [Keycloak](https://www.keycloak.org) _Open Source Identity and Access Management_
* This repo contains an updated version for Keycloak versions 17+ - the so-called Keycloak.X - which is based on Quarkus application platform instead of JBoss Wildfly application server
* It adds a custom endpoint where IOTA Identity credentials (DID) can be posted to
* The plugin tries to verify your _Verifiable Credential_ with the Tangle and hands over the containing user claims to Keycloak's native user management
* From that point on, every communication is **standard-compliant OAuth / OpenID Connect**
* Clients (such as webshops, sites or other consumers) can follow standard protocols and do not need to be aware of DIDs or IOTA Identity
* **very easy to implement for shops and websites**

> This solution is **not decentralized by design**! It acts as a bridge between two protocols!

## Features
* **Login to any web app** with your IOTA Credentials through any SSI wallet
  * very easy for clients to implement the "Login with IOTA" button and specify an Identity Provider with this plugin enabled
* **Ease-of-use**: no need to remember passwords (passwordless login)
* **Security**: underlying standard-compliant protocol and implementation (OAuth, Keycloak)
* **Privacy**: Plugin is "transparent", user data is only persisted during a user wants to be logged in

### Keywords
**DID**, **SSI**, **OAuth**, **OpenID Connect**, **OIDC**, **IOTA**, **Identity**, **web3**

## Architecture & Diagrams

_--architecture--_

### Components
- Keycloak plugin (plugins are called Service Provider Interfaces "SPI" in Keycloak)
- "sidecar": simple Node.js REST wrapper around `identity.rs` (used for DID resolving, connection to Tangle)
  - _Note: can be removed if credential verification can be performed in Java_
- SSI wallet: not yet published 
- Demo Client app: <a href="https://auth.cambriota.dev/demo/" target="_blank">https://auth.cambriota.dev/demo/</a>

[Read more about the different components here.](./docs/COMPONENTS.md)

## For website owners (clients)

Offer "Login with IOTA" on your website!

> CAUTION: This application has not been audited, it should ONLY be used in non-production environments!

You can run your own Keycloak instance with your own config.
You can also register your existing application with the deployed Identity Provider at <a href="https://auth.cambriota.dev" target="_blank">https://auth.cambriota.dev</a>
_(still in development)_.

## Usage
You need a DID document published to the Tangle.
You also need to be able to create and sign Verifiable Credentials and Presentations.

You can use [CLI wallet (not yet published)](https://github.com/cambriota/identity-cli-wallet) to create your DID and Credentials.

Navigate to <a href="https://auth.cambriota.dev/demo/" target="_blank">https://auth.cambriota.dev/demo/</a> to try it out!

## Development & Contribution

### Run

[Read about running your own instance.]() _--link--_

### Install

#### Prerequisites
* Java 11
* Docker, docker-compose

## Quick Start

0. Generate a self-signed cert/key by your own and copy it to a new folder called ./certs
```
openssl req -newkey rsa:2048 -nodes -keyout server.key.pem -x509 -days 3650 -out server.crt.pem
```
```
chmod 755 server.key.pem
```

1. Use this command to build the extension (iota-identity-provider-${SPI_VERSION}.jar) with Gradle (you don't have to install Gradle - gradlew (Gradle Wrapper) is able to download all necessary tools by itself)
```
./gradlew jar
```

2. Use docker-compose to build the images and to run the "keycloak" and the "sidecar" containers

a) Build the images (make sure the Docker Daemon is running...!)
```
docker-compose build
```
b) Start the containers
```
docker-compose up -d
```
c) Attach to the console for viewing logs
```
docker-compose logs -f keycloak
```

Now you can reach the Keycloak.X server in your browser: https://localhost:8443, with user=admin and password=admin as credentials.
There is also loaded the IOTA realm from realm-iota.json automatically at startup.

3. 
In order to stop the docker containers you have to execute
```
<CTRL-C>
```
```
docker-compose down
```

4. Last but not least
a) You need to start the [client app](https://github.com/daniel-mader/vuejs-oauth-demo-client) as well... the Demo-Client can be opened in your browser: http://localhost:4200

b) You need to either use a Wallet CLI or a Wallet App to communicate with the Identity Provider...





### Dev notes
* [Export an existing realm from Keycloak](scripts/export-realm.sh)

### Request
[Example request](docs/example-request.http)

### Debugging
Run `scripts/run-local.sh`, then attach a debugger to `localhost:8787`.

### TODOs
* [ ] add scheduled task to anonymize/wipe user after given time (anonymize: hash email, overwrite all other fields. on next login: hash email again, if already exists: populate fields again)
* [ ] replace manual "Continue" button click action with automatic forwarding (detect valid session in Keycloak)
* [ ] improve styling of Login page
* [ ] add github actions for security scan, code quality, etc.
* [ ] add github flows: release.yaml, publish-docker.yaml
* [ ] add application profile "prod" to build.sh (gradlew, docker)
* [ ] run Keycloak embedded in Spring Boot application / Quarkus / native image?
* [ ] remove unnecessary artifacts (such as `src/main/docker`)
* [ ] create web service that issues VCs for successful email validation. Request those VCs in this plugin to upgrade a user to `email_verified: true`.
* [ ] add some integration tests
