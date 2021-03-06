package EventGenerators;

import Controllers.TrafficLightController;
import Events.TrafficLightEvent;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;

public class TrafficLightEventGenerator extends Thread {
    private final EPServiceProvider engine;
    private final TrafficLightController trafficLightController;

    public TrafficLightEventGenerator() {
        engine = EPServiceProviderManager.getDefaultProvider();
        trafficLightController = TrafficLightController.getCurrentController();
    }

    @Override
    public void run() {
        super.run();
        String currentColor;
        while (true) {
            currentColor = trafficLightController.getStreetTrafficLight();
            try {
                if (trafficLightController.isSuspended()) {
                    sleep(1000L);
                } else {
                    if (currentColor.equals("red")) { // Red
                        sleep(trafficLightController.getRedSeconds() * 1000L);
                        engine.getEPRuntime().sendEvent(new TrafficLightEvent("green", "red", false));
                    } else { // Green
                        System.out.println("Sleeping for: " + trafficLightController.getGreenSeconds());
                        sleep(trafficLightController.getGreenSeconds() * 1000L);
                        engine.getEPRuntime().sendEvent(new TrafficLightEvent("red", "green", false));
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
