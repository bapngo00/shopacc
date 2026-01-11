# Hướng dẫn Deploy ứng dụng Spring Boot

## 🚀 Các nền tảng đề xuất (theo độ dễ)

### 1. **Railway** (Khuyến nghị - Dễ nhất) ⭐
- ✅ Miễn phí $5/tháng credit
- ✅ Tự động build từ GitHub
- ✅ Hỗ trợ MySQL tích hợp
- ✅ Dễ cấu hình

**Các bước:**
1. Đăng ký tại https://railway.app (dùng GitHub login)
2. Tạo project mới → "Deploy from GitHub repo"
3. Chọn repository của bạn
4. Thêm MySQL database:
   - Click "New" → "Database" → "Add MySQL"
5. Cấu hình biến môi trường:
   ```
   SPRING_DATASOURCE_URL=jdbc:mysql://[host]:[port]/[database]
   SPRING_DATASOURCE_USERNAME=[username]
   SPRING_DATASOURCE_PASSWORD=[password]
   ```
6. Deploy tự động!

---

### 2. **Render** (Miễn phí tốt)
- ✅ Miễn phí tier (có thể sleep sau 15 phút không dùng)
- ✅ Tự động build từ GitHub
- ✅ Hỗ trợ MySQL

**Các bước:**
1. Đăng ký tại https://render.com
2. Tạo "New Web Service" → Connect GitHub repo
3. Cấu hình:
   - Build Command: `mvn clean package`
   - Start Command: `java -jar target/23t1020159-0.0.1-SNAPSHOT.jar`
4. Thêm MySQL database riêng
5. Cấu hình Environment Variables

---

### 3. **Heroku** (Phổ biến nhưng có thể tốn phí)
- ⚠️ Không còn free tier
- ✅ Rất ổn định
- ✅ Dễ dùng

**Các bước:**
1. Tạo file `Procfile` trong root:
   ```
   web: java -jar target/23t1020159-0.0.1-SNAPSHOT.jar
   ```
2. Đăng ký Heroku → Tạo app
3. Add-on MySQL (JawsDB hoặc ClearDB)
4. Deploy qua Git hoặc Heroku CLI

---

### 4. **VPS (DigitalOcean/AWS EC2)** (Tự quản lý)
- ✅ Hoàn toàn kiểm soát
- ✅ Không giới hạn
- ⚠️ Cần kiến thức server

---

## 📋 Chuẩn bị trước khi deploy

### 1. Tạo file `.gitignore` (nếu chưa có)
```
target/
*.class
*.jar
*.war
*.log
.idea/
.vscode/
*.iml
application-local.properties
```

### 2. Tạo file `application-production.properties`
```properties
spring.application.name=web
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect

# Port
server.port=${PORT:8080}

# CORS (nếu cần)
spring.web.cors.allowed-origins=*
```

### 3. Cập nhật `pom.xml` để build JAR
Đã có sẵn Spring Boot Maven plugin, không cần thay đổi.

### 4. Build JAR file local để test:
```bash
mvn clean package
java -jar target/23t1020159-0.0.1-SNAPSHOT.jar
```

---

## 🔧 Cấu hình cho Railway (Khuyến nghị)

### Bước 1: Push code lên GitHub
```bash
git init
git add .
git commit -m "Initial commit"
git remote add origin [your-github-repo-url]
git push -u origin main
```

### Bước 2: Deploy trên Railway
1. Vào https://railway.app
2. "New Project" → "Deploy from GitHub repo"
3. Chọn repo → Deploy
4. Thêm MySQL:
   - "New" → "Database" → "Add MySQL"
   - Railway tự động tạo biến môi trường

### Bước 3: Cấu hình Environment Variables
Railway tự động tạo:
- `MYSQLHOST`
- `MYSQLPORT`
- `MYSQLDATABASE`
- `MYSQLUSER`
- `MYSQLPASSWORD`

Thêm vào `application.properties` hoặc dùng biến môi trường:
```properties
spring.datasource.url=jdbc:mysql://${MYSQLHOST}:${MYSQLPORT}/${MYSQLDATABASE}
spring.datasource.username=${MYSQLUSER}
spring.datasource.password=${MYSQLPASSWORD}
```

### Bước 4: Cấu hình Port
Railway tự động set biến `PORT`, cần cấu hình trong `application.properties`:
```properties
server.port=${PORT:8080}
```

---

## 🌐 Sau khi deploy

1. **Kiểm tra logs** trên platform để xem có lỗi không
2. **Test API**: `https://your-app.railway.app/api/products`
3. **Test frontend**: `https://your-app.railway.app/index.html`
4. **Cấu hình domain** (nếu có) trong Settings

---

## ⚠️ Lưu ý quan trọng

1. **Database credentials**: Không commit password vào Git
2. **CORS**: Có thể cần cấu hình CORS trong SecurityConfig
3. **File upload**: Nếu dùng Google Drive, cần cấu hình service account
4. **HTTPS**: Các platform tự động cung cấp HTTPS

---

## 🆘 Troubleshooting

**Lỗi kết nối database:**
- Kiểm tra biến môi trường
- Kiểm tra firewall của database
- Kiểm tra SSL mode

**App không start:**
- Xem logs trên platform
- Kiểm tra port configuration
- Kiểm tra Java version (cần Java 21)

**404 Not Found:**
- Kiểm tra static files path
- Kiểm tra SecurityConfig

---

## 📞 Hỗ trợ

Nếu gặp vấn đề, kiểm tra:
- Logs trên platform
- Database connection
- Environment variables
- Port configuration
