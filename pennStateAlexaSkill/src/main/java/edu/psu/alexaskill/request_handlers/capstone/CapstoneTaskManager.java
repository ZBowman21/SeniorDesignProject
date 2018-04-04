package edu.psu.alexaskill.request_handlers.capstone;

import com.amazon.speech.slu.ConfirmationStatus;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CapstoneTaskManager
{
    private Session session;

    public CapstoneTaskManager(Session session)
    {
        this.session = session;
    }

    @SuppressWarnings("unchecked")
    public SpeechletResponse clockIntoTask(Intent intent)
    {
        SpeechletResponse response = new SpeechletResponse();
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        Slot slot = intent.getSlots().get("Task");
        String slotValue = slot.getValue();
        if(slotValue == null)
        {
            List<String> formattedTasks = getFormattedTaskIds(true);

            if(formattedTasks.isEmpty())
            {
                speech.setText("I could not retrieve your Capstone tasks. Please visit the Capstone website to login.");
            }
            else
            {
                ElicitSlotDirective elicitSlotDirective = new ElicitSlotDirective();
                elicitSlotDirective.setSlotToElicit("Task");

                List<Directive> directives = new ArrayList<>();

                String outputText = "You have the following tasks in Capstone. Which would you like to clock into. ";
                for(int i = 0; i < formattedTasks.size(); i++)
                {
                    outputText += formattedTasks.get(i) + ". ";
                }

                DialogIntent dialogIntent = new DialogIntent();
                dialogIntent.setName(intent.getName());
                Map<String, DialogSlot> dialogSlots = new HashMap<>();
                for(Map.Entry<String, Slot> entry : intent.getSlots().entrySet())
                {
                    Slot oldSlot = entry.getValue();
                    DialogSlot dialogSlot = new DialogSlot();
                    dialogSlot.setName(oldSlot.getName());
                    dialogSlot.setValue(oldSlot.getValue());
                    dialogSlot.setConfirmationStatus(oldSlot.getConfirmationStatus());
                    dialogSlots.put(entry.getKey(), dialogSlot);
                }

                dialogIntent.setSlots(dialogSlots);
                elicitSlotDirective.setUpdatedIntent(dialogIntent);
                directives.add(elicitSlotDirective);
                response.setDirectives(directives);

                speech.setText(outputText);
                response.setNullableShouldEndSession(false);
            }
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
            boolean clockInSuccessfully = clockInRequestSender.sendRequest(trueTaskId);

            if(clockInSuccessfully)
            {
                speech.setText("You have clocked in successfully");
            }
            else
            {
                speech.setText("There was a problem with clocking you into that task. Please try again or visit the Capstone website to clock-in manually.");
            }

        }

        response.setOutputSpeech(speech);
        return response;
    }

    public SpeechletResponse getFormattedTaskListResponse()
    {
        SpeechletResponse response = new SpeechletResponse();
        SimpleCard card = new SimpleCard();
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        String speechContent;
        String cardContent;

        List<String> tasks = getFormattedTaskIds(false);
        if(tasks.isEmpty())
        {
            speechContent = cardContent = "I could not find any Capstone tasks for your team.";
        }
        else
        {
            speechContent = "You currently have the following tasks in Capstone: ";
            cardContent = "You currently have the following tasks in Capstone:\n";

            for(int i = 0; i < tasks.size(); i++)
            {
                speechContent += tasks.get(i) + ". ";
                cardContent += tasks.get(i);
            }

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

        if(result == null)
        {
            return new ArrayList<>();
        }
        else
        {
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
}
