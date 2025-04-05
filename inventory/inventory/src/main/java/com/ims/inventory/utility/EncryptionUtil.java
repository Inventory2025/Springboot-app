package com.ims.inventory.utility;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EncryptionUtil {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        EncryptionUtil obj = new EncryptionUtil();
        obj.getEncrypt("admin");
    }

    public String getEncrypt(String pass) {
        log.info("EncryptionUtil::getEncrypt-->:{}", pass);
        String encryptPass = passwordEncoder.encode(pass);
        log.info("EncryptionUtil::Encrypted password-->:{}", encryptPass);
        return encryptPass;
    }
}
