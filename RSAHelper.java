package com.auth0.pac.test;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import sun.misc.BASE64Decoder;

import java.io.*;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class RSAHelper {

    private static final String PRIVATE_KEY_FILE = "C:\\privateKey.pem";
    private static final String PUBLIC_KEY_FILE = "C:\\publicKey.pem";


    public static RSAPrivateKey readPrivateKey(File keyFile) throws Exception {
        FileInputStream in = new FileInputStream(keyFile);
        byte[] keyBytes = new byte[in.available()];
        in.read(keyBytes);
        in.close();

        String privateKey = new String(keyBytes, "UTF-8");
        privateKey = privateKey.replaceAll("(-+BEGIN RSA PRIVATE KEY-+\\r?\\n|-+END RSA PRIVATE KEY-+\\r?\\n?)", "");

        // don't use this for real projects!
        BASE64Decoder decoder = new BASE64Decoder();
        keyBytes = decoder.decodeBuffer(privateKey);

        // generate private key
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) keyFactory.generatePrivate(spec);
    }

    public static RSAPublicKey readPublicKey(File file) throws Exception {
        // read key bytes
        FileInputStream in = null;
        byte[] keyBytes = new byte[0];

        String pubKey = null;

        in = new FileInputStream(file);

        keyBytes = new byte[in.available()];
        in.read(keyBytes);
        in.close();
        pubKey = new String(keyBytes, "UTF-8");
        pubKey = pubKey.replaceAll("(-+BEGIN PUBLIC KEY-+\\r?\\n|-+END PUBLIC KEY-+\\r?\\n?)", "");

        // don't use this for real projects!
        BASE64Decoder decoder = new BASE64Decoder();
        keyBytes = decoder.decodeBuffer(pubKey);

        // generate public key
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) keyFactory.generatePublic(spec);

    }

    public static void main(String[] args) throws Exception {

        java.security.Security.addProvider(
                new org.bouncycastle.jce.provider.BouncyCastleProvider()
        );

        RSAPublicKey publicKey = readPublicKey(new File(PUBLIC_KEY_FILE));
        RSAPrivateKey privateKey = readPrivateKey(new File(PRIVATE_KEY_FILE));

        System.out.println(
                String.format("Key format: %s, algorithm: %s",
                        publicKey.getFormat(),
                        publicKey.getAlgorithm()));


        System.out.println(
                String.format("Key format: %s, algorithm:, %s ",
                        privateKey.getFormat(),
                        privateKey.getAlgorithm()));


        Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
        String token = JWT.create()
                .withClaim("userId", "2343")
                .withIssuer("auth0")
                .sign(algorithm);
        System.out.println(" token =" + token);


    }


}
