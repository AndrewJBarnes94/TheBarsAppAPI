package com.fellasbar.api.service;

import com.fellasbar.api.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);
    private static final DateTimeFormatter DATE_FMT =
        DateTimeFormatter.ofPattern("MMMM d, yyyy 'at' h:mm a z").withZone(ZoneId.of("America/New_York"));

    private final JavaMailSender mailSender;

    @Value("${fellasbar.mail.from}")
    private String fromEmail;

    @Value("${fellasbar.mail.store-notify}")
    private String storeNotifyEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOrderConfirmation(Order order) {
        if (order.getCustomerEmail() == null) return;
        try {
            var message = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setFrom(fromEmail, "Fellas");
            helper.setTo(order.getCustomerEmail());
            helper.setSubject("Order Confirmed — Fellas");
            helper.setText(buildConfirmationBody(order), true);
            mailSender.send(message);
            log.info("Confirmation email sent to {}", order.getCustomerEmail());
        } catch (Exception e) {
            log.error("Failed to send confirmation email to {}: {}", order.getCustomerEmail(), e.getMessage());
        }
    }

    public void sendOrderNotification(Order order) {
        try {
            var message = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message, false, "UTF-8");
            helper.setFrom(fromEmail, "Fellas Store");
            helper.setTo(storeNotifyEmail);
            helper.setSubject("New Order — $" + String.format("%.2f", order.getTotalCents() / 100.0));
            helper.setText(buildNotificationBody(order), true);
            mailSender.send(message);
            log.info("Order notification sent to {}", storeNotifyEmail);
        } catch (Exception e) {
            log.error("Failed to send order notification: {}", e.getMessage());
        }
    }

    private String buildConfirmationBody(Order order) {
        String orderDate = order.getCreatedAt() != null ? DATE_FMT.format(order.getCreatedAt()) : "—";
        String total = String.format("%.2f", order.getTotalCents() / 100.0);

        String shippingBlock;
        if (order.getShippingName() != null) {
            String line2 = (order.getShippingLine2() != null && !order.getShippingLine2().isBlank())
                ? "<br>" + order.getShippingLine2() : "";
            shippingBlock =
                "<tr><td style='padding:12px 0;border-bottom:1px solid #1e1e1e;'>" +
                "<span style='color:#888;font-size:12px;text-transform:uppercase;letter-spacing:.05em;'>Ship To</span><br>" +
                "<span style='color:#e0e0e0;font-size:15px;'>" + order.getShippingName() + "<br>" +
                order.getShippingLine1() + line2 + "<br>" +
                order.getShippingCity() + ", " + order.getShippingState() + " " + order.getShippingPostalCode() + "<br>" +
                order.getShippingCountry() + "</span></td></tr>";
        } else {
            shippingBlock = "";
        }

        return "<!DOCTYPE html><html><body style='margin:0;padding:0;background:#111;'>" +
            "<div style='background:#111;font-family:-apple-system,BlinkMacSystemFont,sans-serif;max-width:580px;margin:0 auto;padding:40px 24px;'>" +

            // Header
            "<div style='margin-bottom:32px;'>" +
            "<h1 style='color:#c9a227;font-size:32px;margin:0 0 6px;'>Thanks for your order.</h1>" +
            "<p style='color:#888;font-size:15px;margin:0;'>We appreciate the support. Here's everything about your order.</p>" +
            "</div>" +

            // Order meta
            "<table style='width:100%;border-collapse:collapse;margin-bottom:24px;'>" +
            "<tr><td style='padding:12px 0;border-bottom:1px solid #1e1e1e;'>" +
            "<span style='color:#888;font-size:12px;text-transform:uppercase;letter-spacing:.05em;'>Order Date</span><br>" +
            "<span style='color:#e0e0e0;font-size:15px;'>" + orderDate + "</span></td></tr>" +
            "<tr><td style='padding:12px 0;border-bottom:1px solid #1e1e1e;'>" +
            "<span style='color:#888;font-size:12px;text-transform:uppercase;letter-spacing:.05em;'>Order Reference</span><br>" +
            "<span style='color:#e0e0e0;font-size:13px;font-family:monospace;'>" + order.getStripeSessionId() + "</span></td></tr>" +
            "</table>" +

            // Items
            "<div style='background:#0a0a0a;border:1px solid #1e1e1e;border-radius:8px;padding:20px;margin-bottom:24px;'>" +
            "<p style='color:#888;font-size:12px;text-transform:uppercase;letter-spacing:.05em;margin:0 0 12px;'>Items Ordered</p>" +
            "<pre style='font-family:-apple-system,BlinkMacSystemFont,sans-serif;font-size:15px;color:#e0e0e0;white-space:pre-wrap;margin:0;'>" + order.getLineItems() + "</pre>" +
            "<hr style='border:none;border-top:1px solid #1e1e1e;margin:16px 0;'/>" +
            "<p style='color:#c9a227;font-size:20px;font-weight:700;margin:0;text-align:right;'>Total: $" + total + "</p>" +
            "</div>" +

            // Shipping
            "<table style='width:100%;border-collapse:collapse;margin-bottom:32px;'>" +
            shippingBlock +
            "</table>" +

            // Footer
            "<div style='border-top:1px solid #1e1e1e;padding-top:24px;'>" +
            "<p style='color:#555;font-size:13px;margin:0;'>Questions about your order? Reply to this email and we'll take care of you.</p>" +
            "</div>" +

            "</div></body></html>";
    }

    private String buildNotificationBody(Order order) {
        String orderDate = order.getCreatedAt() != null ? DATE_FMT.format(order.getCreatedAt()) : "—";
        String total = String.format("%.2f", order.getTotalCents() / 100.0);

        String shippingBlock;
        if (order.getShippingName() != null) {
            String line2 = (order.getShippingLine2() != null && !order.getShippingLine2().isBlank())
                ? "<br>" + order.getShippingLine2() : "";
            shippingBlock =
                "<tr><td style='padding:12px 0;border-bottom:1px solid #ddd;'>" +
                "<span style='color:#999;font-size:12px;text-transform:uppercase;'>Ship To</span><br>" +
                "<strong>" + order.getShippingName() + "</strong><br>" +
                order.getShippingLine1() + line2 + "<br>" +
                order.getShippingCity() + ", " + order.getShippingState() + " " + order.getShippingPostalCode() + "<br>" +
                order.getShippingCountry() + "</td></tr>";
        } else {
            shippingBlock = "<tr><td style='padding:12px 0;color:#999;'>No shipping address on file.</td></tr>";
        }

        return "<!DOCTYPE html><html><body style='margin:0;padding:0;background:#f5f5f5;'>" +
            "<div style='font-family:-apple-system,BlinkMacSystemFont,sans-serif;max-width:580px;margin:0 auto;background:#fff;padding:40px 24px;'>" +

            // Header
            "<div style='background:#0a0a0a;border-radius:8px;padding:20px 24px;margin-bottom:28px;'>" +
            "<h1 style='color:#c9a227;font-size:24px;margin:0 0 4px;'>New Order</h1>" +
            "<p style='color:#888;font-size:14px;margin:0;'>Someone just bought something. Time to ship.</p>" +
            "</div>" +

            // Summary row
            "<table style='width:100%;border-collapse:collapse;margin-bottom:24px;'>" +
            "<tr><td style='padding:12px 0;border-bottom:1px solid #eee;'>" +
            "<span style='color:#999;font-size:12px;text-transform:uppercase;'>Order Date</span><br>" +
            "<strong>" + orderDate + "</strong></td></tr>" +
            "<tr><td style='padding:12px 0;border-bottom:1px solid #eee;'>" +
            "<span style='color:#999;font-size:12px;text-transform:uppercase;'>Customer Email</span><br>" +
            "<strong>" + order.getCustomerEmail() + "</strong></td></tr>" +
            "<tr><td style='padding:12px 0;border-bottom:1px solid #eee;'>" +
            "<span style='color:#999;font-size:12px;text-transform:uppercase;'>Stripe Session</span><br>" +
            "<span style='font-family:monospace;font-size:13px;'>" + order.getStripeSessionId() + "</span></td></tr>" +
            "</table>" +

            // Items
            "<div style='background:#f9f9f9;border:1px solid #eee;border-radius:8px;padding:20px;margin-bottom:24px;'>" +
            "<p style='color:#999;font-size:12px;text-transform:uppercase;margin:0 0 12px;'>Items</p>" +
            "<pre style='font-family:-apple-system,BlinkMacSystemFont,sans-serif;font-size:15px;color:#111;white-space:pre-wrap;margin:0;'>" + order.getLineItems() + "</pre>" +
            "<hr style='border:none;border-top:1px solid #eee;margin:16px 0;'/>" +
            "<p style='font-size:20px;font-weight:700;color:#111;margin:0;text-align:right;'>Total: $" + total + "</p>" +
            "</div>" +

            // Shipping
            "<table style='width:100%;border-collapse:collapse;'>" +
            shippingBlock +
            "</table>" +

            "</div></body></html>";
    }
}
