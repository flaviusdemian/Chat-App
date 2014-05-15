
var chatModule = function () {
    var v = document.getElementById("audioSound");
    var maximumNumberOfChatWindows;

    function playChatSound() {
        v.play();
    };

    function getNumberOfWindowsToDisplay() {
        maximumNumberOfChatWindows = Math.floor(($(window).width() - leftSideWidth) / windowWidth);
        if (maximumNumberOfChatWindows > 1) {
            maximumNumberOfChatWindows -= 1;
        } else {
            if (maximumNumberOfChatWindows == 0) {
                maximumNumberOfChatWindows = 1;
            }
        }
        return maximumNumberOfChatWindows;
    }

    function animateExpandedChatEntry(itemId) {
        $("#" + itemId).closest('li').effect('bounce', {}, 2000, function () {
            setTimeout(function () {
                $("#" + itemId).closest('li').removeAttr("style");
                $("#" + itemId).animate({ scrollTop: $("#" + itemId)[0].scrollHeight }, 500);
            }, 1000);
        });
    }

    function animateMinimizedChatEntry(message, itemId) {

        toastr.options = {
            "closeButton": false,
            "debug": false,
            "positionClass": "toast-top-left",
            "onclick": null,
            "showDuration": "9000",
            "hideDuration": "1000",
            "timeOut": "3000",
            "extendedTimeOut": "1000",
            "showEasing": "swing",
            "hideEasing": "linear",
            "showMethod": "fadeIn",
            "hideMethod": "fadeOut"
        };
        toastr.info(message);

        $("#extraUsersCount").effect('bounce', {}, 2000, function () {
            setTimeout(function () {
                $("#extraUsersCount").removeAttr("style");
            }, 1000);
        });
        mainViewModel.showExtraUsers(true);
        
        $("#" + itemId).effect('pulsate', {}, 5000, null);
    }

    function propagateMessageToUI(message) {

        var match = ko.utils.arrayFirst(mainViewModel.availableExpandedChatEntries(), function (item) {
            return message.fromId === item.user.id;
        });

        if (match != null) {
            var messageForUI = new Object();
            messageForUI.content = message.content;
            messageForUI.sendByMe = ko.observable(false);
            match.messages.push(messageForUI);

            animateExpandedChatEntry(message.fromId);
        }
        else {
            var chatEntry = new Object();
            var senderUser = ko.utils.arrayFirst(mainViewModel.availableUsers(), function (item) {
                return message.fromId === item.id;
            });

            if (senderUser != null) {
                chatEntry.user = senderUser;
                var receivedMessage = new Object();
                receivedMessage.content = message.content;
                receivedMessage.sendByMe = ko.observable(false);
                chatEntry.messages = ko.observableArray([]);
                chatEntry.messages.push(receivedMessage);
            }
            else {
                alert("an error occurred!");
            }

            //check if it exists in the availableChatEntries array
            match = ko.utils.arrayFirst(mainViewModel.availableChatEntries(), function (item) {
                return message.fromId === item.user.id;
            });

            if (match != null) {
                //it exits in the minimized array
                animateMinimizedChatEntry(message.fromName + ' said ' + message.content, message.fromId + 'minimized');
            }
            else {
                // it does not exist in the availableChatEntries array
                mainViewModel.availableChatEntries.push(chatEntry);
                if (mainViewModel.availableChatEntries().length > chatModule.getNumberOfWindowsToDisplay()) {
                    mainViewModel.availableMinimizedChatEntries.push(chatEntry);
                    animateMinimizedChatEntry(message.fromName + ' said ' + message.content, message.fromId + 'minimized');
                }
                else {
                    mainViewModel.availableExpandedChatEntries.push(chatEntry);
                }
            }
        }
        $("#bottomChatContainer").css("width", $(window).width() - leftSideWidth);
        $("#bottomChatContainer").show();
        playChatSound();

    }

    return {
        playChatSound: playChatSound,
        propagateMessageToUI: propagateMessageToUI,
        getNumberOfWindowsToDisplay: getNumberOfWindowsToDisplay
    };

}();