# Hướng dẫn sửa lỗi HTTP 500

## Nguyên nhân
Lỗi HTTP 500 xảy ra vì database chưa có cột `logo_image` mới.

## Giải pháp

### Cách 1: Tự động (Khuyến nghị)
1. **Restart Spring Boot application**
   - Hibernate sẽ tự động thêm cột `logo_image` vào bảng `products`
   - Vì đã có `spring.jpa.hibernate.ddl-auto=update` trong `application.properties`

2. **Kiểm tra logs** để xem có lỗi gì không

### Cách 2: Thủ công (Nếu cách 1 không hoạt động)

Chạy SQL trực tiếp trên database:

```sql
ALTER TABLE products 
ADD COLUMN logo_image LONGTEXT NULL;
```

### Cách 3: Kiểm tra và sửa lỗi

1. **Xem logs của Spring Boot** để biết lỗi cụ thể
2. **Kiểm tra kết nối database** có đúng không
3. **Đảm bảo** `spring.jpa.hibernate.ddl-auto=update` trong `application.properties`

## Sau khi sửa

1. Restart server
2. Refresh trang admin
3. Kiểm tra lại
