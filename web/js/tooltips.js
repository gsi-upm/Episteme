function createTooltips() {
    $(".draggableInfo").tooltip({
        position: {
            my: "center+10 bottom-10",
            at: "center top"
        }
    });
    $(".droppableInfo").tooltip({
        position: {
            my: "center+10 top+10",
            at: "center bottom"
        }
    });
    $(".filterInfo").tooltip({
        position: {
            my: "center bottom-10",
            at: "center top"
        }
    });
};
$(document).ready(createTooltips);
