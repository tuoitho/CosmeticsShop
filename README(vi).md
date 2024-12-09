# Cosmetics Shop 💄

**Cosmetics Shop** là một nền tảng thương mại điện tử hiện đại, được xây dựng bằng các công nghệ tiên tiến như **Java 21**, **Spring Boot 3**, và **Thymeleaf**. Hệ thống sử dụng JPA cho ORM, tích hợp **Spring Security 6.1** để xác thực và phân quyền mạnh mẽ, đồng thời hỗ trợ đăng nhập Google để tăng sự tiện lợi cho người dùng và sử dụng reCAPTCHA nhằm tăng cường bảo mật. Nền tảng dựa trên **MySQL** để lưu trữ dữ liệu đáng tin cậy và **Redis** để quản lý mã OTP hiệu quả. Ngoài ra, hệ thống hỗ trợ thanh toán linh hoạt qua **PayPal** và **VNPay**, sử dụng **AJAX** để mang lại trải nghiệm người dùng mượt mà, và **Cloudinary** để lưu trữ và tối ưu hóa hình ảnh sản phẩm trên đám mây.

## Mục đích 🎯

**Cosmetics Shop** cung cấp một nền tảng trực tuyến giúp người dùng dễ dàng duyệt, đánh giá và mua sắm các sản phẩm mỹ phẩm. Nền tảng ưu tiên bảo mật và trải nghiệm người dùng, mang đến môi trường an toàn cho giao dịch, tùy chọn thanh toán linh hoạt, và tính năng duyệt sản phẩm trực quan. Dù bạn tìm kiếm sản phẩm chăm sóc da, trang điểm hay phụ kiện làm đẹp, nền tảng này đảm bảo một trải nghiệm mua sắm liền mạch, được hỗ trợ bởi công nghệ mạnh mẽ.

## Điểm nổi bật 🌟  

- 🚀 Hoạt động như một trang web bán hàng thực tế với nghiệp vụ bài toán được xác định đúng đắn.


- 🎯 Quán triệt mạnh mẽ các nguyên tắc của Lập trình hướng đối tượng, các mối quan hệ và thuộc tính được thiết lập một cách chính xác, chẳng hạn sản phẩm thì không có tính chất số lượng,...

- 🔐 Tính năng bảo mật tiên tiến*:  
  - Định nghĩa phương thức lấy thông tin người dùng đăng nhập thông qua lớp `SecurityContextHolder`độc lập sư phụ thuộc vào các cách lấy thông tin khác như seesion.  
  - Thực hiện kiểm tra tài khoản bị khóa khi đăng nhập bằng một lớp tùy chỉnh.  
  - Tùy chỉnh các lớp để xử lý đăng nhập thành công và thất bại.  
  - Gán được vai trò cho người dùng khi đăng nhập bằng OAuth2.  

---

## Công nghệ sử dụng 🛠️

- **Backend**: Java 21, Spring Boot 3, Spring Security 6.1  
- **Frontend**: Thymeleaf, HTML, CSS, JavaScript  
- **Cơ sở dữ liệu**: MySQL 🗄️  
- **ORM**: JPA (Java Persistence API) 🔄  
- **Quản lý phiên bản**: Git, GitHub 🌐
  **Tools**: Dbeaver, Mysql Workbench 8.0, Github desktop, Intellij Idea
- **Khác**: Cloudinary, JWT, Redis, Captcha, Oauth2, ...
### 🔐 **Hỗ trợ nhiều loại xác thực**  
- Đăng nhập mặc định của Spring Security  
- Đăng nhập OAuth2 (ví dụ: Google Login)  
- Đăng nhập Remember-me sử dụng cookie  

### ✅ **Kiểm tra trạng thái xác thực**  
Dễ dàng xác định trạng thái xác thực của người dùng chỉ với một phương thức.

Vui lòng xem tại [README.md](https://github.com/PhatBee/Cosmetics_Shop/blob/master/README.md)
