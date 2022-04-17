package com.halftusk.authentication.authservice.utils;

import lombok.extern.slf4j.Slf4j;
import java.util.Random;

@Slf4j
public class OtpGenerator {

    public static String generateOtp(){
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        String otp = String.format("%06d", number);
        log.info("Random generated otp ", otp);
        return otp;
    }
}
