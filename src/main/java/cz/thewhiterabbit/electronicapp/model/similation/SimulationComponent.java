package cz.thewhiterabbit.electronicapp.model.similation;

import cz.thewhiterabbit.electronicapp.model.objects.ActivePoint;

import java.util.List;

public interface SimulationComponent {
    public void setNode(ActivePoint activePoint, NetlistNode node);
    public NetlistNode getNode(ActivePoint activePoint);
    public String getSimulationComponent();
    public List<NetlistNode> getNodes();
    public void setNode(NetlistNode oldNode, NetlistNode newNode);
    public void removeNode(NetlistNode oldNode);
    public void setName(String name);
    public String getName();
    public Boolean isProbeActive();
    public String getProbeName();
    public void setProbeName(String name);
    public String getComponentName();
}
