/*<![CDATA[*/
$(document).ready(function() {
//	$("#selector_id_/*[[${id}]]*/").change(getPreview($("#selector_id_/*[[${id}]]*/").text()));
	$("#selector_id_/*[[${id}]]*/").change(getPreview($("#selector_id_data option:selected").text()));
});
/*]]>*/

function getPreview(str) {
//	var str = "";
//	$( "#selector_id_/*[[${id}]]*/ option:selected" ).each(function() {
//      str += $( this ).text() + " ";
//    });;
    
//    vat str = $( "#selector_id_data option:selected" ).text();
	$('#feedback').text(str);
}

