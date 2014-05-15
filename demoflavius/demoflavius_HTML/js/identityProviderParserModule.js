var identityProviderParserModule = function () {
    function getShrotProvider(longProviderId) {
        var result = longProviderId.split(":");
        if (result != null && result.length == 2) {
            return result[1];
        }
        return null;
    };

    return {
        getShrotProvider: getShrotProvider
    };
}();
