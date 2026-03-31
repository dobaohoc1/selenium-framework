package com.lab9.bai1;

import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * <h2>LoginTest – Test class kiểm thử đăng nhập SauceDemo</h2>
 *
 * <p>
 * Tích hợp Allure Report với đầy đủ annotations và step logging.
 * </p>
 *
 * @author Lab11 - CI/CD Framework
 * @version 2.0
 */
@Feature("Xác thực người dùng") // Nhóm feature trong Allure
public class LoginTest extends BaseTest {

        private static final String LOGIN_DATA = "testdata/login_data.json";

        // =========================================================
        // TEST METHODS
        // =========================================================

        /**
         * TC01 – Đăng nhập thành công với tài khoản hợp lệ.
         */
        @Test(description = "TC01 - Đăng nhập thành công")
        @Story("UC-001: Đăng nhập bằng tài khoản hợp lệ")
        @Description("Kiểm thử đăng nhập với username/password hợp lệ → trang Products hiển thị")
        @Severity(SeverityLevel.CRITICAL)
        public void testLoginSuccess() {
                Map<String, String> data = TestDataReader.getTestCase(LOGIN_DATA, "TC01_LoginSuccess");

                Allure.step("Đọc test data từ JSON", () -> {
                        Assert.assertNotNull(data.get("username"), "Username không được null");
                        Assert.assertNotNull(data.get("password"), "Password không được null");
                });

                LoginPage loginPage = new LoginPage(getDriver());

                Allure.step("Mở trang đăng nhập", () -> loginPage.openLoginPage());

                Allure.step("Nhập thông tin đăng nhập (username + password)", () -> loginPage.login(
                                ConfigReader.getUsername(), // Đọc từ env hoặc config
                                ConfigReader.getPassword()));

                Allure.step("Kiểm tra chuyển trang thành công → Products hiển thị", () -> Assert.assertTrue(
                                loginPage.isLoginSuccessful(),
                                "Sau khi đăng nhập, trang Products phải hiển thị"));
        }

        /**
         * TC02 – Đăng nhập thất bại với mật khẩu sai.
         */
        @Test(description = "TC02 - Đăng nhập thất bại - mật khẩu sai")
        @Story("UC-002: Đăng nhập với thông tin sai")
        @Description("Kiểm thử đăng nhập với mật khẩu sai → hiển thị thông báo lỗi")
        @Severity(SeverityLevel.NORMAL)
        public void testLoginWrongPassword() {
                Map<String, String> data = TestDataReader.getTestCase(LOGIN_DATA, "TC02_LoginWrongPassword");
                String expectedError = data.get("expectedResult");

                LoginPage loginPage = new LoginPage(getDriver());

                Allure.step("Mở trang đăng nhập", () -> loginPage.openLoginPage());

                Allure.step("Nhập mật khẩu sai và submit",
                                () -> loginPage.login(data.get("username"), data.get("password")));

                Allure.step("Kiểm tra thông báo lỗi hiển thị", () -> Assert.assertTrue(
                                loginPage.isErrorDisplayed(),
                                "Phải hiển thị thông báo lỗi khi mật khẩu sai"));

                Allure.step("Kiểm tra nội dung thông báo lỗi đúng", () -> {
                        String actualError = loginPage.getErrorMessage();
                        Assert.assertTrue(
                                        actualError.contains(expectedError),
                                        "Thông báo lỗi không đúng.\nExpected: " + expectedError
                                                        + "\nActual: " + actualError);
                });
        }

        /**
         * TC03 – Đăng nhập với tài khoản bị khóa.
         */
        @Test(description = "TC03 - Đăng nhập với tài khoản bị locked")
        @Story("UC-003: Xử lý tài khoản bị khóa")
        @Description("Kiểm thử đăng nhập với tài khoản locked → hiển thị thông báo bị khóa")
        @Severity(SeverityLevel.NORMAL)
        public void testLoginLockedUser() {
                Map<String, String> data = TestDataReader.getTestCase(LOGIN_DATA, "TC03_LoginLockedUser");
                String expectedError = data.get("expectedResult");

                LoginPage loginPage = new LoginPage(getDriver());

                Allure.step("Mở trang đăng nhập", () -> loginPage.openLoginPage());

                Allure.step("Đăng nhập với tài khoản bị locked",
                                () -> loginPage.login(data.get("username"), data.get("password")));

                Allure.step("Kiểm tra thông báo lỗi bị khóa hiển thị", () -> Assert.assertTrue(
                                loginPage.isErrorDisplayed(),
                                "Phải hiển thị thông báo lỗi với tài khoản bị khóa"));

                Allure.step("Kiểm tra nội dung thông báo locked đúng", () -> {
                        String actualError = loginPage.getErrorMessage();
                        Assert.assertTrue(
                                        actualError.contains(expectedError),
                                        "Thông báo lỗi không đúng.\nExpected: " + expectedError
                                                        + "\nActual: " + actualError);
                });
        }
}
