@Before(order = 0)
public void configureWireMock(
        Scenario scenario) {

    ScenarioTagUtils
            .getWiremockActive(scenario)
            .ifPresent(
                    WireMockStateManager::apply
            );
}



@After(order = 0)
public void restoreWireMock() {

    WireMockStateManager.restore();
}
