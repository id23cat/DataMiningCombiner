function handlerGetProjectsList(getUrl, divId) {

//	return function getProjectList() {
//		$.ajax({
//			type: "GET",
//			url: getUrl,
//			cache: false,
//			success: function (data) {
//				console.log(data);
//				$(divId).text(data);
//			}
//		})
//	}

    function insertDataToHtml(data) {
        var items = [];
        console.log(data);
        $.each(data, function (index, element) {
            var selfLink = jQuery.grep(element.links, function (link) {
                console.log(link.rel == "self");
                return link.rel == "self";
            })[0].href;
            console.log(selfLink);
            items.push("<li> <a id='" + element.projectId + "' "
                + "href='" + selfLink + "'"
                + ">"
                + element.name
                + "<\a> </li>")
        });

        $("<ul/>", {
            "class": "my-new-list",
            html: items.join("")
        }).appendTo(divId);
    }

    return function () {
        $.getJSON(getUrl, insertDataToHtml);
    }
}
