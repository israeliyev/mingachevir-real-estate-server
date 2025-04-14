package io.mingachevir.mingachevirrealestateserver.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class VisitorUtils {

    private static final String VISITOR_COOKIE_NAME = "visitorId";
    private static final String VISITOR_HEADER_NAME = "X-Visitor-Fingerprint";

    // Generate a unique visitor ID based on IP, User-Agent, and fingerprint
    public static String generateVisitorId(HttpServletRequest request, HttpServletResponse response) {
        // Try to get existing visitor ID from cookie
        String visitorId = getVisitorIdFromCookie(request);
        if (visitorId != null) {
            return visitorId;
        }

        // If no cookie, generate a new visitor ID
        String ipAddress = getClientIpAddress(request);
        String userAgent = request.getHeader("User-Agent");
        String fingerprint = request.getHeader(VISITOR_HEADER_NAME); // Sent by frontend

        // Combine IP, User-Agent, and fingerprint to create a unique hash
        String rawVisitorData = ipAddress + userAgent + (fingerprint != null ? fingerprint : "");
        visitorId = hashData(rawVisitorData);

        // Set the visitor ID as a cookie
        setVisitorIdCookie(response, visitorId);

        return visitorId;
    }

    // Get visitor ID from cookie
    private static String getVisitorIdFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (VISITOR_COOKIE_NAME.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    // Set visitor ID as a cookie
    private static void setVisitorIdCookie(HttpServletResponse response, String visitorId) {
        Cookie cookie = new Cookie(VISITOR_COOKIE_NAME, visitorId);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(365 * 24 * 60 * 60); // 1 year expiry
        response.addCookie(cookie);
    }

    // Get client IP address
    private static String getClientIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }

    // Hash data to create a unique identifier
    private static String hashData(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // Fallback to UUID if hashing fails
            return UUID.randomUUID().toString();
        }
    }

    // Get current request (helper method)
    public static HttpServletRequest getCurrentRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }

    // Get current response (helper method)
    public static HttpServletResponse getCurrentResponse() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getResponse() : null;
    }
}
