boolean updated = configurationManager.updateIfRequired(
        tokenOwnerId,
        CardBrand.AMEX,
        TgsConfigurationScope.SETTINGS,
        request,
        () -> post(request)
);


Для wallets:
boolean updated = configurationManager.updateIfRequired(
        tokenOwnerId,
        CardBrand.AMEX,
        TgsConfigurationScope.WALLETS,
        request,
        () -> post(request)
);


Для FULL:
boolean updated = configurationManager.updateIfRequired(
        tokenOwnerId,
        CardBrand.AMEX,
        TgsConfigurationScope.FULL,
        request,
        () -> post(request)
);
