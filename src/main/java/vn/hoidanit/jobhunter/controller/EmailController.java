package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.hoidanit.jobhunter.service.EmailService;
import vn.hoidanit.jobhunter.service.SubscriberService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1")
public class EmailController {
    private final EmailService emailService;
    private final SubscriberService subscriberService;

    public EmailController(final EmailService emailService, SubscriberService subscriberService) {
        this.emailService = emailService;
        this.subscriberService = subscriberService;
    }

    @GetMapping("/email")
    @ApiMessage("Send simple email")
    public String sendSimpleEmail() {
//        this.emailService.sendSimpleEmail();
//        this.emailService.sendEmailSync("lebachinhpt@gmail.com", "test send email", "<h1><b>hello</b></h1>", false, true);
//        this.emailService.sendEmailFromTemplateSync("lebachinhpt@gmail.com", "test send email", "job");
        this.subscriberService.sendSubscribersEmailJobs();
        return "Email Sent";
    }
}
