package edu.psu.alexaskill.request_handlers.capstone;

import com.amazon.speech.speechlet.Directive;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.dialog.directives.DialogIntent;
import com.amazon.speech.speechlet.dialog.directives.DialogSlot;
import com.amazon.speech.speechlet.dialog.directives.ElicitSlotDirective;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import edu.pennstate.api.model.GetCapstoneTaskListResult;
import net.ricecode.similarity.*;

import java.util.ArrayList;
import java.util.List;

public class CapstoneTaskManager
{
    private Session session;

    public CapstoneTaskManager(Session session)
    {
        this.session = session;
    }

    public SpeechletResponse getTaskSpokenTaskAnalysis(DialogIntent intent)
    {
        SpeechletResponse response = new SpeechletResponse();
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        DialogSlot slot = intent.getSlots().get("Task");
        String slotValue = slot.getValue();
        if(slotValue == null)
        {
            //Slot has not been filled, get task list for user and do elicit prompt
            CapstoneTaskListRequestSender taskListRequestSender = new CapstoneTaskListRequestSender(session.getUser().getAccessToken());
            GetCapstoneTaskListResult result = (GetCapstoneTaskListResult)taskListRequestSender.sendRequest(null);
            List<String> tasks = result.getCapstoneTaskList();

            ElicitSlotDirective elicitSlotDirective = new ElicitSlotDirective();
            elicitSlotDirective.setSlotToElicit("Task");

            List<Directive> directives = new ArrayList<Directive>();
            directives.add(elicitSlotDirective);
            response.setDirectives(directives);

            String outputText = "You have the following tasks in Capstone. Which would you like to clock into. ";
            for(int i = 0; i < tasks.size(); i++)
            {
                outputText += tasks.get(i) + " ";
            }
            speech.setText(outputText);
            response.setNullableShouldEndSession(false);
        }
        else if(isInteger(slotValue))
        {
            //User has given the ID, so just clock them in
            CapstoneClockInRequestSender clockInRequestSender = new CapstoneClockInRequestSender(session.getUser().getAccessToken());
            clockInRequestSender.sendRequest(slotValue);
            speech.setText("You have clocked in successfully");
        }
        else
        {
            //User stated a full task, get all tasks, remove the ids, and attempt to do string compare. Find the greatest match above x, get id and attempt to clock in
            CapstoneTaskListRequestSender taskListRequestSender = new CapstoneTaskListRequestSender(session.getUser().getAccessToken());
            GetCapstoneTaskListResult result = (GetCapstoneTaskListResult)taskListRequestSender.sendRequest(null);
            List<String> tasks = result.getCapstoneTaskList();
            List<String> removedIds = removeTaskIds(tasks);

            int matchedTask = matchSpokenTask(slotValue, removedIds);
            if(matchedTask > -1)
            {
                //matched task found, get true ID and send clock-in request
                String taskID = tasks.get(matchedTask).substring(0, tasks.get(matchedTask).indexOf(" "));
                CapstoneClockInRequestSender clockInRequestSender = new CapstoneClockInRequestSender(session.getUser().getAccessToken());
                clockInRequestSender.sendRequest(taskID);
                speech.setText("You have clocked in successfully");
            }
            else
            {
                speech.setText("I could not find that task in Capstone.");
            }
        }

        response.setOutputSpeech(speech);
        return response;
    }

    private boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    private List<String> removeTaskIds(List<String> tasks)
    {
        List<String> removedIds = new ArrayList<String>();
        for(int i = 0; i < tasks.size(); i++)
        {
            String s = tasks.get(i);
            int k = s.indexOf(" ", s.indexOf(" ") + 1);
            String task = s.substring(k);
            removedIds.add(task);
        }
        return removedIds;
    }

    private int matchSpokenTask(String spokenTask, List<String> tasks)
    {
        double match = 0;
        int matchedTaskIndex = -1;

        LevenshteinDistanceStrategy levenshteinDistanceStrategy = new LevenshteinDistanceStrategy();

        for(int i = 0; i < tasks.size(); i++)
        {
            double currentMatch = levenshteinDistanceStrategy.score(spokenTask, tasks.get(i));
            if(currentMatch >= match && currentMatch >= 0.80)
            {
                match = currentMatch;
                matchedTaskIndex = i;
            }
        }
        return matchedTaskIndex;
    }
}
