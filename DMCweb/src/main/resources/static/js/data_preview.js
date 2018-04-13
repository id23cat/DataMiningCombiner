///*<![CDATA[*/
//$(document).ready(function() {
////	$("#selector_id_/*[[${id}]]*/").change(getPreview($("#selector_id_/*[[${id}]]*/").text()));
//	getPreview($("#selector_id_data option:selected").text());
//});
///*]]>*/

function getPreview(textid, name, url) {
//	$(textid).text(url + "/" + name);
	gettext(textid, url + "/" + name);
}

function gettext(textid, geturl) {
	$.ajax({
		type: "GET",
		url: geturl,
		cache: false,
		success: function (data){
			$(textid).html($(data).find("#preview_id").html());
		}
	})
}
