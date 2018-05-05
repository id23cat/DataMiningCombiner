$("#checkAll").change(function() {

	$("input[id^='chkbx_']").prop('checked', $(this).prop("checked"));
	$("input[id^='chkbx_']").change()

});

$("input[id^='chkbx_']").change(function() {
	var idchk = $(this)[0].id;
	var multiplier_element = "#" + idchk.replace("chkbx_", "mul_grp_");
	var datatype_element = "#" + idchk.replace("chkbx_", "type_grp_");
	var type = "#" + idchk.replace("chkbx_", "type_");

	setHidden($(multiplier_element), !$(this).prop("checked"));
	setHidden($(datatype_element), !$(this).prop("checked"));
	if($(this).prop("checked")) {
		$(type).change();
	}

});


$("select[id^='type_']").change(function() {
	hiddenMultilier(this);
})



function hiddenMultilier(selector) {
	var selectorId = $(selector)[0].id;
	var multiplier_element = "#" + selectorId.replace("type_", "mul_grp_");
	console.log("selsected: " + $(selector).val());
	setHidden($(multiplier_element), !($(selector).val() == "NUMERIC"));
}

function setHidden(id, hidden) {
	$(id).prop('hidden', hidden);
}

