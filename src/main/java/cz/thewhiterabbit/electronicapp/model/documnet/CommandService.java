package cz.thewhiterabbit.electronicapp.model.documnet;

import cz.thewhiterabbit.electronicapp.view.canvas.DrawingAreaEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CommandService {
    private final Document parentDocument;
    private final DocumentCommandInterpreter interpreter;
    private final Stack<DrawingAreaEvent> undoList = new Stack<>();
    private final Stack<DrawingAreaEvent> redoList = new Stack<>();

    public CommandService(Document document){
        this.parentDocument = document;
        this.interpreter = new DocumentCommandInterpreter(document);
    }

    public void undo(){
        if(undoList.empty()) return;

        redoList.push(undoList.pop());
        while(!undoList.empty() && undoList.peek().getEventType() != DrawingAreaEvent.EDITING_FINISHED){
            DrawingAreaEvent event = undoList.pop();
            System.out.println("UNDO: " + event.getEventType());
            interpreter.interpretReverse(event);
            redoList.push(event);
        }
    }

    public void redo(){
        while(!redoList.empty() && redoList.peek().getEventType() != DrawingAreaEvent.EDITING_FINISHED){
            DrawingAreaEvent event = redoList.pop();
            System.out.println("REDO: " + event.getEventType());
            interpreter.interpret(event);
            undoList.push(event);
        }
        if(!redoList.empty() && redoList.peek().getEventType() == DrawingAreaEvent.EDITING_FINISHED){
            undoList.push(redoList.pop());
        }
    }

    public void interpret(DrawingAreaEvent drawingAreaEvent){
        interpreter.interpret(drawingAreaEvent);
        undoList.push(drawingAreaEvent);
        redoList.clear();
        //System.out.println(drawingAreaEvent.getEventType());
    }

    public boolean undoEmpty(){
        return undoList.size() == 0;
    }

    public boolean redoEmpty(){
        return redoList.size() == 0;
    }


}
