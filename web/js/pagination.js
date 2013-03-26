function paginate() {
    $('#draggableElements').pajinate({
        num_page_links_to_display: 15,
        abort_on_small_lists: true,
        items_per_page: 15,
    });
    //Buttons trigger pagination
    $('.dragContainer').find('.prevBtn').unbind('click'); //prevent multiple binding (multiple calls)
    $('.dragContainer').find('.prevBtn').click(function (e) {
        e.preventDefault();
        $('.previous_link').trigger('click');
    });
    $('.dragContainer').find('.nextBtn').unbind('click');
    $('.dragContainer').find('.nextBtn').click(function (e) {
        e.preventDefault();
        $('.next_link').trigger('click');
    });
    //Keyboard arrows trigger pagination
    $(document).unbind('keydown');
    $(document).keydown(function (event) {
        if (!$('#search').is(":focus")) { //prevent pagination while typing on search
            switch (event.keyCode) {
                case 37:
                    $('.previous_link').trigger('click');
                    break;
                case 39:
                    $('.next_link').trigger('click');
                    break;
            }
        }
    });
};
