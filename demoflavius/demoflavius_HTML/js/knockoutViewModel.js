
function chatViewModel() {
    var self = this;

    self.availableUsers = ko.observableArray([]);
    self.availableChatEntries = ko.observableArray([]);
    self.availableExpandedChatEntries = ko.observableArray([]);
    self.availableMinimizedChatEntries = ko.observableArray([]);

    self.maximumNumberOfFullWidthWindows = ko.observable("");
    self.showNumberOfMinimizedUsers = ko.observable(false);
    self.showExtraUsers = ko.observable(false);

    self.startChat = function (user) {
        var match = ko.utils.arrayFirst(self.availableChatEntries(), function (item) {
            return user.id === item.user.id;
        });

        if (!match) {
            var chatEntry = new Object();
            chatEntry.user = user;
            chatEntry.messages = ko.observableArray([]);
            self.availableChatEntries.push(chatEntry);

            if (self.availableChatEntries().length > chatModule.getNumberOfWindowsToDisplay()) {
                self.availableMinimizedChatEntries.push(chatEntry);
                self.showNumberOfMinimizedUsers(true);
            }
            else {
                self.availableExpandedChatEntries.push(chatEntry);
                self.showNumberOfMinimizedUsers(false);
            }

            $("#bottomChatContainer").css("width", $(window).width() - leftSideWidth);
            $("#bottomChatContainer").show();
        }
    };

    self.closeChat = function (entry) {

        self.availableChatEntries.remove(entry);
        self.availableExpandedChatEntries.remove(entry);

        if (self.availableMinimizedChatEntries().length >= 1) {
            var entryToIntroduceInExpanded = self.availableMinimizedChatEntries()[0];
            self.availableExpandedChatEntries.push(entryToIntroduceInExpanded);
            self.availableMinimizedChatEntries.shift();
        }

        if (self.availableMinimizedChatEntries().length == 0) {
            self.showNumberOfMinimizedUsers(false);
            self.showExtraUsers(false);
        }
        else {
            self.showNumberOfMinimizedUsers(true);
        }

        if (self.availableChatEntries().length == 0) {
            $("#bottomChatContainer").hide();
        }
    };

    self.sendMessage = function (currentUserReference) {
        var match = ko.utils.arrayFirst(self.availableChatEntries(), function (entry) {
            return currentUserReference.user.id === entry.user.id;
        });

        if (match) {
            var content = match.user.currentMessage();
            if (content != null && content != "") {
                var messageForUI = new Object();
                messageForUI.content = match.user.currentMessage();
                messageForUI.sendByMe = ko.observable(true);
                match.messages.push(messageForUI);

                $("#" + currentUserReference.user.id).animate({ scrollTop: $("#" + currentUserReference.user.id)[0].scrollHeight }, 500);

                //send message to azure mobile
                var currentUser = azureMobileServicesModule.getCurrentUser();
                if (typeof (currentUser) != "undefined" && currentUser != null) {
                    var message = new Object();
                    message.id = guidGeneratorModule.generateGuid();
                    message.content = match.user.currentMessage();
                    message.deviceType = "Web";
                    message.fromId = currentUser.providerIdLong;
                    message.fromName = currentUser.name;
                    message.fromPicture = currentUser.picture;
                    message.toId = currentUserReference.user.id.replace("_", ":");

                    azureMobileServicesModule.getMessageTable().insert(message);
                } else {
                    alert("Something went wrong!");
                }
                //end

                match.user.currentMessage(null);
                chatModule.playChatSound();
            }
        } else {
            alert("something went wrong!");
        }
    };

    self.showExtraUsersClicked = function () {
        if (self.showExtraUsers() == true) {
            self.showExtraUsers(false);
        } else {
            self.showExtraUsers(true);
        }
    };

    self.showDetailsMouseOver = function (entry) {

        var match = ko.utils.arrayFirst(self.availableChatEntries(), function (item) {
            return entry.user.id === item.user.id;
        });

        if (typeof (match) != 'undefined' && match != null) {
            var chatEntry = new Object();
            chatEntry.user = entry.user;
            chatEntry.messages = ko.observableArray([]);

            var lastExpandedEntryIndex = chatModule.getNumberOfWindowsToDisplay();
            var entryToBeMovedToMinimazedZone = self.availableExpandedChatEntries()[lastExpandedEntryIndex - 1];
            self.availableExpandedChatEntries.remove(entryToBeMovedToMinimazedZone);
            self.availableExpandedChatEntries.push(entry);
            self.availableMinimizedChatEntries.remove(entry);
            self.availableMinimizedChatEntries.push(entryToBeMovedToMinimazedZone);
        }
    };

    self.showDetailsMouseOut = function () {
        self.showMinimizedUsers(false);
    };

    self.adjustHoveredItemStyle = function (data, event) {
        var $el = $(event.target);
        if ($el.hasClass("minimizedUsersEntry")) {
            $el.addClass('selectedItem');
        } else {
            $el.parent().addClass('selectedItem');
        }
    };

    self.removeHoveredItemStyle = function (data, event) {
        var $el = $(event.currentTarget);
        if ($el.hasClass("minimizedUsersEntry")) {
            $el.removeClass('selectedItem');
        } else {
            $el.parent().removeClass('selectedItem');
        }
    };

    self.removeMinimizedEntry = function (entry) {

        var match = ko.utils.arrayFirst(self.availableMinimizedChatEntries(), function (item) {
            return entry.user.id === item.user.id;
        });


        self.availableMinimizedChatEntries.remove(match);
        self.availableChatEntries.remove(match);

        if (self.availableMinimizedChatEntries().length == 0) {
            self.showNumberOfMinimizedUsers(false);
            self.showExtraUsers(false);
        }
        else {
            self.showNumberOfMinimizedUsers(true);
        }
    };
};
