
function selectAndGetPreview(textid, name, url) {
	$(textid).text(url + "/" + name);
	getPreview(textid, url + "/" + name);
	$("#previewForm_id").attr('action', "NoNE");
	$("#test").text('rrrrr');
}

function getPreview(textid, geturl) {
	$.ajax({
		type: "GET",
		url: geturl,
		cache: false,
		data: {
			showCheckboxes: true
		},
		success: function (data){
			$(textid).html($(data).find("#preview_id").html()).fadeIn();
		}
	})
}
