
ko.bindingHandlers.enterKey = {
    init: function (element, valueAccessor, allBindings, vm) {
        ko.utils.registerEventHandler(element, "keyup", function (event) {
            if (event.keyCode === 13) {
                ko.utils.triggerEvent(element, "change");
                valueAccessor().call(vm, vm);
                //set "this" to the data and also pass it as first arg, in case function has "this" bound
            }

            return true;
        });
    }
};

var leftSideWidth = 250;
var windowWidth = 260;
var mainViewModel = new chatViewModel();

$(document).ready(function () {
    azureMobileServicesModule.initialize();
    mainViewModel.maximumNumberOfFullWidthWindows(chatModule.getNumberOfWindowsToDisplay());
    ko.applyBindings(mainViewModel);
});
