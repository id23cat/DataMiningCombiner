function insertFunctionsTree(treeData, actionUrl, step) {
	 $('#tree').treeview({
     	data: treeData,
     	onNodeSelected: function(event, node) {
     		$.ajax({
     			url: actionUrl + "/" + step,
     			type: "POST",
     			contentType : 'application/json; charset=utf-8',
     			data: JSON.stringify(node),
     			success: function(data){
     				$("#selected").text(node.text + ": " +node.id + "; URL=" + actionUrl);
     				console.log($(data).find("#methodDetails").html());
     				$("#details").html($(data).find("#methodDetails").html()).fadeIn();
     			}
     		})
     	}
     });
}