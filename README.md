# 🧪 Selenium POM Framework – Lab 9 / Lab 11

[![Selenium Tests](https://github.com/<YOUR_USERNAME>/selenium-framework/actions/workflows/selenium-ci.yml/badge.svg)](https://github.com/<YOUR_USERNAME>/selenium-framework/actions/workflows/selenium-ci.yml)

## 📌 Giới thiệu

Framework kiểm thử tự động sử dụng **Selenium 4 + TestNG + Page Object Model (POM)**.  
Áp dụng cho website SauceDemo (`https://www.saucedemo.com`).

### Công nghệ sử dụng

| Công nghệ | Phiên bản |
|-----------|-----------|
| Java | 17 |
| Selenium WebDriver | 4.18.1 |
| TestNG | 7.9.0 |
| WebDriverManager | 5.7.0 |
| Maven | 3.9+ |

---

## 📁 Cấu trúc project

```
lab9/
├── bai1/                         # Bài 1 – BasePage + BaseTest
│   ├── src/
│   │   ├── main/java/com/lab9/bai1/
│   │   │   ├── BasePage.java     # Page Object cơ sở (7 core methods)
│   │   │   ├── LoginPage.java    # Page Object trang login
│   │   │   ├── ConfigReader.java # Đọc config.properties
│   │   │   └── TestDataReader.java # Đọc JSON test data
│   │   └── test/java/com/lab9/bai1/
│   │       ├── BaseTest.java     # Setup/teardown WebDriver (ThreadLocal)
│   │       └── LoginTest.java    # Test cases đăng nhập
│   ├── testng.xml                # Chạy toàn bộ (parallel 3 threads)
│   └── testng-smoke.xml          # Smoke test cho CI/CD
├── bai2/ ... bai7/               # Các bài tiếp theo
├── .github/
│   └── workflows/
│       └── selenium-ci.yml       # GitHub Actions CI/CD pipeline
├── .gitignore
└── README.md
```

---

## 🚀 Chạy local

### Yêu cầu

- Java 17+
- Maven 3.9+
- Chrome browser (WebDriverManager tự tải chromedriver)

### Chạy tất cả test (bai1)

```bash
cd bai1
mvn clean test
```

### Chạy smoke test (dùng cho CI)

```bash
cd bai1
mvn clean test -Dbrowser=chrome -Denv=dev -DsuiteXmlFile=testng-smoke.xml
```

### Chạy trên browser khác

```bash
# Firefox
mvn clean test -Dbrowser=firefox

# Chrome headless
CI=true mvn clean test -Dbrowser=chrome
```

---

## ⚙️ Cấu hình

Cấu hình URL và credentials trong `src/main/resources/config.properties`:

```properties
base.url=https://www.saucedemo.com
username=standard_user
password=secret_sauce
```

> ⚠️ **Không commit thông tin nhạy cảm!** Dùng GitHub Secrets cho CI/CD.

---

## 🔧 CI/CD

Project được tích hợp **GitHub Actions** tại `.github/workflows/selenium-ci.yml`.

- Trigger: mỗi lần push lên `main` hoặc `develop`
- Môi trường: `ubuntu-latest` với Chrome headless
- Kết quả: upload lên GitHub Actions Artifacts (lưu 30 ngày)

---

## 📊 Kết quả test

Test reports được lưu tại:
- `target/surefire-reports/` – TestNG XML report
- `target/screenshots/` – Ảnh chụp màn hình khi test FAIL

---

## 👤 Tác giả

- **Họ tên:** [Tên sinh viên]  
- **MSSV:** [Mã số sinh viên]  
- **Môn:** Kiểm thử phần mềm tự động
