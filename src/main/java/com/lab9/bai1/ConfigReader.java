package com.lab9.bai1;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * <h2>ConfigReader - Đọc cấu hình từ config.properties</h2>
 *
 * <p>
 * Singleton class đọc file {@code config.properties} từ classpath.
 * Cung cấp các method tiện ích để lấy URL, timeout theo môi trường.
 * </p>
 *
 * <h3>Cách dùng:</h3>
 * 
 * <pre>{@code
 * String url = ConfigReader.getBaseUrl("dev");
 * int timeout = ConfigReader.getExplicitTimeout();
 * }</pre>
 *
 * @author Lab9 - Page Object Model Framework
 * @version 1.0
 */
public class ConfigReader {

    private static final String CONFIG_FILE = "config.properties";
    private static Properties props;

    // Khởi tạo 1 lần khi class được load (Singleton)
    static {
        props = new Properties();
        try (InputStream is = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream(CONFIG_FILE)) {
            if (is == null) {
                throw new RuntimeException("[ConfigReader] Không tìm thấy file: " + CONFIG_FILE);
            }
            props.load(is);
        } catch (IOException e) {
            throw new RuntimeException("[ConfigReader] Lỗi đọc file config: " + e.getMessage(), e);
        }
    }

    // Không cho khởi tạo instance
    private ConfigReader() {
    }

    /**
     * Lấy Base URL theo môi trường.
     *
     * @param env tên môi trường: "dev", "staging", "prod"
     * @return URL tương ứng, mặc định dùng "dev" nếu không tìm thấy
     */
    public static String getBaseUrl(String env) {
        String key = "url." + env.toLowerCase().trim();
        String url = props.getProperty(key);
        if (url == null || url.isEmpty()) {
            System.err.println("[ConfigReader] Không tìm thấy key: " + key + " → dùng url.dev");
            url = props.getProperty("url.dev", "https://www.saucedemo.com/");
        }
        return url;
    }

    /**
     * Lấy thời gian Explicit Wait mặc định (giây).
     *
     * @return giá trị timeout, mặc định 15 giây
     */
    public static int getExplicitTimeout() {
        return Integer.parseInt(props.getProperty("timeout.explicit", "15"));
    }

    /**
     * Lấy thời gian chờ tải trang (giây).
     *
     * @return giá trị timeout, mặc định 30 giây
     */
    public static int getPageLoadTimeout() {
        return Integer.parseInt(props.getProperty("timeout.pageload", "30"));
    }

    /**
     * Lấy giá trị bất kỳ từ config theo key.
     *
     * @param key tên property
     * @return giá trị tương ứng, hoặc null nếu không tìm thấy
     */
    public static String get(String key) {
        return props.getProperty(key);
    }

    /**
     * Lấy username để đăng nhập.
     *
     * <p>
     * <b>Ưu tiên đọc từ biến môi trường</b> {@code APP_USERNAME}
     * (được set bởi GitHub Secrets khi chạy CI/CD).
     * Nếu không có → fallback về {@code app.username} trong config.properties
     * (dùng khi chạy local).
     * </p>
     *
     * <pre>{@code
     * // CI/CD: GitHub Actions set APP_USERNAME = secrets.SAUCEDEMO_USERNAME
     * // Local: đọc từ config.properties → app.username=standard_user
     * }</pre>
     *
     * @return username, không bao giờ null
     */
    public static String getUsername() {
        // Ưu tiên 1: Biến môi trường (GitHub Secrets → không lộ trong code)
        String username = System.getenv("APP_USERNAME");
        if (username == null || username.isBlank()) {
            // Fallback: đọc từ config.properties khi chạy local
            username = props.getProperty("app.username", "standard_user");
        }
        return username;
    }

    /**
     * Lấy password để đăng nhập.
     *
     * <p>
     * <b>Ưu tiên đọc từ biến môi trường</b> {@code APP_PASSWORD}
     * (được set bởi GitHub Secrets khi chạy CI/CD).
     * Nếu không có → fallback về {@code app.password} trong config.properties
     * (dùng khi chạy local).
     * </p>
     *
     * @return password, không bao giờ null
     */
    public static String getPassword() {
        // Ưu tiên 1: Biến môi trường (GitHub Secrets → không lộ trong code)
        String password = System.getenv("APP_PASSWORD");
        if (password == null || password.isBlank()) {
            // Fallback: đọc từ config.properties khi chạy local
            password = props.getProperty("app.password", "secret_sauce");
        }
        return password;
    }
}
