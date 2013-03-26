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
};
$(document).ready(createTooltips);
