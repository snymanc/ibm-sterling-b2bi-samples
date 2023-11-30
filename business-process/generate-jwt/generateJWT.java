/*
Class-Name:    generateJWT.java
Package:    JavaTasks

Description:    Generate Java Web Token [JWT]

Created By:        Clayton Snyman

Changes:
========
Date       | Owner                          | Description
===========|================================|=============================================================
30/11/2023 | Clayton Snyman                 | v1.0.00 - Initial commit

Notes:
=====
- Parameters
    * jwtFlowId		= Workflow Id
    * userId        = User Id from request
    * headerType    = Set type of JWT, extracted from Generate-JWT.properties
    * setIAT        = Boolean to create Issued At Time in milliseconds, extracted from Generate-JWT.properties
    * setEXP        = Boolean to create Expiry Time in milliseconds, extracted from Generate-JWT.properties
    * setIssuer		= Boolean to create Issuer, JWT issuer, extracted from Generate-JWT.properties
    * uniqueId		= String, combination of flowId
    * issuer        = String, issuer, extracted from Generate-JWT.properties
    * subject		= String,  subject, extracted from Generate-JWT.properties
    * ttlMillis		= Long, Time-to-Live in milliseconds, extracted from Generate-JWT.properties
    * rsaPrivateKey	= String, signing RSA Private Key, extracted from Generate-JWT.properties
    * keyAlgorithm	= String, extracted from Generate-JWT.properties
    * signAlgorithm	= String, extracted from Generate-JWT.properties
- Output written ProcessData
- Logging to noapp.log

*/

// B2Bi Package
import com.javatask.methods.*;

// 3rd Party Jars
// JWT
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

// PEM formatting
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;

// JAVA
import java.security.interfaces.RSAPrivateKey;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;
import javax.xml.bind.DatatypeConverter;

// WorkFLow ID
String flowId = (String)wfc.getWFContent("jwtFlowId");

// WorkFLow Details
String userId = (String)wfc.getWFContent("userId");

// WorkFLow JWT Properties
String type = (String)wfc.getWFContent("headerType");
String setIAT = (String)wfc.getWFContent("setIAT");
String setEXP = (String)wfc.getWFContent("setEXP");
String setIssuer = (String)wfc.getWFContent("setIssuer");
String uniqueId = (String)wfc.getWFContent("uniqueId");
String issuer = (String)wfc.getWFContent("issuer");
String subject = (String)wfc.getWFContent("subject");
long ttlMillis = Long.parseLong((String)wfc.getWFContent("ttlMillis"));
String rsaPrivateKey = (String)wfc.getWFContent("rsaPrivateKey");
String keyAlgorithm = (String)wfc.getWFContent("keyAlgorithm");
String signAlgorithm = (String)wfc.getWFContent("signAlgorithm");


// Set jwtIAT & jwtEXP
long nowMillis = System.currentTimeMillis();
long expMillis = nowMillis + ttlMillis;
Date now = new Date(nowMillis);
Date exp = new Date(expMillis);

// Set Header
JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.parse(signAlgorithm));
jwsHeader.setCustomParameter("type", type);

// Set Payload
JWTClaimsSet jwtClaims = new JWTClaimsSet();

if (setIssuer.equalsIgnoreCase("true")) { 
    jwtClaims.setJWTID(flowId);
    jwtClaims.setIssuer(issuer);
    jwtClaims.setSubject(subject);
}


if (setIAT.equalsIgnoreCase("true")) { 
    jwtClaims.setIssueTime(now);
}

if (setEXP.equalsIgnoreCase("true")) { 
    jwtClaims.setExpirationTime(exp);
}

jwtClaims.setCustomClaim("userId", userId);

// Convert RSA Private Key to bytes
byte[] data = DatatypeConverter.parseBase64Binary(rsaPrivateKey);

// Add PKCS#8 formatting
ASN1EncodableVector v = new ASN1EncodableVector();
v.add(new ASN1Integer(0));
ASN1EncodableVector v2 = new ASN1EncodableVector();
v2.add(new ASN1ObjectIdentifier(PKCSObjectIdentifiers.rsaEncryption.getId()));
v2.add(DERNull.INSTANCE);
v.add(new DERSequence(v2));
v.add(new DEROctetString(data));
ASN1Sequence seq = new DERSequence(v);
byte[] privKey = seq.getEncoded("DER");

PKCS8EncodedKeySpec spec = new  PKCS8EncodedKeySpec(privKey);
KeyFactory fact = KeyFactory.getInstance(keyAlgorithm);
PrivateKey key = fact.generatePrivate(spec);

// Sign the JWT w/ RSA-SHA256
JWSSigner signer = new RSASSASigner((RSAPrivateKey)key);
SignedJWT signedJWT = new SignedJWT(jwsHeader, jwtClaims);
signedJWT.sign(signer);

// JWT
String token = signedJWT.serialize();

//Write to PcData
wfc.addWFContent("token", token);
wfc.addWFContent("IAT", Long.toString(nowMillis));
wfc.addWFContent("EXP", Long.toString(expMillis));
wfc.addWFContent("uniqueId", uniqueId);
wfc.addWFContent("issuer", issuer);
wfc.addWFContent("subject", subject);
wfc.addWFContent("claims", "userId=" + userId);

// Write JWT into noapp.log
System.out.println("********** JWT **********");
System.out.println("JWT GENERATED: " + token);
System.out.println("JWT GENERATED by flowId: " + flowId);
System.out.println("JWT setIAT: " + setIAT);
System.out.println("JWT IAT: " + Long.toString(nowMillis));
System.out.println("JWT setEXP: " + setEXP);
System.out.println("JWT EXP: " + Long.toString(expMillis));
System.out.println("JWT setIssuer: " + setIssuer);


return "JWT Generated";

