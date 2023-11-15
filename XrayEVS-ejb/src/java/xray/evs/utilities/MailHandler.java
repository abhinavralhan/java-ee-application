/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package xray.evs.utilities;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import xray.evs.dto.Participant;
import xray.evs.dto.Poll;
import xray.evs.entities.ParticipantEntity;
import xray.evs.entities.PollEntity;
import xray.evs.entities.TokenEntity;

/**
 *
 * @author Admin
 */
@Stateless
@LocalBean
public class MailHandler {

    public static final String SERVER_URL = "https://localhost:8181/XrayEVS";

    public String datePattern = "yyyy/MM/dd HH:mm:ss";

    public String tokenMessage = "XrayEVS: Voting has commenced for poll {0}.";
    public String pollMessage = "XrayEVS: Poll {0} has been deleted.";
    public String publishMessage = "XrayEVS: Results for poll {0} have been published.";
    public String changeDateMessage = "XrayEVS: The date for poll {0} has been modified.";
    public String reminderOfPollMessage = "XrayEVS Reminder: Don't forget to vote in the poll labeled as {0}.";

    public String detailedTokenMessage = "<p>Hello {0},</p> <p>You have been enrolled as a participant in the following poll:</p> "
            + "<p>Poll Title: {1}<br/>"
            + "Start Date & Time: {2}<br/> "
            + "End Date & Time: {3}<br/>"
            + "Description: {4}</p>"
            + "<p>To cast your vote, please visit our website and enter the following token:</p>"
            + "<p>Token: {5} </p>"
            + "<p>Alternatively, you can click on the following link to vote:</p>"
            + "<p><a href=" + SERVER_URL + "/pages/participant/voting.xhtml?token={5}>" + SERVER_URL + "/pages/participant/voting.xhtml?token={5}</a></p>";

    public String detailedDeleteMessage = "<p>Hello {0},</p> <p>The specified poll has been removed.</p>"
            + "<p>Poll Title: {1} <br/>"
            + "Start Date & Time: {2} <br/>"
            + "End Date & Time: {3} <br/>"
            + "Description: {4}</p>";

    public String detailedPollDeletionByAdminMessage = "<p>Hello {0},</p> <p>The designated poll has been deleted by an administrator.</p>"
            + "<p>Poll Title: {1} <br/>"
            + "Start Date & Time: {2} <br/>"
            + "End Date & Time: {3} <br/>"
            + "Description: {4}</p>";

    public String detailedPollPublishMessage = "<p>Hello {0},</p> <p>The specified poll has been made public.</p>"
            + "<p>Poll Title: {1} <br/>"
            + "Start Date & Time: {2} <br/>"
            + "End Date & Time: {3} <br/>"
            + "Description: {4} </p>"
            + "<p>You can view the results by clicking on the following link:</p>"
            + "<p><a href=" + SERVER_URL + "/pages/participant/voting_result.xhtml?token={5}>" + SERVER_URL + "/pages/participant/voting_result.xhtml?token={5}</a></p>";

    public String detailedDateChangedMessage = "<p>Hello {0},</p> <p>Changes have been made to the poll dates.</p>"
            + "<p>Poll Title: {1} <br/>"
            + "Start Date & Time: {2} <br/>"
            + "End Date & Time: {3} <br/>"
            + "Description: {4}</p>"
            + "<p>To cast your vote, please visit our website and enter the following token:</p>"
            + "<p>Token: {5} </p>"
            + "<p>Alternatively, you can click on the following link to vote:</p>"
            + "<p><a href=" + SERVER_URL + "/pages/participant/voting.xhtml?token={5}>" + SERVER_URL + "/pages/participant/voting.xhtml?token={5}</a></p>";

    public String detailedReminderMessage = "<p>Hello {0},</p> <p>This is a reminder as you haven't voted in the following poll yet.</p>"
            + "<p>Poll Title: {1} <br/>"
            + "Start Date & Time: {2} <br/>"
            + "End Date & Time: {3} <br/>"
            + "Description: {4} </p>"
            + "<p>To cast your vote, please visit our website and enter the following token:</p>"
            + "<p>Token: {5} </p>"
            + "<p>Alternatively, you can click on the following link to vote:</p>"
            + "<p><a href=" + SERVER_URL + "/pages/participant/voting.xhtml?token={5}>" + SERVER_URL + "/pages/participant/voting.xhtml?token={5}</a></p>";
    
    @Resource(lookup = "mail/xrayevs")
    private Session mailSession;

    public void sendTokenMail(PollEntity pollEntity) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
        Map<ParticipantEntity, String> tokenList = mapParticipantToToken(pollEntity);
        for (ParticipantEntity participant : pollEntity.getParticipants()) {
            Message msg = new MimeMessage(mailSession);
            try {
                msg.setSubject("EVS System Message");
                msg.setSentDate(new Date());
                msg.setFrom(InternetAddress.parse(
                        pollEntity.getOrganizers().get(0).getEmail() + "@uni-koblenz.de",
                        false)[0]);
                msg.setRecipients(
                        Message.RecipientType.TO,
                        InternetAddress.parse(
                                participant.getEmail(),
                                false)
                );
                msg.setContent(MessageFormat.format(detailedTokenMessage,
                        participant.getEmail(),
                        pollEntity.getTitle(),
                        pollEntity.getStartDate().format(formatter),
                        pollEntity.getEndDate().format(formatter),
                        pollEntity.getDescription(),
                        tokenList.get(participant)), "text/html; charset=utf-8");
                Transport.send(msg);
                System.out.println("Sent email! ####################################################################################################");
            } catch (MessagingException ex) {
                Logger.getLogger(ex.getMessage());
            }
        }
    }

    public void sendPollDeleteMail(Poll poll) {
        DateFormat dateFormat = new SimpleDateFormat(datePattern);
        for (Participant participant : poll.getParticipants()) {
            Message msg = new MimeMessage(mailSession);
            try {
                msg.setSubject(MessageFormat.format(pollMessage, poll.getTitle()));
                msg.setSentDate(new Date());
                msg.setFrom(InternetAddress.parse(
                        poll.getOrganizer().get(0).getEmail() + "@uni-koblenz.de",
                        false)[0]);
                msg.setRecipients(
                        Message.RecipientType.TO,
                        InternetAddress.parse(
                                participant.getEmail(),
                                false)
                );
                msg.setContent(MessageFormat.format(detailedDeleteMessage,
                        participant.getEmail(),
                        poll.getTitle(),
                        dateFormat.format(poll.getStartDate()),
                        dateFormat.format(poll.getEndDate()),
                        poll.getDescription()), "text/html; charset=utf-8");
                Transport.send(msg);
                System.out.println("Sent delete email! ####################################################################################################");
            } catch (MessagingException ex) {
                Logger.getLogger(ex.getMessage());
            }
        }
    }

    public void sendPollDeleteByAdminMail(Poll poll) {
        DateFormat dateFormat = new SimpleDateFormat(datePattern);
        List<Participant> receivers = poll.getParticipants();
        Participant organizer = new Participant();
        organizer.setEmail(poll.getOrganizer().get(0).getEmail());
        receivers.add(organizer);
        for (Participant participant : receivers) {
            Message msg = new MimeMessage(mailSession);
            try {
                msg.setSubject(MessageFormat.format(pollMessage, poll.getTitle()));
                msg.setSentDate(new Date());
                msg.setFrom(InternetAddress.parse(
                        poll.getOrganizer().get(0).getEmail() + "@uni-koblenz.de",
                        false)[0]);
                msg.setRecipients(
                        Message.RecipientType.TO,
                        InternetAddress.parse(
                                participant.getEmail(),
                                false)
                );
                msg.setContent(MessageFormat.format(detailedPollDeletionByAdminMessage,
                        participant.getEmail(),
                        poll.getTitle(),
                        dateFormat.format(poll.getStartDate()),
                        dateFormat.format(poll.getEndDate()),
                        poll.getDescription()), "text/html; charset=utf-8");
                Transport.send(msg);
                System.out.println("Sent delete email! ####################################################################################################");
            } catch (MessagingException ex) {
                Logger.getLogger(ex.getMessage());
            }

        }

    }

    public void sendReminder(List<TokenEntity> tokens, PollEntity pollEntity) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
        for (TokenEntity token : tokens) {
            Message msg = new MimeMessage(mailSession);
            try {
                msg.setSubject("Reminder!");
                msg.setSentDate(new Date());
                msg.setFrom(InternetAddress.parse(
                        pollEntity.getOrganizers().get(0).getEmail() + "@uni-koblenz.de",
                        false)[0]);
                msg.setRecipients(
                        Message.RecipientType.TO,
                        InternetAddress.parse(
                                token.getParticipant().getEmail(),
                                false)
                );
                msg.setContent(MessageFormat.format(detailedReminderMessage,
                        token.getParticipant().getEmail(),
                        pollEntity.getTitle(),
                        pollEntity.getStartDate().format(formatter),
                        pollEntity.getEndDate().format(formatter),
                        pollEntity.getDescription(),
                        token.getTokenValue()), "text/html; charset=utf-8");
                Transport.send(msg);
                System.out.println("Sent reminder  email! ####################################################################################################");
            } catch (MessagingException ex) {
                Logger.getLogger(ex.getMessage());
            }
        }
    }

    public void sendResultsMail(Poll poll) {
        DateFormat dateFormat = new SimpleDateFormat(datePattern);
        for (Participant participant : poll.getParticipants()) {
            Message msg = new MimeMessage(mailSession);
            try {
                msg.setSubject(MessageFormat.format(publishMessage, poll.getTitle()));
                msg.setSentDate(new Date());
                msg.setFrom(InternetAddress.parse(
                        poll.getOrganizer().get(0).getEmail() + "@uni-koblenz.de",
                        false)[0]);
                msg.setRecipients(
                        Message.RecipientType.TO,
                        InternetAddress.parse(
                                participant.getEmail(),
                                false)
                );
                msg.setContent(MessageFormat.format(detailedPollPublishMessage,
                        participant.getEmail(),
                        poll.getTitle(),
                        dateFormat.format(poll.getStartDate()),
                        dateFormat.format(poll.getEndDate()),
                        poll.getDescription(),
                        poll.getResultToken()), "text/html; charset=utf-8");
                Transport.send(msg);
            } catch (MessagingException ex) {
                Logger.getLogger(ex.getMessage());
            }
        }
    }

    public Map<ParticipantEntity, String> mapParticipantToToken(PollEntity pollEntity) {
        Map<ParticipantEntity, String> participantTokenList = new HashMap<>();

        if (pollEntity.getTrackingIsEnabled()) {
            List<TokenEntity> tokens = pollEntity.getTokens();
            for (TokenEntity token : tokens) {
                participantTokenList.put(token.getParticipant(), token.getTokenValue());
            }
        } else {
            List<TokenEntity> tokens = pollEntity.getTokens();
            for (ParticipantEntity participant : pollEntity.getParticipants()) {
                Random rand = new Random();
                TokenEntity randomToken = tokens.get(rand.nextInt(tokens.size()));
                participantTokenList.put(participant, randomToken.getTokenValue());
                tokens.remove(randomToken);
            }
        }

        return participantTokenList;
    }

    public void sendTestMail() {
        System.out.println("delete the calling function!######################################################################");
    }
}
