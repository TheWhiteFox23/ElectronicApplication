package cz.thewhiterabbit.electronicapp.canvas.layout;

public enum Priority {
    ALWAYS_ON_BOTTOM(0),
    LOW(25),
    NONE(50),
    HIGH(75),
    ALWAYS_ON_TOP(100);

    private final int priorityIndex;

    Priority(int priorityIndex){
        this.priorityIndex = priorityIndex;
    }

    public int getPriorityIndex() {
        return priorityIndex;
    }
}
