//package com.example.SplitLoop.expense.entity;
//
//import com.example.SplitLoop.group.entity.Group;
//import com.example.SplitLoop.user.entity.User;
//import jakarta.persistence.*;
//import jdk.jfr.Frequency;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//@Entity
//@Table(name = "expenses")
//@Getter
//@Setter
//public class Expense {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    private UUID id;
//
//    @ManyToOne
//    @JoinColumn(name = "group_id")
//    private Group group;
//
//    @ManyToOne
//    @JoinColumn(name = "created_by")
//    private User createdBy;
//
//    @ManyToOne
//    @JoinColumn(name = "paid_by")
//    private User paidBy;
//
//    private String name;
//
//    private BigDecimal amount;
//
//    private LocalDateTime createdAt;
//
//    private Frequency frequency;      // MONTHLY, WEEKLY, YEARLY...
//
//    private LocalDate startDate;
//
//    private LocalDate nextExecution;
//
//    private Boolean active;
//
//}