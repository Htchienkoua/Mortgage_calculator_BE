package com.academy.mortgage.model;

import com.academy.mortgage.model.enums.ApplicationStatus;
import com.academy.mortgage.model.enums.PaymentScheduleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "applications")
public class Applications {

    @Id
    @GeneratedValue
    private Long applicationId;
    @JoinColumn(name = "user_id")
    private Long userId;
    private String coApplicantEmail;
    private BigDecimal monthlyIncome;
    private BigDecimal coApplicantsIncome;
    private BigDecimal totalHouseholdIncome;
    private Boolean obligations;
    private BigDecimal mortgageLoans;
    private BigDecimal consumerLoans;
    private BigDecimal leasingAmount;
    private BigDecimal creditCardLimit;
    private BigDecimal availableMonthlyPayment;
    private String realEstateAddress;
    private BigDecimal realEstatePrice;
    private BigDecimal downPayment;
    private BigDecimal loanAmount;
    private Integer loanTerm;
    private BigDecimal interestRateMargin;
    private BigDecimal interestRateEuribor;
    private Integer euriborTerm;
    @Enumerated(EnumType.STRING)
    private PaymentScheduleType paymentScheduleType;
    private Integer amountOfKids;
    private Integer applicantsAmount;
    @Enumerated(EnumType.STRING)
    private ApplicationStatus applicationStatus;
}
