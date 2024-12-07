package com.cosmeticsellingwebsite.controller.admin;

import com.cosmeticsellingwebsite.dto.ProductFeedbackDTO;
import com.cosmeticsellingwebsite.entity.Customer;
import com.cosmeticsellingwebsite.entity.FeedbackResponse;
import com.cosmeticsellingwebsite.entity.Product;
import com.cosmeticsellingwebsite.entity.ProductFeedback;
import com.cosmeticsellingwebsite.service.impl.CustomerService;
import com.cosmeticsellingwebsite.service.impl.FeedbackResponseService;
import com.cosmeticsellingwebsite.service.impl.ProductFeedbackService;
import com.cosmeticsellingwebsite.service.impl.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/feedbacks")
public class FeedbackController {
    @Autowired
    private ProductFeedbackService productFeedbackService;

    @Autowired
    private FeedbackResponseService feedbackResponseService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @GetMapping
    public String listFeedbacks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String filter,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductFeedback> feedbackPage;

        if ("responded".equals(filter)) {
            feedbackPage = productFeedbackService.getRespondedFeedbacks(keyword, pageable);
        } else if ("not_responded".equals(filter)) {
            feedbackPage = productFeedbackService.getNotRespondedFeedbacks(keyword, pageable);
        } else {
            feedbackPage = productFeedbackService.searchFeedbacks(keyword, pageable);
        }

        List<Customer> customers = customerService.getAllCustomers();

        List<ProductFeedbackDTO> feedbackDTOs = feedbackPage.getContent().stream()
                .map(f -> {
                    String customerName = customers.stream()
                            .filter(c -> c.getUserId().equals(f.getCustomerId()))
                            .map(Customer::getFullname)
                            .findFirst()
                            .orElse("Unknown");
                    return new ProductFeedbackDTO(
                            f.getProductFeedbackId(),
                            f.getOrderId(),
                            customerName,
                            f.getFeedbackDate(),
                            f.getFeedbackResponse()
                    );
                })
                .collect(Collectors.toList());

        // Truyền dữ liệu vào model
        model.addAttribute("productFeedbacks", feedbackDTOs);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", feedbackPage.getTotalPages());
        model.addAttribute("pageSize", size);
        model.addAttribute("keyword", keyword);
        model.addAttribute("filter", filter);

        return "admin/admin-feedback-list";
    }
    // Hiển thị form phản hồi
    @GetMapping("/respond/{feedbackId}")
    public String showFeedbackResponseForm(@PathVariable Long feedbackId, Model model) {
        // Lấy thông tin đánh giá
        Optional<ProductFeedback> feedbackOptional = productFeedbackService.findById(feedbackId);
        if (feedbackOptional.isEmpty()) {
            model.addAttribute("error", "Không tìm thấy đánh giá.");
            return "error";
        }

        ProductFeedback feedback = feedbackOptional.get();

        // Lấy thông tin khách hàng
        Customer customer = customerService.findById(feedback.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng."));

        // Lấy thông tin sản phẩm
        Product product = productService.findById(feedback.getProduct().getProductId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm."));

        // Nếu phản hồi đã tồn tại, hiển thị phản hồi hiện có
        FeedbackResponse feedbackResponse = feedback.getFeedbackResponse();
        if (feedbackResponse == null) {
            feedbackResponse = new FeedbackResponse();
        }

        model.addAttribute("customer", customer);
        model.addAttribute("product", product);
        model.addAttribute("productFeedback", feedback);
        model.addAttribute("feedbackResponse", feedbackResponse);

        return "admin/admin-feedback-response"; // Tên file HTML
    }

    // Xử lý lưu phản hồi
    @PostMapping("/respond/{feedbackId}")
    public String submitFeedbackResponse(@ModelAttribute FeedbackResponse feedbackResponse,
                                         @RequestParam Long feedbackId) {
        // Lấy thông tin đánh giá
        ProductFeedback feedback = productFeedbackService.findById(feedbackId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đánh giá."));

        // Kiểm tra xem phản hồi đã tồn tại chưa
        FeedbackResponse existingResponse = feedbackResponseService.findByFeedbackId(feedbackId);

        if (existingResponse != null) {
            // Nếu phản hồi đã tồn tại, cập nhật phản hồi cũ
            existingResponse.setComment(feedbackResponse.getComment());
            existingResponse.setResponseDate(LocalDateTime.now());
            feedbackResponseService.update(existingResponse);
        } else {
            // Nếu chưa có phản hồi, tạo mới
            feedbackResponse.setProductFeedback(feedback);
            feedbackResponse.setResponseDate(LocalDateTime.now());
            feedbackResponseService.save(feedbackResponse);
        }

        return "redirect:/admin/feedbacks"; // Chuyển hướng về danh sách đánh giá
    }
}
