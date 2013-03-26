function createModal() {
    $('#confirmOverlay').remove();
};
$(document).ready(createModal);

function deleteOfferModal(result) {
    $.confirm({
        'title': self.lang().j1,
        'message': self.lang().j2,
        'buttons': {
            'Ok': {
                'class': 'red',
                'action': function () {
                    result(true);
                }
            },
            'Cancel': {
                'class': 'gray',
                'action': function () {
                    result(false);
                }
            }
        }
    });
};

function createOfferModal(result) {
    $.confirm({
        'title': self.lang().j3,
        'message': self.lang().j4,
        'buttons': {
            'Ok': {
                'class': 'blue',
                'action': function () {
                    result(true);
                }
            },
            'Cancel': {
                'class': 'gray',
                'action': function () {
                    result(false);
                }
            }
        }
    });
};

function finalizeModal(result) {
    $.confirm({
        'title': self.lang().j5,
        'message': self.lang().j6,
        'buttons': {
            'Ok': {
                'class': 'blue',
                'action': function () {
                    result(true);
                }
            },
            'Cancel': {
                'class': 'gray',
                'action': function () {
                    result(false);
                }
            }
        }
    });
};

function changesModal(result) {
    $.confirm({
        'title': self.lang().j9,
        'message': self.lang().j10,
        'buttons': {
            'Ok': {
                'class': 'blue',
                'action': function () {
                    result(true);
                }
            },
            'Cancel': {
                'class': 'gray',
                'action': function () {
                    result(false);
                }
            }
        }
    });
};

function futureModal(result) {
    $.confirm({
        'title': self.lang().j7,
        'message': self.lang().j8,
        'buttons': {
            'Ok': {
                'class': 'red',
                'action': function () {}
            }
        }
    });
};

function errorModal(messageText) {
    $.confirm({
        'title': 'Error!',
        'message': messageText,
        'buttons': {
            'Ok': {
                'class': 'red',
                'action': function () {}
            }
        }
    });
};

function infoModal(messageText) {
    $.confirm({
        'title': 'Info!',
        'message': messageText,
        'buttons': {
            'Ok': {
                'class': 'red',
                'action': function () {}
            }
        }
    });
};
