//package com.cosmeticsellingwebsite.entity;
//
//import com.cosmeticsellingwebsite.enums.OrderStatus;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.springframework.format.annotation.DateTimeFormat;
//
//import java.time.LocalDateTime;
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//public class OrderStatusHistory {
//    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
//    @Id
//    private Long orderStatusHistoryId;
//    @Enumerated(jakarta.persistence.EnumType.STRING)
//    private OrderStatus status;
//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
//    private LocalDateTime updateAt;
//    private String description;
//
////    @ManyToOne
////    @JoinColumn(name = "orderId", referencedColumnName = "orderId")
////    private Order order;
//}
