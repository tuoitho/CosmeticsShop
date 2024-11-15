package vn.oneshop.cosmetics_shop.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "Feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedbackId;
    private String comment;
    private Date reviewDate;
    private Double rating;

    @ManyToOne
    private Order order;

    @ManyToOne
    private Customer customer;

    @OneToOne(mappedBy = "feedback")
    private FeedbackResponse response;

}
