package cz.thewhiterabbit.electronicapp.view.events;

import cz.thewhiterabbit.electronicapp.model.similation.SimulationFile;
import cz.thewhiterabbit.electronicapp.model.similation.SimulationResult;
import cz.thewhiterabbit.electronicapp.view.canvas.CanvasObject;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

//TODO transform into command
public class SimulationEvents extends Event {

    public static final EventType<SimulationEvents> ANY = new EventType<>(Event.ANY, "SIMULATION_ANY");
    public static final EventType<SimulationEvents> SIMULATE_CLICKED = new EventType<>(Event.ANY, "SIMULATE_CLICKED");
    public static final EventType<SimulationEvents> START_SIMULATION = new EventType<>(Event.ANY, "START_SIMULATION");
    public static final EventType<SimulationEvents> SIMULATION_FINISHED = new EventType<>(Event.ANY, "SIMULATION_FINISHED");

    private SimulationFile simulationFile;
    private SimulationResult simulationResult;

    public SimulationEvents(EventType<? extends Event> eventType) {
        super(eventType);
    }

    public SimulationEvents(Object o, EventTarget eventTarget, EventType<? extends Event> eventType) {
        super(o, eventTarget, eventType);
    }

    public SimulationEvents(EventType<? extends Event> eventType, SimulationFile simulationFile) {
        super(eventType);
        this.simulationFile = simulationFile;
    }

    public SimulationEvents(Object o, EventTarget eventTarget, EventType<? extends Event> eventType, SimulationFile simulationFile) {
        super(o, eventTarget, eventType);
        this.simulationFile = simulationFile;
    }

    public SimulationEvents(EventType<? extends Event> eventType, SimulationResult simulationResult) {
        super(eventType);
        this.simulationResult = simulationResult;
    }

    public SimulationEvents(Object o, EventTarget eventTarget, EventType<? extends Event> eventType, SimulationResult simulationResult) {
        super(o, eventTarget, eventType);
        this.simulationResult = simulationResult;
    }

    public SimulationFile getSimulationFile() {
        return simulationFile;
    }

    public SimulationResult getSimulationResult() {
        return simulationResult;
    }
}
