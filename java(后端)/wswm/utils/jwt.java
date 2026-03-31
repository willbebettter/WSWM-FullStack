package wswm.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class jwt {
    static String key = "5Zyo5L2g54i354i36Z2i5YmN54uX5Y+r5ZWl5ZGi5bCP54uX5Lic6KW/";

    public static String givejwt(String usr, String photo, Integer minute) {
        Map<String, Object> dx = new HashMap<>();
        dx.put("usr", usr);
        dx.put("photo", photo);
        return Jwts.builder().signWith(SignatureAlgorithm.HS256, key).addClaims(dx).setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * minute)).compact();
    }

    public static boolean pipeijwt(String jwt) {
        try {
            Jwts.parser().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String giveappjwt(String name, String photo, Integer minute) {
        Map<String, Object> dx = new HashMap<>();
        dx.put("name", name);
        dx.put("photo", photo);
        return Jwts.builder().signWith(SignatureAlgorithm.HS256, key).addClaims(dx).setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * minute)).compact();
    }

    public static Map<String, Object> getyh(String token) {
        try {
            Map<String, Object> dx = Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody();
            return dx;
        } catch (Exception e) {
            throw new RuntimeException("token验证失败");
        }
    }
    public static boolean jyapptoken(String token) {
        try {
            Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
