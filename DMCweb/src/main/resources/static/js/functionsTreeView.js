function insertFunctionsTree(treeData, actionUrl, id, step=id) {

    $('#tree' + id).treeview({
        data: treeData
        /*onNodeSelected: function(event, node) {
            console.log("Call getting selected function details");
            $.ajax({
                url: actionUrl + "/" + step,
                type: "POST",
                contentType : 'application/json; charset=utf-8',
                data: JSON.stringify(node),
                success: function(data){
                    $("#selected"+id).text(node.text + ": " +node.id + "; URL=" + actionUrl);
                    console.log($(data).find("#methodDetails").html());
                    $("#details"+id).html($(data).find("#methodDetails").html()).fadeIn();
                }
            })
        }*/
    });
}