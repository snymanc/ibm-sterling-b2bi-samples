# IBM Sterling B2B: Generate JWT

Samples to generate a JWT.


| Type              | filename                     | Description of Role                         |
|-------------------|------------------------------|---------------------------------------------|
| Business Process  | generate-jwt.bpml            | Business Process to generate JWT.           |
| Java File         | generateJWT.java             | Java code to generate JWT.                  |
| B2Bi Properties   | Generate-JWT.properties      | Key/value pairs located in [properties file](https://www.ibm.com/docs/en/b2b-integrator/6.1.0?topic=customizations-property-files). |

## 3rd Party Jars
### Jars used in java file.
| Jar                                                                                  | Version | Description                                                                                |
|--------------------------------------------------------------------------------------|---------|--------------------------------------------------------------------------------------------|
| [Nimbus-JOSE-JWT](https://mvnrepository.com/artifact/com.nimbusds/nimbus-jose-jwt)   | 9.37    | Java library for Javascript Object Signing and Encryption (JOSE) and JSON Web Tokens (JWT) |
| [bcprov-jdk15on](https://mvnrepository.com/artifact/org.bouncycastle/bcprov-jdk15on) | 1.70    | The Bouncy Castle Crypto package is a Java implementation of cryptographic algorithms. This jar contains JCE provider and lightweight API for the Bouncy Castle Cryptography APIs for JDK 1.5 and up. |
| [json-smart](https://mvnrepository.com/artifact/net.minidev/json-smart)              | 2.50    | JSON (JavaScript Object Notation) is a lightweight data-interchange format. |
| [commons-codec](https://mvnrepository.com/artifact/commons-codec/commons-codec)      | 1.16    | The Apache Commons Codec package contains simple encoder and decoders for various formats such as Base64 and Hexadecimal. In addition to these widely used encoders and decoders, the codec package also maintains a collection of phonetic encoding utilities. |

### Install command
``` shell
installDir/bin/install3rdParty.sh jose-jwt 9.37 -j ~/jars/nimbus-jose-jwt/nimbus-jose-jwt-9.37.jar ~/jars/nimbus-jose-jwt/bcprov-jdk15on-170.jar ~/jars/nimbus-jose-jwt/commons-codec-1.16.jar ~/jars/nimbus-jose-jwt/json-smart-2.5.0.jar
```

## On B2Bi Dashboard

1) Create a new business process generate-jwt.
2) Create a Java-Task Service

* Description: *Generic JavaTask*
* System Name: *JavaTask*
* Source Location Mode: *File*
* Filename: */*
* Relative Path: *No*
