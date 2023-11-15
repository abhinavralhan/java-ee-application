/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package xray.evs.utilities;

import java.util.Base64;
import java.util.Random;

/**
 *
 * @author Admin
 */
public class Tokens {

    public static String generateToken() {
        int leftLimit = 91;
        int rightLimit = 116;
        int targetStringLength = 16;

        try {
            Thread.sleep(1);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        Random random = new Random();

        String randomString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        long timestampmillis = System.currentTimeMillis();
        String baseToken = timestampmillis + randomString;
        String token = Base64.getEncoder().withoutPadding().encodeToString(baseToken.getBytes());

        return token;
    }
}
