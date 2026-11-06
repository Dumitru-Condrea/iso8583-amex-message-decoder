public void updateAndVerifyTokenOwnerSettings(...) {
    updateAndVerify(..., TgsConfigurationScope.SETTINGS);
}

public void updateAndVerifyTokenOwnerWallets(...) {
    updateAndVerify(..., TgsConfigurationScope.WALLETS);
}

public void updateAndVerifyTokenOwnerConfiguration(...) {
    updateAndVerify(..., TgsConfigurationScope.FULL);
}


private void updateAndVerify(
        ObjectNode request,
        TgsConfigurationScope scope) {

    changeTokenOwnerState(request);

    refreshCaches();

    configurationValidator.verify(
            tokenOwnerId,
            brand,
            scope,
            request
    );
}
