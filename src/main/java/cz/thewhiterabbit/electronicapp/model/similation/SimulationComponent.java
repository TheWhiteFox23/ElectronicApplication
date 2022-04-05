package cz.thewhiterabbit.electronicapp.model.similation;

import cz.thewhiterabbit.electronicapp.model.objects.ActivePoint;

public interface SimulationComponent {
    public void setNode(ActivePoint activePoint, NetlistNode node);
    public NetlistNode getNode(ActivePoint activePoint);
    public String getSimulationComponent();
}
