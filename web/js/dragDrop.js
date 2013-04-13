function OnloadFunction() {
    //DRAGGABLE COMPANIES/OFFERS
    $(".draggableCompanies").draggable({
        revert: "invalid",
        helper: "clone",
        start: function (event, ui) {
            startAction($(ui.helper), $(this));
        },
        stop: function (event, ui) {
            stopAction($(ui.helper), $(this));
        }
    });
    //CLICKABLE COMPANIES
    $(".clickableCompanies").click(function() {
      clickAction($(this));
    });
    $(".clickableRecommended").click(function() {
      clickAction($(this));
    });
    //DROPPABLE COMPANIES
    $(".droppableCompany").droppable({
        accept: ".draggable",
        drop: function (event, ui) {
            dropAction($(ui.helper), $(this));
            console.log("company drop");
        }
    });
    $.each($('.droppableCompany'), function () {
        if ($(this).hasClass('stateDisabled')) {
            $(this).droppable("disable");
        } else {
            $(this).droppable("enable");
        }
    });
    //WHEN A COMPANY IS DROPPED CAN BE DRAGGED
    $(".draggableCompany").draggable({
        helper: "clone",
        start: function (event, ui) {
            $(ui.helper).css('box-shadow', ' 0 0 40px #999');
            $(ui.helper).css('z-index', '999');
            $(ui.helper).addClass("dragged");
        },
        stop: function (event, ui) {
            stopCompanyAction($(ui.helper), $(this));
        }
    });
    $.each($('.draggableCompany'), function () {
        if ($(this).hasClass('stateDisabled')) {
            $(this).draggable("disable");
        } else {
            $(this).draggable("enable");
        }
    });
    //WHEN A COMPANY IS DRAGGED FROM DROPPABLE

    function stopCompanyAction($helper, $original) {
        var dropIndex = $original.attr('index');
        self.currentSearch.result()[dropIndex].entityName('');
        self.currentSearch.result()[dropIndex].entityLogo('');
        self.currentSearch.changed('true');
        self.currentSearch.completed('false');
        $original.removeClass("draggableCompany");
    }
    //WHEN A COMPANY IS DROPPED

    function dropAction($drag, $drop) {
        self.currentSearch.changed('true');
        var compName = $drag.find('.draggableText').text()
        var compLogo = $drag.find('.defaultLogo').attr('src');
        var dropIndex = $drop.attr('index');
        self.currentSearch.result()[dropIndex].entityName(compName);
        self.currentSearch.result()[dropIndex].entityLogo(compLogo);
        var nextEntity = $('.draggableEmpty').attr('index')
        if (nextEntity == undefined) {
            self.currentSearch.completed('true');
            finalizeModal(function (result) {
                if (result) {
                    sammyPlugin.trigger('redirectEvent', {
                        url_data: '#/finalize'
                    });
                } else {
                    OnloadFunction();
                }
            });
            return;
        }
        self.currentSearch.completed('false');
        sammyPlugin.trigger('redirectEvent', {
            url_data: '#/composer' + '/' + self.currentSearch.name() + '/entity/' + nextEntity
        });
    }

    //WHEN A COMPANY IS CLICKED

    function clickAction($drag) {
        var compName = $drag.find('.draggableText').text()
        var compLogo = $drag.find('.defaultLogo').attr('src');
        self.currentSearch.result()[0].entityName(compName);
        self.currentSearch.result()[0].entityLogo(compLogo);

        sammyPlugin.trigger('redirectEvent', {
                        url_data: '#/finalize'
        });

    }
    
    //WHEN A COMPANY IS DRAGGED

    function startAction($helper, $original) {
        $helper.css('box-shadow', ' 0 0 40px #999');
        $helper.css('z-index', '999');
        $helper.addClass("dragged");
        storedOpacity = $original.css('opacity');
        $original.css('opacity', ' 0.5');
        $helper.find(".companyMedal").removeClass('gold').removeClass('silver').removeClass('bronze');
    }
    //WHEN A COMPANY IS RELEASED AND NOT DROPPED

    function stopAction($helper, $original) {
        $helper.css('box-shadow', ' 0 0 40px #999');
        $helper.css('z-index', '999');
        $helper.addClass("dragged");
        $original.css('opacity', storedOpacity);
    }
};
$(document).ready(OnloadFunction);
//RESET DROPPABLE COMPANY STATE

function resetDroppables() {
    self.semanticOrder(false);
    $.each($('.droppableCompany'), function () {
        $(this).removeClass('no_selected');
        $(this).removeClass('selected');
        $(this).animate({
            opacity: 1
        }, 20);
        if (!$(this).hasClass('draggableCompany')) $(this).droppable("enable");
    });
}
