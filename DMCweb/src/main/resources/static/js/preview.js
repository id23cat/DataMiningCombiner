$("#checkAll").change(function() {

	$("input[id^='chkbx_']").prop('checked', $(this).prop("checked"));
	$("input[id^='chkbx_']").change()

});

$("input[id^='chkbx_']").change(function() {
	var idchk = $(this)[0].id;
	var multiplier_element = "#" + idchk.replace("chkbx_", "mul_grp_");
	var datatype_element = "#" + idchk.replace("chkbx_", "type_grp_");

	$(multiplier_element).prop('hidden', !$(this).prop("checked"));
	$(datatype_element).prop('hidden', !$(this).prop("checked"));

});