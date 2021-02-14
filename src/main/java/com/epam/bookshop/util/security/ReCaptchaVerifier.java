package com.epam.bookshop.util.security;

import com.epam.bookshop.config.AppConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Verifies recaptcha response
 */
public class ReCaptchaVerifier {
    private static final Logger logger = LoggerFactory.getLogger(ReCaptchaVerifier.class);

    private static final String SITE_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";
    private static final String SECRET_KEY = AppConfigurator.getInstance().getReCaptchaSecretKey();
    private static final String ACCEPT_LANG = "Accept-Language";
    private static final String EN_LAND_DATA = "en-US,en;q=0.5";
    private static final String POST_METHOD = "POST";
    private static final String SUCCESS = "success";

    private static ReCaptchaVerifier instance;
    private static final ReentrantLock lock = new ReentrantLock();

    /**
     * Threadsafe instantiation
     *
     * @return instance of {@link ReCaptchaVerifier} class
     */
    public static ReCaptchaVerifier getInstance() {
        lock.lock();
        try {
            if (instance == null) {
                instance = new ReCaptchaVerifier();
            }
        } finally {
            lock.unlock();
        }

        return instance;
    }


    /**
     * Verifies whether user passed captcha validation
     *
     * @param gRecaptchaResponse response generated from captcha
     * @return true if and only if verification finished successfully
     */
    public boolean verify(String gRecaptchaResponse) {
        if (gRecaptchaResponse == null || gRecaptchaResponse.length() == 0) {
            return false;
        }

        try {
            URL verifyUrl = new URL(SITE_VERIFY_URL);

            HttpsURLConnection conn = (HttpsURLConnection) verifyUrl.openConnection();

            conn.setRequestMethod(POST_METHOD);
            conn.setRequestProperty(ACCEPT_LANG, EN_LAND_DATA);
            String postParams = "secret=" + SECRET_KEY + "&response=" + gRecaptchaResponse;
            conn.setDoOutput(true);

            OutputStream outStream = conn.getOutputStream();
            outStream.write(postParams.getBytes());
            outStream.flush();
            outStream.close();

            int responseCode = conn.getResponseCode();
            logger.info("user validation at " + LocalDateTime.now() + "responseCode=" + responseCode);

            InputStream is = conn.getInputStream();

            JsonReader jsonReader = Json.createReader(is);
            JsonObject jsonObject = jsonReader.readObject();
            jsonReader.close();

            // ==> {"success": true}
            logger.info("server response: " + jsonObject);

            return jsonObject.getBoolean(SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }
}
