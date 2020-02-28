# Java JWT RS256
Java implementation of JSON Web Token (JWT)


## Get it

### Maven

```xml
<dependency>
    <groupId>com.auth0</groupId>
    <artifactId>java-jwt</artifactId>
    <version>3.10.0</version>
</dependency>
```

### Gradle

```gradle
implementation 'com.auth0:java-jwt:3.10.0'
```


## Usage


### Create and Sign a Token

* Example using `RS256`

```java
//Get the key instance
RSAPublicKey publicKey = readPublicKey(new File(PUBLIC_KEY_FILE));
RSAPrivateKey privateKey = readPrivateKey(new File(PRIVATE_KEY_FILE));
try {
    Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
    String token = JWT.create()
        .withIssuer("auth0")
        .sign(algorithm);
} catch (JWTCreationException exception){
    //Invalid Signing configuration / Couldn't convert Claims.
}
```

If a Claim couldn't be converted to JSON or the Key used in the signing process was invalid a `JWTCreationException` will raise.


### Verify a Token

You'll first need to create a `JWTVerifier` instance by calling `JWT.require()` and passing the `Algorithm` instance. If you require the token to have specific Claim values, use the builder to define them. The instance returned by the method `build()` is reusable, so you can define it once and use it to verify different tokens. Finally call `verifier.verify()` passing the token.

* Example using `RS256`

```java
String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXUyJ9.eyJpc3MiOiJhdXRoMCJ9.AbIJTDMFc7yUa5MhvcP03nJPyCPzZtQcGEp-zWfOkEE";
//Get the key instance
RSAPublicKey publicKey = readPublicKey(new File(PUBLIC_KEY_FILE));
RSAPrivateKey privateKey = readPrivateKey(new File(PRIVATE_KEY_FILE));
try {
    Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
    JWTVerifier verifier = JWT.require(algorithm)
        .withIssuer("auth0")
        .build(); //Reusable verifier instance
    DecodedJWT jwt = verifier.verify(token);
} catch (JWTVerificationException exception){
    //Invalid signature/claims
}
```
