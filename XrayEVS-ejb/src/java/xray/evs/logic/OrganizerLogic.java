/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package xray.evs.logic;
import xray.evs.dto.Organizer;
import xray.evs.dto.ParticipantList;
import xray.evs.dto.Poll;
import xray.evs.dto.ListParticipators;
import xray.evs.dto.Question;
import xray.evs.dto.Item;
import java.util.List;
import java.time.LocalDateTime;
import javax.ejb.Remote;
import xray.evs.dto.Participant;
import xray.evs.entities.ItemEntity;
import xray.evs.entities.ParticipantEntity;
import xray.evs.entities.PollEntity;
import xray.evs.entities.QuestionEntity;
import xray.evs.utilities.PollState;

/**
 *
 * @author abhinavralhan
 */
@Remote
public interface OrganizerLogic {
    
    public static final String ORGANIZER_ROLE = "ORGANIZER";

    public Organizer getCurrentUser();

    public String storePoll(Poll poll);
 
    public boolean deletePoll(String uuid);

    public Poll getPoll(String uuid);

    public Poll createPoll(String title, LocalDateTime startDate, LocalDateTime endDate, String description, Boolean trackingIsEnabled, List<Participant> participants, List<Question> questions, PollState pollState);

    public List<Poll> getPollList();

    public List<String> getListOfPollTitle();

    public List<Question> getQuestionList(List<QuestionEntity> questionEntity);

    public List<Participant> getParticipants(List<ParticipantEntity> participantEntity);

    public List<Item> getItemList(List<ItemEntity> itemEntity);

    public String startPoll(Poll poll);

    public String publishPoll(String uuid);

    public String sendReminders(String uuid);

    public void createToken(PollEntity pollEntity);

    public ParticipantList createParticipantList(String Name, List<ListParticipators> participants);

    public ParticipantList getParticipantList(String uuid);

    public List<ParticipantList> getParticipantLists();

    public String storeParticipantList(ParticipantList participantList);

    public boolean deleteParticipantList(String uuid);
}
