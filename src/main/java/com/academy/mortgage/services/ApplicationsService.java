package com.academy.mortgage.services;

import com.academy.mortgage.exceptions.ApplicationNotFoundException;
import com.academy.mortgage.exceptions.ApplicationNotSavedException;
import com.academy.mortgage.exceptions.MailNotSentException;
import com.academy.mortgage.exceptions.UserNotFoundException;
import com.academy.mortgage.model.Applications;
import com.academy.mortgage.model.User;
import com.academy.mortgage.model.api.request.ApplicationRequest;
import com.academy.mortgage.model.api.request.ApplicationStatusUpdateRequest;
import com.academy.mortgage.model.api.response.ApplicationsResponse;
import com.academy.mortgage.model.api.response.UsersApplicationResponse;
import com.academy.mortgage.model.enums.ApplicationStatus;
import com.academy.mortgage.repositories.ApplicationsRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApplicationsService {
    @Autowired
    ApplicationsRepository applicationsRepository;
    @Autowired
    UserService userService;

    @Autowired
    private JavaMailSender javaMailSender;

    public List<ApplicationsResponse> getApplications() {
        List<Applications> applications = applicationsRepository.findAll();
        List<ApplicationsResponse> responseList = new ArrayList<>();

        for (Applications application : applications) {
            User user = userService.getUserById(application.getUserId());
            ApplicationsResponse response = buildApplicationsResponse(application, user);
            responseList.add(response);
        }

        return responseList;
    }

    public Applications addApplication(ApplicationRequest applicationRequest) {

        User user = null;
        String password = null;
        boolean newUser = false;

        try {
            user = userService.getUserByEmail(applicationRequest.getEmail());
            user.setFirstName(applicationRequest.getFirstName());
            user.setLastName(applicationRequest.getLastName());
            user.setPhoneNumber(applicationRequest.getPhoneNumber());
            user.setPersonalNumber(applicationRequest.getPersonalNumber());
            user.setAddress(applicationRequest.getAddress());
            userService.updateUser(user);

        } catch (UserNotFoundException e) {
            password = RandomStringUtils.randomAlphanumeric(10);
            user = userService.addUser(applicationRequest, password);
            newUser = true;
        }

        Applications application = Applications.builder()
                .userId(user.getId())
                .monthlyIncome(applicationRequest.getMonthlyIncome())
                .coApplicantsIncome(applicationRequest.getCoApplicantsIncome())
                .totalHouseholdIncome(applicationRequest.getTotalHouseholdIncome())
                .obligations(applicationRequest.getObligations())
                .mortgageLoans(applicationRequest.getMortgageLoans())
                .consumerLoans(applicationRequest.getConsumerLoans())
                .leasingAmount(applicationRequest.getLeasingAmount())
                .creditCardLimit(applicationRequest.getCreditCardLimit())
                .availableMonthlyPayment(applicationRequest.getMonthlyPayment())
                .realEstateAddress(applicationRequest.getRealEstateAddress())
                .realEstatePrice(applicationRequest.getRealEstatePrice())
                .downPayment(applicationRequest.getDownPayment())
                .loanAmount(applicationRequest.getLoanAmount())
                .loanTerm(applicationRequest.getLoanTerm())
                .interestRateMargin(applicationRequest.getInterestRateMargin())
                .interestRateEuribor(applicationRequest.getInterestRateEuribor())
                .euriborTerm(applicationRequest.getEuriborTerm())
                .paymentScheduleType(applicationRequest.getPaymentScheduleType())
                .amountOfKids(applicationRequest.getAmountOfKids())
                .applicantsAmount(applicationRequest.getApplicants())
                .coApplicantEmail(applicationRequest.getCoApplicantEmail())
                .applicationStatus(ApplicationStatus.RECEIVED)
                .build();
        Applications applications = null;
        try {
            applications = applicationsRepository.save(application);
        } catch (Exception e) {
            System.out.println("Unexpected error occurred while saving application: " + e.getMessage());
            throw new ApplicationNotSavedException("Unexpected error occurred while saving application. Please try again later.");
        }
        try {
            if (newUser) {
                sendWelcomeEmail(user.getEmail(), password);
            } else {
                sendApplicationSubmittedEmail(user.getEmail());
            }
        } catch (MailSendException e) {
            System.out.println("Error sending email: " + e.getMessage());
            throw new MailNotSentException("Your application has been received. We will contact you soon for further steps.");
        }
        return applications;
    }

    private void sendWelcomeEmail(String toEmail, String tempPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@shrimp-eating-bankers.com");
        message.setTo(toEmail);
        message.setSubject("Welcome to Shrimp Eating Bankers");
        message.setText("Dear customer,\n\nThank you for creating an account with Shrimp Eating Bankers. \n\nYour temporary password is: " + tempPassword + "\n\nPlease use this password to login to your account and set up a new, secure password.\n\nOur team is currently reviewing your application and will be in touch with you shortly.\n\nBest regards,\nThe Shrimp Eating Bankers Team");
        javaMailSender.send(message);
    }

    private void sendApplicationSubmittedEmail(String toEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@shrimp-eating-bankers.com");
        message.setTo(toEmail);
        message.setSubject("Your Loan Application Has Been Submitted");
        message.setText("Dear customer,\n\nThank you for submitting a loan application with Shrimp Eating Bankers. Our team is currently reviewing your application and will be in touch with you shortly.\n\nBest regards,\nThe Shrimp Eating Bankers Team");
        javaMailSender.send(message);
    }

    public List<ApplicationsResponse> getApplicationsByUserEmail(String email) {
        User user = userService.getUserByEmail(email);

        if (user == null) {
            throw new UserNotFoundException(email);
        }
        Long userId = user.getId();
        List<Applications> applications = applicationsRepository.findAllByUserId(userId);
        List<ApplicationsResponse> responseList = new ArrayList<>();
        for (Applications application : applications) {
            ApplicationsResponse response = buildApplicationsResponse(application, user);
            responseList.add(response);
        }
        return responseList;
    }

    public void updateApplicationStatus(ApplicationStatusUpdateRequest applicationStatusUpdateRequest) {
        Long applicationId = applicationStatusUpdateRequest.getApplicationId();
        Applications application = applicationsRepository.findByApplicationId(applicationStatusUpdateRequest.getApplicationId());
        if (application == null) {
            throw new ApplicationNotFoundException(applicationId);
        }
        application.setApplicationStatus(applicationStatusUpdateRequest.getApplicationStatus());
        applicationsRepository.save(application);
    }

    public List<ApplicationsResponse> getApplicationsByUserId(Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            throw new UserNotFoundException(userId);
        }
        List<Applications> applications = applicationsRepository.findAllByUserId(userId);
        List<ApplicationsResponse> responseList = new ArrayList<>();
        for (Applications application : applications) {
            ApplicationsResponse response = buildApplicationsResponse(application, user);
            responseList.add(response);
        }
        return responseList;
    }

    private ApplicationsResponse buildApplicationsResponse(Applications application, User user) {
        return ApplicationsResponse.builder()
                .applicationId(application.getApplicationId())
                .applicants(application.getApplicantsAmount())
                .amountOfKids(application.getApplicantsAmount())
                .monthlyIncome(application.getMonthlyIncome())
                .coApplicantsIncome(application.getCoApplicantsIncome())
                .obligations(application.getObligations())
                .mortgageLoans(application.getMortgageLoans())
                .consumerLoans(application.getConsumerLoans())
                .leasingAmount(application.getLeasingAmount())
                .creditCardLimit(application.getCreditCardLimit())
                .monthlyPayment(application.getAvailableMonthlyPayment())
                .realEstateAddress(application.getRealEstateAddress())
                .realEstatePrice(application.getRealEstatePrice())
                .downPayment(application.getDownPayment())
                .loanAmount(application.getLoanAmount())
                .loanTerm(application.getLoanTerm())
                .paymentScheduleType(application.getPaymentScheduleType())
                .interestRateMargin(application.getInterestRateMargin())
                .euriborTerm(application.getEuriborTerm())
                .interestRateEuribor(application.getInterestRateEuribor())
                .totalHouseholdIncome(application.getTotalHouseholdIncome())
                .coApplicantEmail(application.getCoApplicantEmail())
                .applicationStatus(application.getApplicationStatus())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .personalNumber(user.getPersonalNumber())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .build();
    }

}

