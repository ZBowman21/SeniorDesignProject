package edu.psu.alexaskill.request_handlers.capstone;

import com.amazon.speech.speechlet.Directive;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.dialog.directives.DialogIntent;
import com.amazon.speech.speechlet.dialog.directives.DialogSlot;
import com.amazon.speech.speechlet.dialog.directives.ElicitSlotDirective;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;
import edu.pennstate.api.model.GetCapstoneTaskListResult;

import java.util.ArrayList;
import java.util.List;

public class CapstoneTaskManager
{
    private Session session;

    public CapstoneTaskManager(Session session)
    {
        this.session = session;
    }

    @SuppressWarnings("unchecked")
    public SpeechletResponse clockIntoTask(DialogIntent intent)
    {
        SpeechletResponse response = new SpeechletResponse();
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        DialogSlot slot = intent.getSlots().get("Task");
        String slotValue = slot.getValue();
        if(slotValue == null)
        {
            List<String> formattedTasks = getFormattedTaskIds(true);

            ElicitSlotDirective elicitSlotDirective = new ElicitSlotDirective();
            elicitSlotDirective.setSlotToElicit("Task");

            List<Directive> directives = new ArrayList<>();
            directives.add(elicitSlotDirective);
            response.setDirectives(directives);

            String outputText = "You have the following tasks in Capstone. Which would you like to clock into. ";
            for(int i = 0; i < formattedTasks.size(); i++)
            {
                outputText += formattedTasks.get(i) + ". ";
            }

            speech.setText(outputText);
            response.setNullableShouldEndSession(false);
        }
        else
        {
            //User has given the formatted ID, so get the true ID
            int formattedTaskId = Integer.parseInt(slotValue);
            formattedTaskId -=1;

            List<String> tasks = (ArrayList<String>)session.getAttribute("tasks");

            String task = tasks.get(formattedTaskId);
            String trueTaskId = task.substring(0, task.indexOf(" "));

            CapstoneClockInRequestSender clockInRequestSender = new CapstoneClockInRequestSender(session.getUser().getAccessToken());
            clockInRequestSender.sendRequest(trueTaskId);
            speech.setText("You have clocked in successfully");
        }

        response.setOutputSpeech(speech);
        return response;
    }

    public SpeechletResponse getFormattedTaskListResponse()
    {
        SpeechletResponse response = new SpeechletResponse();
        SimpleCard card = new SimpleCard();
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();

        List<String> tasks = getFormattedTaskIds(false);
        String speechContent = "You currently have the following tasks in Capstone: ";
        String cardContent = "You currently have the following tasks in Capstone:\n";

        for(int i = 0; i < tasks.size(); i++)
        {
            speechContent += tasks.get(i) + ". ";
            cardContent += tasks.get(i);
        }

        speech.setText(speechContent);
        card.setContent(cardContent);

        response.setOutputSpeech(speech);
        response.setCard(card);

        return  response;
    }

    private List<String> getFormattedTaskIds(boolean cacheOriginalTasks)
    {
        CapstoneTaskListRequestSender taskListRequestSender = new CapstoneTaskListRequestSender(session.getUser().getAccessToken());
        GetCapstoneTaskListResult result = (GetCapstoneTaskListResult)taskListRequestSender.sendRequest(null);
        List<String> tasks = result.getCapstoneTaskList();

        if(cacheOriginalTasks)
        {
            session.setAttribute("tasks", tasks);
        }

        List<String> formattedIds = new ArrayList<>();
        for(int i = 0; i < tasks.size(); i++)
        {
            String s = tasks.get(i);
            int k = s.indexOf(" ", s.indexOf(" ") + 1);
            String taskNoId = s.substring(k);
            String taskNewId = (i + 1) + " : " + taskNoId;
            formattedIds.add(taskNewId);
        }
        return formattedIds;
    }
}
